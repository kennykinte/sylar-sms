package com.sylar.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sylar.service.UserService;
import com.sylar.util.JsonUtil;

@Controller
public class JspForward {

	@Autowired
	private UserService userService;

	/**
	 * 首页
	 */
	@RequestMapping(value = "/index")
	public String index() {
		return "index";
	}

	/**
	 * 联系人管理
	 */
	@RequestMapping(value = "/usermanager")
	public String userManager() {
		return "usermanager";
	}

	/**
	 * 短信管理
	 */
	@RequestMapping(value = "/smsmanager")
	public String smsManager() {
		return "smsmanager";
	}

	/**
	 * 统计图
	 */
	@RequestMapping(value = "/analysis")
	public String chartlet() {
		return "analysis";
	}

	/**
	 * 导入/导出
	 */
	@RequestMapping(value = "/exchange")
	public String exchange() {
		return "exchange";
	}

	/**
	 * OPEN联系人
	 */
	@RequestMapping(value = "/user")
	public String userManager(Model model, String type, int id, String groupid) {
		if (type != null) {
			if ("V".equals(type) || "M".equals(type)) {
				// type为V-查看,M-修改
				String tReturn = JsonUtil.map2json(userService.get(id));
				model.addAttribute("type", type);
				model.addAttribute("model", tReturn);
			} else {
				// type为其他(包括A-新增)
				model.addAttribute("type", "A");
				// model变量在新增状态下存放一个数字,即分组id, 前端未选择分组的情况下,传递0(未分组好友)进来
				model.addAttribute("model", groupid);
			}
		}
		return "user";
	}
}
