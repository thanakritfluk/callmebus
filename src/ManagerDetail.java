import javafx.beans.property.*;

public class ManagerDetail {
    private IntegerProperty id;
    private StringProperty depart;
    private StringProperty arrive;
    private StringProperty departinfo;
    private StringProperty arriveinfo;
    private StringProperty company;
    private DoubleProperty cost;

    public ManagerDetail(int id, String depart, String arrive, String departinfo, String arriveinfo, String company, double cost) {
        this.id = new SimpleIntegerProperty(id);
        this.depart = new SimpleStringProperty(depart);
        this.arrive = new SimpleStringProperty(arrive);
        this.departinfo = new SimpleStringProperty(departinfo);
        this.arriveinfo = new SimpleStringProperty(arriveinfo);
        this.company = new SimpleStringProperty(company);
        this.cost = new SimpleDoubleProperty(cost);

    }


    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getDepart() {
        return depart.get();
    }

    public StringProperty departProperty() {
        return depart;
    }

    public String getArrive() {
        return arrive.get();
    }

    public StringProperty arriveProperty() {
        return arrive;
    }

    public String getDepartinfo() {
        return departinfo.get();
    }

    public StringProperty departinfoProperty() {
        return departinfo;
    }

    public String getArriveinfo() {
        return arriveinfo.get();
    }

    public StringProperty arriveinfoProperty() {
        return arriveinfo;
    }

    public String getCompany() {
        return company.get();
    }

    public StringProperty companyProperty() {
        return company;
    }

    public double getCost() {
        return cost.get();
    }

    public DoubleProperty costProperty() {
        return cost;
    }
}
