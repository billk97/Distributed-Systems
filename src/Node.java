import com.sun.corba.se.pept.broker.Broker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

public class Node implements Serializable ,Runnable {
    public static long serialVersionUID= 2745435983589125303L;
    public ArrayList<Brocker> BrokerList = new ArrayList<Brocker>();
    private String Name;
    private String IpAddress;
    private String Type;
    private int Port;
    private String Hash;
    public Node(){}
    public Node(ArrayList<Brocker> brokerList, String name, String ipAddress, String type, int port, String hash) {
        BrokerList = brokerList;
        Name = name;
        IpAddress = ipAddress;
        Type = type;
        Port = port;
        Hash = hash;
    }

    public void setBrokerList(ArrayList<Brocker> brokerList) {
        BrokerList = brokerList;
    }
    public ArrayList<Brocker> getBrokerList(){
        return BrokerList;
    }

    //todo for the thread put inside hire the functions
    @Override
    public void run() {

    }

    /**this function will activate the node  for the first time and make
     * it ready (up) it will start listening in a specific port to accept
     * traffic
     * **/
    public void initialize(int NodeListenerPort) throws IOException, ClassNotFoundException{
        /**the nodes ip**/
        String NodeIp = Inet4Address.getLocalHost().getHostAddress();
        /**A socket is not a port!!!!  you open a socket to listen and when a connection request
         * is send then a new Socket each time gets created and listen in the same port!!!!
         * **/
        ServerSocket listenerSocket= null;
        Socket connection = null;
        /**a new Socket is created for the specific port**/
        listenerSocket=new ServerSocket(NodeListenerPort);
        /**just watting for ever**/
        while(true){
            /**the connection is accepted that means a new socket and now a new port
             * has been created for the communication **/
            connection =listenerSocket.accept();
            //todo make a thread to make the connection or to save something
            //Thread t1 = new Experiment.myThread(connection);
        }//end while
    }//end Initialize

    /**allows each subclass Broker,Publisher,Subscriber to call this method
     * broker.setBrokerList
     * broker --> object type broker
     * connection --> a socket in order to send the object via tcp**/
    public void setBrokerList(Brocker brocker, Socket connection) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
            BrokerList.add((Brocker)in.readObject());
            in.close();
            out.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }//end setBrokerList
    /**will perform a complete connection whit the other node**/
    public  void connect(String ip ,int port){

    }
    public void Disconect(){

    }
    public void UpdateNodes(){

    }
}
