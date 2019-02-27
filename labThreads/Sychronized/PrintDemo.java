package Sychronized;

public class PrintDemo {
    private int x=10;

    public void printCount(String t_name){
        while(x>0){
            System.out.println("Counter  ---  "+x+" "+t_name);
            try {
                Thread.sleep((int) Math.random()*500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            x--;
        }

    }
}
