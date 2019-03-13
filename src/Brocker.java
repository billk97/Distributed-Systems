import DataTypes.Topic;
import DataTypes.Bus;
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
public class Brocker implements Node {
    private ArrayList<Subscriber> registeredSubscribers = new ArrayList<Subscriber>();
    private ArrayList<Publisher> registerPublisher= new ArrayList<Publisher>();
    private String BrokerRange=null;
    private int Port;
    private int BrokerId;
    private String BrokerIp ;
    Socket socket= null;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;

    public String getBrokerRange(){
        return BrokerRange;
    }

    public Brocker(int BrokerId){
        this.BrokerId=BrokerId;
    }
    /**this function will activate the broker for the first time and make
     * it ready to be connected will open an thread to listen to
     * this means to make the Broker hear/Experiment.Server to accept traffic
     * **/
    @Override
    public void initialize(int listeningPort) throws IOException, ClassNotFoundException {
        BrokerIp  = Inet4Address.getLocalHost().getHostAddress();
        Port=listeningPort;
        calculateKeys();
        ServerSocket listenerSocket= null;
        Socket connection = null;
        System.out.println("Broker Initialization");
        listenerSocket= new ServerSocket(listeningPort);
        /**adds a broker to the list if **/
        Node.BrokerList.add(this);
        while (true){
            System.out.println("Broker is up ");
            connection=listenerSocket.accept();
            ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
            System.out.println((Bus)in.readObject());
            in.close();
            out.close();
            connection.close();
            System.out.println("Connection closed");
            //listenerSocket.close();
        }
    }//end initialize


    public void calculateKeys() throws UnknownHostException {
        /**returns the ip address of the mashin**/
        Md5 md5 = new Md5();
        BrokerRange = md5.HASH(BrokerIp+Integer.toString(Port));
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
    }//end acceptConnection
    public void acceptConnection(Subscriber sub){
//
//        if(sub.busLineRequest <in> BrokerRange ){
//            registeredSubscribers.add(sub);
    //    }
    }
    public void pull(Topic topic){

    }

    @Override
    public void connect(String Ip ,int port) {
        try {
            socket = new Socket(InetAddress.getByName("e"),12);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Experiment.Client Running");
            out.writeUTF("message");
            out.flush();
            out.writeObject(this);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//end connect

    @Override
    public void Disconect() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**this function will be responcible to update the other hosts
     * 1) every time it has new data example publisher every time it hash a new lockation
     * 2) broker every time it recives new data**/
    @Override
    public void UpdateNodes() {

    }
}//end class
