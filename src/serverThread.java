import java.io.IOException;

public class serverThread implements Runnable{
    private String threadType;
    private int threadId;
    public serverThread(String threadType, int threadId){
        this.threadType=threadType;
        this.threadId=threadId;
    }
    public void run(){
        try {
            new Server().openServer();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
