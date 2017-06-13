package cn.superman.util;

public class CommonUtil {
	private static final long MB = 1024 * 1024;

	public static String formatMemory(long memory) {
		StringBuilder builder = new StringBuilder();
		long temp = 0;

		if (memory > MB) {
			temp = memory / MB;
			builder.append(temp + "MB");
			memory -= temp * MB;
		} else {
			builder.append("小于1M");
		}

		return builder.toString();
	}
}
