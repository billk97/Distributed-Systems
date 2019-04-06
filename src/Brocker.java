import com.sun.corba.se.pept.broker.Broker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.*;
import java.util.ArrayList;

public class Brocker extends Node implements Runnable , Serializable {
    private static final long serialVersionUID = -6643274596837043061L;
    /**brokerBusList = the bus list for which a broker is responsible **/
    protected ArrayList<String []> brokerBusList = new ArrayList<>();
    private ServerSocket listenerSocket = null;
    private Socket connection= null;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;

    public  Brocker(){
        super();
    }
    public Brocker(int port, String ip){
        ipAddress=ip;
        port=this.port;
    }

    public void run (){
        brokerListener();
    }

    public void startServer(){
        BrokerList.add(this);
        initialize();
        try {
            Thread BrokerAddThread1 = new Thread(new BrokerConnect("192.168.1.70",4202,4202));
            Thread BrokerAddThread2 = new Thread(new BrokerConnect("192.168.1.74",4202,4202));
            listenerSocket= new ServerSocket(port);
            while (true){
                /**connection accepted means a new socket and a new port have been created for the communication **/
                System.out.println("Server is up and waiting ");
                connection=listenerSocket.accept();
                Thread t = new Thread(this);//check if thread has access to memory
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//startServer

    public void brokerListener(){
        try {
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
            out.writeUTF("Server: Connection Successful ");
            out.flush();
            String client = connection.getInetAddress().getHostAddress();
            System.out.println("Connected client : "+ client);
            String request = in.readUTF();
            System.out.println("Request for: "+ request + "from -->" + client);
            if(request.equals("BrokerList")){
                calculateKeys();
                printBusLinesArray();
                out.writeObject(BrokerList);
                out.flush();
            }
            else if(request.equals("BrockerAdd")){
                int newBrokerPort = Integer.parseInt(in.readUTF());
                Brocker b1 = new Brocker(newBrokerPort,client);
                System.out.println("b1 port: "+ b1.port);
                System.out.println("b1 ip: "+b1.ipAddress);
                BrokerList.add(b1);
                calculateKeys();
                printBrokerBusList();
                System.err.println("BrokeList.size: "+ BrokerList.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**calculates the ip + port + BUS ID  --> md5 hash**/
    //todo what happens if you want to re calculate the keys
    //todo the table needs to be cleared?
    public void calculateKeys() throws IOException  {
        Md5 md5 = new Md5();
        String hashLine;
        Read r = new Read();
        r.readBusLines();
        brokerBusList=new ArrayList<String []>();
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
        BigInteger maxBrokHash=new BigInteger(BrokerList.get(BrokerList.size()-1).calculateIpPortHash(),16);
        for(Brocker b:BrokerList){
            brokHash= new BigInteger(b.calculateIpPortHash(),16);
            for(int i=0;i<busLineHashList.size();i++){
                if(busLineHashList.get(i)!="0") {
                    lineHash = new BigInteger(busLineHashList.get(i), 16);
                    if (lineHash.mod(maxBrokHash).compareTo(brokHash) <= 0) {
                        b.brokerBusList.add(busLinesList.get(i));
                        busLineHashList.set(i, "0");
                    }
                }
            }
        }
    }//end calculateKeys

    /**sorts the BrokerList based on the Hashes(Ip+port) the contain. smaller to bigger**/
    public void sortBrokerList(){
        int n = BrokerList.size();
        BigInteger temp1;
        BigInteger temp2;
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n - i - 1; j++){
                temp1= new BigInteger(BrokerList.get(j).calculateIpPortHash(),16);
                temp2= new BigInteger(BrokerList.get(j + 1).calculateIpPortHash(),16);
                if (temp1.compareTo(temp2) > 0) {
                    // swap brocker elements in the arraylist
                    Brocker temp = BrokerList.get(j);
                    BrokerList.set(j, BrokerList.get(j + 1));
                    BrokerList.set(j + 1, temp);
                }
            }
        }
    }
    public String calculateIpPortHash(){
        Md5 md5 = new Md5();
        String brokerHash;
        brokerHash = md5.HASH(ipAddress +Integer.toString(port));
        return brokerHash;
    }
    public void printBrokerBusList(){
        System.out.println("Broker has lines : ");
        for(int i=0;i<brokerBusList.size();i++){
            System.out.println(brokerBusList.get(i)[1]);
        }
    }

    public ArrayList<String[]> getBrokerBusList(){
        return brokerBusList;
    }




}//end class Broker
