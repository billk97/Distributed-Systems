package com.aueb.opabus.CodeFolder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BrokerConnect extends Node implements Runnable {
    private String RemoteBrokerIp;
    private int RemoteBrokerPort;
    /**RemoteBrokerIp= my Ip , RemoteBrokerPort = the remote broker port , port= this brokers port**/
    public BrokerConnect(String RemoteBrokerIp, int RemoteBrokerPort,int port){
        this.RemoteBrokerIp=RemoteBrokerIp;
        this.RemoteBrokerPort=RemoteBrokerPort;
        this.port=port;
    }
    public void run(){
        MyConnection();
    }
    /**Sends request for connection between two brokers **/
    public void MyConnection(){
        Socket socket = connect(RemoteBrokerIp,RemoteBrokerPort);
        try {
            System.out.println("Connected");
            ObjectInputStream  in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println(in.readUTF());
            out.writeUTF("BrokerAdd");
            out.flush();
            out.writeUTF(Integer.toString(port));
            out.flush();
        } catch (IOException e) {
            System.out.println("No connection could be established");
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

    public void setPort(int port){
        this.port=port;
    }

}
