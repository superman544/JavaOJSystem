package cn.superman.web.service.page;

public class PageQuery<T> {
	/**
	 * 分页中想要的分页页码
	 */
	private int wantPageNumber = 0;
	/**
	 * 每页展示多少条数据
	 */
	private int wantPageShowCount = 5;

	/**
	 * 用于条件查询的条件判断实体类
	 */
	private T conditionEntity;

	/**
	 * 排序的
	 */
	private String orderByAttributeName;

	/**
	 * 排序类型，是升序还是降序
	 */
	private OrderByType orderByType;

	public PageQuery() {

	}

	public PageQuery(int wantPageNumber, int wantPageShowCount,
			T conditionEntity) {
		this.wantPageNumber = wantPageNumber;
		this.wantPageShowCount = wantPageShowCount;
		this.conditionEntity = conditionEntity;
	}

	public static enum OrderByType {
		/**
		 * 降序
		 */
		ASC,
		/**
		 * 升序
		 */
		DESC
	}

	public int getWantPageNumber() {
		return wantPageNumber;
	}

	public void setWantPageNumber(int wantPageNumber) {
		this.wantPageNumber = wantPageNumber;
	}

	public int getWantPageShowCount() {
		return wantPageShowCount;
	}

	public void setWantPageShowCount(int wantPageShowCount) {
		this.wantPageShowCount = wantPageShowCount;
	}

	public T getConditionEntity() {
		return conditionEntity;
	}

	public void setConditionEntity(T conditionEntity) {
		this.conditionEntity = conditionEntity;
	}

	public String getOrderByAttributeName() {
		return orderByAttributeName;
	}

	public void setOrderByAttributeName(String orderByAttributeName) {
		this.orderByAttributeName = orderByAttributeName;
	}

	public OrderByType getOrderByType() {
		return orderByType;
	}

	public void setOrderByType(OrderByType orderByType) {
		this.orderByType = orderByType;
	}

}
