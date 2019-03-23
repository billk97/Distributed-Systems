import DataTypes.Bus;
import DataTypes.Topic;
import DataTypes.Value;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.ArrayList;

public class Publisher extends Node{
    private String myHash=null;
    Md5 md5= new Md5();
    private String brokerIp = "192.168.1.65";
    private int brokerPort = 4202;
    private ArrayList<Brocker> localBrockerList= null;

    public Publisher(int port, String ip){
        super(port,ip);
    }

    public void startPublisher(){
        getBrokerList();
    }
    /**hashes the DataTypes.Topic it recives and compairs it with the
     * hashes that exist in the BrokerList **/
//    public Brocker hashTopic(Topic topic){
//        Md5 md5 = new Md5();
//        myHash=md5.HASH(topic.getBusLine());
//        for (String brocker : localBrockerList){
//            if(myHash.compareTo(brocker.getBrokerRange())==-1){
//                return brocker;
//            }
//            else if(Integer.parseInt(myHash)%Integer.parseInt(brocker.getBrokerRange())< Integer.parseInt(brocker.getBrokerRange())){
//                return brocker;
//            }
//        }
//        System.out.println("Broker does not exists");
//        return null;
//    }

    public String getMyHash(){
        Md5 md5 =new Md5();
        myHash=md5.HASH(brokerIp+Integer.toString(brokerPort));
        return myHash;
    }

    /**this function gets the list of all Brokers via tcp connection **/
    public void getMyBrokerList(){
        Socket socket = connect(brokerIp,brokerPort);
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            System.out.println(in.readUTF());
            out.writeUTF("BrokerList");
            out.flush();
            localBrockerList=new ArrayList<Brocker>((ArrayList<Brocker>)in.readObject());
            System.out.println("broker1: " + localBrockerList.get(0).ipAddress);
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            Disconnect(socket);
        }
    }//end getBrokerList
    /**takes 2 Objects as arguments
     * value(bus,latitude,lontitude)
     * topic(busLine)
     * and sends them to the broker**/
    public void push (Value value ,Topic topic)  {
        Socket socket=connect(brokerIp, brokerPort);
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            System.out.println(in.readUTF());
            out.writeUTF("Push");
            out.flush();
            out.writeObject(topic);
            out.flush();
            out.writeObject(value);
            out.flush();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            Disconnect(socket);
        }
    }//end push

    public void notitfyFailure(Brocker  broker){

    }
    public void setMyHash(String myHash) {
        this.myHash = myHash;
    }

    public String getBrokerIp() {
        return brokerIp;
    }

    public void setBrokerIp(String brokerIp) {
        this.brokerIp = brokerIp;
    }

    public int getBrokerPort() {
        return brokerPort;
    }

    public void setBrokerPort(int brokerPort) {
        this.brokerPort = brokerPort;
    }
}//end Class Publisher
