public class Value {
    private Bus bus;
    private  double latidude;
    private double longtitude;

    public Value(Bus bus, double latidude, double longtitude) {
        this.bus = bus;
        this.latidude = latidude;
        this.longtitude = longtitude;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public double getLatidude() {
        return latidude;
    }

    public void setLatidude(double latidude) {
        this.latidude = latidude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }
}
