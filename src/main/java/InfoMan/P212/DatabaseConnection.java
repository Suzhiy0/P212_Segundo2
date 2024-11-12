package InfoMan.P212;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private String url  = "jdbc:mysql://localhost:3306/p212_Infoman_fxapp_db";
    private String user = "app";
    private String password = "1234";
    private Connection connection;

    public DatabaseConnection(){
        try{
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected successfully!! naisu :>");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
