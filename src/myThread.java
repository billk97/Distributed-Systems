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
        Node node =new Node("bill","localhost","Client",4322,344);
        connectClient(bus,node);
    }//end run
    public void connectClient(Bus bus, Node node){
        try {
            System.out.println("ip: " +node.getIpAddress()+" port "+ node.getPort());
            Thread.sleep(100);
            new Client().startClient(node.getIpAddress(),node.getPort(),"mesage",bus);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}//end class myThread
