import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Login_Controller implements Initializable {
    @FXML
    PasswordField managerPassword;
    @FXML
    Button login;
    @FXML
    Button menu;


    private List<String> passwordList = new ArrayList<>();
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    private final SceneChanger sceneChanger = new SceneChanger();


    public void loginButtonHandler(ActionEvent actionEvent) {
        boolean check = true;
        String password = managerPassword.getText();
        if (password.equals("")) {
            errMsgSet("Error Dialog", "Please input a password", "");
            check = false;
        }
        for (String pw : passwordList) {
            if (password.equals(pw)) {
                sceneChanger.changeScene(actionEvent, "Management_Interface.fxml");
                check = false;
            }
        }
        if (check) errMsgSet("Error Dialog", "Incorrect password", "");
    }

    public void errMsgSet(String title, String header, String content) {
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public List<String> getPassword() {
        String password = PropertyManager.getProperty("password1","");
        System.out.println("password : " + password);
        String password2 = PropertyManager.getProperty("password2","");
        passwordList.add(password);
        passwordList.add(password2);
        return passwordList;
    }

    public void menu(ActionEvent mouseEvent) {
        sceneChanger.changeScene(mouseEvent, "Main_Interface.fxml");
    }


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
        getPassword();
    }
}
