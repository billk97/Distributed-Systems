import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;

/**A broker will:
 * --- Initialization/System Image ---
 * 1) receive an Object Type MyNode for initialization
 * 2) Hash the required values
 * 3) save it in an ArrayList
 * 4) send to all other Nodes (the one in the HashTable)
 * --- Traffic Manipulation ---
 *
 * --- Work/responsibility ---
 * 1) receive an Object Type Bus
 * 2) lock up where to send it
 * 3) send it to al consumers in the same time
 *
 * **/
public class Brocker implements Node {
    private ArrayList<Subscriber> registeredSubscribers = new ArrayList<Subscriber>();
    private ArrayList<Publisher> registerPublisher= new ArrayList<Publisher>();
    private String BrokerRange=null;
    private int Port=4200;
    public String getBrokerRange(){
        return BrokerRange;
    }


    /**this function will activate the broker for the first time and make
     * it ready to be connected will open an thread to listen to
     * this means to make the Broker hear/Server to accept traffic
     * **/
    @Override
    public void initialize(int listeningPort) throws IOException {
        Port=listeningPort;
        ServerSocket listenerSocket= null;
        Socket connection = null;
        System.out.println("Brocker Initialazation");
        listenerSocket= new ServerSocket(listeningPort);
        /**adds a broker to the list if **/
        Node.BrokerList.add(this);
        while (true){
            System.out.println("Broker is ready ");
            connection=listenerSocket.accept();
            ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
            in.close();
            out.close();
            connection.close();
        }
    }
    public void calculateKeys() throws UnknownHostException {
        /**returns the ip address of the mashin**/
        String BrokerIp  = Inet4Address.getLocalHost().getHostAddress();
        Md5 md5 = new Md5();
        BrokerRange = md5.HASH(BrokerIp+Integer.toString(Port));
        System.out.println("BrokerRange: "+BrokerRange);
    }
    /**will accept a connaction if the Publisher's hash is with in the
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

    @Override
    public void connect() {
        String Ip =null ;
        int port = 0;
        Socket socket= null;
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        try {
            socket = new Socket(InetAddress.getByName("e"),12);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Client Running");
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

    }

    @Override
    public void UpdateNodes() {

    }
}//end class
