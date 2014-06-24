package hu.radio.tilos.model;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import java.util.Date;

public class PersistenceTest {

    @Test
    public void test(){
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("tilos");
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
}
