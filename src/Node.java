import java.io.IOException;
import java.util.HashSet;

public interface Node {
    public HashSet <String> BrokerList = new HashSet();
    public HashSet brokers();
    public void initialize(int Hash) throws IOException;
    public  void connect();
    public void Disconect();
    public void UpdateNodes();
}
