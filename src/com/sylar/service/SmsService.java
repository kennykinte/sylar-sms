package com.sylar.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sylar.dao.SmsDao;
import com.sylar.dao.UserGroupDao;
import com.sylar.dao.UsersDao;
import com.sylar.entity.Usergroup;
import com.sylar.entity.Users;
import com.sylar.util.JsonUtil;

@Service
public class SmsService {
	@Autowired
	private SmsDao smsDao;

	@Autowired
	private UserGroupDao userGroupDao;

	@Autowired
	private UsersDao userDao;

	/**
	 * 构建联系人树
	 */
	@SuppressWarnings( { "rawtypes", "unchecked" })
	public List userTree(String type, int groupid) {
		// 构建分组
		if ("group".equals(type)) {
			List groupMapList = new ArrayList();
			List groupList = userGroupDao.list();
			Iterator it = groupList.iterator();
			while (it.hasNext()) {
				Map<String, Object> map = new HashMap<String, Object>();
				Usergroup usergroup = (Usergroup) it.next();
				map.put("id", usergroup.getId());
				map.put("text", usergroup.getGroupname());
				map.put("state", "closed");
				map.put("attributes", false);
				groupMapList.add(map);
			}
			return groupMapList;
		} else {
			// 构建组内联系人
			List userMapList = new ArrayList();
			List userList = userDao.listByGroupId(groupid, "username", "asc", 1, 999);
			Iterator it = userList.iterator();
			while (it.hasNext()) {
				Map<String, Object> map = new HashMap<String, Object>();
				Users user = (Users) it.next();
				map.put("id", user.getId());
				map.put("text", user.getUsername() + " (" + user.getTelephone() + ")");
				map.put("iconCls", user.getSex() == 0 ? "icon-user" : "icon-user-female");
				map.put("attributes", user.getTelephone());
				userMapList.add(map);
			}
			return userMapList;
		}
	}

	/**
	 * 查询短信
	 */
	@SuppressWarnings( { "rawtypes", "unchecked" })
	public Map<String, Object> listSms(String userTel, short smstype, int page, int pagesize) {
		Map<String, Object> map = new HashMap<String, Object>();
		List smsContent = smsDao.listSms(userTel, smstype, page, pagesize);
		int smsCount = smsDao.countSms(userTel, smstype);
		map.put("smsCount", smsCount);
		map.put("smsContent", JsonUtil.smsList2Map(smsContent));
		return map;
	}

	/**
	 * 实体类列表转为MAP列表,为Tree服务 -- 暂未实现
	 */
	public <T> List<Map<String, Object>> beanList2MapList(List<T> beanList) {
		List<Map<String, Object>> returnMapList = new ArrayList<Map<String, Object>>();
		Iterator<T> it = beanList.iterator();
		while (it.hasNext()) {
			T t = it.next();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", t.hashCode());
			map.put("text", t.getClass().getSimpleName());
			returnMapList.add(map);
		}
		return returnMapList;
	}
}
