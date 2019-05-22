package com.aueb.opabus.CodeFolder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Publisher extends Node {
    private String myHash=null;
    Md5 md5= new Md5();
    private String brokerIp ;
    private int brokerPort ;
    private int PublisherRange; //defines the range that it will read
    /**bill-> what dose that refer to ?**/
    //contains for each busLine id a table with all the positions
    private int rangeStart;
    private int rangeEnd;
    private HashMap<String,ArrayList<String []>> busPositionsHashMap = new HashMap<>(); //HashMap<busline,buspositions>
    //contains a local copy of the BrockerList
    private ArrayList<Brocker> localBrockerList= null;
    //constructor
    public Publisher(int port, String ip, int start, int end){
        super(port,ip);
        /**bill-> (suggestion)probably not nice in constructor will be better with getter setter**/
        rangeStart=start;
        rangeEnd=end;
    }

    /**this will be the first function called when the publisher is started**/
    public void startPublisher(){
        getMyBrokerList();
        readBusInformation();
        push();
    }

    /**stores In busPositionHash the buslineID an a table with the position**/
    public void readBusInformation(){
        Read r = new Read();
        /**localy stored the busLines**/
        ArrayList<String []> BusLinesArray = r.readBusLines();
        System.out.println("BusLIneArray: "+ BusLinesArray.size());
        for(int i=rangeStart; i<rangeEnd;i++){
            /**stores for the specific lineCode/LineId the positions on a ArrayList**/
            String busLineCode=BusLinesArray.get(i)[0];
            ArrayList<String []> tempArray = r.readBusPosition(busLineCode);
            /**adds busLineId and a table with positions**/
            busPositionsHashMap.put(BusLinesArray.get(i)[1],tempArray);
        }
        //System.out.println("hashmap size ="+busPositionsHashMap.size());
    }//end readBusInformation

    public String findArrayMaxLength(){
        int max=0;
        String maxKey=" ";
        for(String key: busPositionsHashMap.keySet()){
            if(max< busPositionsHashMap.get(key).size()){
                max= busPositionsHashMap.get(key).size();
                maxKey= key;
            }
        }
        System.out.println("MAX ARRAY HAS LENGHT = "+max+" with lineId ="+maxKey);
        return maxKey;
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

    /**search for the broker with who is responsible for the busline:busOfPub **/
    public Brocker findBus(String busOfPub){
        for(int i=0;i<localBrockerList.size();i++) { //search at local broker list
            int BrokerRangeListSize = localBrockerList.get(i).getBrokerBusList().size(); //size of the BrokerRangeList for each broker
            for (int j = 0; j < BrokerRangeListSize; j++) { //search to every broker's hashmap's arraylist
                String busOfBroker = localBrockerList.get(i).getBrokerBusList().get(j)[1];
                if (busOfPub.equals(busOfBroker)) {
                    return localBrockerList.get(i);
                }
            }
        }
        System.out.println("ERROR:The bus dont exists at the broker's list");
        return null;
    }
    /**notify the main broker for a failure**/
    public void notifyFailure(){
        Socket socket = connect(brokerIp,brokerPort);
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            System.out.println(in.readUTF());
            out.writeUTF("Broker Failed");
            out.flush();
            getMyBrokerList();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//end notifyFailure
    /**takes 2 Objects as arguments
     * value(bus,latitude,lontitude)
     * topic(busLine)
     * and sends them to the broker**/
    public void push ()  {
        String maxLineId = findArrayMaxLength();
        for(int i=0;i<busPositionsHashMap.get(maxLineId).size();i++) {
            for (String buslineId : busPositionsHashMap.keySet()) { //for every buslineId=buslineId search for the broker with same buslineId

                String ip = findBus(buslineId).ipAddress; //responsible broker's ip
                int port = findBus(buslineId).port; //responsible broker's port
                Socket socket = connect(ip, port);
                ObjectOutputStream out = null;
                try {
                    out = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    System.out.println(in.readUTF());
                    out.writeUTF("Push");
                    out.flush();
                    out.writeUTF(buslineId);
                    out.flush();

                    if(busPositionsHashMap.get(buslineId).size()>i) {
                        out.writeObject(busPositionsHashMap.get(buslineId).get(i));
                        out.flush();
                    }else{
                        String[] zeroTable =new String[6];
                        out.writeObject(zeroTable);
                        out.flush();
                    }
//                out.writeObject(busPositionsHashMap.get(buslineId));
//                out.flush();
                    out.close();
                    in.close();
                    TimeUnit.SECONDS.sleep(2);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Disconnect(socket);
                }
            }//end for2
        }//end for1
    }//end push
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
        Publisher pub = new Publisher(4202,"192.168.1.65",0,20);
        pub.readBusInformation();
        pub.findArrayMaxLength();

    }
}//end Class com.aueb.opabus.CodeFolder.Publisher
