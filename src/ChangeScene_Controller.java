import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

/**
 * Controller of main page handle the button to change to another scene.
 * @author Thanakrit Daorueang,Piyaphol Wiengperm.
 */
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

    /**
     * Use to switch to booking page.
     */
    @FXML
    public void setBuyticketPressed(MouseEvent mouseEvent) {
        sceneChanger.changeScene(mouseEvent, "Booking_Interface.fxml");
    }

    /**
     * Use to switch to login page.
     * @param mouseEvent
     */
    @FXML
    public void setManagerPressed(MouseEvent mouseEvent) {
        sceneChanger.changeScene(mouseEvent,"Login_Interface.fxml");
    }


    @FXML
    public void exit(){
        System.exit(1);
    }
}
