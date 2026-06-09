package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import auth.VerificationToken;
import entity.User;
import entity.enums.TokenType;
import util.HibernateUtil;

public class VerificationTokenDAO {
	
	public boolean save(VerificationToken token) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = session.beginTransaction();
		
		try {
			session.persist(token);	
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
	
	public VerificationToken findByToken(String token) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		String hql = "FROM VerificationToken WHERE token = :token";
		
		Query<VerificationToken> query = session.createQuery(hql, VerificationToken.class);
		
		query.setParameter("token", token);
		
		VerificationToken verificationToken = query.uniqueResult();
			
		session.close();

		return verificationToken;
	}
	
	public boolean update(VerificationToken token) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = session.beginTransaction();
		
		try {
			session.merge(token);
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
	
	public List<VerificationToken> findActiveVerificationTokens(User user){
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		String hql = "FROM VerificationToken WHERE user = :user AND isUsed = false AND tokenType = :tokenType";
		
		Query<VerificationToken> query = session.createQuery(hql, VerificationToken.class);
		
		query.setParameter("user", user);
		
		query.setParameter("tokenType", TokenType.EMAIL_VERIFICATION);
		
		List<VerificationToken> result = query.getResultList();
		
		session.close();
		
		return result;
	}

}
