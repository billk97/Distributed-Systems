package Sychronized;

public class ThreadDemo extends Thread{
    private String threadName;
    final PrintDemo PD;

    ThreadDemo(String name,PrintDemo pd){
        threadName =name;
        PD=pd;
    }
    public void run(){
        synchronized (PD) { //tha pernei mono ena thread pd kathe fora
            PD.printCount(threadName);
        }
        System.out.println("Thread "+ threadName +" exiting. ");
    }
}
