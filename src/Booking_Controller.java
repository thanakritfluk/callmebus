import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Booking_Controller {
    @FXML
    Button bookbutton;
    @FXML
    CheckBox oneway;
    @FXML
    CheckBox roundtrip;
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
    ComboBox amount;

    public void whichMark(MouseEvent mouseEvent) {
        if (oneway.isSelected()) {
            System.out.println("One way marked");
            roundtrip.setDisable(true);
            returndate.setDisable(true);
            returntime.setDisable(true);
        } else if (roundtrip.isSelected()) {
            System.out.println("Round trip marked");
            oneway.setDisable(true);
        } else {
            roundtrip.setDisable(false);
            oneway.setDisable(false);
            returndate.setDisable(false);
            returntime.setDisable(false);
        }
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
