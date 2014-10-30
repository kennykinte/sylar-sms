package com.sylar.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sylar.entity.Users;
import com.sylar.service.UserService;

@Controller
public class UserAction {
	@Autowired
	private UserService userService;

	/**
	 * 联系人列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/userlist")
	public Object usergroup(int groupid, String sort, String order, int page, int rows) {
		return userService.list(groupid, sort, order, page, rows);
	}
	
	/**
	 * 联系人增删改操作
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/useraction")
	public Object userOperate(String type, Users user, String id) {
		if ("ins".equals(type)) { // 新增
			return userService.save(user);
		}
		if ("upd".equals(type)) { // 更新
			if (id != null && !("").equals(id)) {
				int userId = Integer.valueOf(id);
				return userService.update(user, userId);
			}
		}
		if ("del".equals(type)) { // 删除(可能是多个id,以逗号[,]分隔
			if (id != null && !("").equals(id)) {
				return userService.remove(id);
			}
		}
		if ("move".equals(type)) { // 移动分组(可能是多个id,以逗号[,]分隔
			if (id != null && !("").equals(id)) {
				return userService.updateGroup(id, user);
			}
		}
		return user;
	}

}
