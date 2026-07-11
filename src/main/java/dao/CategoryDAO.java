package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import entity.Category;
import util.HibernateUtil;

public class CategoryDAO {
	
	public boolean save(Category category) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = session.beginTransaction();
		
		try {
			session.persist(category);
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
	
	public boolean update(Category category) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = session.beginTransaction();
		
		try {
			session.merge(category);
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

	public Category findById(Long id) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Category category = session.find(Category.class, id);

		session.close();

		return category;

	}
	
	public List<Category> findAll() {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		String hql = "FROM Category";
		
		Query<Category> query = session.createQuery(hql, Category.class);
		
		List<Category> categoryList = query.getResultList();
		
		session.close();
		
		return categoryList;
	}
	
	public boolean delete(Category category) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = session.beginTransaction();
		
		try {
			Category mergedCategory = session.merge(category);
			session.remove(mergedCategory);
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

	public Category findByName(String categoryName) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		String hql = "FROM Category where name = :name";
		
		Query<Category> query = session.createQuery(hql, Category.class);
	
		query.setParameter("name", categoryName);
		
		Category category = query.uniqueResult();
		
		session.close();
		
		return category;
	}
}
