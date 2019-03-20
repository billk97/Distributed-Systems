import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Brocker v1 = new Brocker(4202, "192.168.1.25");
        Brocker v2 = new Brocker(4203, "192.168.1.65");
        v1.printNodeInfo();
        v1.setBrokerList(v1,0);
        v1.setBrokerList(v2,1);
        System.out.println("list: "+v1.BrokerList.get(0).ipAddress);
        v1.startServer();

    }
}
