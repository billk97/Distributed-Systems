import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**this is a small implementation for the Md5 algorithm used to hash the ip&port
 * and the buslines **/
//TODO make a function to receive string and return a String but hashed
//Todo clean it up and make it easy and flexible to user for the rest of the project
//todo make a class compare tha will compare two Hashes remember to use modulo
public class Md5 {
    private MessageDigest md;
    private String BusLine;

    public String HASH(String StringToHash)  {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("MD5 hashing has some problem");
            e.printStackTrace();
        }
        md.update(StringToHash.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        System.out.println(myHash);
        return myHash;
    }

    public static void main(String[] args) {
        Md5 m5 =new Md5();
        m5.HASH("172"+Integer.toString(420));
    }
}//end classMd5Implement
