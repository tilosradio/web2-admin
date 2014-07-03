package hu.tilos.radio.backend;


import hu.radio.tilos.model.Author;
import hu.radio.tilos.model.Show;
import hu.tilos.radio.backend.data.SearchResponse;
import hu.tilos.radio.backend.data.SearchResponseElement;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import javax.naming.directory.SearchResult;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import static org.apache.lucene.util.Version.*;
import static org.apache.lucene.util.Version.LUCENE_48;

@Path("v1/search")
public class SearchController {

    private EntityManagerFactory emf;

    @GET
    @Path("index")
    public String index() {
        try {

            EntityManager manager = emf.createEntityManager();


            //Directory index = new RAMDirectory();
            Directory index = FSDirectory.open(new File("/tmp/index"));

            IndexWriterConfig config = new IndexWriterConfig(LUCENE_48, createAnalyzer());


            IndexWriter w = new IndexWriter(index, config);
            addAuthors(w);
            w.close();

            return "Indexing is finished";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "ERROR";
        }
    }

    public Analyzer createAnalyzer() {
        return new StopwordAnalyzerBase(LUCENE_48) {

            @Override
            protected TokenStreamComponents createComponents(final String fieldName, final Reader reader) {
                final StandardTokenizer src = new StandardTokenizer(matchVersion, reader);
                src.setMaxTokenLength(10);

                TokenStream tok = new StandardFilter(matchVersion, src);
                tok = new LowerCaseFilter(matchVersion, tok);
                tok = new ASCIIFoldingFilter(tok);
                tok = new StopFilter(matchVersion, tok, stopwords);

                return new TokenStreamComponents(src, tok) {
                    @Override
                    protected void setReader(final Reader reader) throws IOException {
                        src.setMaxTokenLength(10);
                        super.setReader(reader);
                    }
                };
            }
        };

    }


    @Path(value = "query")
    @GET
    @Produces("application/json")
    public SearchResponse search(@QueryParam("q") String search) throws IOException, ParseException {
        Directory index = FSDirectory.open(new File("/tmp/index"));

        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(
                LUCENE_48,
                new String[]{"name", "alias", "definition", "introduction", "description"},
                createAnalyzer());
        queryParser.setAllowLeadingWildcard(true);

        org.apache.lucene.search.Query q = queryParser.parse(search);

        IndexReader reader = IndexReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(10, true);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        SearchResponse r = new SearchResponse();
        for (int i = 0; i < hits.length; ++i) {
            SearchResponseElement e = new SearchResponseElement();
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            e.setScore(hits[i].score);
            e.setType(d.getField("type").stringValue());
            //e.setId((Long) d.getField("id").numericValue());
            e.setAlias(d.getField("alias").stringValue());
            e.setTitle(d.getField("name").stringValue());
            e.setDescription("");
            r.addElement(e);
        }
        index.close();
        return r;
    }

    private void addAuthors(IndexWriter w) throws IOException {
        EntityManager manager = emf.createEntityManager();
        TypedQuery<Author> q = manager.createQuery("SELECT e FROM hu.radio.tilos.model.Author e", Author.class);
        List<Author> result = q.getResultList();
        for (Author a : result) {
            Document doc = new Document();
            doc.add(new TextField("name", a.getName(), Field.Store.YES));
            if (a.getAlias() != null) {
                doc.add(new TextField("alias", a.getAlias(), Field.Store.YES));
            }
            if (a.getIntroduction() != null) {
                doc.add(new TextField("introduction", a.getIntroduction(), Field.Store.NO));
            }
            doc.add(new TextField("type", "author", Field.Store.YES));
            doc.add(new IntField("id", a.getId(), Field.Store.YES));
            w.addDocument(doc);
        }
        manager.close();

    }

    private void addShows(IndexWriter w) throws IOException {
        EntityManager manager = emf.createEntityManager();
        TypedQuery<Show> q = manager.createQuery("SELECT e FROM hu.radio.tilos.model.Show e", Show.class);
        List<Show> result = q.getResultList();
        for (Show show : result) {
            Document doc = new Document();
            doc.add(new TextField("name", show.getName(), Field.Store.YES));
            if (show.getAlias() != null) {
                doc.add(new TextField("alias", show.getAlias(), Field.Store.YES));
            }
            if (show.getDefinition() != null) {
                doc.add(new TextField("description", show.getDefinition(), Field.Store.NO));
            }
            if (show.getDescription() != null) {
                doc.add(new TextField("introduction", show.getDescription(), Field.Store.NO));
            }
            doc.add(new TextField("type", "show", Field.Store.YES));
            doc.add(new IntField("id", show.getId(), Field.Store.YES));
            w.addDocument(doc);
        }
        manager.close();

    }


    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }
}
