import DataTypes.Topic;
import DataTypes.Bus;
import com.sun.corba.se.pept.broker.Broker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;

/**A broker will:
 * --- Initialization/System Image ---
 * 1) receive an Object Type MyNode for initialization
 * 2) Hash the required values
 * 3) save it in an ArrayList
 * 4) send to all other Nodes (the one in the HashTable)
 * --- Traffic Manipulation ---
 *
 * --- Work/responsibility ---
 * 1) receive an Object Type DataTypes.Bus
 * 2) lock up where to send it
 * 3) send it to al consumers in the same time
 *
 * **/
public class Brocker extends Node implements Runnable{
    private ArrayList<Subscriber> registeredSubscribers = new ArrayList<Subscriber>();
    private ArrayList<Publisher> registerPublisher= new ArrayList<Publisher>();
    private String BrokerRange=null;
    private int BrokerPort;
    private int BrokerId;
    private Socket socket;
    private String BrokerIp= Inet4Address.getLocalHost().getHostAddress();
    public String getBrokerRange(){
        return BrokerRange;
    }
    public Brocker() throws UnknownHostException {}
    public Brocker(Socket socket) throws UnknownHostException {
        this.socket=socket;
    }
    public Brocker(int BrokerId,String BrokerIp, int BrokerPort) throws UnknownHostException {
        //todo super();
        this.BrokerId=BrokerId;
        this.BrokerIp=BrokerIp;
        this.BrokerPort=BrokerPort;
    }
    public void run(){
        readHash(socket);
        System.out.println(socket.getInetAddress());
    }
    public void readHash(Socket socket1){
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket1.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket1.getInputStream());
            out.writeUTF("Server--> Connection Successful ");
            out.flush();
            String clientHash=in.readUTF();
            System.out.println(clientHash);
            out.writeUTF("Server--> Closing connection");
            out.flush();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**calculates the ip + port --> md5 hash**/
    //todo calculate for which key the broker is responsible
    public void calculateKeys() throws UnknownHostException {
        Md5 md5 = new Md5();
        BrokerRange = md5.HASH(BrokerIp+Integer.toString(BrokerPort));
        System.out.println("BrokerRange: "+BrokerRange);
    }

    /**will accept a connection if the Publisher's hash is with in the
     * range of the keys that he can accept**/
    public Publisher acceptConnection(Publisher pub){
        if(Integer.parseInt(pub.getMyHash() )< Integer.parseInt(BrokerRange)){
            registerPublisher.add(pub);
            return pub;
        }
        else if(Integer.parseInt(pub.getMyHash())%Integer.parseInt(BrokerRange)<Integer.parseInt(BrokerRange)){
            registerPublisher.add(pub);
            return pub;
        }
        return null;
    }
    //todo
    /**this function will be responsible to update the other hosts
     * 1) every time it has new data example (publisher sends new Value)
     * 2) broker every time it receives new data**/
    @Override
    public void UpdateNodes() {

    }
}//end class
