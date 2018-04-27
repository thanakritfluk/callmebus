import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;

public class ChangeScene_Controller {
    @FXML
    Button ticket;
    @FXML
    Button manager;
    @FXML
    FlowPane main;
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

    @FXML
    public void setBuyticketPressed(MouseEvent mouseEvent) {
        changeScene(mouseEvent, "Booking_Interface.fxml");
        System.out.println("Buy Ticket clicked");
    }

    @FXML
    public void setManagerPressed(MouseEvent mouseEvent) {
        changeScene(mouseEvent,"ManageBus_Interface.fxml");
        System.out.println("Manager clicked");
    }



}
