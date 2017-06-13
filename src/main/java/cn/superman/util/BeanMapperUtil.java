package cn.superman.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dozer.DozerBeanMapper;

public class BeanMapperUtil {
	private static DozerBeanMapper dozer = new DozerBeanMapper();

	private BeanMapperUtil() {
		throw new AssertionError("不要实例化工具类哦");
	}

	/**
	 * 构造新的destinationClass实例对象，通过source对象中的字段内容
	 * 映射到destinationClass实例对象中，并返回新的destinationClass实例对象。
	 * 
	 * @param source
	 *            源数据对象
	 * @param destinationClass
	 *            要构造新的实例对象Class
	 */
	public static <T> T map(Object source, Class<T> destinationClass) {
		return dozer.map(source, destinationClass);
	}

	public static <T, E> List<T> mapList(List<E> sourceList,
			Class<T> destinationClass) {
		Iterator<E> iterator = sourceList.iterator();
		List<T> list = new ArrayList<T>();

		while (iterator.hasNext()) {
			list.add(map(iterator.next(), destinationClass));
		}

		return list;
	}

	/**
	 * 将对象source的所有属性值拷贝到对象destination中.
	 * 
	 * @param source
	 *            对象source
	 * @param destination
	 *            对象destination
	 */
	public static void copy(Object source, Object destinationObject) {
		dozer.map(source, destinationObject);
	}
}
