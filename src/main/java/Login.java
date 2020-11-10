import java.sql.*;

/**
 * Соединение с сервером mySQL и базой данных
 */
public class Login {
    private Connection con;
    private Statement st;
    private ResultSet rs;

    public Login(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/history");
            st = con.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

}
