package com.sylar.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.sylar.entity.Usergroup;

@Repository
public class UserGroupDao {
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 列表
	 * 
	 */
	@SuppressWarnings({ "rawtypes" })
	public List list() {
		return getSession().createQuery("from Usergroup order by groupname").list();
	}

	/**
	 * 新增
	 */
	public void save(Usergroup ug) {
		getSession().save(ug);
	}

	/**
	 * 更新
	 */
	public void update(int id, String gpname) {
		Session session = getSession();
		Usergroup ugInDb = (Usergroup) session.load(Usergroup.class, id);
		ugInDb.setGroupname(gpname);
		session.update(ugInDb);
	}

	/**
	 * 删除
	 */
	public void remove(int id) {
		Session session = getSession();
		session.delete(session.load(Usergroup.class, id));
	}
}
