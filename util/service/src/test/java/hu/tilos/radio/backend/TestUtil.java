package hu.tilos.radio.backend;

import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.dataset.xml.XmlDataSetWriter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class TestUtil {

    public static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(TestUtil.class.getResourceAsStream("/db.properties"));
        return properties;
    }

    public static EntityManagerFactory initPersistence() {
        try {
            Properties properties = loadProperties();
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
            Properties properties = loadProperties();
            JdbcDatabaseTester tester = new JdbcDatabaseTester(
                    properties.getProperty("jdbc.driver"),
                    properties.getProperty("jdbc.url"),
                    properties.getProperty("jdbc.user"),
                    properties.getProperty("jdbc.password"));
            tester.setDataSet(new FlatXmlDataSet(SearchControllerTest.class.getResourceAsStream("baseData.xml")));
            //new XmlDataSetWriter(new FileWriter("/tmp/test.xml")).write(new FlatXmlDataSet(SearchControllerTest.class.getResourceAsStream("baseData.xml")));
            tester.onSetup();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }
}
