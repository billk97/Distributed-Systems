import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public void startClient(String serverIp,int port ,String message ,Bus bus) throws IOException {
        System.out.println("StartClient");
        Socket socket =null;
        ObjectOutputStream out =null;
        ObjectInputStream in = null;
        socket= new Socket(InetAddress.getByName(serverIp),port);
        out= new ObjectOutputStream(socket.getOutputStream());
        in= new ObjectInputStream(socket.getInputStream());
        out.writeUTF(message);
        out.flush();
        out.writeObject(bus);
        out.flush();
    }
}
