import java.io.IOException;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        /**defines the broker port and ip for this machine **/
        Brocker v1 = new Brocker(4202, "172.16.2.44");
        String table[] = new String[3];
        /**defines the remote broker**/
        table[0] = "172.16.2.44";
        table[1] = "172.16.2.45";
        table[2] = "172.16.2.46";
        v1.ConnectServers(table);
        v1.startServer();
    }
}
