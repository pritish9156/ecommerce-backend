package util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	static SessionFactory sessionFactory = null;
	
	static {
		
		Configuration config = new Configuration().configure();
		
		try {
			sessionFactory = config.buildSessionFactory();
		}
		catch(Exception e) {
		    e.printStackTrace();
		}
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
