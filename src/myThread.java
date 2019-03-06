import java.io.IOException;
/**this is a client type thread two emulate multiple clients in the same machine
 * idea: to have multiple clients that will do the same thing or thou parameters to slightly
 * different while being flexible**/
//TODO rename to clientThread
public class myThread implements Runnable  {
    private String threadType;
    private int threadId;
    //constructor
    public myThread(String threadType , int threadId){
        this.threadType=threadType;this.threadId=threadId;
    }
    @Override
    /**this is what the thread will do when up and running**/
    public void run() {
        //new object type bus
        Bus bus = new Bus("b12",30,10,"patision");
        try {
            Thread.sleep(1000);
            //TODO make the below parameters
            new Client().startClient("172.0.0.1",4201,"hy im bill ",bus);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }//end run
}//end class myThread
