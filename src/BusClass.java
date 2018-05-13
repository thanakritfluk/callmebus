/**
 * Enum class contain the bus class and price of each class.
 * @author Thanakrit Daorueang,Piyaphol Wiengperm
 */
public enum BusClass {
    First(400),
    Business(200),
    Economy(100);

    private final double value;

    BusClass(double value){
        this.value = value;
    }

    public double getValue(){
        return value;
    }
}
