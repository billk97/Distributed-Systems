import com.sun.corba.se.pept.broker.Broker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.*;
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
    public Node(String ipAddress, String type, int port) {
        IpAddress = ipAddress;
        Type = type;
        Port = port;
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
            System.out.println("Server connecton wating");
            connection =listenerSocket.accept();
            //todo make a thread to make the connection or to save something
            new Thread(new Brocker(connection)).start();
        }//end while
    }//end Initialize

    /**allows each subclass Broker,Publisher,Subscriber to call this method
     * broker.setBrokerList
     * broker --> object type broker
     * connection --> a socket in order to send the object via tcp**/
    public void setBrokerList(Brocker brocker) {
            BrokerList.add(brocker);
    }//end setBrokerList
    /**will perform a complete connection whit the other node**/
    public  Socket connect(String ip ,int port){
        try {
            InetAddress host = Inet4Address.getByName(ip);
            Socket socket = new Socket(host,port);
            return socket;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("No socket could be returned");
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
}
