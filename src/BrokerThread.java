import java.io.IOException;

public class BrokerThread implements Runnable {
    Brocker b1= new Brocker(1);
    public void run()
    {
        try {
            b1.calculateKeys();
            b1.initialize(4201);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)  {
        BrokerThread t1 = new BrokerThread();
        new Thread(t1).start();
        Publisher p1 = new Publisher();
        System.out.println("waiting");
        for(Brocker brocker : p1.getBrokerList()){
            System.out.println(brocker.getBrokerRange());
        }
    }
}