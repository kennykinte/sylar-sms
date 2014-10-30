package com.sylar.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sylar.dao.SmsDao;
import com.sylar.entity.Sms;
import com.sylar.util.SylarUtils;

import au.com.bytecode.opencsv.CSVReader;

@Service
public class ExchangeService {

	@Autowired
	private SmsDao smsDao;

	/**
	 * 短信导入
	 * 
	 * @param file
	 * @throws Exception
	 */
	public void updateImport(MultipartFile file) throws Exception {
		
		// 读取上传的文件
		InputStream in = file.getInputStream();
		InputStreamReader reader = new InputStreamReader(in, "unicode");
		CSVReader csvreader = new CSVReader(reader);
		
		// 循环插入DB
		Sms sms; // 短信对象
		String[] nextLine; // 读取的行
		csvreader.readNext(); // 跳过第一行(标题行)
		while ((nextLine = csvreader.readNext()) != null) {
			
			// 读取行转化为Sms对象
			sms = readFromWdj(nextLine);
			
			// 调用DAO
			smsDao.save(sms);
		}
		csvreader.close();
	}

	// ------
	// 私有方法
	// ------
	
	/**
	 * 读取行转化为Sms对象
	 * @param readLine
	 * @return
	 */
	private Sms readLine2Sms(String[] readLine){
		
		// 各临时变量
		String telTemp, timeTemp, contTemp, typeTemp;

		// CSV导出格式或有不同
		if (readLine.length == 1) {
			String[] tempArray = readLine[0].split(",");
			telTemp = tempArray[0];
			timeTemp = tempArray[1];
			contTemp = "";
			for (int i = 2, j = tempArray.length - 2; i < j; i++) {
				contTemp += tempArray[i];
			}
			typeTemp = tempArray[tempArray.length - 2];
		} else {
			telTemp = readLine[0];
			timeTemp = readLine[1];
			contTemp = readLine[2];
			typeTemp = readLine[3];
		}

		// 处理手机号码
		// 13705171472 (陈亮)
		// +8613851738675 (顾贝贝)
		// 187-5186-0502 (谷玲)		
		if (telTemp.startsWith("+86")) {
			telTemp = telTemp.substring(3); // 去除+86
		}		
		telTemp = telTemp.replaceAll("-", ""); // 去除下划线
		telTemp = telTemp.substring(0, 11); // 去除(姓名)

		// 设置Sms对象属性
		Sms sms = new Sms();
		sms.setTelephone(telTemp);
		sms.setPosttime(Timestamp.valueOf(timeTemp));
		sms.setSmscontent(contTemp);
		sms.setFixed((short) 1);
		if ("recv".equals(typeTemp)) {
			sms.setSmstype((short) 1);
		} else {
			sms.setSmstype((short) 0);
		}
		
		// 返回Sms对象
		return sms;
	}
	
	/**
	 * 读取行转化为Sms对象
	 * @param readLine
	 * @return
	 */
	private Sms readFromWdj(String[] readLine){
		
		// 各临时变量
		String telTemp, timeTemp, contTemp, typeTemp;

		// sms,deliver,	+8615195956218,周炜行,,2014. 5.18 16: 8,9,喔喔，没事就问问，你不回来那我晚上逛街去了
		// sms,submit,周炜行,	+8615195956218,,2014. 5.18 16: 7,9,明天回去的喔，咋啦
		typeTemp = readLine[1];
		if ("deliver".equals(typeTemp)) {
			telTemp = readLine[2];
		} else {
			telTemp = readLine[3];
		}
		// 处理时间格式
		timeTemp = readLine[5].replaceAll("\\.", "-").replaceAll(" ", "0");
		timeTemp = timeTemp.subSequence(0, 10) + " " + timeTemp.substring(11) + ":00";
		contTemp = readLine[7];

		// 处理手机号码
		telTemp = telTemp.replaceAll("\\t", "");
		if (telTemp.startsWith("+86")) {
			telTemp = telTemp.substring(3); // 去除+86
		}		
		telTemp = telTemp.replaceAll("-", ""); // 去除下划线
		//telTemp = telTemp.substring(0, 11); // 去除(姓名)

		// 设置Sms对象属性
		Sms sms = new Sms();
		sms.setTelephone(telTemp);
		sms.setPosttime(Timestamp.valueOf(timeTemp));
		sms.setSmscontent(contTemp);
		sms.setFixed((short) 1);
		if ("deliver".equals(typeTemp)) {
			sms.setSmstype((short) 1);
		} else {
			sms.setSmstype((short) 0);
		}
		
		// 返回Sms对象
		return sms;
	}
	
	
	/**
	 * 验证读取的行是否符合短信类
	 * 
	 * @param testStringArray
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean validateData(String[] testStringArray) {

		if (null != testStringArray && 5 == testStringArray.length) {

			// 手机号码
			if (!SylarUtils.isNullOrEmpty(testStringArray[0])) {

			}

			// 收发时间
			if (!SylarUtils.isNullOrEmpty(testStringArray[1])) {
			}

			// 短信内容
			if (!SylarUtils.isNullOrEmpty(testStringArray[2])) {
			}

			// 短信类型
			if (!SylarUtils.isNullOrEmpty(testStringArray[3])) {
			}

			// 短信状态
			if (!SylarUtils.isNullOrEmpty(testStringArray[4])) {
			}

		}

		return true;
	}
	
	public static void main(String[] args){
		ExchangeService s = new ExchangeService();
		try {
			File file = new File("E:\\1.csv");
			// 读取上传的文件
			InputStream in = new FileInputStream(file);
			InputStreamReader reader = new InputStreamReader(in, "utf-8");
			CSVReader csvreader = new CSVReader(reader);

			// 循环插入DB
			Sms sms; // 短信对象
			String[] nextLine; // 读取的行
			csvreader.readNext(); // 跳过第一行(标题行)
			while ((nextLine = csvreader.readNext()) != null) {
				sms = s.readFromWdj(nextLine);
				System.out.println(sms);
			}
			csvreader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
