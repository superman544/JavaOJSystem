package cn.superman.web.service.page;

import java.util.List;

import cn.superman.web.dao.base.BaseDao;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public abstract class PageService<T, E> {
	public abstract BaseDao<T, E> getUseDao();

	public E getDefaultCondition() {
		return null;
	}

	public PageResult<T> getPage(int pageShowCount, int wantPaegNumber) {
		wantPaegNumber = wantPaegNumber < 1 ? 1 : wantPaegNumber;
		PageQuery<E> pageQuery = new PageQuery<E>();
		pageQuery.setWantPageNumber(wantPaegNumber);
		pageQuery.setWantPageShowCount(pageShowCount);
		pageQuery.setConditionEntity(getDefaultCondition());
		return getPageResult(pageQuery);
	}

	public PageResult<T> getPage(int pageShowCount, int wantPaegNumber,
			E condition) {
		wantPaegNumber = wantPaegNumber < 1 ? 1 : wantPaegNumber;
		PageQuery<E> pageQuery = new PageQuery<E>();
		pageQuery.setWantPageNumber(wantPaegNumber);
		pageQuery.setWantPageShowCount(pageShowCount);
		pageQuery.setConditionEntity(condition);
		return getPageResult(pageQuery);
	}

	public PageResult<T> firstPage(int pageShowCount) {
		PageQuery<E> pageQuery = new PageQuery<E>();
		pageQuery.setWantPageNumber(1);
		pageQuery.setWantPageShowCount(pageShowCount);
		pageQuery.setConditionEntity(getDefaultCondition());
		return getPageResult(pageQuery);
	}

	public PageResult<T> firstPage(PageQuery<E> pageQuery) {
		pageQuery.setWantPageNumber(1);
		return getPageResult(pageQuery);
	}

	public PageResult<T> nextPage(int pageShowCount, int currentPage) {
		PageQuery<E> pageQuery = new PageQuery<E>();
		pageQuery.setWantPageNumber(currentPage + 1);
		pageQuery.setWantPageShowCount(pageShowCount);
		pageQuery.setConditionEntity(getDefaultCondition());
		return getPageResult(pageQuery);
	}

	public PageResult<T> nextPage(PageQuery<E> pageQuery) {
		return getPageResult(pageQuery);
	}

	public PageResult<T> prePage(int pageShowCount, int currentPage) {
		PageQuery<E> pageQuery = new PageQuery<E>();
		pageQuery.setWantPageNumber(currentPage - 1);
		pageQuery.setWantPageShowCount(pageShowCount);
		pageQuery.setConditionEntity(getDefaultCondition());
		return getPageResult(pageQuery);
	}

	public PageResult<T> prePage(PageQuery<E> pageQuery) {
		return getPageResult(pageQuery);
	}

	private PageResult<T> getPageResult(PageQuery<E> pageQuery) {
		PageResult<T> pageResult = new PageResult<T>();

		if (pageQuery.getOrderByAttributeName() == null) {
			PageHelper.startPage(pageQuery.getWantPageNumber(),
					pageQuery.getWantPageShowCount());
		} else {
			PageHelper.startPage(pageQuery.getWantPageNumber(),
					pageQuery.getWantPageShowCount(),
					pageQuery.getOrderByAttributeName() + " "
							+ pageQuery.getOrderByType().name());
		}

		List<T> list = null;
		if (pageQuery.getConditionEntity() == null) {
			list = getUseDao().find();
		} else {
			list = getUseDao()
					.findWithCondition(pageQuery.getConditionEntity());
		}

		PageInfo<T> info = new PageInfo<T>(list);
		pageResult.setResult(list);
		pageResult.setTotalCount(info.getTotal());
		pageResult.setCurrentPage(info.getPageNum());
		pageResult.setTotalPage(info.getPages());
		return pageResult;
	}

}
