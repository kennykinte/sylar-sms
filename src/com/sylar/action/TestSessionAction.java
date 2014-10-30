package com.sylar.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("user")
public class TestSessionAction {

	@ModelAttribute("user")
	public User getUser() {
		User user = new User();
		user.setName("sylar");
		return user;
	}

	@RequestMapping(value = "testsession")
	public String handle1(@ModelAttribute("user") User user) {
		int count = user.getCount();
		count++;
		user.setCount(count);
		return "test";
	}
}
