import dto.request.LoginRequest;
import dto.response.AuthResponse;
import entity.User;
import entity.enums.Role;
import service.AuthService;
import util.JwtUtil;

public class App{
	
	public static void main(String[] args) {
		
		User user = new User();

		user.setEmail("test@gmail.com");
		user.setRole(Role.CUSTOMER);
		
		String token = JwtUtil.generateToken(user);

		System.out.println(token);

		System.out.println(
				JwtUtil.extractEmail(token));

		System.out.println(
				JwtUtil.validateToken(token));

		System.out.println(
				JwtUtil.validateToken("abc"));
	
	}
}