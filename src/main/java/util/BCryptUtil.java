package util;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptUtil {

	public static String hashPassword(String password) {
		
		return BCrypt.hashpw(password, BCrypt.gensalt(12));
		
	}
	
	public static boolean verifyPassword(String rawPassword, String hashedPassword) {
		
		return BCrypt.checkpw(rawPassword, hashedPassword);
		
	}
}
