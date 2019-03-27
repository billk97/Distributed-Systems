import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BrokerConnect extends Node implements Runnable {
    private String RemoteBrokerIp;
    private int RemoteBrokerPort;
    public BrokerConnect(String RemoteBrokerIp, int RemoteBrokerPort){
        this.RemoteBrokerIp=RemoteBrokerIp;
        this.RemoteBrokerPort=RemoteBrokerPort;
    }
    public void run(){
        //connection();
    }
    public void connection(){
        Socket socket = connect(RemoteBrokerIp,RemoteBrokerPort);
        try {
            ObjectInputStream  in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println(in.readUTF());
            out.writeUTF("BrokerAdd");
            out.flush();
            out.writeUTF(Integer.toString(port));
            BrokerList.add(new Brocker(RemoteBrokerPort,RemoteBrokerIp));
            out.flush();
        } catch (IOException e) {
            System.out.println("No connection could be established");
            //e.printStackTrace();
        }
    }

    /**geter seter **/
    public String getRemoteBrokerIp() {
        return RemoteBrokerIp;
    }

    public void setRemoteBrokerIp(String remoteBrokerIp) {
        RemoteBrokerIp = remoteBrokerIp;
    }

    public int getRemoteBrokerPort() {
        return RemoteBrokerPort;
    }

    public void setRemoteBrokerPort(int remoteBrokerPort) {
        RemoteBrokerPort = remoteBrokerPort;
    }

}
