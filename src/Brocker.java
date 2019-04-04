import DataTypes.Bus;
import DataTypes.Topic;
import DataTypes.Value;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Brocker extends Node implements Runnable , Serializable {
    private static final long serialVersionUID = -1799537022412025503L;
    private ArrayList<Subscriber> registeredSubscribers = new ArrayList<Subscriber>();
    private ArrayList<Publisher> registerPublisher= new ArrayList<Publisher>();
    private String brokerRange =null;
    protected ArrayList<String []> brokerRangeList = new ArrayList<>();
    private ArrayList<String[]> RemoteBrokers = new ArrayList<String[]>();
    private HashMap<String,ArrayList<String []>> localBusPositionsHashMap = new HashMap<>();
    /**contains the position recieved from publisher**/
    private Socket socket;
    private int brokerId;
    private String brokerHash;
    public void add(String[] temp){
        RemoteBrokers.add(temp);
    }
    //constructor
    public Brocker(int port,String ip){
        super(port,ip);
        //BrokerList.add(this);
    }
    //constructor
    public Brocker(Socket socket,ArrayList<Brocker> BrokerList,ArrayList<String []>brokerRangeList,ArrayList<String []> BusLinesArray,HashMap<String,ArrayList<String []>> localBusPositionsHashMap){
        this.socket=socket;
        this.BrokerList=BrokerList;
        this.brokerRangeList=brokerRangeList;
        this.BusLinesArray=BusLinesArray;
        this.localBusPositionsHashMap=localBusPositionsHashMap;
    }

    public void run(){
        brokerListener(socket);
        System.out.println(socket.getInetAddress());
    }

    /**A socket is not a port!!!!  you open a socket to listen and when a connection request
     * is send then a new Socket each time gets created and listen in the same port!!!!**/
    //todo find out why the BrokerList has more objects than it should
    public void startServer(){
        ServerSocket listenerSocket =null;
        Socket connection=null;
        BrokerList.add(this);
        initialize();
        System.out.println(BusLinesArray.size());
        System.out.println("Brocker:"+ BrokerList.size());
        //todo inside a for for each element in the arraylist
        for(String [] b1 :RemoteBrokers){
            new Thread(new BrokerConnect(b1[0],Integer.parseInt(b1[1]),Integer.parseInt(b1[2]))).start();
        }
        try {
            listenerSocket= new ServerSocket(port);//a new Socket is created for the specific port
            while (true){
                /**connection is accepted that means a new socket and now a new port has been created for the communication **/
                System.out.println("Server up and  waiting");
                connection =listenerSocket.accept();
                Thread t = new Thread(new Brocker(connection,getBrokerList(),brokerRangeList,BusLinesArray,localBusPositionsHashMap));
                t.start();
                t.join();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }//end startServer

    public void brokerListener(Socket socket1){
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket1.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket1.getInputStream());
            out.writeUTF("Server: Connection Successful ");
            out.flush();
            String client=socket1.getInetAddress().getHostName();
            System.out.println("connected client: " +client);
            String request =in.readUTF();
            System.out.println("request for: "+ request);
            /**returns the List of the brokers**/
            if(request.equals("BrokerList")&& BrokerList!=null){
                calculateKeys();//temporary
                printBrokerRangeSList();
                out.writeObject(BrokerList);
                out.flush();
            }
            else if(request.equals("BrokerAdd")){
                String newBrokerIp = socket.getInetAddress().getHostName();
                int newBrokerPort=Integer.parseInt(in.readUTF());//kati paizei edo to bgazei o
                Brocker b1 = new Brocker(newBrokerPort,newBrokerIp);
                BrokerList.add(b1);
                calculateKeys();
                printBrokerRangeSList();
                String temp = in.readUTF();
                System.out.println("BrokerList.size: "+BrokerList.size());
                if(temp.equals("ping")){
                    while(true) {
                        out.writeUTF("ping");
                        out.flush();
                        System.out.println(in.readUTF());
                    }
                }
            }
            /**receives the object of push**/
            else if(request.equals("Push")){
                String temp = in.readUTF();
                System.err.println("buslineId: "+temp);
                String newtemp =convertBusLineIdToBus(temp);
                System.err.println("NewbuslineId: "+newtemp);
                //if(acceptPublisher(temp)==true){
                     ArrayList<String[]> positionList =(ArrayList<String []>) in.readObject();
                     localBusPositionsHashMap.put(temp,positionList);
                     System.out.println("positionList.size: "+ positionList.size());
                    System.err.println("here");
                //}
                //else {
                    //in.readObject();
                    //System.err.println("does not belong here");
                //}
            }
            else if (request.equals("Subscribe")){
                Topic localTopic =(Topic) in.readObject();
                System.err.println("localtopic: "+localTopic.getBusLine());
                ArrayList<String[]> local= findValue(localTopic);
                Bus b1 = new Bus();
                b1.setBusLineId(local.get(0)[0]);
                b1.setRouteCode(local.get(0)[1]);
                b1.setVehicleId(local.get(0)[2]);
                Topic topic1 = new Topic("bill");
                Value value1 = new Value(b1,Double.parseDouble(local.get(1)[3]),Double.parseDouble(local.get(1)[4]));
                out.writeObject(value1);
                out.flush();
                //todo search for the bus line
                //todo send the Values back
                //out.writeObject(value);
            }
            else if(request.equals("Unsubscribe")){
                Topic localTopic = (Topic) in.readObject();
                String subscriberIp = socket.getInetAddress().getHostName();
                //Todo delete consumer for registeredSubscribers
            }
            System.out.println("request Successful");
            out.close();
            in.close();
            System.out.println("connection closed");
        } catch (IOException e) {
            System.out.println("Broker failed");
            System.out.println("BrokerListSize: "+ BrokerList.size());
            /**if a broker disconects for eny reason the other broker deletes th broker from the arraylist**/
            for(int i=0; i<BrokerList.size(); i++){
                if(BrokerList.get(i).getIpAddress().equals(socket1.getInetAddress().getHostName())){
                    System.out.println(BrokerList.get(i).ipAddress);
                    BrokerList.remove(i);
                    System.out.println("BrokerListSize: "+ BrokerList.size());
                }
            }
            //e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }//end brokerListener
    private String convertBusLineIdToBus(String busLineId){
        String bus=null;
        System.out.println(BusLinesArray.size());
        for(String [] local :BusLinesArray){
            if(busLineId.equals(local[0])){
                bus=local[1];
                return  bus;
            }
        }
        return null;
    }
    private String convertBusToBusLineId(String bus){
        String busLineId=null;
        System.out.println(BusLinesArray.size());
        for(String [] local :BusLinesArray){
            if(bus.equals(local[1])){
                busLineId=local[0];
                return  busLineId;
            }
        }
        return busLineId;
    }
    /**this function is responcible for finding the busLineId from the hasmap
     * and returns the list of all the bus positoons for the specific bus**/
    private ArrayList<String []> findValue(Topic localTopic){
        String temp =localTopic.getBusLine();
        String bus=convertBusToBusLineId(temp);
        for(String key : localBusPositionsHashMap.keySet()){
            if(key.equals(bus)){
                return localBusPositionsHashMap.get(key);
            }
        }
        return null;
    }

    public String calculateBrokerHash(){
        Md5 md5 = new Md5();
        brokerHash = md5.HASH(ipAddress +Integer.toString(port));
        //System.out.println("brokerRange: "+ brokerHash);
        return brokerHash;
    }
    /**calculates the ip + port + BUS ID  --> md5 hash**/
    //BUS ID
    public void calculateKeys() throws IOException {
        Md5 md5 = new Md5();
        String hashLine;
        Read r = new Read();
        r.readBusLines();
        ArrayList<String []> busLinesList = r.readBusLines();
        ArrayList<String> busLineHashList = new ArrayList<>(); //na to svisw
        //pernaw ta lineid apo to source,ta hasharw kai bazw ta hash sto busLineHashTable
        for(int i=0;i<busLinesList.size();i++){
            hashLine = md5.HASH(busLinesList.get(i)[1]);
            busLineHashList.add(hashLine);
        }
        //sortarw tin brokerList me basi to megalitero brokerhash
        sortBrokerList();
        BigInteger brokHash;//temporary metablites gia tis sigriseis mes to if
        BigInteger lineHash;//temporary metablites gia tis sigriseis mes to if
        BigInteger maxBrokHash=new BigInteger(BrokerList.get(BrokerList.size()-1).calculateBrokerHash(),16);
        for(Brocker b:BrokerList){
            brokHash= new BigInteger(b.calculateBrokerHash(),16);
            for(int i=0;i<busLineHashList.size();i++){
                if(busLineHashList.get(i)!="0") {
                    lineHash = new BigInteger(busLineHashList.get(i), 16);
                    if (lineHash.mod(maxBrokHash).compareTo(brokHash) <= 0) {
                        b.brokerRangeList.add(busLinesList.get(i));
                        busLineHashList.set(i, "0");
                    }
                }
            }
        }
    }//end calculateKeys

    //taksinomei ta brokerHashes apo to mikrotero sto megalitero
    public void sortBrokerList(){
        int n = BrokerList.size();
        BigInteger temp1;
        BigInteger temp2;
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n - i - 1; j++){
                 temp1= new BigInteger(BrokerList.get(j).calculateBrokerHash(),16);
                 temp2= new BigInteger(BrokerList.get(j + 1).calculateBrokerHash(),16);
                if (temp1.compareTo(temp2) > 0) {
                    // swap brocker elements in the arraylist
                    Brocker temp = BrokerList.get(j);
                    BrokerList.set(j, BrokerList.get(j + 1));
                    BrokerList.set(j + 1, temp);
                }
            }
        }
    }


    public void printBrokerRangeSList(){
        System.out.println("Broker has lines : ");
        for(int i=0;i<brokerRangeList.size();i++){
            System.out.println(brokerRangeList.get(i)[1]);

        }
    }
    public boolean acceptPublisher(String lineId){
        for (int i=0; i<brokerRangeList.size();i++){
            if(brokerRangeList.get(i)[1].equals(lineId)){
                return true;
            }
        }
        return false;
    }//end acceptPublisher

    public ArrayList<String[]> getBrokerRangeList(){
        return brokerRangeList;
    }

    public void printBrokerList(){
        System.out.println("size :"+BrokerList.size());
        for(Brocker b: BrokerList){
            System.out.println("Brocker"+b.brokerId+" hash: "+b.calculateBrokerHash());
        }
    }
    public String getBrokerRange(){
        return brokerRange;
    }


    /**will accept a connection if the Publisher's hash is with in the
     * range of the keys that he can accept**/

    //todo
    /**this function will be responsible to update the other hosts
     * 1) every time it has new data example (publisher sends new Value)
     * 2) broker every time it receives new data**/
    @Override
    public void UpdateNodes() {

    }
}//end class Broker
