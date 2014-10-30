package com.sylar.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sylar.service.SmsService;

@Controller
public class SmsAction {
	@Autowired
	private SmsService smsService;

	/**
	 * 联系人树
	 */
	@ResponseBody
	@RequestMapping(value = "/sms/usergroup")
	public Object listbygroup(String type, int groupid) {
		return smsService.userTree(type, groupid);
	}

	/**
	 * 查询短信
	 */
	@ResponseBody
	@RequestMapping(value = "/sms/qry")
	public Object querySms(String userTel, short smstype, int page, int pagesize) {
		return smsService.listSms(userTel, smstype, page, pagesize);
	}
}
