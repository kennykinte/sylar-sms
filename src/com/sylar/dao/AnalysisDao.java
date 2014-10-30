package com.sylar.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AnalysisDao {
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
	 * 分析短信收发情况
	 * 
	 * @param smstype
	 *            短信类型: rec/snd/其他
	 * @param sort
	 *            排序字段: count(短信条数)/first(第一条发送时间)/latest(最后条发送时间)
	 * @param order
	 *            排序方式 : asc/desc
	 * @param page
	 *            页码: 为0则不分页
	 * @param pagesize
	 *            页容
	 */
	@SuppressWarnings("rawtypes")
	public List analysisSms(String smstype, String sort, String order, int page, int pagesize) {
		String strSQL = "select username,count(1) as count, min(posttime) as first, max(posttime) as latest "
				+ "from sms left join users on sms.telephone=users.telephone group by users.username ";
		// 短信类型(发送/接收/全部)
		if ("rec".equals(smstype))
			strSQL += " ,sms.smstype having sms.smstype=1 ";
		else if ("snd".equals(smstype))
			strSQL += " ,sms.smstype having sms.smstype=0 ";
		// 排序字段
		if ("count".equals(sort))
			strSQL += " order by count(1) ";
		if ("first".equals(sort))
			strSQL += " order by min(posttime) ";
		if ("latest".equals(sort))
			strSQL += " order by  max(posttime) ";
		// 排序方式
		strSQL += order;
		// 分页
		SQLQuery query = getSession().createSQLQuery(strSQL);
		if (page != 0) {
			query.setFirstResult((page - 1) * pagesize);
			query.setMaxResults(pagesize);
		}
		return query.list();
	}
}
