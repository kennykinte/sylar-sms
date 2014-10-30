package com.sylar.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sylar.dao.UsersDao;
import com.sylar.entity.Users;

@Service
public class UserService {

	@Autowired
	private UsersDao userDao;
	@Autowired
	private UserGroupService userGroupService;

	/**
	 * 列表
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Object> list(int groupid, String sort, String order, int page, int rows) {
		Map<String, Object> map = new HashMap<String, Object>();
		List userList;
		if (groupid == 999) { // 999代表全部联系人
			userList = userDao.list(sort, order, page, rows);
			map.put("total", userDao.listSize());
		} else {
			userList = userDao.listByGroupId(groupid, sort, order, page, rows);
			map.put("total", userDao.listSizeByGroup(groupid));
		}
		setFieldText(userList);
		map.put("rows", userList);
		map.put("isSuccess", true);
		return map;
	}

	/**
	 * 单查
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> get(int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Users user = userDao.get(id);
		map.put("isSuccess", true);
		map.put("user", user);
		return map;
	}

	/**
	 * 更新
	 * 
	 * @param user
	 * @return
	 */
	public Map<String, Object> update(Users user, int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			userDao.update(user, id);
			map.put("isSuccess", true);
		} catch (Exception e) {
			map.put("isSuccess", false);
			map.put("errorMsg", e.getMessage());
		}
		return map;
	}

	/**
	 * 新增
	 * 
	 * @param user
	 * @return
	 */
	public Map<String, Object> save(Users user) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			userDao.save(user);
			map.put("isSuccess", true);
		} catch (Exception e) {
			map.put("isSuccess", false);
		}
		return map;
	}

	/**
	 * 删除
	 */
	public Map<String, Object> remove(String ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String[] idStringArray = ids.split(",");
			for (String id : idStringArray) {
				userDao.remove(Integer.valueOf(id));
			}
			map.put("isSuccess", true);
		} catch (Exception e) {
			map.put("isSuccess", false);
		}
		return map;
	}

	/**
	 * 移动分组
	 */
	public Map<String, Object> updateGroup(String ids, Users user) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String[] idStringArray = ids.split(",");
			for (String id : idStringArray) {
				userDao.updateGroup(Integer.valueOf(id), user.getGroupid());
			}
			map.put("isSuccess", true);
		} catch (Exception e) {
			map.put("isSuccess", false);
		}
		return map;
	}

	// 私有方法--------------------------------------------------------------------------------
	/**
	 * 内存外显
	 */
	@SuppressWarnings("rawtypes")
	private void setFieldText(List rows) {
		Map groupMap = userGroupService.list2Map();
		Iterator it = rows.iterator();
		while (it.hasNext()) {
			Users user = (Users) it.next();
			user.setGroupText((String) groupMap.get(user.getGroupid()));
			user.setSexText(user.getSex() == 0 ? "男" : "女");
		}
	}

}
