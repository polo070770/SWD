package utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	public MD5(){};

public String getMD5Hex(final String inputString){

    MessageDigest md;
    
	try {
		md = MessageDigest.getInstance("MD5");
	    md.update(inputString.getBytes());

	    byte[] digest = md.digest();
	    return convertByteToHex(digest);
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return "";
    
}

private  String convertByteToHex(byte[] byteData) {

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < byteData.length; i++) {
        sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
    }

    return sb.toString();
	}
}