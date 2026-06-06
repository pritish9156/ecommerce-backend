package entity;

import util.HibernateUtil;

public class App{
	
	public static void main(String[] args) {
		HibernateUtil.getSessionFactory();
	}
}