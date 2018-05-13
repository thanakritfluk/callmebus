import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ResourceBundle;

public class ManageCompany_Controller implements Initializable {
    @FXML
    TextField name;
    @FXML
    Button add;
    @FXML
    Button remove;
    @FXML
    Button back;
    @FXML
    TableView<CompanyDetail> complist;
    @FXML
    TableColumn<CompanyDetail, String> namelist;

    private Jdbc_Manage connect = new Jdbc_Manage();
    private Alert alert = new Alert(Alert.AlertType.ERROR);

    public void back(ActionEvent mouseEvent) {
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.changeScene(mouseEvent, "Management_Interface.fxml");
    }

    @FXML
    public void removeData() {
        connect.removeRecord("company", "name", complist.getSelectionModel().getSelectedItem().getName());
        loadDataToTable();
        complist.getSortOrder().add(namelist);
    }

    @FXML
    public void addData() {
        if(name.getText().equals("")){
            errMsgSet("Error Dialog", "Please input company name", "");
        }else {
            connect.insertRecordCompany(name.getText());
        loadDataToTable();
    }
    }

    public void errMsgSet(String title, String header, String content) {
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void loadDataToTable() {
        ObservableList<CompanyDetail> data = connect.loadManageCompany();
        namelist.setCellValueFactory(new PropertyValueFactory<>("name"));
        complist.setItems(null);
        complist.setItems(data);
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
        loadDataToTable();
    }
}
