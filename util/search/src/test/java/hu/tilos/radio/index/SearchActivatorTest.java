package hu.tilos.radio.index;

import hu.radio.tilos.model.Author;
import hu.radio.tilos.model.ListenerStat;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.*;
import org.apache.lucene.util.Version;
import org.junit.Test;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.apache.lucene.util.Version.LUCENE_48;
import static org.junit.Assert.*;

public class SearchActivatorTest {

    private EntityManager manager;

    @Test
    public void test() throws Exception {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("tilos");
        manager = factory.createEntityManager();

        Directory index = createIndex(false);
        search(index, "xxx");


        manager.close();
    }

    private void search(Directory index, String search) throws IOException, ParseException {
        StandardAnalyzer analyzer = new StandardAnalyzer(LUCENE_48);
        //

        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(
                Version.LUCENE_48,
                new String[]{"name", "alias", "introduction"},
                new StandardAnalyzer(Version.LUCENE_48));
        queryParser.setAllowLeadingWildcard(true);

        org.apache.lucene.search.Query q = queryParser.parse("toth");

        IndexReader reader = IndexReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(10, true);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + hits[i].score + d.getField("alias") + " " + d.getField("name"));
        }
    }

    private Directory createIndex(boolean reindex) throws IOException {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("tilos");
        EntityManager manager = factory.createEntityManager();

        StandardAnalyzer analyzer = new StandardAnalyzer(LUCENE_48);
        //Directory index = new RAMDirectory();
        Directory index = FSDirectory.open(new File("/tmp/index"));

        IndexWriterConfig config = new IndexWriterConfig(LUCENE_48, analyzer);

        if (reindex) {
            IndexWriter w = new IndexWriter(index, config);
            addAuthors(w);
            w.close();
        }
        System.out.println("Indexing is finished");
        return index;
    }

    private void addAuthors(IndexWriter w) throws IOException {

        TypedQuery<Author> q = manager.createQuery("SELECT e FROM hu.radio.tilos.model.Author e", Author.class);
        List<Author> result = q.getResultList();
        for (Author a : result) {
            System.out.println(a.getAlias());
            Document doc = new Document();
            doc.add(new StringField("name", a.getName(), Field.Store.YES));
            if (a.getAlias() != null) {
                doc.add(new StringField("alias", a.getAlias(), Field.Store.YES));
            }
            if (a.getIntroduction() != null) {
                doc.add(new TextField("introduction", a.getIntroduction(), Field.Store.NO));
            }
            doc.add(new StringField("type", "author", Field.Store.YES));
            doc.add(new IntField("id", a.getId(), Field.Store.NO));
            w.addDocument(doc);
        }

    }

}