import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public interface Node {
    public ArrayList<Brocker> BrokerList = new ArrayList<Brocker>();
    public void initialize(int Hash) throws IOException, ClassNotFoundException;
    public  void connect(String ip ,int port);
    public void Disconect();
    public void UpdateNodes();
}
