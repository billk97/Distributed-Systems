import DataTypes.Topic;

public class TestSub {

    public static void main(String[] args) {
        Subscriber su = new Subscriber("192.168.1.65",4202);
        Topic topic = new Topic("bill");
        su.register(topic);
    }

}
