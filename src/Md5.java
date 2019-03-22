import javax.xml.bind.DatatypeConverter;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**this is a small implementation for the Md5 algorithm used to hash the ip&port
 * and the buslines **/
public class Md5 {
    private MessageDigest md;
    private String BusLine;
    /**this function returns the hash of a string**/
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
        //System.out.println(myHash);
        return myHash;
    }//end HASH
    /**compares two hashes and returns the bigger one **/
    public String CompareHashes(String Hash1, String Hash2){
        int result= Hash1.compareTo(Hash2);
        //when Hash1>Hash2
        if(result==1)
        {
            return Hash1;
        }
        //when Hash2>Hash1
        else if(result==-1)
        {
            return Hash2;
        }//when Hash1=Hash2
        else return Hash1;
    }//end compare
    /**a small implementation/example/test**/
    public static void main(String[] args) {
        Md5 m5 =new Md5();
        m5.HASH("172"+Integer.toString(420));
    }
}//end classMd5Implement
