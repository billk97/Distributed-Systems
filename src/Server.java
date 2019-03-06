import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public void openServer() throws IOException, ClassNotFoundException {
        ServerSocket listenerSocket= null;
        Socket connection = null;
        runServer(connection,listenerSocket);
        listenerSocket.close();
    }

    private void runServer(Socket connection,ServerSocket listenerSocket) throws IOException, ClassNotFoundException {
        while (true)
        {
            connection=listenerSocket.accept();
            ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
            System.out.println(in.readUTF());
            System.out.println((Bus)in.readObject());
            in.close();
            out.close();
            connection.close();
        }
    }//end runServer



}//end
