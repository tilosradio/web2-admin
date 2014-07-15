package hu.tilos.radio.backend;

import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Properties;

public class TestUtil {
    public static EntityManagerFactory initPersistence() {
        try {
            Properties properties = new Properties();
            properties.load(TestUtil.class.getResourceAsStream("/db.properties"));
            properties.setProperty("openjpa.ConnectionURL", properties.getProperty("jdbc.url"));
            properties.setProperty("openjpa.ConnectionDriverName", properties.getProperty("jdbc.driver"));
            properties.setProperty("openjpa.ConnectionUserName", properties.getProperty("jdbc.user"));
            properties.setProperty("openjpa.ConnectionPassword", properties.getProperty("jdbc.password"));

            return Persistence.createEntityManagerFactory("tilos-test", properties);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void inidTestData() {
        try {
            Properties properties = new Properties();
            properties.load(TestUtil.class.getResourceAsStream("/db.properties"));
            JdbcDatabaseTester tester = new JdbcDatabaseTester(
                    properties.getProperty("jdbc.driver"),
                    properties.getProperty("jdbc.url"),
                    properties.getProperty("jdbc.user"),
                    properties.getProperty("jdbc.password"));
            tester.setDataSet(new FlatXmlDataSet(SearchControllerTest.class.getResourceAsStream("baseData.xml")));
            tester.onSetup();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }
}
