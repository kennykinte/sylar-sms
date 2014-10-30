package com.sylar.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.sylar.entity.Sms;

public class JsonUtil {
	// private static Log log = LogFactory.getLog(JsonUtil.class);

	public static String object2json(Object obj) {
		StringBuilder json = new StringBuilder();
		if (obj == null) {
			json.append("\"\"");
		} else if (obj instanceof String || obj instanceof Integer || obj instanceof Float || obj instanceof Boolean || obj instanceof Short || obj instanceof Double || obj instanceof Long || obj instanceof BigDecimal || obj instanceof BigInteger || obj instanceof Byte) {
			json.append("\"").append(string2json(obj.toString())).append("\"");
		} else if (obj instanceof Object[]) {
			json.append(array2json((Object[]) obj));
		} else if (obj instanceof List) {
			json.append(list2json((List<?>) obj));
		} else if (obj instanceof Map) {
			json.append(map2json((Map<?, ?>) obj));
		} else if (obj instanceof Set) {
			json.append(set2json((Set<?>) obj));
		} else {
			json.append(bean2json(obj));
		}
		return json.toString();
	}

	public static String bean2json(Object bean) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		PropertyDescriptor[] props = null;
		try {
			props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
		} catch (IntrospectionException e) {
		}
		if (props != null) {
			for (int i = 0; i < props.length; i++) {
				try {
					String name = object2json(props[i].getName());
					String value = object2json(props[i].getReadMethod().invoke(bean));
					json.append(name);
					json.append(":");
					json.append(value);
					json.append(",");
				} catch (Exception e) {
				}
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	public static String list2json(List<?> list) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	public static String array2json(Object[] array) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (array != null && array.length > 0) {
			for (Object obj : array) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	public static String map2json(Map<?, ?> map) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		if (map != null && map.size() > 0) {
			for (Object key : map.keySet()) {
				json.append(object2json(key));
				json.append(":");
				json.append(object2json(map.get(key)));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	public static String set2json(Set<?> set) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (set != null && set.size() > 0) {
			for (Object obj : set) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	public static String string2json(String s) {
		if (s == null)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				if (ch >= '\u0000' && ch <= '\u001F') {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}
	
	/*----自定义方法-----------------------------------------------------------------------------------------*/

	@SuppressWarnings("rawtypes")
	public static String allSms2Json(List allSms) {

		StringBuilder json = new StringBuilder();
		json.append("{\"allSmsCount\":");

		if (allSms != null && allSms.size() > 0) {

			json.append(allSms.size() + ",");

			// 分出接收、发送
			ArrayList<Sms> recSms = new ArrayList<Sms>();
			ArrayList<Sms> sndSms = new ArrayList<Sms>();
			Sms s;

			Iterator itrSms = allSms.iterator();
			while (itrSms.hasNext()) {
				s = (Sms) itrSms.next();
				if (s.getSmstype() == 1) {
					recSms.add(s);
				} else if (s.getSmstype() == 0) {
					sndSms.add(s);
				}
			}

			// 组合接收短信
			json.append("\"recSms\":{\"recCount\":");
			json.append(recSms.size() + ",\"sms\":[");

			json.append(sms2Json(recSms));
			json.append("]},");

			// 组合发送短信
			json.append("\"sndSms\":{\"recCount\":");
			json.append(sndSms.size() + ",\"sms\":[");

			json.append(sms2Json(sndSms));
			json.append("]}}");

		} else {

		}
		return json.toString();
	}

	/**
	 * 分离出收发后的List，进行日期分割
	 * 
	 * @param smsList
	 * @return
	 */
	public static String sms2Json(List<Sms> smsList) {

		StringBuilder json = new StringBuilder();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");

		// 日期集合
		LinkedHashSet<String> dateSet = new LinkedHashSet<String>();

		if (smsList != null && smsList.size() > 0) {

			for (Sms cmsg : smsList) {
				dateSet.add(df.format(cmsg.getPosttime()));
			}

			Iterator<String> itdateset = dateSet.iterator();
			while (itdateset.hasNext()) {
				String tt = itdateset.next();
				json.append("{\"date\":\"" + tt + "\",\"sms\":[");

				for (Sms cmsg : smsList) {
					if (tt.equals(df.format(cmsg.getPosttime()))) {
						json.append("{\"time\":");
						json.append("\"" + tf.format(cmsg.getPosttime()) + "\",");
						json.append("\"sms\":\"" + cmsg.getSmscontent().replaceAll("\\s*|\t|\r|\n", "") + "\""); // 过滤空格、回车、换行、制表
						json.append("},");
					}
				}

				json.setCharAt(json.length() - 1, ']');
				json.append("},");
			}
			json.setCharAt(json.length() - 1, ' ');
		}
		return json.toString();
	}
	
	/**
	 * 短信列表重组为"日期-该日期下短信"映射
	 * @param smsList
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, Object> smsList2Map(List<Sms> smsList){
		Map<String, Object> returnMap = new TreeMap<String, Object>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
		TreeSet<String> dateSet = new TreeSet<String>();//日期（不包含时间）集合（不重复）
		if (smsList != null && smsList.size() > 0) {
			//迭代所有短信集合，把日期（不包含时间）加进日期集合
			for (Sms cmsg : smsList) {
				dateSet.add(df.format(cmsg.getPosttime()));
			}
			//迭代日期集合
			Iterator<String> itdateset = dateSet.iterator();
			while (itdateset.hasNext()) {
				List smsDate = new ArrayList();//单个日期内短信集合
				String tt = itdateset.next();
				//迭代所有短信集合，匹配当前日期的加进"单个日期内短信集合"
				for(Sms cmsg:smsList){
					if(tt.equals(df.format(cmsg.getPosttime()))){
						smsDate.add(cmsg);
					}
				}
				//将此"单个日期内短信集合"加入最终返回Map
				returnMap.put(tt, smsDate);
			}
		}
		return returnMap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {

		List asm = new ArrayList<Sms>();

		Sms s1 = new Sms();
		s1.setPosttime(Timestamp.valueOf("2012-01-01 12:53:00"));
		s1.setSmscontent("sms1");

		Sms s2 = new Sms();
		s2.setPosttime(Timestamp.valueOf("2012-02-01 12:53:00"));
		s2.setSmscontent("sms2");

		asm.add(s1);
		asm.add(s2);

		//System.out.println(allSms2Json(asm));
		
		System.out.println(smsList2Map(asm));
	}
}
