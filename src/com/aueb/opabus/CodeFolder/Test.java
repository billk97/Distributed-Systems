package com.aueb.opabus.CodeFolder;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        /**defines the broker port and ip for this machine **/
        Brocker v1 = new Brocker(4202, "192.168.1.103");
        String table[] = new String[2];
        /**defines the remote broker**/
        //table[0] = "192.168.1.87";
        //table[1] = "192.168.1.74";
        //table[2] = "192.168.1.70";
        //v1.ConnectServers(table);
        v1.startServer();
    }
}
