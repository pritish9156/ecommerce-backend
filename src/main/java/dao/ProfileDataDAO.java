package dao;

import org.hibernate.Session;
import entity.User;
import util.HibernateUtil;

public class ProfileDataDAO {

	public Long getOrderCount(User user) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Long count = session.createQuery("SELECT COUNT(o) FROM Order o WHERE o.user = :user", Long.class)
				.setParameter("user", user)
				.uniqueResult();
		
		session.close();
		
		return count;
	}

	public Long getWishlistCount(User user) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Long count = session.createQuery("SELECT COUNT(w) FROM Wishlist w WHERE w.user = :user", Long.class)
				.setParameter("user", user)
				.uniqueResult();
		
		session.close();
		
		return count;
	}
	
	public Long getReviewsCount(User user) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Long count = session.createQuery("SELECT COUNT(r) FROM Review r WHERE r.user = :user", Long.class)
				.setParameter("user", user)
				.uniqueResult();
		
		session.close();
		
		return count;
	}

	public Long getAddressCount(User user) {
	
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Long count = session.createQuery("SELECT COUNT(a) FROM Address a WHERE a.user = :user", Long.class)
				.setParameter("user", user)
				.uniqueResult();
		
		session.close();
		
		return count;
	}

}
