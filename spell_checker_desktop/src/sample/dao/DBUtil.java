package sample.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    public static Connection connection = null;

    /*
    Получает соединение к базе данных, получает параметры из db.properties.
     */
    public static Connection getConnection(){
        if(connection != null){
            return connection;
        } else {
            try{
                Properties properties = new Properties();
                InputStream inputStream = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
                properties.load(inputStream);
                String driver = properties.getProperty("driver");
                String url = properties.getProperty("url");
                String user = properties.getProperty("user");
                String password = properties.getProperty("password");
                Class.forName(driver);
                connection = DriverManager.getConnection(url, user, password);
            } catch (IOException | ClassNotFoundException | SQLException e){
                System.out.println("Connection Failed! Check output console");
                e.printStackTrace();
            }
            return connection;
        }
    }
}
