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

import com.sylar.entity.Sms;

@Repository
public class SmsDao {
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	
	/**
	 * 获取Hibernate的Session
	 */
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 查询短信
	 * 
	 * @param telephone
	 *            联系人手机号码
	 * @param smstype
	 *            短信类型: 1:接收的; 0:发送
	 * @param page
	 *            页码
	 * @param pagesize
	 *            页容
	 * @return 短信List
	 */
	@SuppressWarnings("rawtypes")
	public List listSms(String telephone, short smstype, int page, int pagesize) {
		Criteria criteria = getSession().createCriteria(Sms.class);
		// 按时间升序
		criteria.addOrder(Order.asc("posttime"));
		// 页码及显示条数
		criteria.setFirstResult((page - 1) * pagesize);
		criteria.setMaxResults(pagesize);
		// 条件:手机号码
		Criterion telephoneEq = Restrictions.eq("telephone", telephone);
		criteria.add(telephoneEq);
		// 条件:接收or发送
		Criterion smstypeEq = Restrictions.eq("smstype", smstype);
		criteria.add(smstypeEq);
		return criteria.list();
	}

	/**
	 * 获取短信总条数
	 * 
	 * @param telephone
	 *            联系人手机号码
	 * @param smstype
	 *            短信类型: 1:接收的; 0:发送
	 * @return 短信总条数
	 */
	public int countSms(String telephone, short smstype) {
		return getSession().createCriteria(Sms.class) //
				.add(Restrictions.eq("smstype", smstype)) //
				.add(Restrictions.eq("telephone", telephone)) //
				.list() //
				.size(); //
	}

	/**
	 * 新增短信
	 */
	public void save(Sms sms) {
		try {
			getSession().save(sms);
		} catch (Exception e) {
			System.out.println("插入DB失败: [" + sms.getTelephone() + " - " + sms.getPosttime() + " - " + sms.getSmscontent() + "]");
			throw e;
		}
	}
}
