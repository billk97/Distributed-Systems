/**The Bus class represents a Bus that a publisher will pulling from the data
 * BusName: the name of the bus example: B12
 * BusStop : the current/nearest Bus Stop (Suggestion)
 * Latitude: of the bus for a given time
 * Longitude : of the bus for a given time **/
public class Bus {
    private String BusName;
    private String BusStop;
    private double Latitude;
    private double Longitude;
    //creating a empty constructor in case its needed
    public Bus(){}
    //creating a constructor with attributes inside
    public Bus (String BusName , double Latitude, double Longitude, String BusStop){
        this.BusName=BusName;
        this.BusStop=BusStop;
        this.Latitude=Latitude;
        this.Longitude = Longitude;
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

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getBusStop() {
        return BusStop;
    }

    public void setBusStop(String busStop) {
        BusStop = busStop;
    }
    public String toString()
    {
        return "Bus Name: " + BusName + " Latitude: " +Latitude + " Longitude: " + Longitude + " Bus Stop: "+ BusStop  ;
    }
}//end class Bus

