package hu.tilos.radio.backend;


import hu.radio.tilos.model.*;
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
import org.apache.lucene.store.RAMDirectory;
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
import java.text.SimpleDateFormat;
import java.util.List;

import static org.apache.lucene.util.Version.*;
import static org.apache.lucene.util.Version.LUCENE_48;

@Path("api/v1/search")
public class SearchController {

    private EntityManagerFactory emf;

    private Directory index;


    private Directory getIndex() {
        if (index == null) {
            index = new RAMDirectory();
            index();
        }
        return index;
    }

    @GET
    @Path("index")
    public String index() {
        try {

            EntityManager manager = emf.createEntityManager();


            IndexWriterConfig config = new IndexWriterConfig(LUCENE_48, createAnalyzer());

            IndexWriter w = new IndexWriter(getIndex(), config);
            addAuthors(w);
            addShows(w);
            addPages(w);
            addEpisodes(w);
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
                tok = new ASCIIFoldingFilter(tok, true);
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
    @Security(role = Role.GUEST)
    public SearchResponse search(@QueryParam("q") String search) throws IOException, ParseException {


        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(
                LUCENE_48,
                new String[]{"name", "alias", "definition", "introduction", "description"},
                createAnalyzer());
        queryParser.setAllowLeadingWildcard(true);

        org.apache.lucene.search.Query q = queryParser.parse(search);

        IndexReader reader = IndexReader.open(getIndex());
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
            e.setUri(d.getField("uri").stringValue());
            e.setTitle(d.getField("name").stringValue());
            e.setDescription(d.getField("description").stringValue());
            r.addElement(e);
        }
        return r;
    }

    private void addAuthors(IndexWriter w) throws IOException {
        EntityManager manager = emf.createEntityManager();
        TypedQuery<Author> q = manager.createQuery("SELECT e FROM Author e", Author.class);
        List<Author> result = q.getResultList();
        for (Author a : result) {
            Document doc = new Document();
            doc.add(new TextField("name", a.getName(), Field.Store.YES));
            if (a.getAlias() != null) {
                doc.add(new TextField("alias", a.getAlias(), Field.Store.NO));
            }
            if (a.getIntroduction() != null) {
                doc.add(new TextField("introduction", a.getIntroduction(), Field.Store.NO));
                doc.add(new TextField("description", shorten(a.getIntroduction(), 100), Field.Store.YES));
            } else {
                doc.add(new TextField("description", "", Field.Store.YES));
            }

            doc.add(new TextField("type", "author", Field.Store.YES));
            doc.add(new TextField("uri", "/author/" + a.getAlias(), Field.Store.YES));
            w.addDocument(doc);
        }
        manager.close();

    }

    private String shorten(String text, int i) {
        if (text.length() > 100) {
            return text.substring(0, 99) + "...";
        } else {
            return text;
        }
    }

    private void addShows(IndexWriter w) throws IOException {
        EntityManager manager = emf.createEntityManager();
        TypedQuery<Show> q = manager.createQuery("SELECT e FROM Show e", Show.class);
        List<Show> result = q.getResultList();
        for (Show show : result) {
            Document doc = new Document();
            doc.add(new TextField("name", show.getName(), Field.Store.YES));
            if (show.getAlias() != null) {
                doc.add(new TextField("alias", show.getAlias(), Field.Store.NO));
            }
            if (show.getDefinition() != null) {
                doc.add(new TextField("description", show.getDefinition(), Field.Store.YES));
            } else {
                doc.add(new TextField("description", "", Field.Store.YES));
            }
            if (show.getDescription() != null) {
                doc.add(new TextField("introduction", show.getDescription(), Field.Store.NO));
            }
            doc.add(new TextField("type", "show", Field.Store.YES));
            doc.add(new TextField("uri", "/show/" + show.getAlias(), Field.Store.YES));

            w.addDocument(doc);
        }
        manager.close();

    }

    private void addPages(IndexWriter w) throws IOException {
        EntityManager manager = emf.createEntityManager();
        TypedQuery<TextContent> q = manager.createQuery("SELECT e FROM TextContent e where e.type = 'page'", TextContent.class);
        List<TextContent> result = q.getResultList();
        for (TextContent page : result) {
            Document doc = new Document();
            doc.add(new TextField("content", safe(page.getContent()), Field.Store.NO));
            //doc.add(new TextField("alias", safe(page.getAlias()), Field.Store.NO));
            doc.add(new TextField("name", safe(page.getTitle()), Field.Store.YES));
            doc.add(new TextField("description", shorten(safe(page.getContent()), 100), Field.Store.YES));
            doc.add(new TextField("content", safe(page.getContent()), Field.Store.NO));
            doc.add(new TextField("type", "page", Field.Store.YES));
            if (page.getAlias() != null && page.getAlias().length() > 0) {
                doc.add(new TextField("uri", "/page/" + page.getAlias(), Field.Store.YES));
            } else {
                doc.add(new TextField("uri", "/page/" + page.getId(), Field.Store.YES));
            }
            w.addDocument(doc);
        }
        manager.close();

    }

    private void addEpisodes(IndexWriter w) throws IOException {
        EntityManager manager = emf.createEntityManager();
        TypedQuery<Episode> q = manager.createQuery("SELECT e FROM Episode e where e.text is not null", Episode.class);
        List<Episode> result = q.getResultList();
        for (Episode episode : result) {
            TextContent text = episode.getText();
            Document doc = new Document();
            doc.add(new TextField("content", safe(text.getContent()), Field.Store.NO));
            //doc.add(new TextField("alias", safe(page.getAlias()), Field.Store.NO));
            doc.add(new TextField("name", safe(text.getTitle()), Field.Store.YES));
            doc.add(new TextField("description", shorten(safe(text.getContent()), 100), Field.Store.YES));
            doc.add(new TextField("content", safe(text.getContent()), Field.Store.NO));
            doc.add(new TextField("type", "page", Field.Store.YES));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            doc.add(new TextField("uri", "/episode/" + episode.getShow().getAlias() + "/" + dateFormat.format(episode.getPlannedFrom()), Field.Store.YES));
            w.addDocument(doc);
        }
        manager.close();

    }


    private String safe(String content) {
        if (content == null) {
            return "";
        }
        return content;
    }


    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }
}
