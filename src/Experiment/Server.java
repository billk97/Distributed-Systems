package Experiment;

import DataTypes.Bus;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    /**creates to 2 Socket Type Objects **/
    private ServerSocket listenerSocket= null;
    private Socket connection = null;
    /**opens the Experiment.Server**/
    public void openServer() throws IOException, ClassNotFoundException {
        System.out.println("Start Experiment.Server");
        runServer(connection,listenerSocket);
        listenerSocket.close();
    }
    /**runServer: is responsible for the running part of the Experiment.Server
     * requires two arguments  **/
    private void runServer(Socket connection1,ServerSocket listenerSocket) throws IOException, ClassNotFoundException {
        System.out.println("run Experiment.Server");
        listenerSocket=new ServerSocket(4321);
        while (true)
        {
            System.out.println("waiting");
            connection1=listenerSocket.accept();
            ObjectOutputStream out = new ObjectOutputStream(connection1.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(connection1.getInputStream());
            System.out.println(in.readUTF());
            System.out.println((Bus)in.readObject());
            in.close();
            System.out.println("waiting2");
            out.close();
            connection1.close();
        }

    }//end runServer
}//end class Experiment.Server
