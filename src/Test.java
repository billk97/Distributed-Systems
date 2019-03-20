import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Brocker v1 = new Brocker(4202, "192.168.1.25");
        Brocker v2 = new Brocker(4203, "172.16.2.25");
        v1.printNodeInfo();
        v1.setBrokerList(v1);
        v1.setBrokerList(v2);
        v1.startServer();
    }
}
