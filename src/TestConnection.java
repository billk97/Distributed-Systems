public class TestConnection {
    /**Nothing special about the this class just for testing
     * **/
    public static void main(String[] args) {
        serverThread s1= new serverThread("server",1);
        new Thread(s1).start();
        System.out.println("Experiment.Server created");
        myThread m1 = new myThread("client",2);
        myThread m2 = new myThread("client",3);
        new Thread(m1).start();
        System.out.println("Client 1 created");
        new Thread(m2).start();
        System.out.println("Client 2 created");
    }//end main
}//end class TestConnection