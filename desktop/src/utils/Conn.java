package utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;
import java.util.Properties;

public class Conn {
    public static Connection getConnection() throws IOException {
        Properties properties = new Properties();

        InputStream inputStream = Conn.class.getClassLoader().getResourceAsStream("config/application.properties");
        properties.load(inputStream);

        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        try{
            return DriverManager.getConnection(url, username, password);
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
