import java.io.IOException;

public class myThread implements Runnable  {
    private String threadType;
    private int threadId;
    public myThread(String threadType , int threadId){
        this.threadType=threadType;this.threadId=threadId;
    }
    @Override
    public void run() {
        Bus bus = new Bus("b12",30,10,"patision");
        try {
            Thread.sleep(1000);
            new Client().startClient("172.0.0.1",4201,"hy im bill ",bus);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

}
