import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller of management page.
 * @author Thanakrit Daorueang,Piyaphol Wiengperm
 */
public class Management_Controller {
    @FXML
    Button timetable;
    @FXML
    Button company;
    @FXML
    Button menu;
    private final SceneChanger sceneChanger = new SceneChanger();

    /**
     * Use to change to manage page.
     */
    public void timetableHandler(ActionEvent mouseEvent){
        sceneChanger.changeScene(mouseEvent,"ManageBus_Interface.fxml");
    }

    /**
     * Use to change to manage company page.
     */
    public void companyHandler(ActionEvent mouseEvent){
        sceneChanger.changeScene(mouseEvent,"ManageCompany_Interface.fxml");
    }

    /**
     * Use to change to main page.
     */
    public void backMenu(ActionEvent mouseEvent){
        sceneChanger.changeScene(mouseEvent,"Main_Interface.fxml");
    }
}
