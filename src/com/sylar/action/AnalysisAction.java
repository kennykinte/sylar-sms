package com.sylar.action;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sylar.service.AnalysisService;

@Controller
public class AnalysisAction {

	@Autowired
	private AnalysisService analysisService;

	/**
	 * 获取分析
	 * 
	 * @param smstype
	 *            短信类型: rec/snd/其他
	 * @param sort
	 *            排序字段: count(短信条数)/first(第一条发送时间)/latest(最后条发送时间)
	 * @param order
	 *            排序方式 : asc/desc
	 * @param page
	 *            页码: 为0则不分页
	 * @param rows
	 *            页容
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/analysis/getanalysis")
	public Object getAnalysis(String smstype, String sort, String order, int page, int rows) {
		return analysisService.getAnalysis(smstype, sort, order, page, rows);
	}

	/**
	 * 获取图表
	 * 
	 * @param charttype
	 *            图表类型: bar/pie
	 * @param showcount
	 *            统计的短信条数
	 * @param smstype
	 *            短信类型: rec/snd/其他
	 * @param order
	 *            排序方式: asc/desc
	 * @return 表示图表的字节数组
	 */
	@ResponseBody
	@RequestMapping(value = "/analysis/getchart")
	public byte[] getChartByte(String charttype, int showcount, String smstype) throws IOException {
		return analysisService.getChartByte(charttype, showcount, smstype);
	}
}
