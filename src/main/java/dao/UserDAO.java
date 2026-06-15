package dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import entity.User;
import util.HibernateUtil;

public class UserDAO {
	
	public boolean save(User user) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = session.beginTransaction();
		
		try {
			session.persist(user);
			tx.commit();
			return true;
		}
		catch(Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		finally {
			session.close();
		}
		
		return false;
	
	}
	
	
	public User findByEmail(String email) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		System.out.println("Searching Email = " + email);
		
		String hql = "FROM User WHERE email = :email";
		Query<User> query = session.createQuery(hql, User.class);
			
		query.setParameter("email", email);
		
		User user = query.uniqueResult();
		
		System.out.println("user = " +user);
		
		session.close();
			
		return user;

	}
	
	public User findByMobileNumber(String mobileNumber) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "FROM User WHERE mobileNumber = :mobileNumber";
		Query<User> query = session.createQuery(hql, User.class);
			
		query.setParameter("mobileNumber", mobileNumber);
			
		User user = query.uniqueResult();
		
		session.close();
		
		return user;	

	}
	
	public boolean update(User user) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = session.beginTransaction();
		
		try {
			session.merge(user);
			tx.commit();
			return true;
		}
		catch(Exception e) {
			tx.rollback();
		}
		finally {
			session.close();
		}
		
		return false;
	}

}
