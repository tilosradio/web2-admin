package hu.tilos.radio.backend;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.dataset.xml.XmlDataSetWriter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class TestUtil {

    public static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(TestUtil.class.getResourceAsStream("/db.properties"));
        return properties;
    }

    public static DataSource initDatasource() {
        try {
            Properties properties = loadProperties();
            MysqlDataSource ds = new MysqlDataSource();
            Class.forName(properties.getProperty("jdbc.driver")).newInstance();
            ds.setPassword(properties.getProperty("jdbc.password"));
            ds.setURL(properties.getProperty("jdbc.url"));
            ds.setUser(properties.getProperty("jdbc.user"));
            return ds;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public static Connection initConnection() {
        try {
            Properties properties = loadProperties();
            Class.forName(properties.getProperty("jdbc.driver")).newInstance();
            Connection conn = DriverManager.getConnection(properties.getProperty("jdbc.url"), properties.getProperty("jdbc.user"), properties.getProperty("jdbc.password"));
            return conn;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
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
