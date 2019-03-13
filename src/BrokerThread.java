import java.io.IOException;

public class BrokerThread {
    public static void main(String[] args) throws IOException {
        Brocker b1= new Brocker();
        b1.initialize(4201);
        Brocker b2=new Brocker();
        b2 .initialize(4203);
        Publisher p1 = new Publisher();
        p1.initialize(4204);
        // Subscriber s1 = new Subscriber();
        //s1.initialize(4206);
    }
}
