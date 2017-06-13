package cn.superman.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	private DateUtil() {
		throw new AssertionError("不要实例化工具类哦");
	}

	public static String getYYYYMMddToday() {
		StringBuilder builder = new StringBuilder();

		Calendar calendar = Calendar.getInstance();
		builder.append(calendar.get(Calendar.YEAR)).append("-")
				.append(calendar.get(Calendar.MONTH) + 1).append("-")
				.append(calendar.get(Calendar.DAY_OF_MONTH));
		return builder.toString();
	}

	public static String formatToYYYYMMddHHmm(Date time) {
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm");
		return format.format(time);
	}
}
