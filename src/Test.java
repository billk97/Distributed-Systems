import java.io.IOException;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        Scanner in = new Scanner(System.in);
//        System.out.println("Setting up broker! ");
//        System.out.print("Set brocker Ip: ");
//        String brokerIp =in.nextLine();
//        System.out.print("Set Up Broker port: ");
//        int brokerport = Integer.parseInt(in.nextLine());
        /**defines the broker port and ip for this machine **/
        Brocker v1 = new Brocker(4202, "172.16.2.25");
        //Brocker v1 = new Brocker(4202, "192.168.1.65");
        String table [] = new String[3];
        /**defines the remote broker**/
        //table[0]="172.16.1.72";
        table[0]="172.20.1.71";
        table[1]="4202";
        table[2]="4202";
        //v1.add(table);
        v1.startServer();
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
       // Brocker v2 = new Brocker(4203, "192.168.1.65");
        //Brocker v3 = new Brocker(4204,"192.168.1.55");
        //Brocker v4 = new Brocker(4205,"192.168.1.35");
       // v1.connectToBroker("localhost", 4204);
//        v1.printNodeInfo();
//        v1.setBrokerList(v1,0);
//        v1.setBrokerList(v2,1);
//        System.out.println("list: "+v1.BrokerList.get(0).ipAddress);
        //v1.printBrokerRangeMap();
        //v1.printBrokerList();
        //v1.sortBrokerList();
    }
}
