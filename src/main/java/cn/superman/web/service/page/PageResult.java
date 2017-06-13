package cn.superman.web.service.page;

import java.util.List;

public class PageResult<T> {
    /**
     * 当前第几页
     */
    private int currentPage;
    /**
     * 总页数
     */
    private int totalPage;

    /**
     * 总记录条数
     */
    private long totalCount;

    /**
     * 分页查询的结果集
     */
    private List<T> result;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

}
