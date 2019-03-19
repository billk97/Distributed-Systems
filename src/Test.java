import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;

public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Brocker v1 = new Brocker(4202, "192.168.1.65");
        v1.printpORT();
        v1.startServer();

    }
}
