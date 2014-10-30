package com.sylar.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SylarUtils {

	/**
	 * 判断字符串是否为空
	 * 
	 */
	public static boolean isNullOrEmpty(Object o) {
		return o == null || String.valueOf(o).trim().length() == 0; 
	}

	/**
	 * 日期格式化
	 */
	public static String formatDateStr(Date date, String f) {
		return new SimpleDateFormat(f).format(date);
	}
}
