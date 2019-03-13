import DataTypes.Topic;

import java.io.IOException;
import java.util.ArrayList;

public class Publisher implements Node{
    private String myHash=null;
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
        return Node.BrokerList;
    }
    public void notitfyFailure(Brocker  broker){

    }

    @Override
    public void initialize(int Hash) throws IOException {

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
}
