import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller of booking page have method to set items in combobox realtime such as
 * when select one way in combobox will have just depart time.
 * @author Thanakrit Daorueang,Piyaphol Wiengperm
 */
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
    ComboBox company;
    @FXML
    ComboBox seat;
    @FXML
    ComboBox<BusClass> busclass;

    private Jdbc_Manage connect = new Jdbc_Manage();
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
        managerDetailsList = connect.loadAllData();
        oneway.setToggleGroup(group);
        roundtrip.setToggleGroup(group);
        loadDataDepart();

        roundtrip.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * Invoked when a specific event of the type for which this handler is
             * registered happens.
             *
             * @param event the event which occurred
             */
            @Override
            public void handle(ActionEvent event) {
                roundTrip();
                depart.setOnAction(getRoundTripDateEventHandler());
                arrive.setOnAction(getRoundTripDateEventHandler());
                setRoundTripCompany();
            }
        });
        oneway.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * Invoked when a specific event of the type for which this handler is
             * registered happens.
             *
             * @param event the event which occurred
             */
            @Override
            public void handle(ActionEvent event) {
                oneWay();
                depart.setOnAction(getOneWayDateEventHandler());
                arrive.setOnAction(getOneWayDateEventHandler());
                setOneWayCompany();
            }
        });
        setBusClass();
        setSeat();
    }

    /**
     * Set the one way data time by company in combobox with set on date time is on action .
     */
    public void setOneWayCompany() {
        datetime.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                company.getItems().clear();
                for (int i = 0; i < managerDetailsList.size(); i++) {
                    String arr = "Depart: " + managerDetailsList.get(i).getDepartinfo();
                    if (arr.contains(datetime.getSelectionModel().getSelectedItem().toString())) {
                        company.getItems().add(managerDetailsList.get(i).getCompany());

                    }
                }
            }
        });
    }

    /**
     * Set the round trip data time by company in combobox with set on date time is on action .
     */
    public void setRoundTripCompany() {
        datetime.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                company.getItems().clear();
                for (int i = 0; i < managerDetailsList.size(); i++) {
                    String arr = "Depart: " + managerDetailsList.get(i).getDepartinfo() + " Arrive: " + managerDetailsList.get(i).getArriveinfo();
                        if (arr.contains(datetime.getSelectionModel().getSelectedItem().toString())) {
                            company.getItems().add(managerDetailsList.get(i).getCompany());
                        }
                }
            }
        });
    }

    /**
     * Use to set the date time combobox by method one way to check the available
     * @return Event handler
     */
    public EventHandler getOneWayDateEventHandler() {
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void handle(Event event) {
                oneWay();
            }
        };
        return eventHandler;
    }

    /**
     * Method to check the available date time.
     */
    public void oneWay() {
        datetime.getItems().clear();
        for (ManagerDetail managerDetail : managerDetailsList) {
            if (depart.getSelectionModel().getSelectedItem() == null) return;
            if (arrive.getSelectionModel().getSelectedItem() == null) return;
            if (managerDetail.getDepart().equals(depart.getSelectionModel().getSelectedItem()) && managerDetail.getArrive().equals(arrive.getSelectionModel().getSelectedItem())
                    && managerDetail.getArriveinfo() == null) {
                datetime.setPromptText("");
                    String arr = "Depart: " + managerDetail.getDepartinfo();
                    datetime.getItems().add(arr);
            }
        }
        if (datetime.getItems().isEmpty()) datetime.setPromptText("No time for routing");
    }

    /**
     * Use to set the date time combobox by method round trip to check the available
     * @return Event handler
     */
    public EventHandler getRoundTripDateEventHandler() {
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void handle(Event event) {
                roundTrip();
            }
        };
        return eventHandler;
    }

    /**
     * Method to check the available date time.
     */
    public void roundTrip() {
        datetime.getItems().clear();
        for (ManagerDetail managerDetail : managerDetailsList) {
            if (arrive.getSelectionModel().getSelectedItem() == null) return;
            if (depart.getSelectionModel().getSelectedItem() == null) return;
            if (managerDetail.getDepart().equals(depart.getSelectionModel().getSelectedItem()) && managerDetail.getArrive().equals(arrive.getSelectionModel().getSelectedItem())
                    && managerDetail.getArriveinfo() != null) {
                datetime.setPromptText("");
                String arr = "Depart: " + managerDetail.getDepartinfo() + " Arrive: " + managerDetail.getArriveinfo();
                datetime.getItems().add(arr);
            }
        }
        if (datetime.getItems().isEmpty()) datetime.setPromptText("No time for routing");
    }

    /**
     * Use to load all province in to combobox.
     */
    public void loadDataDepart() {
        ObservableList<Object> dataCombobox = connect.loadDepartData();
        depart.setItems(null);
        depart.setItems(dataCombobox);
        arrive.setItems(null);
        arrive.setItems(dataCombobox);
    }

    /**
     * Use to handle change to main page.
     */
    @FXML
    public void backToMenu(MouseEvent mouseEvent) {
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.changeScene(mouseEvent, "Main_Interface.fxml");
        System.out.println("Back To Menu");
    }


    /**
     * Set the amount of seat.
     */
    public void setSeat() {
        seat.getItems().addAll(1, 2, 3, 4, 5);
        seat.getSelectionModel();
    }

    /**
     * Use to set the combobox of bus class by input the enum.
     */
    public void setBusClass() {
        busclass.getItems().addAll(BusClass.values());
        busclass.getSelectionModel();
    }

    /**
     * Use to calculated price by latitude and longitude.
     * @param from
     * @param to
     * @return price of calculated
     */
    public double calculateTicketCost(String from, String to) {
        double depart_lat = Double.valueOf(connect.getTextFromSelectColumn("province_lat", "province_th", "province_name", from));
        double depart_lon = Double.valueOf(connect.getTextFromSelectColumn("province_lon", "province_th", "province_name", from));
        double return_lat = Double.valueOf(connect.getTextFromSelectColumn("province_lat", "province_th", "province_name", to));
        double return_lon = Double.valueOf(connect.getTextFromSelectColumn("province_lon", "province_th", "province_name", to));
        Distance_Cal cal_Distance = new Distance_Cal();
        double price = price_Calculate(cal_Distance.distance(depart_lat, depart_lon, return_lat, return_lon));
        return price;
    }

    /**
     * Use to calculated by distance.
     * @param distance
     * @return price of calculated
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
     * Return the price of finish calculated.
     * @param ticketCost
     * @param addCost
     * @param seat
     * @return price
     */
    public String totalCost(double ticketCost, double addCost, double seat) {
        return String.valueOf((ticketCost + addCost) * seat);
    }

    /**
     * Use to set the booking detail before sent to payment page.
     */
    public void setBookingDetail() {
        double ticketCost = calculateTicketCost(depart.getSelectionModel().getSelectedItem().toString(), arrive.getSelectionModel().getSelectedItem().toString());
        double seats = Double.parseDouble(seat.getSelectionModel().getSelectedItem().toString());

        String companyBook = company.getSelectionModel().getSelectedItem().toString();
        String departBook = depart.getSelectionModel().getSelectedItem().toString();
        String returnBook = arrive.getSelectionModel().getSelectedItem().toString();
        String busclassBook = busclass.getSelectionModel().getSelectedItem().toString();
        String ticketcostBook = String.valueOf(ticketCost * seats);
        String addcostBook = String.valueOf(busclass.getSelectionModel().getSelectedItem().getValue() * seats);
        String totalcostBook = totalCost(ticketCost, busclass.getSelectionModel().getSelectedItem().getValue(), seats);
        String seatBook = seat.getSelectionModel().getSelectedItem().toString();
        if(roundtrip.isSelected()) {
            String[] dateTime = datetime.getSelectionModel().getSelectedItem().toString().split("A");
            String departDate = dateTime[0].replaceAll("Depart: ", "").toString();
            String returnDate = dateTime[1].replaceAll("rrive: ", "").toString();
            bookingDetail = new BookingDetail(departBook,returnBook,departDate,returnDate,companyBook,busclassBook,ticketcostBook,addcostBook,totalcostBook,seatBook);

        } else {
            String dateTime = datetime.getSelectionModel().getSelectedItem().toString();
            bookingDetail = new BookingDetail(departBook, returnBook, dateTime.replaceAll("Depart: ", "").toString(), "", companyBook, busclassBook, ticketcostBook, addcostBook, totalcostBook, seatBook);
        }
    }


    /**
     * Return the booking detail to use in another class
     */
    public static BookingDetail getBookingDetail() {
        return bookingDetail;
    }

    /**
     * Use to change scene to payment page.
     */
    public void handleBooking(ActionEvent mouseEvent) {
        setBookingDetail();
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.changeScene(mouseEvent, "Payment.fxml");
        System.out.println("Go to payment window.");

    }
}
