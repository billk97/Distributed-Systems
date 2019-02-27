package threadsLab;

public class myThreadRunnable implements Runnable{
        String input;

        public myThreadRunnable(String input){this.input=input;}


    public void run(){
        for(int i=0;i<10;i++){
            System.err.println(i+":\t"+input);
            try {
                Thread.sleep((int)(Math.random()*500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
            myThreadRunnable t = new myThreadRunnable("Distributed");

            Thread tr=new Thread(t);
            tr.start();
            myThreadRunnable t2= new myThreadRunnable("Systems");
            new Thread(t2).start();

    }
}
