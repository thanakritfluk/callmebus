import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Booking_Controller implements Initializable {
    private static BookingDetail bookingDetail;
    @FXML
    Button bookbutton;
    @FXML
    RadioButton oneway;
    @FXML
    RadioButton roundtrip;
    @FXML
    ComboBox depart;
    @FXML
    ComboBox arrive;
    @FXML
    ComboBox datetime;
    @FXML
    Label company;
    @FXML
    ComboBox seat;
    @FXML
    ComboBox<BusClass> busclass;

//    public   BookingDetail bookingDetail;
    private Jdbc_Manage connect = new Jdbc_Manage();
    private ObservableList<Object> dataCombobox;
    private ToggleGroup group = new ToggleGroup();
    private List<ManagerDetail> managerDetailsList;


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
        managerDetailsList = loadAllData();
        oneway.setToggleGroup(group);
        roundtrip.setToggleGroup(group);
        loadDataDepart();
        depart.setOnAction(getDateEventHandler());
        arrive.setOnAction(getDateEventHandler());
        setCompany();
        setBusClass();
        setSeat();
    }

    public void setCompany() {
        datetime.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                company.setText(null);
                for (int i = 0; i < managerDetailsList.size(); i++) {
                    String arr = "Depart: " +managerDetailsList.get(i).getDepartinfo()  + " Arrive: " +managerDetailsList.get(i).getArriveinfo() ;
                    if (arr.contains(datetime.getSelectionModel().getSelectedItem().toString())) {
                        company.setText(managerDetailsList.get(i).getCompany());

                    }
                }
            }
        });
    }

    public EventHandler getDateEventHandler() {
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void handle(Event event) {
                datetime.getItems().clear();
                for (ManagerDetail managerDetail : managerDetailsList) {
                    if (arrive.getSelectionModel().getSelectedItem() == null) return;
                    if (depart.getSelectionModel().getSelectedItem() == null) return;
                    if (managerDetail.getDepart().equals(depart.getSelectionModel().getSelectedItem()) && managerDetail.getArrive().equals(arrive.getSelectionModel().getSelectedItem())) {
                        datetime.setPromptText("");
                        String arr = "Depart: " + managerDetail.getDepartinfo() + " Arrive: " + managerDetail.getArriveinfo();
                        datetime.getItems().add(arr);
                    }
                }
                if (datetime.getItems().isEmpty()) datetime.setPromptText("No time for routing");
            }
        };
        return eventHandler;
    }


    public List<ManagerDetail> loadAllData() {
        ArrayList<ManagerDetail> list = new ArrayList<>();
        Connection connection = connect.Connect();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM managebus");
            while (rs.next()) {
                ManagerDetail managerDetail = new ManagerDetail(rs.getInt("id"),
                        rs.getString("depart"),
                        rs.getString("arrive"),
                        rs.getString("departinfo"),
                        rs.getString("arriveinfo"),
                        rs.getString("company"),
                        rs.getDouble("cost"));
                list.add(managerDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void loadDataDepart() {

        try {
            Connection connection = connect.Connect();
            dataCombobox = FXCollections.observableArrayList();
            // Execute query and store result in a resultset
            ResultSet rs = connection.createStatement().executeQuery("SELECT province_name FROM province_th");
            while (rs.next()) {
                //get string from db,whichever way
                dataCombobox.add(rs.getString("province_name"));
            }

        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }

        depart.setItems(null);
        depart.setItems(dataCombobox);
        arrive.setItems(null);
        arrive.setItems(dataCombobox);
    }

    @FXML
    public void backToMenu(MouseEvent mouseEvent) {
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.changeScene(mouseEvent, "Main_Interface.fxml");
        System.out.println("Back To Menu");
    }



    public void setSeat(){
        seat.getItems().addAll(1,2,3,4,5);
        seat.getSelectionModel();
    }

    public void setBusClass(){
        busclass.getItems().addAll(BusClass.values());
        busclass.getSelectionModel();
    }

    public double calculateTicketCost(String from,String to){
        double depart_lat = Double.valueOf(connect.getTextFromSelectColumn("province_lat", "province_th", "province_name", from));
        double depart_lon = Double.valueOf(connect.getTextFromSelectColumn("province_lon", "province_th", "province_name", from));
        double return_lat = Double.valueOf(connect.getTextFromSelectColumn("province_lat", "province_th", "province_name", to));
        double return_lon = Double.valueOf(connect.getTextFromSelectColumn("province_lon", "province_th", "province_name", to));
        Distance_Cal cal_Distance = new Distance_Cal();
        double price = price_Calculate(cal_Distance.distance(depart_lat, depart_lon, return_lat, return_lon));
        return price;
    }

    public double price_Calculate(double distance){
        double price = 0;
        if (distance >= 1000) return price = 1000;
        else if (distance >= 700) return price = 750;
        else if (distance >= 500) return price = 500;
        else if (distance >= 250) return price = 200;
        else if (distance >= 100) return price = 150;
        else return price = 100;
    }

    public String totalCost(double ticketCost,double addCost,double seat){
        return String.valueOf((ticketCost+addCost)*seat);
    }

    public void setBookingDetail(){
        double ticketCost = calculateTicketCost(depart.getSelectionModel().getSelectedItem().toString(),arrive.getSelectionModel().getSelectedItem().toString());
        String[] dateTime = datetime.getSelectionModel().getSelectedItem().toString().split("A");
        double seats = Double.parseDouble(seat.getSelectionModel().getSelectedItem().toString());
        bookingDetail = new BookingDetail(depart.getSelectionModel().getSelectedItem().toString(),arrive.getSelectionModel().getSelectedItem().toString(),
                dateTime[0].replaceAll("Depart: ","").toString(),
                dateTime[1].replaceAll("rrive: ","").toString(),
                company.getText(),busclass.getSelectionModel().getSelectedItem().toString(),
                String.valueOf(ticketCost*seats),String.valueOf(busclass.getSelectionModel().getSelectedItem().getValue()*seats),
                totalCost(ticketCost,busclass.getSelectionModel().getSelectedItem().getValue(),seats),
                seat.getSelectionModel().getSelectedItem().toString());
    }

    public static BookingDetail getBookingDetail(){
        return bookingDetail;
    }

    public void handleBooking(ActionEvent mouseEvent){
        setBookingDetail();
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.changeScene(mouseEvent,"Payment.fxml");
        System.out.println("Go to payment window.");

    }
}
