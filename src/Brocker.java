import DataTypes.Bus;
import DataTypes.Topic;
import DataTypes.Value;
import com.sun.corba.se.pept.broker.Broker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Brocker extends Node implements Runnable, Serializable {
    private static final long serialVersionUID = -6643274596837043061L;
    /**
     * brokerBusList = the bus list for which a broker is responsible
     **/
    public ArrayList<String[]> brokerBusList = new ArrayList<>();
    private HashMap<String, ArrayList<String[]>> BusInformationHashMap = new HashMap<>();
    public ArrayList<Brocker> BrokerList = new ArrayList<Brocker>();
    private Socket socket;


    public Brocker() {
    }

    public Brocker(int port, String ip) {
        ipAddress = ip;
        this.port = port;
    }

    public Brocker(Socket socket, ArrayList<Brocker> BrokerList, ArrayList<String[]> brokerBusList,
                   ArrayList<String[]> BusLinesArrays, HashMap<String, ArrayList<String[]>> BusInformationHashMap) {
        this.BrokerList = BrokerList;
        this.socket = socket;
        this.BusLinesArray = BusLinesArrays;
        this.BusInformationHashMap = BusInformationHashMap;
        this.brokerBusList = brokerBusList;
    }

    public void run() {
        brokerListener(socket);
    }

    public void startServer() {
        ServerSocket listenerSocket = null;
        Socket connection=null;
        BrokerList.add(this);
        initialize();
        try {
            listenerSocket = new ServerSocket(port);
            Thread BrokerAddThread1 = new Thread(new BrokerConnect("172.16.2.45", 4202, 4202));
            Thread BrokerAddThread2 = new Thread(new BrokerConnect("172.16.2.46", 4202, 4202));
            BrokerAddThread1.start();
            BrokerAddThread2.start();
            while (true) {
                /**connection accepted means a new socket and a new port have been created for the communication **/
                System.out.println("Server is up and waiting ");
                connection = listenerSocket.accept();
                Thread t = new Thread(new Brocker(connection, BrokerList, brokerBusList, BusLinesArray, BusInformationHashMap));//check if thread has access to memory
                t.start();
                t.join();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }//startServer


    public void brokerListener(Socket socket) {

        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeUTF("Server: Connection Successful ");
            out.flush();
            String client = socket.getInetAddress().getHostAddress();
            System.out.println("Connected client : " + client);
            String request = in.readUTF();
            System.out.println("Request for: " + request + " from --> " + client);
            if (request.equals("BrokerList")) {
                calculateKeys();
                //printBusLinesArray();
                out.writeObject(BrokerList);
                out.flush();
            } else if (request.equals("BrokerAdd")) {
                int newBrokerPort = Integer.parseInt(in.readUTF());
                Brocker b1 = new Brocker(newBrokerPort, client);
                System.out.println("b1 port: " + b1.port);
                System.out.println("b1 ip: " + b1.ipAddress);
                BrokerList.add(b1);
                calculateKeys();
                printBrokerBusList();
                System.err.println("BrokeList.size: " + BrokerList.size());
            } else if (request.equals("Push")) {
                String LineCode = in.readUTF();
                System.err.println("LineCode: "+ LineCode);
                String Bus = convertLineCodeToBus(LineCode);
                System.err.println("Bus: "+ Bus);
                if(acceptPublisher(Bus)==true){
                    ArrayList<String[]> positionList =(ArrayList<String[]>)in.readObject();
                    BusInformationHashMap.put(LineCode,positionList);
                    System.out.println("positionList.size: "+ positionList.size());
                }
                else {
                    in.readObject();
                    System.err.println("This broker is not responsible for this bus");
                }
            }else if(request.equals("Subscribe")){
                Topic topic =(Topic) in.readObject();
                System.err.println("LocalTopic: "+ topic.getBusLine());
                ArrayList<String[]> local= findValue(topic);
                if(local!=null){
                    Bus b1 = new Bus();
                    b1.setBusLineId(local.get(0)[0]);
                    b1.setRouteCode(local.get(0)[1]);
                    b1.setVehicleId(local.get(0)[2]);
                    Value value1 = new Value(b1,Double.parseDouble(local.get(1)[3]),Double.parseDouble(local.get(1)[4]));
                    out.writeObject(value1);
                    out.flush();
                }
            }
            else if(request.equals("Unsubscribe")){

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private String[] findValueRemmoteBroker(Topic bus){
        String localTable [] =new String[2];
        for(Brocker broker :BrokerList){
            for(String [] t :brokerBusList){
                if(t[1].equals(bus.getBusLine())){
                    localTable[0]=broker.ipAddress;
                    localTable[1]=Integer.toString(broker.port);
                    return localTable;
                }
            }
        }
        return null;
    }
    public boolean acceptPublisher(String lineId){
        for (int i=0; i<brokerBusList.size();i++){
            if(brokerBusList.get(i)[1].equals(lineId)){
                return true;
            }
        }
        return false;
    }//end acceptPublisher
    private String convertLineCodeToBus(String LineCode){
        String bus=null;
        for(String [] local :brokerBusList){
            if(LineCode.equals(local[0])){
                bus=local[1];
                return bus;
            }
        }
        return null;
    }//convertLineCodeToBus

    /**this function is responsible for finding the busLineId from the hasmap
     * and returns the list of all the bus positions for the specific bus**/
    private ArrayList<String []> findValue(Topic localTopic){
        String temp =localTopic.getBusLine();
        String bus=convertBusToLineCode(temp);
        for(String key : BusInformationHashMap.keySet()){
            if(key.equals(bus)){
                return BusInformationHashMap.get(key);
            }
        }
        return null;
    }

    private String convertBusToLineCode(String bus){
        String LineCode=null;
        for(String [] local : brokerBusList){
            if(bus.equals(local[1])){
                LineCode=local[0];
                return LineCode;
            }
        }
        return LineCode;
    }
    /**
     * calculates the ip + port + BUS ID  --> md5 hash
     **/
    //todo what happens if you want to re calculate the keys
    //todo the table needs to be cleared?
    public void calculateKeys() throws IOException {
        Md5 md5 = new Md5();
        Read r = new Read();
        String hashLine;
        ArrayList<String[]> busLinesList =new ArrayList<String[]>();
        busLinesList = r.readBusLines();
        ArrayList<String> busLineHashList = new ArrayList<>(); //na to svisw
        //pernaw ta lineid apo to source,ta hasharw kai bazw ta hash sto busLineHashTable
        for (int i = 0; i < busLinesList.size(); i++) {
            hashLine = md5.HASH(busLinesList.get(i)[1]);
            busLineHashList.add(hashLine);
        }
        //sortarw tin brokerList me basi to megalitero brokerhash
        sortBrokerList();
        BigInteger brokHash;//temporary metablites gia tis sigriseis mes to if
        BigInteger lineHash;//temporary metablites gia tis sigriseis mes to if
        BigInteger maxBrokHash = new BigInteger(BrokerList.get(BrokerList.size() - 1).calculateIpPortHash(), 16);
        for (Brocker b : BrokerList) {
            b.brokerBusList = new ArrayList<String[]>();
            brokHash = new BigInteger(b.calculateIpPortHash(), 16);
            for (int i = 0; i < busLineHashList.size(); i++) {
                if (busLineHashList.get(i) != "0") {
                    lineHash = new BigInteger(busLineHashList.get(i), 16);
                    if (lineHash.mod(maxBrokHash).compareTo(brokHash) <= 0) {
                        b.brokerBusList.add(busLinesList.get(i));
                        busLineHashList.set(i, "0");
                    }
                }
            }
        }
        printBrokerBusList();
        //busLinesList.clear();
    }//end calculateKeys

    /**
     * sorts the BrokerList based on the Hashes(Ip+port) the contain. smaller to bigger
     **/
    public void sortBrokerList() {
        int n = BrokerList.size();
        BigInteger temp1;
        BigInteger temp2;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                temp1 = new BigInteger(BrokerList.get(j).calculateIpPortHash(), 16);
                temp2 = new BigInteger(BrokerList.get(j + 1).calculateIpPortHash(), 16);
                if (temp1.compareTo(temp2) > 0) {
                    // swap brocker elements in the arraylist
                    Brocker temp = BrokerList.get(j);
                    BrokerList.set(j, BrokerList.get(j + 1));
                    BrokerList.set(j + 1, temp);
                }
            }
        }
    }

    public String calculateIpPortHash() {
        Md5 md5 = new Md5();
        String brokerHash;
        brokerHash = md5.HASH(ipAddress + Integer.toString(port));
        return brokerHash;
    }

    public void printBrokerBusList() {
        for(Brocker b2 : BrokerList){
            System.out.println("Brockers with ip: "+b2.getIpAddress()+" has busses:");
            for (String [] temp :b2.brokerBusList){
                System.out.println(temp[1]);
            }
        }
    }

    public ArrayList<String[]> getBrokerBusList() {
        return brokerBusList;
    }


}//end class Broker
