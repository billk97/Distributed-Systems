import DataTypes.Bus;
import DataTypes.Topic;
import DataTypes.Value;

public class TestPub {
    public static void main(String[] args) {
        //Publisher p1 = new Publisher(4201,"192.168.1.65",0,20);
        Publisher p1 = new Publisher(4202,"192.168.1.65",0,20);
        p1.setBrokerIp("192.168.1.65");
        p1.setBrokerPort(4202);
        p1.startPublisher();
    }
}

