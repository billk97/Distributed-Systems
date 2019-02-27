package threadsLab;

public class myThread extends Thread{
    String input;

    public myThread(String input){
        this.input= input;
    }
    public void run(){
        for(int i=0;i<10;i++){
            System.out.println(i+":\t"+input);
            try {
                sleep((int)(Math.random()*500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        Thread t= new myThread("Distributed");
        Thread t2= new myThread("Systems");
        t.start();
        t2.start();
    }
}
