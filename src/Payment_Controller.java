import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Payment_Controller implements Initializable {
    @FXML
    Button cancel;
    @FXML
    Button accept;
    @FXML
    Label company;
    @FXML
    Label busclass;
    @FXML
    Label from;
    @FXML
    Label to;
    @FXML
    Label depart_date;
    @FXML
    Label return_date;
    @FXML
    Label totalcost;
    @FXML
    Label addcost;
    @FXML
    Label ticketcost;
    @FXML
    Label seat;
    private final SceneChanger sceneChanger = new SceneChanger();

    public void cancelButtonHandler(ActionEvent mouseEvent){
        sceneChanger.changeScene(mouseEvent, "Booking_Interface.fxml");
    }

    public void acceptButtonHandler(){
        ticketToPDF();
    }

    public void ticketToPDF(){
        BookingDetail bookingDetail = Booking_Controller.getBookingDetail();
        Document document = new Document(PageSize.A5);
        FileChooser fileChooser;
        File output = null;
        try{
            fileChooser = new FileChooser();
            fileChooser.setInitialFileName("TicketDetail.pdf");
            fileChooser.setInitialDirectory(new File("src"));
            output = fileChooser.showSaveDialog(new Stage());

            PdfWriter.getInstance(document, new FileOutputStream(output));
            document.open();

            Paragraph titleShow = new Paragraph("Ticket Detail", FontFactory.getFont(FontFactory.HELVETICA,16, Font.BOLD));
            titleShow.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(titleShow);
            LocalDate date = LocalDate.now();
            Paragraph dateShow = new Paragraph(date.toString());
            dateShow.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(dateShow);
            String company = String.format("%s%40s","Coach Company:",bookingDetail.getCompany());
            Paragraph companyShow = new Paragraph(company,FontFactory.getFont(FontFactory.HELVETICA,14,Font.BOLD));
            companyShow.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(companyShow);
            String busclass = String.format("%s%51s","Class:",bookingDetail.getBusclass());
            Paragraph classShow = new Paragraph(busclass, FontFactory.getFont(FontFactory.HELVETICA,14, Font.BOLD));
            classShow.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(classShow);
            String from = String.format("%s%51s","From:",bookingDetail.getFrom());
            Paragraph fromShow = new Paragraph(from,FontFactory.getFont(FontFactory.HELVETICA,14,Font.BOLD));
            fromShow.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(fromShow);
            String to = String.format("%s%53s","To:",bookingDetail.getTo());
            Paragraph toShow = new Paragraph(to,FontFactory.getFont(FontFactory.HELVETICA,14,Font.BOLD));
            toShow.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(toShow);
            String depart_time = String.format("%s%52s","Depart Time:",bookingDetail.getDepart_time());
            Paragraph departTime = new Paragraph(depart_time,FontFactory.getFont(FontFactory.HELVETICA,14,Font.BOLD));
            departTime.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(departTime);
            String return_time = String.format("%s%51s","Return Time:",bookingDetail.getArrive_time());
            Paragraph returnTime = new Paragraph(return_time,FontFactory.getFont(FontFactory.HELVETICA,14,Font.BOLD));
            returnTime.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(returnTime);
            String seat = String.format("%s%30s","Number of seats:",bookingDetail.getSeat());
            Paragraph seatShow = new Paragraph(seat,FontFactory.getFont(FontFactory.HELVETICA,14,Font.BOLD));
            seatShow.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(seatShow);
            String ticket_cost = String.format("%s%39s","Ticket Cost:",bookingDetail.getTicketcost());
            Paragraph ticketCost = new Paragraph(ticket_cost,FontFactory.getFont(FontFactory.HELVETICA,14,Font.BOLD));
            ticketCost.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(ticketCost);
            String add_cost = String.format("%s%32s","Additional Cost:",bookingDetail.getAddcost());
            Paragraph addCost = new Paragraph(add_cost,FontFactory.getFont(FontFactory.HELVETICA,14,Font.BOLD));
            addCost.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(addCost);
            String total_cost = String.format("%s%41s","Total Cost:",bookingDetail.getTotalcost());
            Paragraph totalCost = new Paragraph(total_cost,FontFactory.getFont(FontFactory.HELVETICA,14,Font.BOLD));
            totalCost.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(totalCost);

            document.close();
        }catch (DocumentException ex){
            ex.printStackTrace();
        }catch (FileNotFoundException e) {
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
        BookingDetail bookingDetail = Booking_Controller.getBookingDetail();
        from.setText(bookingDetail.getFrom());
        to.setText(bookingDetail.getTo());
        depart_date.setText(bookingDetail.getDepart_time().replaceAll("Depart: ",""));
        return_date.setText(bookingDetail.getArrive_time().replaceAll("rrive: ",""));
        company.setText(bookingDetail.getCompany());
        busclass.setText(bookingDetail.getBusclass());
        ticketcost.setText(bookingDetail.getTicketcost());
        addcost.setText(bookingDetail.getAddcost());
        totalcost.setText(bookingDetail.getTotalcost());
        seat.setText(bookingDetail.getSeat());
    }
}
