import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;

/**A broker will:
 * --- Initialization/System Image ---
 * 1) receive an Object Type MyNode for initialization
 * 2) Hash the required values
 * 3) save it in an HashMap or HashTable
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
    private ServerSocket listenerSocket= null;
    private Socket connection = null;
    private int listeningPort=4201;
    private String BrokerRange=null;

    @Override
    public HashSet brokers() {
        return null;
    }

    /**this function will activate the brocker for the first time and make
     * it ready to be connected **/
    @Override
    public void initialize(int Hash) throws IOException {
        System.out.println("Brocker Initialazation");
        listenerSocket= new ServerSocket(listeningPort);
        while (true){
            System.out.println("Broker is ready ");
            connection=listenerSocket.accept();
            ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
        }


    }

    @Override
    public void connect() {

    }

    @Override
    public void Disconect() {

    }

    @Override
    public void UpdateNodes() {

    }
    public void calculateKeys() throws UnknownHostException {
        /**returns the ip address of the mashin**/
        String BrokerIp  = connection.getInetAddress().getHostAddress();
        Md5 md5 = new Md5();
        BrokerRange = md5.HASH(BrokerIp+Integer.toString(listeningPort));

    }
    /**will accept a connaction if the Publisher's hash is with in the
     * range of the keys that he can accept**/
    public Publisher acceptConnection(Publisher pub){
        if(pub.getMyHash() < BrokerRange ){
            registerPublisher.add(pub);
            return pub;
        }
        else if(pub.hashTopic%BrokerRange<BrokerRange){
            registerPublisher.add(pub);
            return pub;
        }
        return null;
    }//end acceptConnection
}
