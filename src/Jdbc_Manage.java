import java.sql.*;
import java.time.LocalDateTime;

public class Jdbc_Manage {
    static final String username = "callmebus";
    static final String password = "Faflukfon1@";
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/callmebus";
    static Connection connection;

    public Connection Connect() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, username, password);
            return connection;
        } catch (SQLException e) {
            System.err.println("Can't connect");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Can't close");
            }
        }
    }

    public String createStateMent(Object... values) {
        StringBuilder sql = new StringBuilder();
        for (Object value : values) {
            if (value==null) {
                sql.append("NULL, ");
                continue;
            }
            if (value.getClass().equals(String.class))
                sql.append("\"").append(value).append("\"");
            else if (value.getClass().equals(LocalDateTime.class)) {
                String v = value.toString();
                v = v.replace('T', ' ');
                sql.append("\'").append(v).append("\'");
            } else sql.append(value);
            if (!value.equals(values[values.length - 1])) sql.append(", ");
        }
        return sql.toString();
    }

    public void insertRecordManage(Object... values) {
        connection = Connect();
        Statement statement;
        try {
            statement = connection.createStatement();
            String sql = "insert into " + " managebus " + "(depart,arrive,departinfo,arriveinfo,company,cost)" + " values(";
            sql += createStateMent(values);
            sql += ")";
            statement.execute(sql);
        } catch (SQLException e) {
            System.err.println("Cannot excute");
            e.getErrorCode();
        } finally {
            closeConnection();
        }
    }

    public void insertRecordCompany(String company){
        connection = Connect();
        Statement statement;
        try {
            statement = connection.createStatement();
                    String sql = "insert into " + "company" + " (name) " + "values(";
                    sql += createStateMent(company) ;
                    sql += ")";
                    statement.execute(sql);
        } catch (SQLException e){
            System.err.println("Cannot excute");
            e.getErrorCode();
        } finally {
            closeConnection();
        }
    }


    public void removeRecord(String tablename, String colum, Object values) {
        connection = Connect();
        Statement statement;
        try {
            statement = connection.createStatement();
            String sql = "delete from " + tablename + " where " + colum + " = " + createStateMent(values);
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public ResultSet getAllDataTable(String tablename) {
        connection = Connect();
        ResultSet resultSet = null;
        try {
            resultSet = connection.createStatement().executeQuery("SELECT * from " + tablename);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return resultSet;
    }

    public String getTextFromSelectColumn(String selectComlumn, String tablename, String whereColumn, String equals) {
        connection = Connect();
        ResultSet rs = null;
        String result = null;
        try {
            rs = connection.createStatement().executeQuery("SELECT " + selectComlumn + " FROM " + tablename + " WHERE " + whereColumn + " = " +"\""+ equals+"\"");
            if (rs.next()) result = rs.getString(selectComlumn);
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return result.replace(" ","");
    }


}
