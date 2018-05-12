import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;


public class ChangeScene_Controller {
    @FXML
    Button ticket;
    @FXML
    Button manager;
    @FXML
    Button exit;
    @FXML
    FlowPane main;
    private final SceneChanger sceneChanger =new SceneChanger();

    @FXML
    public void setBuyticketPressed(MouseEvent mouseEvent) {
        sceneChanger.changeScene(mouseEvent, "Booking_Interface.fxml");
    }

    @FXML
    public void setManagerPressed(MouseEvent mouseEvent) {
        sceneChanger.changeScene(mouseEvent,"Login_Interface.fxml");
    }


    @FXML
    public void exit(){
        System.exit(1);
    }
}
