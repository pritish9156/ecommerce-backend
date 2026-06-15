package dao;

import entity.Brand;
import util.HibernateUtil;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;

public class BrandDAO {
	
	public boolean save(Brand brand) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = session.beginTransaction();
		
		try {
			
			session.persist(brand);
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
	
	public Brand findBrandById(Long id) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Brand brand = session.find(Brand.class, id);
		
		session.close();
		
		return brand;
	}
	
	public boolean update(Brand brand) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = session.beginTransaction();
		
		try {
			
			session.merge(brand);
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
	
	public boolean delete(Brand brand) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = session.beginTransaction();
		
		try {
			
			Brand mergedBrand = session.merge(brand);
			session.remove(mergedBrand);
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
	
	public boolean deactivateBrand(Long id) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = session.beginTransaction();
		
		String hql = "UPDATE Brand set isActive = false WHERE id = :id";
		
		MutationQuery query = session.createMutationQuery(hql);
		
		query.setParameter("id", id);
		
		try {
			
			int res = query.executeUpdate();
			
			if(res > 0) {
				tx.commit();
				return true;
			}
			
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		finally {
			session.close();
		}
		
		return false;
	}

	public Brand findBrandByName(String name) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		String hql = "FROM Brand WHERE name = :name";
		
		Query<Brand> query = session.createQuery(hql, Brand.class);
		
		query.setParameter("name", name);	
			
		Brand brand = query.uniqueResult();
			
		session.close();
		
		return brand;
	}

	public List<Brand> findAll() {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		String hql = "FROM Brand WHERE isActive = true";
		
		Query<Brand> query = session.createQuery(hql, Brand.class);	
			
		List<Brand> brands = query.getResultList();
			
		session.close();
		
		return brands;
		
	}

	public Brand findBrandBySlug(String slug) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		String hql = "FROM Brand WHERE slug = :slug";
		
		Query<Brand> query = session.createQuery(hql, Brand.class);
		
		query.setParameter("slug", slug);	
			
		Brand brand = query.uniqueResult();
			
		session.close();
		
		return brand;
	}

}
