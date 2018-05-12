import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class Management_Controller {
    @FXML
    Button timetable;
    @FXML
    Button company;
    @FXML
    Button menu;
    private final SceneChanger sceneChanger = new SceneChanger();

    public void timetableHandler(ActionEvent mouseEvent){
        sceneChanger.changeScene(mouseEvent,"ManageBus_Interface.fxml");
    }

    public void companyHandler(ActionEvent mouseEvent){
        sceneChanger.changeScene(mouseEvent,"ManageCompany_Interface.fxml");
    }

    public void backMenu(ActionEvent mouseEvent){
        sceneChanger.changeScene(mouseEvent,"Main_Interface.fxml");
    }
}
