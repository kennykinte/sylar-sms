package com.sylar.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sylar.dao.UserGroupDao;
import com.sylar.entity.Usergroup;

@Service
public class UserGroupService {
	@Autowired
	private UserGroupDao userGroupDao;

	/**
	 * 获取用户分组列表, 转成Tree格式, 包一层root, 用在[联系人管理]左侧树上
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List list() {
		// DB内存储的分组记录
		List allGroupList = userGroupDao.list();
		List groupInDb = new ArrayList();
		Iterator it = allGroupList.iterator();
		while (it.hasNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			Usergroup usergroup = (Usergroup) it.next();
			map.put("id", usergroup.getId());
			map.put("text", usergroup.getGroupname());
			groupInDb.add(map);
		}
		// 构建root
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("id", 999);
		root.put("text", "所有联系人");
		root.put("iconCls", "icon-roles");
		root.put("children", groupInDb);
		List rootList = new ArrayList();
		rootList.add(root);
		return rootList;
	}

	/**
	 * 转成Map
	 */
	@SuppressWarnings("rawtypes")
	public Map list2Map() {
		Map<Integer, String> groupMap = new HashMap<Integer, String>();
		List allGroupList = userGroupDao.list();
		Iterator it = allGroupList.iterator();
		while (it.hasNext()) {
			Usergroup usergroup = (Usergroup) it.next();
			groupMap.put(usergroup.getId(), usergroup.getGroupname());
		}
		return groupMap;
	}

	/**
	 * 新增或修改
	 */
	public Map<String, Object> update(int id, String groupname) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (id == 998) { // 998为临时ID,表示节点为新增
				Usergroup ug = new Usergroup();
				ug.setGroupname(groupname);
				userGroupDao.save(ug);
			} else {
				userGroupDao.update(id, groupname);
			}
			map.put("isSuccess", true);
		} catch (Exception e) {
			map.put("isSuccess", false);
			map.put("errorMsg", e.getMessage());
		}
		return map;
	}

	/**
	 * 删除
	 */
	public void remove(int id) {
		userGroupDao.remove(id);
	}
}
