import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
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
    Button clear;
    private ObservableList<ManagerDetail> data;
    private ObservableList dataCombobox;
    private Jdbc_Manage connect = new Jdbc_Manage();
    private Alert alert = new Alert(Alert.AlertType.ERROR);


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
        loadDataFromDB();
        setDepartfrom();
//        add.fire();
    }

    @FXML
    public void clear() {
        loadDataFromDB();
    }

    @FXML
    public void setDepartfrom() {
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
        if (roundtrip.isSelected() && !oneway.isSelected() || oneway.isSelected() && !roundtrip.isSelected()) {
            if (departfrom.getSelectionModel() != null && returnto.getSelectionModel() != null) {
                String depart = departfrom.getSelectionModel().getSelectedItem().toString();
                String to = returnto.getSelectionModel().getSelectedItem().toString();
                if (company.getSelectionModel() != null) {
                    String companyinfo = "fluk";
                    if (departdate.getValue()!=null && returndate.getValue()!=null) {
                        if (departtime.getText().length() + returntime.getText().length() == 10) {

                            String[] depart_time = departtime.getText().split(":");
                            String[] return_time = returntime.getText().split(":");
                            LocalDateTime departinfo = LocalDateTime.of(departdate.getValue().getYear(), departdate.getValue().getMonth(), departdate.getValue().getDayOfMonth(), Integer.parseInt(depart_time[0]), Integer.parseInt(depart_time[1]), 00);
                            LocalDateTime arriveinfo = LocalDateTime.of(returndate.getValue().getYear(), returndate.getValue().getMonth(), returndate.getValue().getDayOfMonth(), Integer.parseInt(return_time[0]), Integer.parseInt(return_time[1]), 00);
                            connect.insertRecord("managebus", depart, to, departinfo, arriveinfo, "ske", 250.0);
                            loadDataFromDB();
                        } else errMsgSet("Error Dialog", "Time input error", "time should be this form like 03:30");
                    } else errMsgSet("Error Dialog", "Please select the date", "");
                } else errMsgSet("Error Dialog", "Please select the company", "");
            } else errMsgSet("Error Dialog", "Please select depart and return", "");
        } else errMsgSet("Error Dialog", "Please select only one roundtrip or oneway", "");
    }


    @FXML
    public void getTime() {
        String departtimes = departtime.getText();
        String returntimes = returntime.getText();
        if (departtimes.length() + returntimes.length() != 10) {
            status.setText("Incorrect time input");
        } else {

        }
    }


    public void loadDataFromDB() {
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
