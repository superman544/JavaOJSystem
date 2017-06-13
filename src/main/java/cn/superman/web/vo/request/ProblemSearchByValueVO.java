package cn.superman.web.vo.request;

import javax.validation.constraints.NotNull;

public class ProblemSearchByValueVO {
    @NotNull(message = "请选择每页展示的条目数")
    private Integer pageShowCount;
    @NotNull(message = "请选择想查看的页数")
    private Integer wantPageNumber;
    @NotNull(message = "请输入具体题目价值")
    private Integer problemValue;

    public Integer getPageShowCount() {
        return pageShowCount;
    }

    public void setPageShowCount(Integer pageShowCount) {
        this.pageShowCount = pageShowCount;
    }

    public Integer getWantPageNumber() {
        return wantPageNumber;
    }

    public void setWantPageNumber(Integer wantPageNumber) {
        this.wantPageNumber = wantPageNumber;
    }

    public Integer getProblemValue() {
        return problemValue;
    }

    public void setProblemValue(Integer problemValue) {
        this.problemValue = problemValue;
    }

}
