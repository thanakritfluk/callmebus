import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        connect.insertRecordCompany(name.getText());
        loadDataToTable();
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
