package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import entity.Tags;

import util.HibernateUtil;

public class TagDAO {

	public boolean save(Tags tag) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		try {
			session.persist(tag);
			tx.commit();
			return true;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		return false;
	}

	public boolean update(Tags tag) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {
			session.merge(tag);
			tx.commit();
			return true;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		return false;
	}

	public Tags findById(Long id) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Tags tag = session.find(Tags.class, id);

		session.close();

		return tag;

	}

	public List<Tags> findAll() {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "FROM Tags";

		Query<Tags> query = session.createQuery(hql, Tags.class);

		List<Tags> tagList = query.getResultList();

		session.close();

		return tagList;
	}

	public boolean delete(Tags tag) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {
			Tags mergedTag = session.merge(tag);
			session.remove(mergedTag);
			tx.commit();
			return true;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		return false;
	}

	public Tags findByName(String tagName) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "FROM Tags where name = :name";

		Query<Tags> query = session.createQuery(hql, Tags.class);

		query.setParameter("name", tagName);

		Tags tags = query.uniqueResult();

		session.close();

		return tags;
	}

	public List<Tags> findByProductId(Long productId) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "SELECT t FROM Product p JOIN p.tags t WHERE p.id = :productId";

		Query<Tags> query = session.createQuery(hql, Tags.class);

		query.setParameter("productId", productId);

		List<Tags> tags = query.getResultList();

		session.close();

		return tags;
	}
}
