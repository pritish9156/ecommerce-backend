package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import entity.User;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import io.jsonwebtoken.Jwts;

public class JwtUtil {

	private static String secret;
	private static long expiry;
	private static Properties properties;
	
	static {
		
		InputStream inputStream = JwtUtil.class.getClassLoader()
				.getResourceAsStream("application.properties");
		
		if (inputStream == null) {
            throw new RuntimeException("Sorry, unable to find application.properties");
        }
		else {
			properties = new Properties();
			
			try {
				properties.load(inputStream);
				secret = properties.getProperty("jwt.secret");
				expiry = Long.parseLong(properties.getProperty("jwt.expiry"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static SecretKey getSigningKey() {

	    return Keys.hmacShaKeyFor(
	            secret.getBytes(
	                    StandardCharsets.UTF_8));
	}
	
	public static String generateToken(User user) {

	    Date now = new Date();

	    Date expiryDate = new Date(now.getTime() + expiry);

	    return Jwts.builder()

	            .subject(user.getEmail())

	            .claim(
	                    "userId",
	                    user.getId())

	            .claim(
	                    "role",
	                    user.getRole().name())

	            .issuedAt(now)

	            .expiration(expiryDate)

	            .signWith(getSigningKey())

	            .compact();
	}
	
	public static String extractEmail(String token) {

	    return Jwts.parser()
	            .verifyWith((SecretKey) getSigningKey())
	            .build()
	            .parseSignedClaims(token)
	            .getPayload()
	            .getSubject();
	}
	
	public static boolean validateToken(String token) {

	    try {

	        Jwts.parser()
	            .verifyWith((SecretKey) getSigningKey())
	            .build()
	            .parseSignedClaims(token);

	        return true;

	    } catch (Exception e) {

	        return false;
	    }
	}
}
