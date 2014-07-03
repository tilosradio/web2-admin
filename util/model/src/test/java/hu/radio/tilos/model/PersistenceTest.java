package hu.radio.tilos.model;

import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

public class PersistenceTest {

    @Test
    @Ignore
    public void test() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("tilos-test");
        EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();
        ListenerStat stat = new ListenerStat();
        stat.setType(0);
        stat.setCount(12);
        stat.setDate(new Date());
        manager.persist(stat);
        manager.getTransaction().commit();
        manager.close();
    }

    @Test
    @Ignore
    public void testSelect() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("tilos-test");
        EntityManager manager = factory.createEntityManager();

        List<Author> authors = manager.createQuery("select a FROM Author a", Author.class).setMaxResults(10).getResultList();
        for (Author a : authors) {
            System.out.println(a.getName());
        }
        manager.close();
    }
}
