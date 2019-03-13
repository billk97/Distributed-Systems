/**this class represents a NodeImpl/machine connected to the network and our programme
 * Name: from the host name
 * IpAddress: the ipAddress of the machine
 * Type: Broker, Consumer, Publisher
 * Port: the port that the host is listening to
 * Hash the Hash of the Ip&Port
 * ADD HEAR !!!!!!! CPU/RAM USAGE**/
public class NodeImpl {
    private String Name;
    private String IpAddress;
    private String Type;
    private int Port;
    private int Hash;
    //TODO implement the two parameters bellow
    //private double CpuUsage;
    //private double RamUsage;
    //creating a empty constructor in case its needed
    public NodeImpl(){}
    //creating a constructor with attributes inside
    public NodeImpl(String Name, String IpAddress, String Type, int Port , int Hash){
        this.Name=Name;
        this.IpAddress=IpAddress;
        this.Type=Type;
        this.Port=Port;
        this.Hash=Hash;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIpAddress() {
        return IpAddress;
    }

    public void setIpAddress(String ipAddress) {
        IpAddress = ipAddress;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public int getPort() {
        return Port;
    }

    public void setPort(int port) {
        Port = port;
    }

    public int getHash() {
        return Hash;
    }

    public void setHash(int hash) {
        Hash = hash;
    }
    public String toString(){
        return "Name: "+ Name+" Ip Address: "+ IpAddress+ " Type: "+ Type + " Port: "+ Port +" Hash: "+ Hash;
    }
}//end class NodeImpl
