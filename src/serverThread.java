import Experiment.Server;

import java.io.IOException;
/**this is a Experiment.Server thread
 * idea:  to have this server in the same machine with other stuff even in the same main
 * if needed in order to be more flexible **/
public class serverThread implements Runnable{
    private String threadType;
    private int threadId;
    //and add them as attributes
    //TODO make a function getCpuUsage
    //TODO make a function getRamUsage
    //constructor
    public serverThread(String threadType, int threadId){
        this.threadType=threadType;
        this.threadId=threadId;
    }
    /**this is what the thread will do when up and running**/
    public void run(){
        try {
            new Server().openServer();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }//end run
}//end class ServerThread
