import DataTypes.Bus;
import DataTypes.Topic;
import DataTypes.Value;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Publisher extends Node{
    private String myHash=null;
    Md5 md5= new Md5();
    private String brokerIp ;
    private int brokerPort ;
    private int PublisherRange=5;
    private HashMap<String,ArrayList<String []>> busPositionsHash = new HashMap<>();
    private ArrayList<Brocker> localBrockerList= null;

    public Publisher(int port, String ip){
        super(port,ip);
    }

    public void readBusInformation(){
        Read r = new Read();
        ArrayList<String []> BusLinesArray = r.readBusLines();
        for(int i=0; i<PublisherRange;i++){
            ArrayList<String []> tempArray = r.readBusPosition(BusLinesArray.get(i)[0]);
            busPositionsHash.put(BusLinesArray.get(i)[1],tempArray);
        }
        //System.out.println("hashmap size ="+busPositionsHash.size());
    }

    public void printBusPostionHash(){
        for(String key: busPositionsHash.keySet()){
            System.out.println("Bus: "+key+" has linehashes: ");
            for(int i=0;i<busPositionsHash.get(key).size();i++){
                System.out.println(busPositionsHash.get(key).get(i)[0]+" "+busPositionsHash.get(key).get(i)[1]+" "+busPositionsHash.get(key).get(i)[2]+" "+busPositionsHash.get(key).get(i)[3]+" "+busPositionsHash.get(key).get(i)[4]+" "+busPositionsHash.get(key).get(i)[5]);
            }

        }
    }




    public void startPublisher(){
        getBrokerList();
    }

    /**this function gets the list of all Brokers via tcp connection **/
    public void getMyBrokerList(){
        Socket socket = connect(brokerIp,brokerPort);
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            System.out.println(in.readUTF());
            out.writeUTF("BrokerList");
            out.flush();
            localBrockerList=new ArrayList<Brocker>((ArrayList<Brocker>)in.readObject());
            System.out.println("broker1: " + localBrockerList.get(0).ipAddress);
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Server sims down");
            e.printStackTrace();
        }
        finally {
            Disconnect(socket);

        }
    }//end getBrokerList
    /**takes 2 Objects as arguments
     * value(bus,latitude,lontitude)
     * topic(busLine)
     * and sends them to the broker**/
    public void push (Value value ,Topic topic)  {
        Socket socket=connect(brokerIp, brokerPort);
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            System.out.println(in.readUTF());
            out.writeUTF("Push");
            out.flush();
            out.writeObject(topic);
            out.flush();
            out.writeObject(value);
            out.flush();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            Disconnect(socket);
        }
    }//end push

    public void notitfyFailure(Brocker  broker){

    }
    public void setMyHash(String myHash) {
        this.myHash = myHash;
    }

    public String getBrokerIp() {
        return brokerIp;
    }

    public void setBrokerIp(String brokerIp) {
        this.brokerIp = brokerIp;
    }

    public int getBrokerPort() {
        return brokerPort;
    }

    public void setBrokerPort(int brokerPort) {
        this.brokerPort = brokerPort;
    }

    public static void main(String[] args) {
        Publisher pub1= new Publisher(4402,"local host");
        pub1.readBusInformation();
        pub1.printBusPostionHash();
    }
}//end Class Publisher
