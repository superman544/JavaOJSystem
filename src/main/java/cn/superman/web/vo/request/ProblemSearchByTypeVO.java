package cn.superman.web.vo.request;

import javax.validation.constraints.NotNull;

public class ProblemSearchByTypeVO {
    @NotNull(message = "请选择每页展示的条目数")
    private Integer pageShowCount;
    @NotNull(message = "请选择想查看的页数")
    private Integer wantPageNumber;
    @NotNull(message = "请选择具体的题目类型")
    private Integer problemTypeId;

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

    public Integer getProblemTypeId() {
        return problemTypeId;
    }

    public void setProblemTypeId(Integer problemTypeId) {
        this.problemTypeId = problemTypeId;
    }

}
