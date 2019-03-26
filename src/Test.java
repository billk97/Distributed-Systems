import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Brocker v1 = new Brocker(4202, "192.168.1.75",1);
        Brocker v2 = new Brocker(4203, "192.168.1.65",2);
        //Brocker v3 = new Brocker(4204,"192.168.1.55",3);
        //Brocker v4 = new Brocker(4205,"192.168.1.35",4);
//        v1.printNodeInfo();
//        v1.setBrokerList(v1,0);
//        v1.setBrokerList(v2,1);
//        System.out.println("list: "+v1.BrokerList.get(0).ipAddress);
        v1.calculateKeys();
        //v1.startServer();
        v1.printBrokerRangeMap();
        //v1.printBrokerList();
        //v1.sortBrokerList();
    }
}
