/**
 * Object that contain the detail of booking.
 * @author Thanakrit Daorueang,Piyaphol Wiengperm
 */
public class BookingDetail {
    private String from;
    private String to;
    private String depart_time;
    private String arrive_time;
    private String company;
    private String busclass;
    private String ticketcost;
    private String addcost;
    private String totalcost;
    private String seat;

    public BookingDetail(String from, String to, String depart_time, String arrive_time, String company, String busclass, String ticketcost, String addcost, String totalcost, String seat) {
        this.from = from;
        this.to = to;
        this.depart_time = depart_time;
        this.arrive_time = arrive_time;
        this.company = company;
        this.busclass = busclass;
        this.ticketcost = ticketcost;
        this.addcost = addcost;
        this.totalcost = totalcost;
        this.seat = seat;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getDepart_time() {
        return depart_time;
    }

    public String getArrive_time() {
        return arrive_time;
    }

    public String getCompany() {
        return company;
    }

    public String getBusclass() {
        return busclass;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setDepart_time(String depart_time) {
        this.depart_time = depart_time;
    }

    public void setArrive_time(String arrive_time) {
        this.arrive_time = arrive_time;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setBusclass(String busclass) {
        this.busclass = busclass;
    }

    public void setTicketcost(String ticketcost) {
        this.ticketcost = ticketcost;
    }

    public void setAddcost(String addcost) {
        this.addcost = addcost;
    }

    public void setTotalcost(String totalcost) {
        this.totalcost = totalcost;
    }

    public String getTicketcost() {
        return ticketcost;
    }

    public String getAddcost() {
        return addcost;
    }

    public String getTotalcost() {
        return totalcost;
    }

    public String getSeat() {
        return seat;
    }
}
