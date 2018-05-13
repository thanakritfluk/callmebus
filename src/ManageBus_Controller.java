import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Controller of manage page to handle the action of add and remove data to data base.
 * @author Thanakrit Daorueang,Piyaphol Wiengperm
 */
public class ManageBus_Controller implements Initializable {
    @FXML
    Button main, add;
    @FXML
    ComboBox departfrom;
    @FXML
    ComboBox returnto;
    @FXML
    ComboBox company;
    @FXML
    DatePicker departdate;
    @FXML
    TextField departtime;
    @FXML
    DatePicker returndate;
    @FXML
    TextField returntime;
    @FXML
    TableView<ManagerDetail> manage;
    @FXML
    TableColumn<ManagerDetail, Integer> id;
    @FXML
    TableColumn<ManagerDetail, String> from;
    @FXML
    TableColumn<ManagerDetail, String> to;
    @FXML
    TableColumn<ManagerDetail, String> departinfo;
    @FXML
    TableColumn<ManagerDetail, String> arriveinfo;
    @FXML
    TableColumn<ManagerDetail, String> companyinfo;
    @FXML
    TableColumn<ManagerDetail, Double> cost;
    @FXML
    Button remove;
    @FXML
    RadioButton oneway;
    @FXML
    RadioButton roundtrip;

    private ObservableList<ManagerDetail> data;
    private ObservableList dataCombobox;
    private Jdbc_Manage connect = new Jdbc_Manage();
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    private ToggleGroup toggleGroup = new ToggleGroup();

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        oneway.setToggleGroup(toggleGroup);
        roundtrip.setToggleGroup(toggleGroup);
        connect = new Jdbc_Manage();
        //Load Data
        loadDataToCompany();
        loadDataFromManagebus();
        loadDataForDepart();
        //Sort by dating.
        manage.getSortOrder().add(departinfo);
        //Make textField input in right form
        makeRightFormTextField(departtime);
        makeRightFormTextField(returntime);
    }

    /**
     * Use to handle the text field can only input in the right form.
     * @param textField
     */
    @FXML
    public void makeRightFormTextField(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            //Make it can fill only number and :
            if (newValue.equals(":") || !newValue.matches("\\d*"))
                textField.setText(newValue.replaceAll("[^\\d:]", ""));
            // if it's 5th character then just setText to previous
            if (newValue.length() >= 5) {
                textField.setText(textField.getText().substring(0, 5));
                // if 3th character is not (:) then set it
                if (textField.getText().charAt(2) != ':')
                    textField.setText(textField.getText().substring(0, 2) + ":" + textField.getText().substring(3, 5));
            }
        });
    }

    /**
     * Use to load the province name to combobox.
     */
    @FXML
    public void loadDataForDepart() {
        dataCombobox = connect.loadDepartData();
        departfrom.setItems(null);
        departfrom.setItems(dataCombobox);
        returnto.setItems(null);
        returnto.setItems(dataCombobox);
    }

    /**
     * Use to warning the user input in wrong form.
     * @param title
     * @param header
     * @param content
     */
    public void errMsgSet(String title, String header, String content) {
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Calculated the price by use latitude and longitude.
     * @param depart
     * @param to
     * @return price that have calculated
     */
    public double calPriceFromDistance(String depart, String to) {
        Distance_Cal cal_Distance = new Distance_Cal();
        double depart_lat = Double.valueOf(connect.getTextFromSelectColumn("province_lat", "province_th", "province_name", depart));
        double depart_lon = Double.valueOf(connect.getTextFromSelectColumn("province_lon", "province_th", "province_name", depart));
        double return_lat = Double.valueOf(connect.getTextFromSelectColumn("province_lat", "province_th", "province_name", to));
        double return_lon = Double.valueOf(connect.getTextFromSelectColumn("province_lon", "province_th", "province_name", to));
        double price = price_Calculate(cal_Distance.distance(depart_lat, depart_lon, return_lat, return_lon));
        return price;
    }

    /**
     * This use to handle to add the data to database case of round trip.
     * @param depart
     * @param to
     * @param companyinfo
     * @param price
     */
    public void setAddRoundtripCase(String depart, String to, String companyinfo, double price) {
        if (roundtrip.isSelected()) {
            if (departdate.getValue() != null && returndate.getValue() != null) {
                if (checkDate(departdate, returndate)) {
                    if (departtime.getText().length() == 5 && returntime.getText().length() == 5 && checkTime(departtime, returntime)) {
                        String[] depart_time = departtime.getText().split(":");
                        String[] return_time = returntime.getText().split(":");
                        LocalDateTime departinfo = LocalDateTime.of(departdate.getValue().getYear(), departdate.getValue().getMonth(), departdate.getValue().getDayOfMonth(),
                                Integer.parseInt(depart_time[0]), Integer.parseInt(depart_time[1]), 00);
                        LocalDateTime arriveinfo = LocalDateTime.of(returndate.getValue().getYear(), returndate.getValue().getMonth(), returndate.getValue().getDayOfMonth(),
                                Integer.parseInt(return_time[0]), Integer.parseInt(return_time[1]), 00);
                        connect.insertRecordManage(depart, to, departinfo, arriveinfo, companyinfo, price);
                        clearField();
                        loadDataFromManagebus();
                    }
                   else errMsgSet("Error Dialog", "Please input time", "");
                }
            } else errMsgSet("Error Dialog", "Please select date", "");
        }
    }

    /**
     * Use to handle to add data to database in case one way.
     * @param depart
     * @param to
     * @param companyinfo
     * @param price
     */
    public void setAddOneWayCase(String depart, String to, String companyinfo, double price) {
        if (oneway.isSelected()) {
            if (departdate.getValue() != null) {
                if (departtime.getText().length() == 5) {
                    String[] depart_time = departtime.getText().split(":");
                    LocalDateTime departinfo = LocalDateTime.of(departdate.getValue().getYear(), departdate.getValue().getMonth(),
                            departdate.getValue().getDayOfMonth(), Integer.parseInt(depart_time[0]),
                            Integer.parseInt(depart_time[1]), 00);
                    connect.insertRecordManage(depart, to, departinfo, null, companyinfo, price);
                    clearField();
                    loadDataFromManagebus();
                } else errMsgSet("Error Dialog","Invalid time","");
            } else errMsgSet("Error Dialog", "Please select date", "");
        }
    }

    /**
     * Use to handle the data to database and show error massage.
     */
    @FXML
    public void addData() {
        if (oneway.isSelected() || roundtrip.isSelected()) {
            if (departfrom.getSelectionModel() != null && returnto.getSelectionModel() != null && !departfrom.getSelectionModel().getSelectedItem().toString().equals(returnto.getSelectionModel().getSelectedItem().toString())) {
                String depart = departfrom.getSelectionModel().getSelectedItem().toString();
                String to = returnto.getSelectionModel().getSelectedItem().toString();
                double price = calPriceFromDistance(depart, to);
                if (company.getSelectionModel() != null) {
                    String companyinfo = company.getSelectionModel().getSelectedItem().toString();
                    setAddRoundtripCase(depart, to, companyinfo, price);
                    setAddOneWayCase(depart, to, companyinfo, price);
                }else errMsgSet("Error Dialog", "Please select the company", "");
            }else errMsgSet("Error Dialog", "Please select depart and return in right condition", "");
        }else errMsgSet("Error Dialog", "Please select one way or round trip", "");
        manage.getSortOrder().add(departinfo);
    }

    /**
     * Use to check date that user input.
     * @param departdate
     * @param returndate
     * @return true if date is right and false if wrong.
     */
    public boolean checkDate(DatePicker departdate, DatePicker returndate) {
        if (departdate.getValue().compareTo(returndate.getValue()) > 0) return false;
        else return true;
    }

    public boolean checkTime(TextField text1, TextField text2) {
        String time = text1.getText();
        String[] timeSplit = {};
        if (time.charAt(2) == ':') {
            timeSplit = time.split(":");
            int hour = Integer.parseInt(timeSplit[0]);
            int min = Integer.parseInt(timeSplit[1]);
            if ((hour >= 0 && hour < 24) && (min >= 0 && min < 60)) {
                String time2 = text2.getText();
                String[] timeSplit2 = time2.split(":");
                int hour2 = Integer.parseInt(timeSplit2[0]);
                int min2 = Integer.parseInt(timeSplit2[1]);
                if ((hour2 >= 0 && hour2 < 24) && (min2 >= 0 && min2 < 60)) return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Set the radio button handle.
     */
    public void onewayButtonHandler(){
        returntime.setDisable(true);
        returndate.setDisable(true);
    }

    /**
     * Set the radio button handle.
     */
    public void roundtripButtonHandler(){
        returntime.setDisable(false);
        returndate.setDisable(false);
    }

    /**
     * Use to calculate the price by distance.
     * @param distance
     * @return
     */
    public double price_Calculate(double distance) {
        double price = 0;
        if (distance >= 1000) return price = 1000;
        else if (distance >= 700) return price = 750;
        else if (distance >= 500) return price = 500;
        else if (distance >= 250) return price = 200;
        else if (distance >= 100) return price = 150;
        else return price = 100;
    }

    /**
     * Use to clear all field.
     */
    public void clearField() {
        departtime.clear();
        returntime.clear();
        departdate.getEditor().clear();
        returndate.getEditor().clear();

    }

    /**
     * Use to remove the data base.
     */
    @FXML
    public void removeData() {
        connect.removeRecord("managebus", "id", manage.getSelectionModel().getSelectedItem().getId());
        loadDataFromManagebus();
        manage.getSortOrder().add(departinfo);
    }

    /**
     * Use to load the data to company combobox.
     */
    @FXML
    public void loadDataToCompany() {
        company.setItems(null);
        company.setItems(connect.loadCompanyName());
    }

    /**
     * Use to load all data to table.
     */
    @FXML
    public void loadDataFromManagebus() {
        data = connect.loadAllDataManageBus();
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        from.setCellValueFactory(new PropertyValueFactory<>("depart"));
        to.setCellValueFactory(new PropertyValueFactory<>("arrive"));
        departinfo.setCellValueFactory(new PropertyValueFactory<>("departinfo"));
        arriveinfo.setCellValueFactory(new PropertyValueFactory<>("arriveinfo"));
        companyinfo.setCellValueFactory(new PropertyValueFactory<>("company"));
        cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        manage.setItems(null);
        manage.setItems(data);
    }

    /**
     * Use to handle to change page to management.
     */
    @FXML
    public void back(ActionEvent mouseEvent) {
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.changeScene(mouseEvent, "Management_Interface.fxml");
    }

}
