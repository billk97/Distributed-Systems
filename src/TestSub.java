import DataTypes.Topic;

public class TestSub {

    public static void main(String[] args) {
        //Subscriber su = new Subscriber("192.168.1.65",4202);
        Subscriber su = new Subscriber("192.168.1.65",4202);
        su.setBrokerIp("192.168.1.65");
        su.setBrokerport(4202);
        su.init();
        su.EstablishConnection();
        su.printBusLinesArray();
        Topic topic = new Topic("024");
        su.findBroker(topic);
        su.register(topic);
    }

}
