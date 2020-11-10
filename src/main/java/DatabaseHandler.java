import java.sql.*;

/**
 * Метод подключения к БД
 */
public class DatabaseHandler extends Configs {
    Connection dbConnection;

    public DatabaseHandler() throws SQLException {
    }

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jbdc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
        Class.forName("com.mysql.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    /**
     *
     * @param nameCourt запись названия суда
     * @param adressCourt запись адреса суда
     * @param link запись ссылки на сайт суда
     */


    public void addCourt(String nameCourt, String adressCourt, String link) {
        String insert = "INSERT INTO" + Const.COURT_TABLE + "(" + Const.COURT_NAME +
                "," + Const.COURT_ADRESS + "," + Const.COURT_LINK + ")" + "VALUES(?,?,?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);

            prSt.setString(1, nameCourt);
            prSt.setString(2, adressCourt);
            prSt.setString(3, link);

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param court суд, который исследуется на предмет соответствия id запрашиваемому
     * @return возвращает результат (данные о суде)
     */
    public ResultSet getCourt(Court court) {
        ResultSet resSet = null;

        String select = "SELECT * FROM " + Const.COURT_TABLE + "WHERE " +
                Const.COURT_ID + "=?";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);

            prSt.setString(1, court.getIdCourt());

            resSet = prSt.executeQuery();


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resSet;
    }






}




