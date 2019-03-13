package Experiment;

import DataTypes.Bus;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Client {
    /**this function is responsible for the client connection
     * it requires:
     * the Experiment.Server ip that it wants to communicate to
     * the port of the server
     * the Message type String that it wants to send
     * and an Object type bus**/
    //TODO make two startClient 1 for message 1 for DataTypes.Bus
    //TODO as Melis for an method to include in and out
    public void startClient(String serverIp, int port , Bus bus) throws IOException, InterruptedException {
        System.out.println("StartClient");
        /**port initialization **/
        Socket socket =null;
        ObjectOutputStream out =null;
        ObjectInputStream in = null;
        System.out.println("ip "+serverIp + "port "+port );
        socket= new Socket(InetAddress.getByName(serverIp),port);
        out= new ObjectOutputStream(socket.getOutputStream());
        in= new ObjectInputStream(socket.getInputStream());
        System.out.println("running client");
        /**sending messages **/
        out.writeObject(bus);
        out.flush();
        in.close();
        out.close();
        socket.close();

    }//end startClient

    public static void main(String[] args) throws IOException, InterruptedException {
        Client c1 = new Client();
        Bus b1 = new Bus("a12","3242","aero","27","id","vd");
        c1.startClient("localhost",4201,b1);
    }
}//end Class Experiment.Client
