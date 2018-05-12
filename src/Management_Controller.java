import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;


public class Management_Controller {
    @FXML
    Button timetable;
    @FXML
    Button company;
    @FXML
    Button menu;

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

    public void timetableHandler(ActionEvent mouseEvent){
        changeScene(mouseEvent,"ManageBus_Interface.fxml");
        System.out.println("Go to timetable management window.");
    }

    public void companyHandler(ActionEvent mouseEvent){
        changeScene(mouseEvent,"ManageCompany_Interface.fxml");
        System.out.println("Go to company management window.");
    }

    public void backMenu(ActionEvent mouseEvent){
        changeScene(mouseEvent,"Main_Interface.fxml");
        System.out.println("Back to menu.");
    }
}
