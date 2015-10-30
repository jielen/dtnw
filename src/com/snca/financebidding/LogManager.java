package com.snca.financebidding;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * TODO 自定义日志处理工具类 date:Mar 1, 2010 time:4:05:02 PM author:Administrator
 * email:jxauwxj@126.com
 */
public class LogManager {

	/**
	 * debug级别的信息
	 * 
	 * @param msg
	 */
	public static void debug(String msg) {
		writeToFile(" debug :" + msg);
	}

	/**
	 * info级别的信息
	 * 
	 * @param msg
	 */
	public static void info(String msg) {
		writeToFile(" info :" + msg);
	}

	/**
	 * error级别的信息
	 * 
	 * @param msg
	 */
	public static void error(String msg) {
		writeToFile(" error :" + msg);
	}

	/**
	 * warn级别的信息
	 * 
	 * @param msg
	 */
	public static void warn(String msg) {
		writeToFile(" warn :" + msg);
	}

	/**
	 * 将信息写入相应文件中
	 * 
	 * @param msg
	 */
	private static synchronized void writeToFile(String msg) {
		// 随机读写
		RandomAccessFile raf = null;
		// 文件所在路径
		String dirPath = System.getProperties().getProperty("user.dir")
				+ File.separator + "FJNICallLog";
		// System.out.println("dirPath: " + dirPath);
		try {
			File logFile = new File(dirPath);
			// 文件所在路径不存在则先创建
			if (!logFile.exists()) {
				logFile.mkdir();
			}
			// 以年月日为时间文件名,即每天生成一个文件
			dirPath += File.separator + DateUtil.dateStr() + ".log";
			logFile = new File(dirPath);
			if (logFile.isDirectory()) {
				logFile.delete();
			}
			// rw表示可读可写
			raf = new RandomAccessFile(logFile, "rw");
			// 先跳过原来内容,即到文件末尾
			raf.seek(logFile.length());
			// \r\n表示换行
			msg = "[" + DateUtil.datetimeStr() + "] " + msg + "\r\n";
			// 写入日志信息
			raf.write(msg.getBytes());
		} catch (Exception e) {
			System.out.println("服务器端记录日志失败");
			e.printStackTrace();
		} finally {
			try {
				raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			LogManager.info("test..." + i);
		}
	}
}
