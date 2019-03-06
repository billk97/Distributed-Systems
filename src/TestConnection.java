public class TestConnection {
    public static void main(String[] args) {
        myThread m1 = new myThread(1);
        myThread m2 = new myThread(2);
        new Thread(m1).start();
        new Thread(m2).start();
    }
}