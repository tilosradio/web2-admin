package hu.radio.tilos.jooqmodel;

import hu.tilos.radio.jooqmodel.Tables;
import hu.tilos.radio.jooqmodel.tables.pojos.Radioshow;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        Connection conn = null;

        String userName = "root";
        String password = "";
        String url = "jdbc:mysql://localhost:3306/tilos2";

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url, userName, password);
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            List<Radioshow> result = create.select().from(Tables.RADIOSHOW).limit(10).fetchInto(Radioshow.class);
            for (Radioshow r : result) {
                System.out.println(r);
                System.out.println(r.getName());
            }
        } catch (Exception e) {
            // For the sake of this tutorial, let's keep exception handling simple
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }
}
