package cn.superman.util;

import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

public class JsonUtil {
	private JsonUtil() {
		throw new AssertionError("不要实例化工具类哦");
	}

	private final static Gson gson = new Gson();

	public static String toJson(Object entity) {
		return gson.toJson(entity);
	}

	public static <T> T toBean(String json, Class<T> beanClass) {
		return gson.fromJson(json, beanClass);
	}

	public static <T> List<T> toListBean(String json, Class<T[]> beanClass) {
		T[] t = gson.fromJson(json, beanClass);
		return Arrays.asList(t);
	}
}
