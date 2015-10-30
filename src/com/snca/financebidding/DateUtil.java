package com.snca.financebidding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static String datetimeStr() {
		Date date = new Date();
		// format对象是用来以指定的时间格式格式化时间的
		SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 这里的格式可以自己设置
		// format()方法是用来格式化时间的方法
		String times = from.format(date);
		return times;
	}

	public static String dateStr() {
		Date date = new Date();
		// format对象是用来以指定的时间格式格式化时间的
		SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd"); // 这里的格式可以自己设置
		// format()方法是用来格式化时间的方法
		String dates = from.format(date);
		return dates;
	}

	public static void main(String[] args) {
		System.out.println(dateStr());
		System.out.println(datetimeStr());
	}
}
