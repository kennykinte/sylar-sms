package com.sylar.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.sylar.entity.Users;

@Repository
public class UsersDao {
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 列表人数(全部)
	 */
	public int listSize() {
		return getSession().createCriteria(Users.class).list().size();
	}

	/**
	 * 列表人数(按分组)
	 */
	@SuppressWarnings("rawtypes")
	public int listSizeByGroup(int groupid) {
		Criteria criteria = getSession().createCriteria(Users.class);
		Criterion groupEq = Restrictions.eq("groupid", groupid);
		criteria.add(groupEq);
		List list = criteria.list();
		return list.size();
	}

	/**
	 * 列表(全部)
	 * 
	 * @param sort
	 *            排序字段
	 * @param order
	 *            asc/desc
	 * @param page
	 *            页码
	 * @param pagesize
	 *            页容
	 * @return 联系人列表
	 */
	@SuppressWarnings("rawtypes")
	public List list(String sort, String order, int page, int pagesize) {
		Criteria criteria = getSession().createCriteria(Users.class);
		// 排序
		if ("asc".equals(order)) {
			criteria.addOrder(Order.asc(sort));
		} else {
			criteria.addOrder(Order.desc(sort));
		}
		// 页码
		criteria.setFirstResult((page - 1) * pagesize);
		criteria.setMaxResults(pagesize);
		return criteria.list();
	}

	/**
	 * 列表(按分组)
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public List listByGroupId(int groupid, String sort, String order, int page, int pagesize) {
		Criteria criteria = getSession().createCriteria(Users.class);
		// 排序
		if ("asc".equals(order)) {
			criteria.addOrder(Order.asc(sort));
		} else {
			criteria.addOrder(Order.desc(sort));
		}
		// 页码
		criteria.setFirstResult((page - 1) * pagesize);
		criteria.setMaxResults(pagesize);
		// 条件(分组)
		Criterion groupEq = Restrictions.eq("groupid", groupid);
		criteria.add(groupEq);
		return criteria.list();
	}

	/**
	 * 单查
	 * 
	 * @param id
	 * @return
	 */
	public Users get(Integer id) {
		return (Users) getSession().get(Users.class, id);
	}

	/**
	 * 保存
	 * 
	 * @param user
	 * @return
	 */
	public Integer save(Users user) {
		return (Integer) getSession().save(user);
	}

	/**
	 * 删除
	 * 
	 * @param userId
	 */
	public void remove(Integer userId) {
		Session session = getSession();
		session.delete(session.load(Users.class, userId));
	}

	/**
	 * 更新
	 * 
	 * @param user
	 */
	public void update(Users user, int id) {
		Session session = getSession();
		Users userInDb = (Users) session.load(Users.class, id);
		userInDb.setUsername(user.getUsername());
		userInDb.setGroupid(user.getGroupid());
		userInDb.setTelephone(user.getTelephone());
		userInDb.setSex(user.getSex());
		userInDb.setNote(user.getNote());
		session.update(userInDb);
	}

	/**
	 * 更新(分组)
	 */
	public void updateGroup(int id, int groupid) {
		Session session = getSession();
		Users userInDb = (Users) session.load(Users.class, id);
		userInDb.setGroupid(groupid);
		session.update(userInDb);
	}
}
