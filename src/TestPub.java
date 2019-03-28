import DataTypes.Bus;
import DataTypes.Topic;
import DataTypes.Value;

public class TestPub {
    public static void main(String[] args) {
        Publisher p1 = new Publisher(4201,"192.168.1.65",0,5);
        p1.setBrokerIp("192.168.1.65");
        p1.setBrokerPort(4202);
        Bus b1 = new Bus();
        Topic topic1 = new Topic("bill");
        Value value1 = new Value(b1,0.0,10.0);
        p1.getMyBrokerList();
        p1.push(value1,topic1);
    }
}

