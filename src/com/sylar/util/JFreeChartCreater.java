package com.sylar.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.TextAnchor;
import org.jfree.util.Rotation;

public class JFreeChartCreater {

	/**
	 * 创建BarChart(柱状图/条形图)图表
	 * 
	 */
	public static JFreeChart createBarChart(CategoryDataset dataset) {
		// 生成JFreeChart对象
		JFreeChart chart = ChartFactory.createBarChart3D("短信收发排行统计图", // 标题
				"", // 目录轴的显示标签
				"", // 数值轴的显示标签
				dataset, // 数据集
				PlotOrientation.VERTICAL, // 图表方式：V垂直;H水平
				false, // 是否显示图例
				false, // 是否显示工具提示
				false);// 是否生成URL

		// **************************************************************************************************
		// 标题字体
		chart.getTitle().setFont(new Font("黑体", Font.BOLD, 15));
		// -------------------------------------------------
		CategoryPlot plot = chart.getCategoryPlot();
		// 设置网格背景颜色
		plot.setBackgroundPaint(Color.white);
		// 设置网格竖线颜色
		plot.setDomainGridlinePaint(Color.pink);
		// 设置网格横线颜色
		plot.setRangeGridlinePaint(Color.pink);
		// 设置前景色透明度
		plot.setForegroundAlpha(0.65F);
		// 设置横轴标题
		plot.getDomainAxis().setTickLabelFont(new Font("宋体", Font.TRUETYPE_FONT, 16));
		// ------------------------------------------------
		// 显示每个柱的数值，并修改该数值的字体属性
		BarRenderer3D renderer = new BarRenderer3D();
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBaseItemLabelsVisible(true);
		// 默认的数字显示在柱子中，通过如下两句可调整数字的显示
		// 注意：此句很关键，若无此句，那数字的显示会被覆盖，给人数字没有显示出来的问题
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
		renderer.setItemLabelAnchorOffset(10D);
		// 设置每个柱的颜色
		renderer.setSeriesPaint(0, Color.blue); 
		plot.setRenderer(renderer);
		// **************************************************************************************************

		// 返回
		return chart;
	}

	/**
	 * 创建PieChart(饼图)图表
	 * 
	 */
	public static JFreeChart createPieChart(DefaultPieDataset dataset) {
		// 生成JFreeChart对象
		JFreeChart chart = ChartFactory.createPieChart3D("短信收发量结构图", dataset, false, false, false);

		// **************************************************************************************************
		// 设置标题字体
		chart.getTitle().setFont(new Font("黑体", Font.BOLD, 16));
		// 获得3D的水晶饼图对象
		PiePlot3D pieplot3d = (PiePlot3D) chart.getPlot();
		// 设置开始角度
		pieplot3d.setStartAngle(30D);
		// 设置方向为”顺时针方向“
		pieplot3d.setDirection(Rotation.ANTICLOCKWISE);
		// 设置透明度，0.5F为半透明，1为不透明，0为全透明
		pieplot3d.setForegroundAlpha(0.5F);
		// 没有数据的时候显示的内容
		pieplot3d.setNoDataMessage("无数据显示");
		// 设置标签字体
		pieplot3d.setLabelFont(new Font("宋体", Font.PLAIN, 12));
		// 设置显示图标的格式和内容{0}{1}{2}
		pieplot3d.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}\n{1}({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
		// 图形边框颜色
		pieplot3d.setBaseSectionOutlinePaint(Color.GRAY);
		// 图形边框粗细
		pieplot3d.setBaseSectionOutlineStroke(new BasicStroke(0.0f));
		// 设置突出显示的数据块
		// plot.setExplodePercent("One", 0.1D);
		// 设置背景色
		pieplot3d.setBackgroundPaint(Color.white);
		// 设置背景色透明度
		pieplot3d.setBackgroundAlpha(0.65F);
		// 指定显示的饼图为：圆形(true) 还是椭圆形(false)
		pieplot3d.setCircular(true);
		// **************************************************************************************************

		// 返回
		return chart;
	}
}
