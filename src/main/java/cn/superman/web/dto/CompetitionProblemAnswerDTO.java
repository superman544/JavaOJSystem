package cn.superman.web.dto;

import cn.superman.web.po.CompetitionAccount;

public class CompetitionProblemAnswerDTO {
    private String code;
    // 用于记录提交的代码语言，不过现在默认都是java
    private String codeType;
    private Integer problemId;
    private CompetitionAccount competitionAccount;

    public CompetitionAccount getCompetitionAccount() {
        return competitionAccount;
    }

    public void setCompetitionAccount(CompetitionAccount competitionAccount) {
        this.competitionAccount = competitionAccount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public Integer getProblemId() {
        return problemId;
    }

    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }

}
