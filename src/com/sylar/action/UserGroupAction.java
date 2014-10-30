package com.sylar.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sylar.service.UserGroupService;

@Controller
public class UserGroupAction {
	@Autowired
	private UserGroupService userGroupService;

	/**
	 * 查询分组
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/usergroup/qry")
	public Object qryGroup() {
		return userGroupService.list();
	}

	/**
	 * 新增or更新分组
	 */
	@ResponseBody
	@RequestMapping(value = "/usergroup/upd")
	public Object updGroup(int id, String groupname) {
		return userGroupService.update(id, groupname);
	}

	/**
	 * 删除分组
	 */
	@ResponseBody
	@RequestMapping(value = "/usergroup/del")
	public Object delGroup(int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			userGroupService.remove(id);
			map.put("isSuccess", true);
		} catch (Exception e) {
			map.put("isSuccess", false);
			map.put("errorMsg", e.getMessage());
		}
		return map;
	}
}
