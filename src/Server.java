import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket listenerSocket= null;
    Socket connection = null;

    public void openServer() throws IOException, ClassNotFoundException {
        System.out.println("Start Server");
        ServerSocket listenerSocket= null;
        Socket connection = null;
        runServer(connection,listenerSocket);
        listenerSocket.close();
    }

    private void runServer(Socket connection1,ServerSocket listenerSocket) throws IOException, ClassNotFoundException {
        System.out.println("run Server");
        listenerSocket=new ServerSocket(4231);
        while (true)
        {
            connection1=listenerSocket.accept();
            ObjectOutputStream out = new ObjectOutputStream(connection1.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(connection1.getInputStream());
            System.out.println(in.readUTF());
            System.out.println((Bus)in.readObject());
            in.close();
            out.close();
            connection1.close();
        }
    }//end runServer
}//end
