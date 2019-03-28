import DataTypes.Topic;
import DataTypes.Value;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Publisher extends Node{
    private String myHash=null;
    Md5 md5= new Md5();
    private String brokerIp ;
    private int brokerPort ;
    private int PublisherRange;//defines the range that it will read
    //contains for each busLine id a table with all the positions
    private int rangeStart;
    private int rangeEnd;
    private HashMap<String,ArrayList<String []>> busPositionsHash = new HashMap<>(); //HashMap<busline,buspositions>
    //contains a local copy of the BrockerList
    private ArrayList<Brocker> localBrockerList= null;
    //constructor
    public Publisher(int port, String ip, int start, int end){
        super(port,ip);
        rangeStart=start;
        rangeEnd=end;
    }

    /**this will be the first function called when the publisher is started**/
    public void startPublisher(){
        getBrokerList();
    }

    /**stores In busPositionHash the buslineID an a table with the position
     * **/
    public void readBusInformation(){
        Read r = new Read();
        /**localy stored the busLines**/
        ArrayList<String []> BusLinesArray = r.readBusLines();
        //PublisherRange= the range of busLines responsible
        for(int i=rangeStart; i<rangeEnd;i++){
            /**stores for the specific lineCode/LineId the positions on a ArrayList**/
            String busLineId=BusLinesArray.get(i)[0];
            ArrayList<String []> tempArray = r.readBusPosition(busLineId);
            /**adds busLineId and a table with positions**/
            busPositionsHash.put(BusLinesArray.get(i)[1],tempArray);
        }
        //System.out.println("hashmap size ="+busPositionsHash.size());
    }//end readBusInformation

    /**prints the HashMap busPositionsHash**/
    public void printBusPositionHash(){
        for(String key: busPositionsHash.keySet()){
            System.out.println("Bus: "+key+" has linehashes: ");
            for(int i=0;i<busPositionsHash.get(key).size();i++){
                System.out.println(busPositionsHash.get(key).get(i)[0]+" "+busPositionsHash.get(key).get(i)[1]+" "+busPositionsHash.get(key).get(i)[2]+" "+busPositionsHash.get(key).get(i)[3]+" "+busPositionsHash.get(key).get(i)[4]+" "+busPositionsHash.get(key).get(i)[5]);
            }

        }
    }// end printBusPositionHash

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
    public void push ()  {
        getMyBrokerList();//get the list of brokers in order to find the responsible broker for the topic and stores to the localebrokerlist
        for(String key:busPositionsHash.keySet()){ //for every key=busline search for the broker with same busline
            for(int i=0;i<localBrockerList.size();i++) { //search at local broker list
                int listSize = localBrockerList.get(i).getBrokerRangeMap().get(i).size(); //size of the busLineslist for each broker
                for (int j = 0; j < listSize; j++) { //search to every broker's hashmap's arraylist
                    String bus = localBrockerList.get(i).getBrokerRangeMap().get(i).get(j)[1];

                    if (key.equals(bus)){
                        String ip=localBrockerList.get(i).getIpAddress(); //responsible broker's ip
                        int port = localBrockerList.get(i).getPort(); //responsible broker's port
                        Socket socket=connect(ip, port);
                        ObjectOutputStream out = null;
                        try {
                            out = new ObjectOutputStream(socket.getOutputStream());
                            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                            System.out.println(in.readUTF());
                            out.writeUTF("Push");
                            out.flush();
                            out.writeObject(busPositionsHash.get(key));
                            out.flush();
                            out.close();
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        finally {
                            Disconnect(socket);
                        }
                    }
                }
            }

        }



    }//end push

    public void notitfyFailure(Brocker  broker){ }
    /**setter getter **/
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
        Publisher pub1= new Publisher(4402,"localhost",0,5);
        pub1.readBusInformation();
        pub1.printBusPositionHash();
    }
}//end Class Publisher
