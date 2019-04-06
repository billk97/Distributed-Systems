import DataTypes.Bus;
import com.sun.corba.se.pept.broker.Broker;
import sun.swing.BakedArrayList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class Node implements Serializable {
    private static final long serialVersionUID = -304193945227516524L;
    public  static ArrayList<Brocker> BrokerList= new ArrayList<Brocker>();

    public ArrayList<String []> localeRouteCodesList;
    public ArrayList<String []> BusLinesArray = new ArrayList<>();
    protected String ipAddress ;
    protected int port;
    public Node(){
    }
    public Node(int port , String ipAddress)  {
        this.port=port;
        this.ipAddress=ipAddress;
    }
    public void initialize(){
        Read read = new Read();
        localeRouteCodesList=read.readRouteCodes();
        BusLinesArray = read.readBusLines();
    }

    public void printBusLinesArray(){
        for(String [] tempTable : BusLinesArray){
            System.out.println(tempTable[0]+" "+tempTable[1]+" "+tempTable[2]);
        }
    }


    public ArrayList<Brocker> getBrokerList() {
        return BrokerList;
    }
    //todo for the thread put inside hire the functions
    /**allows each subclass Broker,Publisher,Subscriber to call this method
     * broker.setBrokerList
     * broker --> object type broker
     * connection --> a socket in order to send the object via tcp**/
    /**will perform a complete connection whit the other node**/
    public  Socket connect(String ip ,int port){
        boolean scanning=true;
        while (scanning){
            try {
                InetAddress host = Inet4Address.getByName(ip);
                Socket socket = new Socket(host,port);
                scanning=false;
                return socket;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println(e);
                //System.out.println("No socket could be returned");
            }
        }
        return null;
    }

    public void Disconnect(Socket socket){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**informs the other node for the system instance **/
    public void UpdateNodes(){
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
