/**
 * ManagerDetail is the objects the use to collect the information about management.
 * @author Thanakrit Daorueang,Piyaphol Wiengperm
 */
public class ManagerDetail {
    private int id;
    private String depart;
    private String arrive;
    private String departinfo;
    private String arriveinfo;
    private String company;
    private double cost;

    /**
     * Constructor of managerdetail.
     * @param id
     * @param depart
     * @param arrive
     * @param departinfo
     * @param arriveinfo
     * @param company
     * @param cost
     */
    public ManagerDetail(int id, String depart, String arrive, String departinfo, String arriveinfo, String company, double cost) {
        this.id = (id);
        this.depart = (depart);
        this.arrive = (arrive);
        this.departinfo =(departinfo);
        this.arriveinfo = (arriveinfo);
        this.company = (company);
        this.cost = (cost);
    }

    public int getId() {
        return id;
    }

    public String getDepart() {
        return depart;
    }

    public String getArrive() {
        return arrive;
    }

    public String getDepartinfo() {
        return departinfo;
    }

    public String getArriveinfo() {
        return arriveinfo;
    }

    public String getCompany() {
        return company;
    }

    public double getCost() {
        return cost;
    }
}
