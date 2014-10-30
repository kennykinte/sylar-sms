package com.sylar.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sylar.dao.AnalysisDao;
import com.sylar.util.JFreeChartCreater;

@Service
public class AnalysisService {
	@Autowired
	private AnalysisDao analysisDao;

	/**
	 * 分析短信收发情况
	 * 
	 * @param smstype
	 *            短信类型: rec/snd/其他
	 * @param sort
	 *            排序字段: count(短信条数)/first(第一条发送时间)/latest(最后条发送时间)
	 * @param order
	 *            排序方式 : asc/desc
	 * @param page
	 *            页码: 为0则不分页
	 * @param pagesize
	 *            页容
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Object> getAnalysis(String smstype, String sort, String order, int page, int pagesize) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List resultList = analysisDao.analysisSms(smstype, sort, order, page, pagesize);
		int total = analysisDao.analysisSms(smstype, sort, order, 0, 0).size();
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Iterator it = resultList.iterator();
		for (int i = 0; i < resultList.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			Object[] row = (Object[]) it.next();
			map.put("username", (String) row[0]);
			map.put("count", ((java.math.BigInteger) row[1]).doubleValue());
			map.put("first", df.format((Timestamp) row[2]));
			map.put("latest", df.format((Timestamp) row[3]));
			rows.add(map);
		}
		returnMap.put("total", total);
		returnMap.put("rows", rows);
		return returnMap;
	}

	/**
	 * 获取图表
	 * 
	 * @param charttype
	 *            图表类型: bar/pie
	 * @param showcount
	 *            统计条数
	 * @param smstype
	 *            短信类型: rec/snd/其他
	 * @return 表示图表的字节数组
	 */
	public byte[] getChartByte(String charttype, int showcount, String smstype) throws IOException {
		JFreeChart chart = null;
		if ("bar".equals(charttype))
			chart = JFreeChartCreater.createBarChart(getFirstN(showcount, smstype));
		else if ("pie".equals(charttype))
			chart = JFreeChartCreater.createPieChart(getFirstNPercent(showcount, smstype));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ChartUtilities.writeChartAsJPEG(out, chart, 800, 400);
		return out.toByteArray();
	}

	// ==================================================================================================
	// 私有方法
	// ==================================================================================================

	/**
	 * 创建Bar图数据CategoryDatase<br>
	 * 仅仅取前N条
	 * 
	 * @param showcount
	 *            前N条
	 * @param smstype
	 *            rec/snd/其他,短信类型
	 * @param order
	 *            asc/desc,顺序/倒序
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private CategoryDataset getFirstN(int showcount, String smstype) {
		List resultList = analysisDao.analysisSms(smstype, "count", "desc", 0, 0);
		if (showcount > resultList.size())
			showcount = resultList.size();
		double[][] data = new double[1][showcount];
		String[] rowKeys = { "数量" };
		String[] columnKeys = new String[showcount];
		Iterator it = resultList.iterator();
		for (int i = 0; i < showcount; i++) {
			Object[] row = (Object[]) it.next();
			columnKeys[i] = (String) row[0];
			data[0][i] = ((java.math.BigInteger) row[1]).doubleValue();
		}
		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data);
		return dataset;
	}

	/**
	 * Pie图数据DefaultPieDataset<br>
	 * 显示前N条,剩余加在一起
	 * 
	 * @param showcount
	 *            前N条
	 * @param smstype
	 *            rec/snd/其他,短信类型
	 * @param order
	 *            asc/desc,顺序/倒序
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private DefaultPieDataset getFirstNPercent(int showcount, String smstype) {
		List resultList = analysisDao.analysisSms(smstype, "count", "desc", 0, 0);
		if (showcount > resultList.size())
			showcount = resultList.size();
		DefaultPieDataset dataset = new DefaultPieDataset();
		Iterator it = resultList.iterator();
		for (int i = 0; i < showcount; i++) {
			Object[] row = (Object[]) it.next();
			dataset.setValue((String) row[0], ((java.math.BigInteger) row[1]).doubleValue());
		}
		double restCount = 0;
		while (it.hasNext()) {
			Object[] row = (Object[]) it.next();
			restCount += ((java.math.BigInteger) row[1]).doubleValue();
		}
		if (restCount > 0)
			dataset.setValue("其他", restCount);
		return dataset;
	}

}
