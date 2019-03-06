public class Bus {
    String BusName;
    double Latitude;
    double Lontitude;
    String BusStop;

    public Bus (String BusName , double Latitude, double Lontitude ,String BusStop){
        this.BusName=BusName;
        this.BusStop=BusStop;
        this.Latitude=Latitude;
        this.Lontitude=Lontitude;
    }

    public String getBusName() {
        return BusName;
    }

    public void setBusName(String busName) {
        BusName = busName;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLontitude() {
        return Lontitude;
    }

    public void setLontitude(double lontitude) {
        Lontitude = lontitude;
    }

    public String getBusStop() {
        return BusStop;
    }

    public void setBusStop(String busStop) {
        BusStop = busStop;
    }
    public String toString()
    {
        return "Bus Name: " + BusName + " Latitude: " +Latitude + " Longitude: " + Lontitude + " Bus Stop: "+ BusStop  ;
    }
}

