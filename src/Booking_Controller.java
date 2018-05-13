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

    public EventHandler getOneWayDateEventHandler() {
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void handle(Event event) {
                oneWay();
            }
        };
        return eventHandler;
    }

    void oneWay() {
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

    public EventHandler getRoundTripDateEventHandler() {
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void handle(Event event) {
                roundTrip();
            }
        };
        return eventHandler;
    }

    void roundTrip() {
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

    public void loadDataDepart() {
        ObservableList<Object> dataCombobox = connect.loadDepartData();
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


    public void setSeat() {
        seat.getItems().addAll(1, 2, 3, 4, 5);
        seat.getSelectionModel();
    }

    public void setBusClass() {
        busclass.getItems().addAll(BusClass.values());
        busclass.getSelectionModel();
    }

    public double calculateTicketCost(String from, String to) {
        double depart_lat = Double.valueOf(connect.getTextFromSelectColumn("province_lat", "province_th", "province_name", from));
        double depart_lon = Double.valueOf(connect.getTextFromSelectColumn("province_lon", "province_th", "province_name", from));
        double return_lat = Double.valueOf(connect.getTextFromSelectColumn("province_lat", "province_th", "province_name", to));
        double return_lon = Double.valueOf(connect.getTextFromSelectColumn("province_lon", "province_th", "province_name", to));
        Distance_Cal cal_Distance = new Distance_Cal();
        double price = price_Calculate(cal_Distance.distance(depart_lat, depart_lon, return_lat, return_lon));
        return price;
    }

    public double price_Calculate(double distance) {
        double price = 0;
        if (distance >= 1000) return price = 1000;
        else if (distance >= 700) return price = 750;
        else if (distance >= 500) return price = 500;
        else if (distance >= 250) return price = 200;
        else if (distance >= 100) return price = 150;
        else return price = 100;
    }

    public String totalCost(double ticketCost, double addCost, double seat) {
        return String.valueOf((ticketCost + addCost) * seat);
    }

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


    public static BookingDetail getBookingDetail() {
        return bookingDetail;
    }

    public void handleBooking(ActionEvent mouseEvent) {
        setBookingDetail();
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.changeScene(mouseEvent, "Payment.fxml");
        System.out.println("Go to payment window.");

    }
}
