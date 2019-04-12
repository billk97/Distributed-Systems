import java.io.IOException;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        /**defines the broker port and ip for this machine **/
        Brocker v1 = new Brocker(4202, "192.168.1.65");
        v1.startServer();
//        Scanner in = new Scanner(System.in);
//        System.out.println("Setting up broker! ");
//        System.out.print("Set brocker Ip: ");
//        String brokerIp =in.nextLine();
//        System.out.print("Set Up Broker port: ");
//        int brokerport = Integer.parseInt(in.nextLine());
//        //Brocker v1 = new Brocker(4202, "172.16.2.11");
//        String table [] = new String[3];
//        /**defines the remote broker**/
//        //table[0]="172.16.1.72";
//        table[0]="192.168.1.70";
//        table[1]="4202";
//        table[2]="4202"; //my port
//        v1.add(table);
//        table[0]="192.168.1.74";
//        table[1]="4202";
//        table[2]="4202";//my port
//        v1.add(table);
//        table[0]="192.168.1.87";
//        table[1]="4202";
//        table[2]="4202";//my port
//        v1.add(table);
//        v1.startServer();
        //Brocker v1 = new Brocker(brokerport, brokerIp);
//        System.out.print("Declare remote Broker: y/n");
//        String option = in.nextLine();
//        while(!option.equals("n")||!option.equals("N")){
//            System.out.print("Set remote Broker Ip: ");
//            String remoteBrokerIp = in.nextLine();
//            System.out.print("Set remote Broker Port: ");
//            String remoteBrokerPort = in.nextLine();
//            String table []= new String[2];
//            table[0]=remoteBrokerIp;
//            table[1]=remoteBrokerPort;
//            v1.add(table);
//            System.out.print("Declare another remote Broker: y/n");
//             option = in.nextLine();
//        }
        //v1.calculateKeys();
        //v1.startServer();


    }
}
