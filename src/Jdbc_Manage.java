import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class of manage the data base have method the connect,insert,remove and get data.
 * @author Thanakrit Daorueang,Piyaphol Wiengperm
 */
public class Jdbc_Manage {
    static final String username = "fluke";
    static final String password = "1234";
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://35.194.158.90:3306/CallMeBus?useSSL=false";
    static Connection connection;

    /**
     * Use to connect to the database.
     */
    public static void Connect(){
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, username, password);
        } catch (SQLException e) {
            System.err.println("Can't connect");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Use the set any value to be string by check case.
     * @param values
     * @return string value
     */
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

    /**
     * Use to add data to data base by receive the values.
     * @param values
     */
    public void insertRecordManage(Object... values) {
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

        }
    }

    /**
     * Use to add company name to data base.
     * @param company
     */
    public void insertRecordCompany(String company){
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

        }
    }

    /**
     * Use to remove the database by set the table name,column and values that want to remove.
     * @param tablename
     * @param colum
     * @param values
     */
    public void removeRecord(String tablename, String colum, Object values) {
        Statement statement;
        try {
            statement = connection.createStatement();
            String sql = "delete from " + tablename + " where " + colum + " = " + createStateMent(values);
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        }
    }

    /**
     * Use to get the value of column that need to get from the table name.
     * @param selectComlumn
     * @param tablename
     * @param whereColumn
     * @param equals
     * @return the value of that column
     */
    public String getTextFromSelectColumn(String selectComlumn, String tablename, String whereColumn, String equals) {
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

    /**
     * Use to load the name of company into observable list.
     * @return Observablelist of company detail
     */
    public ObservableList<CompanyDetail> loadManageCompany(){
        ObservableList<CompanyDetail> data = null;
        try {
            data = FXCollections.observableArrayList();
            //ResultSet rs = connection.createStatement().executeQuery("SELECT NAME FROM company");
            PreparedStatement stmt = connection.prepareStatement("SELECT NAME FROM company");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CompanyDetail companyDetail = new CompanyDetail(rs.getString("name"));
                data.add(companyDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Use to load the province name in to observable list.
     * @return Observable list object
     */
    public ObservableList<Object> loadDepartData(){
        ObservableList<Object> dataCombobox = null;
        try {
            dataCombobox = FXCollections.observableArrayList();
            // Execute query and store result in a resultset
            ResultSet rs = connection.createStatement().executeQuery("SELECT province_name FROM province_th");
            while (rs.next()) {
                //get string from db,whichever way
                dataCombobox.add(rs.getString("province_name"));
            }

        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }
        return dataCombobox;
    }

    /**
     * Use to load all data from manage bus table to be list of manager detail.
     * @return List of manager detail
     */
    public List<ManagerDetail> loadAllData(){
        ArrayList<ManagerDetail> list = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM managebus");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ManagerDetail managerDetail = new ManagerDetail(rs.getInt("id"),
                        rs.getString("depart"),
                        rs.getString("arrive"),
                        rs.getString("departinfo"),
                        rs.getString("arriveinfo"),
                        rs.getString("company"),
                        rs.getDouble("cost"));
                list.add(managerDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Use to load company name to be observable list.
     * @return observable list of object.
     */
    public ObservableList<Object> loadCompanyName(){
        ObservableList<Object> dataCombobox = null;
        try {
            dataCombobox = FXCollections.observableArrayList();
            // Execute query and store result in a resultset
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM company");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                //get string from db,whichever way
                dataCombobox.add(rs.getString("name"));
            }

        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }
        return dataCombobox;
    }

    /**
     * Use to load all data from manage bus to be observable list.
     * @return observable list of manager detail
     */
    public ObservableList<ManagerDetail> loadAllDataManageBus(){
       ObservableList<ManagerDetail> data = null;
        try {
            data = FXCollections.observableArrayList();
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM managebus");
            while (resultSet.next()) {
                ManagerDetail managerDetail = new ManagerDetail(resultSet.getInt("id"),
                        resultSet.getString("depart"),
                        resultSet.getString("arrive"),
                        resultSet.getString("departinfo"),
                        resultSet.getString("arriveinfo"),
                        resultSet.getString("company"),
                        resultSet.getDouble("cost"));
                //get string from db,whichever way
                data.add(managerDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

}
