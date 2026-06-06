package entity;

import org.hibernate.Session;
import org.hibernate.Transaction;

import util.HibernateUtil;

public class App{
	
	public static void main(String[] args) {
		Session session = HibernateUtil.getSessionFactory().openSession();
	
		Transaction tx = session.beginTransaction();

		User user = new User();

		user.setFirstName("Pritish");
		user.setLastName("Pawar");
		user.setEmail("pritish@gmail.com");
		user.setPassword("123456");
		user.setMobileNumber("9876543210");
		user.setRole(Role.CUSTOMER);

		session.persist(user);
		
		tx.commit();
		
		session.close();
	
	}
}