import java.io.IOException;

public class BrokerThread implements Runnable {
    Brocker b1= new Brocker();
    public void run()
    {
        try {
            b1.initialize(4201);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)  {
        BrokerThread t1 = new BrokerThread();
        new Thread(t1).start();
    }
}
