import DataTypes.Topic;

public class TestSub {

    public static void main(String[] args) {
        //Subscriber su = new Subscriber("192.168.1.65",4202);
        Subscriber su = new Subscriber("localhost",4202);
        su.setBrokerIp("localhost");
        su.setBrokerport(4202);
        su.init();
        su.EstablishConnection();
        su.disconnect();
        Topic topic = su.readTopicFromConsole();
        su.findBroker(topic);
        su.register(topic);
        su.printAvailableBusLines();
        //su.printBusLinesArray();
        System.out.println("searching for 024");
        su.findBroker(topic);
        su.register(topic);
    }

}
