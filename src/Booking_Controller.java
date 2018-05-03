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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Booking_Controller implements Initializable {
    @FXML
    Button bookbutton;
    @FXML
    RadioButton oneway;
    @FXML
    RadioButton roundtrip;
    @FXML
    ComboBox depart;
    @FXML
    ComboBox arrive;
    @FXML
    ComboBox departdate;
    @FXML
    ComboBox departtime;
    @FXML
    ComboBox returndate;
    @FXML
    ComboBox returntime;
    @FXML
    ComboBox company;
    @FXML
    ComboBox seat;

    private Jdbc_Manage connect = new Jdbc_Manage();
    private ObservableList<Object> dataCombobox;
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
    public void initialize(URL location, ResourceBundle resources) {
        oneway.setToggleGroup(group);
        roundtrip.setToggleGroup(group);
        loadDataDepart();
    }

    public void loadDataDepart() {

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

        depart.setItems(null);
        depart.setItems(dataCombobox);
        arrive.setItems(null);
        arrive.setItems(dataCombobox);
    }

    public void handleBooking() {

    }

    @FXML
    public void backToMenu(MouseEvent mouseEvent) {
        changeScene(mouseEvent, "Main_Interface.fxml");
        System.out.println("Back To Menu");
    }

    @FXML
    public void changeScene(Event event, String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Scene Scene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(Scene);
            window.show();
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            window.setX((primScreenBounds.getWidth() - window.getWidth()) / 2);
            window.setY((primScreenBounds.getHeight() - window.getHeight()) / 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
