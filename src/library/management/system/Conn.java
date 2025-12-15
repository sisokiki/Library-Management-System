package library.management.system;

import java.sql.*;

public class Conn {
    Connection c;
    Statement s;
    public Conn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql:///library_management_system", "root", "sisokiki");
            s = c.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}