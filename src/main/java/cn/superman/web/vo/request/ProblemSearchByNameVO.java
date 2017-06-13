package cn.superman.web.vo.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class ProblemSearchByNameVO {
	@NotNull(message = "请选择每页展示的条目数")
	private Integer pageShowCount;
	@NotNull(message = "请选择想查看的页数")
	private int wantPageNumber;
	@NotBlank(message = "请输入精确或者大致的题目名字")
	private String problemName;

	public Integer getPageShowCount() {
		return pageShowCount;
	}

	public void setPageShowCount(Integer pageShowCount) {
		this.pageShowCount = pageShowCount;
	}

	public int getWantPageNumber() {
		return wantPageNumber;
	}

	public void setWantPageNumber(int wantPageNumber) {
		this.wantPageNumber = wantPageNumber;
	}

	public String getProblemName() {
		return problemName;
	}

	public void setProblemName(String problemName) {
		this.problemName = problemName;
	}

}
