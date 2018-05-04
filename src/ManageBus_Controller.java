import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ManageBus_Controller implements Initializable {
    @FXML
    Label status;
    @FXML
    Button main, add;
    @FXML
    RadioButton roundtrip;
    @FXML
    RadioButton oneway;
    @FXML
    ComboBox departfrom;
    @FXML
    ComboBox returnto;
    @FXML
    ComboBox company;
    @FXML
    DatePicker departdate;
    @FXML
    TextField departtime;
    @FXML
    DatePicker returndate;
    @FXML
    TextField returntime;
    @FXML
    TableView<ManagerDetail> manage;
    @FXML
    TableColumn<ManagerDetail, Integer> id;
    @FXML
    TableColumn<ManagerDetail, String> from;
    @FXML
    TableColumn<ManagerDetail, String> to;
    @FXML
    TableColumn<ManagerDetail, String> departinfo;
    @FXML
    TableColumn<ManagerDetail, String> arriveinfo;
    @FXML
    TableColumn<ManagerDetail, String> companyinfo;
    @FXML
    TableColumn<ManagerDetail, Double> cost;
    @FXML
    Button remove;
    private ObservableList<ManagerDetail> data;
    private ObservableList dataCombobox;
    private Jdbc_Manage connect = new Jdbc_Manage();
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    private final ToggleGroup group = new ToggleGroup();


    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        connect = new Jdbc_Manage();

        oneway.setToggleGroup(group);
        roundtrip.setToggleGroup(group);
        //Load Data
        loadDataToCompany();
        loadDataFromManagebus();
        loadDataForDepart();
        //Sort by dating.
        manage.getSortOrder().add(departinfo);

    }

    @FXML
    public void clear() {
        loadDataFromManagebus();
    }

    @FXML
    public void loadDataForDepart() {
        try {
            Connection connection = connect.Connect();
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

        departfrom.setItems(null);
        departfrom.setItems(dataCombobox);
        returnto.setItems(null);
        returnto.setItems(dataCombobox);

    }

    public void errMsgSet(String title, String header, String content) {
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void addData() {
        if (roundtrip.isSelected() || oneway.isSelected()) {
            if (departfrom.getSelectionModel() != null && returnto.getSelectionModel() != null) {
                String depart = departfrom.getSelectionModel().getSelectedItem().toString();
                String to = returnto.getSelectionModel().getSelectedItem().toString();
                if (company.getSelectionModel() != null) {

                    String companyinfo = company.getSelectionModel().getSelectedItem().toString();

                    if (departdate.getValue() != null && returndate.getValue() != null) {
                        if (departtime.getText().length() + returntime.getText().length() == 10) {
                            double depart_lat = Double.valueOf(connect.getTextFromSelectColumn("province_lat", "province_th", "province_name", depart));
                            double depart_lon = Double.valueOf(connect.getTextFromSelectColumn("province_lon", "province_th", "province_name", depart));
                            double return_lat = Double.valueOf(connect.getTextFromSelectColumn("province_lat", "province_th", "province_name", to));
                            double return_lon = Double.valueOf(connect.getTextFromSelectColumn("province_lon", "province_th", "province_name", to));
                            Distance_Cal cal_Distance = new Distance_Cal();
                            double price = price_Calculate(cal_Distance.distance(depart_lat, depart_lon, return_lat, return_lon));
                            String[] depart_time = departtime.getText().split(":");
                            String[] return_time = returntime.getText().split(":");
                            LocalDateTime departinfo = LocalDateTime.of(departdate.getValue().getYear(), departdate.getValue().getMonth(), departdate.getValue().getDayOfMonth(), Integer.parseInt(depart_time[0]), Integer.parseInt(depart_time[1]), 00);
                            LocalDateTime arriveinfo = LocalDateTime.of(returndate.getValue().getYear(), returndate.getValue().getMonth(), returndate.getValue().getDayOfMonth(), Integer.parseInt(return_time[0]), Integer.parseInt(return_time[1]), 00);
                            connect.insertRecord("managebus", depart, to, departinfo, arriveinfo, companyinfo, price);
                            loadDataFromManagebus();

                        } else errMsgSet("Error Dialog", "Time input error", "time should be this form like 03:30");
                    } else errMsgSet("Error Dialog", "Please select the date", "");
                } else errMsgSet("Error Dialog", "Please select the company", "");
            } else errMsgSet("Error Dialog", "Please select depart and return", "");
        } else errMsgSet("Error Dialog", "Please select roundtrip or oneway", "");
        manage.getSortOrder().add(departinfo);
    }

    public double price_Calculate(double distance) {
        double price = 0;
        if (distance >= 1000) return price = 1000;
        else if (distance >= 700) return price = 750;
        else if (distance >= 500) return price = 500;
        else if (distance >= 250) return price = 200;
        else if (distance >= 100) return price = 150;
        else return price = 100;
    }


    @FXML
    public void removeData() {
        connect.removeRecord("managebus", "id", manage.getSelectionModel().getSelectedItem().getId());
        loadDataFromManagebus();
        manage.getSortOrder().add(departinfo);
    }

    @FXML
    public void loadDataToCompany(){
        try {
            Connection connection = connect.Connect();
            dataCombobox = FXCollections.observableArrayList();
            // Execute query and store result in a resultset
            ResultSet rs = connection.createStatement().executeQuery("SELECT company_name FROM company");
            while (rs.next()) {
                //get string from db,whichever way
                dataCombobox.add(rs.getString("company_name"));
            }

        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }

        company.setItems(null);
        company.setItems(dataCombobox);
    }

    @FXML
    public void loadDataFromManagebus() {
        try {
            Connection connection = connect.Connect();
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

                //System.out.println(resultSet.getString("depart"));
                //get string from db,whichever way
                data.add(managerDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        from.setCellValueFactory(new PropertyValueFactory<>("depart"));
        to.setCellValueFactory(new PropertyValueFactory<>("arrive"));
        departinfo.setCellValueFactory(new PropertyValueFactory<>("departinfo"));
        arriveinfo.setCellValueFactory(new PropertyValueFactory<>("arriveinfo"));
        companyinfo.setCellValueFactory(new PropertyValueFactory<>("company"));
        cost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        manage.setItems(null);
        manage.setItems(data);
    }

    @FXML
    public void changeScene(Event event, String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Scene Scene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(Scene);
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            window.setX((primScreenBounds.getWidth() - window.getWidth()) / 2);
            window.setY((primScreenBounds.getHeight() - window.getHeight()) / 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backToMenu(MouseEvent mouseEvent) {
        System.out.println("Back to menu");
        changeScene(mouseEvent, "Main_Interface.fxml");
    }

}
