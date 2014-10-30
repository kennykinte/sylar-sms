package com.sylar.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sylar.service.ExchangeService;

@Controller
public class ExchangeAction {

	@Autowired
	private ExchangeService exchangeService;

	@RequestMapping(value = "/exchange/import", method = RequestMethod.POST)
	public String importSms(@RequestParam("file") MultipartFile file, Model model) throws Exception {

		exchangeService.updateImport(file);

		model.addAttribute("data", "123");
		return "exchange";
	}
}
