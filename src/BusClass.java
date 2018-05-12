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
