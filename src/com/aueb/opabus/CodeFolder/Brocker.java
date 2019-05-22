package com.aueb.opabus.CodeFolder;

import com.aueb.opabus.CodeFolder.DataTypes.Bus;
import com.aueb.opabus.CodeFolder.DataTypes.Topic;
import com.aueb.opabus.CodeFolder.DataTypes.Value;


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
    private HashMap<String,String[]> BusInformationHashMap = new HashMap<>();
    public ArrayList<Brocker> BrokerList = new ArrayList<Brocker>();
    public ArrayList<String []> remoteBrokerList=new ArrayList<>();
    public  String[] positionTable = new String[6];
    private Socket socket;
    /**default constructor**/
    public Brocker() {}
    /**copy constructor**/
    public Brocker(int port, String ip) {
        ipAddress = ip;
        this.port = port;
    }
    /**copy constructor**/
    public Brocker(Socket socket, ArrayList<Brocker> BrokerList, ArrayList<String[]> brokerBusList,
                   ArrayList<String[]> BusLinesArrays, HashMap<String, String[]> BusInformationHashMap, ArrayList<String []> localeRouteCodesList) {
        this.BrokerList = BrokerList;
        this.socket = socket;
        this.BusLinesArray = BusLinesArrays;
        this.BusInformationHashMap = BusInformationHashMap;
        this.brokerBusList = brokerBusList;
        this.localeRouteCodesList=localeRouteCodesList;
    }

    public void run() {
        brokerListener(socket);
    }
    /**Connects with the remote servers**/
    public void ConnectServers(String [] remoteBrokersIp){
        int RemotePort =4202;
        for(int i=0; i<remoteBrokersIp.length; i++){
            new Thread(new BrokerConnect(remoteBrokersIp[i], RemotePort, RemotePort)).start();
        }
        /**for emergency**/
//        new Thread(new com.aueb.opabus.CodeFolder.BrokerConnect("172.16.2.46", RemotePort, RemotePort)).start();
//        new Thread(new com.aueb.opabus.CodeFolder.BrokerConnect("172.16.2.44", RemotePort, RemotePort)).start();
//        new Thread(new com.aueb.opabus.CodeFolder.BrokerConnect("172.16.2.46", RemotePort, RemotePort)).start();
    }
    /**Is called on the beginning of the server **/
    public void startServer() {
        ServerSocket listenerSocket = null;
        Socket connection=null;
        BrokerList.add(this);
        initialize();
        try {
            listenerSocket = new ServerSocket(port);
            /**for emergency use**/
//
            while (true) {
                /**connection accepted means a new socket and a new port have been created for the communication **/
                System.out.println("Server is up and waiting ");
                connection = listenerSocket.accept();
                Thread t = new Thread(new Brocker(connection, BrokerList, brokerBusList, BusLinesArray, BusInformationHashMap,localeRouteCodesList));//check if thread has access to memory
                t.start();
                t.join();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }//startServer

    /**main Broker function listens for all the requests**/
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
                String LineId = in.readUTF();
                System.err.println("Bus: "+ LineId);
                if(acceptPublisher(LineId)==true){
                    positionTable =(String[])in.readObject();
                    if(BusInformationHashMap.containsKey(LineId)){
                        BusInformationHashMap.replace(LineId,positionTable);
                    }else{
                        BusInformationHashMap.put(LineId,positionTable);
                    }



                }
                else {
                    in.readObject();
                    System.err.println("This broker is not responsible for this bus");
                }
            }else if(request.equals("Subscribe")){
                Topic topic =(Topic) in.readObject();
                System.err.println("LocalTopic: "+ topic.getBusLine());

                Thread t = new Thread(()->{
                    String lastLat = null;
                    while(true) {
                        String[] localLastPosition= findPositionOftheBus(topic);
                        //&& !localLastPosition[3].equals(lastLat)
                        if (localLastPosition != null&& !localLastPosition[3].equals(lastLat) ) {
                                Bus b1 = new Bus();
                                b1.setBusLineCode(localLastPosition[0]);
                                b1.setRouteCode(localLastPosition[1]);
                                b1.setVehicleId(localLastPosition[2]);
                                b1.setLineName(getBusLineName(localLastPosition[1]));
                                b1.setInfo(getBusLineInfo(localLastPosition[1]));
                                b1.setBusLineCode(topic.getBusLine());
                                //b1.setLineNumber(topic.getBusLine());
                                Value value1 = new Value(b1, Double.parseDouble(localLastPosition[3]), Double.parseDouble(localLastPosition[4]));
                                try {
                                    out.writeObject(value1);
                                    out.flush();
//                                Thread.sleep(3000);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
//                            catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
                                lastLat= localLastPosition[3];

                        } else {
                            try {
                                out.writeObject(null);
                                out.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }//end while
                });
                t.start();
            }
            else if(request.equals("Unsubscribe")){

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**finds the Bus Line Name from the localeRouteCodesList**/
    public String getBusLineName(String routeCode){
        for (String[] line : localeRouteCodesList){
            if(line[0].equals(routeCode)){
                return line[3];
            }
        }
        return "Name not found";
    }
    /**finds the Bus Lineinfo from the localeRouteCodesList**/
    public String getBusLineInfo(String routeCode){
        for (String[] line : localeRouteCodesList){
            if(line[0].equals(routeCode)){
                return line[4];
            }
        }
        return "Info not found";
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
    /**checks if the broker is responsible for the bus**/
    public boolean acceptPublisher(String lineId){
        for (int i=0; i<brokerBusList.size();i++){
            if(brokerBusList.get(i)[1].equals(lineId)){
                return true;
            }
        }
        return false;
    }//end acceptPublisher
    /**converts Line Code To bus**/
    private String convertLineCodeToBus(String LineId){
        String bus=null;
        for(String [] local :brokerBusList){
            if(LineId.equals(local[1])){
                bus=local[0];
                return bus;
            }
        }
        return null;
    }//convertLineCodeToBus

    /**this function is responsible for finding the busLineId from the hasmap
     * and returns the list of all the bus positions for the specific bus**/
    private String [] findPositionOftheBus(Topic localTopic){
        String busLineId =localTopic.getBusLine();
        //String busLineCode = convertLineIdToLineCode(busLineId);
        if(BusInformationHashMap.containsKey(busLineId)){
            return BusInformationHashMap.get(busLineId);
        }
        return null;
    }
    /**converts Bus to Line Code**/
    private String convertLineIdToLineCode(String bus){
        String LineCode=null;
        for(String [] local : brokerBusList){
            if(bus.equals(local[1])){
                LineCode=local[0];
                return LineCode;
            }
        }
        System.out.println("There is no linecode match with this lineId");
        return null;
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

    public static void main(String[] args) {

    }

}//end class Broker
