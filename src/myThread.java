import java.io.IOException;

public class myThread implements Runnable  {
    //
    private int threadId;
    public myThread(int threadId){
        this.threadId=threadId;
    }
    @Override
    public void run() {
        Bus bus = new Bus("b12",30,10,"patision");
        try {
            new Client().startClient("172.0.0.1",4201,"hy im bill ",bus);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
