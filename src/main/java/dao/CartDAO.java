package dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import entity.Cart;
import entity.User;
import util.HibernateUtil;

public class CartDAO {
	
	public boolean save(Cart cart) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = session.beginTransaction();
		
		try {
			
			session.persist(cart);
			tx.commit();
			return true;
			
		} catch (Exception e) {
			
			tx.rollback();
			e.printStackTrace();
		}
		finally {
			session.close();
		}
		
		return false;
	}
	
	public boolean update(Cart cart) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = session.beginTransaction();
		
		try {
			
			session.merge(cart);
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
	
	public Cart findByUser(User user) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		String hql = "FROM Cart WHERE user = :user";
		
		Query<Cart> query = session.createQuery(hql, Cart.class);
		
		query.setParameter("user", user);
		
		Cart cart = query.uniqueResult();
		
		session.close();
		
		return cart;
	}
	
	public Cart findById(Long id) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		 Cart cart = session.find(Cart.class, id);
		 
		 session.close();
		 
		 return cart;
		
	}

}
