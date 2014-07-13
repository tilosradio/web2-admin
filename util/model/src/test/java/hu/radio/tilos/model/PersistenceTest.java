package hu.radio.tilos.model;

import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.*;
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

    public void testSelect() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("tilos-test");
        EntityManager manager = factory.createEntityManager();
        Query q = manager.createQuery("SELECT m from Mix m", Mix.class);
//        q.setParameter("id", 548);
//        q.setMaxResults(10);
        List<Mix> resultList = q.getResultList();
        for (Mix a : resultList) {
            System.out.println(a.getId());
            System.out.println(a.getTitle());

        }
        manager.close();
    }
}
