import DataTypes.Topic;
import DataTypes.Value;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Publisher extends Node{
    private String myHash=null;
    private Node node = null;
    Md5 md5= new Md5();

    /**hashes the DataTypes.Topic it recives and compairs it with the
     * hashes that exist in the BrokerList **/
    public Brocker hashTopic(Topic topic){
        Md5 md5 = new Md5();
        myHash=md5.HASH(topic.getBusLine());
        for (Brocker brocker : getBrokerList()){
            if(myHash.compareTo(brocker.getBrokerRange())==-1){
                return brocker;
            }
            else if(Integer.parseInt(myHash)%Integer.parseInt(brocker.getBrokerRange())< Integer.parseInt(brocker.getBrokerRange())){
                return brocker;
            }
        }
        System.out.println("Broker does not exists");
        return null;
    }

    public String getMyHash(){
        return myHash;
    }
    /**this function gets the list of all Brokers**/
    public ArrayList<Brocker> getBrokerList(){
        node=new Node();
        return node.BrokerList;
    }
    public void push (Value value ,Topic topic) throws IOException {
        Socket socket = null;
        ObjectOutputStream out = null;
       // socket= new Socket(InetAddress.getByName(ip),port);
        out= new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(topic);
        out.flush();
        out.close();
    }

    public void notitfyFailure(Brocker  broker){

    }


}
