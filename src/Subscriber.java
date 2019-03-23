import DataTypes.Topic;
import DataTypes.Value;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Subscriber extends Node implements Serializable {
    private static final long serialVersionUID = -2122691439868668146L;
    private String brokerIp = "192.168.1.65";
    private int brokerport= 4202;
    public Subscriber(){}
    public Subscriber(String ip, int port){
        super(port,ip);
    }
    /**register for the first time for a topic**/
    public void register(Topic topic){
        Socket socket = connect(brokerIp,brokerport);
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println(in.readUTF());
            out.writeUTF("Subscribe");
            out.flush();
            out.writeObject(topic);
            out.flush();
            Value localValue = (Value) in.readObject();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            Disconnect(socket);
        }
    }//end register
    /**disconnect from the topic does not receive any more data **/
    private void disconnect(Topic topic){
        Socket socket = connect(brokerIp,brokerport);
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println(in.readUTF());
            out.writeUTF("Unsubscribe");
            out.flush();
            out.writeObject(topic);
            out.flush();
            out.close();
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            Disconnect(socket);
        }
    }//end disconnect
    /**print the data in a readable format**/
    private void VisualiseData(){}
}//end class Subscriber
