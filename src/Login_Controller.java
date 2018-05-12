import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Login_Controller implements Initializable{
    @FXML
    PasswordField managerPassword;
    @FXML
    Button login;
    @FXML
    Button menu;

    private String PASSWORD_FILE = "src/Password.txt";
    private List<String> passwordList = new ArrayList<>();
    private Alert alert = new Alert(Alert.AlertType.ERROR);

    public void loginButtonHandler(ActionEvent actionEvent){
        boolean check = true;
        String password = managerPassword.getText();
        if(password.equals("")){
            errMsgSet("Error Dialog","Please input a password","");
            check = false;
        }
        for(String pw : passwordList){
            if(password.equals(pw)){
                changeScene(actionEvent,"Management_Interface.fxml");
                check = false;
            }
        }
        if(check)errMsgSet("Error Dialog", "Incorrect password", "");
    }

    public void errMsgSet(String title, String header, String content) {
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public List<String> readFile(){
        try {
            FileReader file = new FileReader(PASSWORD_FILE);
            BufferedReader reader = new BufferedReader(file);
            String readLine = reader.readLine();
            while(readLine != null){
                passwordList.add(readLine);
                readLine = reader.readLine();
            }
        }catch (Exception ex){
            ex.getMessage();
        }
        return passwordList;
    }

    public void menu(ActionEvent mouseEvent){
        changeScene(mouseEvent,"Main_Interface.fxml");
        System.out.println("Main interface.");
    }

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
        readFile();
    }
}
