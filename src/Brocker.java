import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

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
public class Brocker extends Node implements Runnable , Serializable {
    private static final long serialVersionUID = -1799537022412025503L;
    private ArrayList<Subscriber> registeredSubscribers = new ArrayList<Subscriber>();
    private ArrayList<Publisher> registerPublisher= new ArrayList<Publisher>();
    private String brokerRange =null;
    private HashMap<Brocker, ArrayList> BrokerRangeMap = new HashMap<>();
    private Socket socket;
    private String brokerHash;
    public String getBrokerRange(){
        return brokerRange;
    }
    public Brocker(int port,String ip){
        super(port,ip);
    }
    public Brocker(Socket socket,ArrayList<Brocker> BrokerList){
        this.socket=socket;
        this.BrokerList=BrokerList;
    }

    public void run(){
        System.out.println("broker1: " + BrokerList.get(0).ipAddress);
        readHash(socket);
        System.out.println(socket.getInetAddress());
    }
    /**A socket is not a port!!!!  you open a socket to listen and when a connection request
     * is send then a new Socket each time gets created and listen in the same port!!!!**/
    public void startServer(){
        ServerSocket listenerSocket =null;
        Socket connection=null;
        setBrokerList(this,0);
        try {
            listenerSocket= new ServerSocket(port);//a new Socket is created for the specific port
            while (true){
                /**the connection is accepted that means a new socket and now a new port
                 * has been created for the communication **/
                System.out.println("Server up and  waiting");
                connection =listenerSocket.accept();
                new Thread(new Brocker(connection,getBrokerList())).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//end startServer

    public void readHash(Socket socket1){
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket1.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket1.getInputStream());
            out.writeUTF("Server: Connection Successful ");
            out.flush();
            String client=socket1.getInetAddress().getHostName();
            System.out.println("client:" +client+" connected ");
            String request =in.readUTF();
            System.out.println("request for: "+ request);
            if(request.equals("BrokerList")&& BrokerList!=null){
                out.writeObject(BrokerList);
                System.out.println("sending");
            }
            System.out.println("request Successful");

            out.writeUTF("Server--> Closing connection");
            out.flush();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String calculateBrokerHash(){
        Md5 md5 = new Md5();
        brokerHash = md5.HASH(ipAddress +Integer.toString(port));
        System.out.println("brokerRange: "+ brokerRange);
        return brokerHash;
    }
    /**calculates the ip + port + BUS ID  --> md5 hash**/
    //todo calculate for which key the broker is responsible
    //BUS ID
    public void calculateKeys() throws IOException {
        Md5 md5 = new Md5();
        String hashLine;
        Read r = new Read();
        r.readBusLines();
        String [][] busLinesTable = r.getBusLinesTable();
        String [] busLineIdHashTable = new String[20];
        //pernaw ta lineid apo to source,ta hasharw kai bazw ta hash sto busLineHashTable
        for(int i=0;i<busLinesTable.length;i++){
            hashLine = md5.HASH(busLinesTable[i][1]);
            busLineIdHashTable[i]= hashLine;
        }
        //just print tables
        for(int i=0;i<busLinesTable.length;i++){
            //System.out.println(busLinesTable[i][0]+" "+busLinesTable[i][1]+" "+busLinesTable[i][2]+" "+busLinesTable[i][3]);
            System.out.println("linehash= "+busLineIdHashTable[i]);
        }

        //sigrinw ta brokerhashes me ta buslinehashes kai ta bazw sto hashmap
        for(Brocker b:BrokerList){
            for(int i=0;i<busLineIdHashTable.length;i++){
                if(b.calculateBrokerHash().compareTo(busLineIdHashTable[i])<0){
                    BrokerRangeMap.put(b,new ArrayList());
                    BrokerRangeMap.get(b).add(busLineIdHashTable[i]);
                }
            }
        }


    }

    /**will accept a connection if the Publisher's hash is with in the
     * range of the keys that he can accept**/
    public Publisher acceptConnection(Publisher pub){
        if(Integer.parseInt(pub.getMyHash() )< Integer.parseInt(brokerRange)){
            registerPublisher.add(pub);
            return pub;
        }
        else if(Integer.parseInt(pub.getMyHash())%Integer.parseInt(brokerRange)<Integer.parseInt(brokerRange)){
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
