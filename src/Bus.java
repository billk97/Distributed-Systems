import java.io.Serializable;

/**The Bus class represents a Bus that a publisher will pulling from the data
 * lineNumber: the name of the bus example: B12
 * routeCode : the current/nearest Bus Stop (Suggestion)
 * vehicleId: of the bus for a given time
 * lineName : of the bus for a given time **/
public class Bus implements Serializable {
    private static final long serialVersionUID = -8463229278480288053L;
    private String lineNumber;
    private String routeCode;
    private String vehicleId;
    private String lineName;
    private String busLineId;
    private String info;
    //creating a empty constructor in case its needed
    public Bus(){}
    //creating a constructor with attributes inside
    public Bus (String lineNumber, String vehicleId, String lineName, String routeCode,String busLineId, String info){
        this.lineNumber = lineNumber;
        this.routeCode = routeCode;
        this.vehicleId = vehicleId;
        this.lineName = lineName;
        this.busLineId=busLineId;
        this.info=info;
    }

    public String getBusLineId() {
        return busLineId;
    }

    public void setBusLineId(String busLineId) {
        this.busLineId = busLineId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }
    public String toString()
    {
        return "Bus Name: " + lineNumber + " vehicleId: " + vehicleId + " lineName: " + lineName + " Bus Stop: "+ routeCode;
    }
}//end class Bus

