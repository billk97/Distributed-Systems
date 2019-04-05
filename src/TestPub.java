import DataTypes.Bus;
import DataTypes.Topic;
import DataTypes.Value;

public class TestPub {
    public static void main(String[] args) {
        //Publisher p1 = new Publisher(4201,"192.168.1.65",0,20);
        Publisher p1 = new Publisher(4201,"172.16.2.11",0,20);
        p1.setBrokerIp("172.16.2.11");
        p1.setBrokerPort(4202);
        p1.startPublisher();
    }
}

