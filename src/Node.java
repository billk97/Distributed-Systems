import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Node {
    public ArrayList<Brocker> BrokerList = new ArrayList<Brocker>();

    public void setBrokerList(Brocker brocker) {
        BrokerList.add(brocker);
    }

    public void initialize(int Hash) throws IOException, ClassNotFoundException{

    }

    public  void connect(String ip ,int port){

    }
    public void Disconect(){

    }
    public void UpdateNodes(){

    }
}
