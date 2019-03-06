import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**this is a small implementation for the Md5 algorithm used to hash the ip&port
 * and the buslines **/
//TODO make a function to receive string and return a String but hashed
//Todo clean it up and make it easy and flexible to user for the rest of the project
//todo make a class compare tha will compare two Hashes remember to use modulo
public class Md5Implement {
    String password="bill";
    MessageDigest md;
    public void run() {
        {
            try {
                md = MessageDigest.getInstance("MD5");
                md.update(password.getBytes());
                byte[] digest = md.digest();
                String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
                System.out.println(myHash);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }//end run

    public static void main(String[] args) {
        Md5Implement m5 =new Md5Implement();
        m5.run();
    }
}//end classMd5Implement
