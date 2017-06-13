package cn.superman.web.vo.request;

import javax.validation.constraints.NotNull;

public class ProblemSearchByDifficultyVO {
    @NotNull(message = "请选择每页展示的条目数")
    private Integer pageShowCount;
    @NotNull(message = "请选择想查看的页数")
    private Integer wantPageNumber;
    @NotNull(message = "请输入具体题目难度")
    private Integer problemDifficulty;

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

    public Integer getProblemDifficulty() {
        return problemDifficulty;
    }

    public void setProblemDifficulty(Integer problemDifficulty) {
        this.problemDifficulty = problemDifficulty;
    }

}
