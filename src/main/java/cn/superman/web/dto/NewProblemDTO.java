package cn.superman.web.dto;

public class NewProblemDTO {
    private String problemName;
    private String problemContent;
    private Integer problemTypeId;
    private long timeLimit;
    private long memoryLimit;
    private Integer problemCreatorId;
    private int problemDifficulty;
    private int problemValue;
    private Boolean isPublish;
    private String problemLabel;

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public String getProblemContent() {
        return problemContent;
    }

    public void setProblemContent(String problemContent) {
        this.problemContent = problemContent;
    }

    public Integer getProblemTypeId() {
        return problemTypeId;
    }

    public void setProblemTypeId(Integer problemTypeId) {
        this.problemTypeId = problemTypeId;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public long getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(long memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public Integer getProblemCreatorId() {
        return problemCreatorId;
    }

    public void setProblemCreatorId(Integer problemCreatorId) {
        this.problemCreatorId = problemCreatorId;
    }

    public int getProblemDifficulty() {
        return problemDifficulty;
    }

    public void setProblemDifficulty(int problemDifficulty) {
        this.problemDifficulty = problemDifficulty;
    }

    public int getProblemValue() {
        return problemValue;
    }

    public void setProblemValue(int problemValue) {
        this.problemValue = problemValue;
    }

    public Boolean getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(Boolean isPublish) {
        this.isPublish = isPublish;
    }

    public String getProblemLabel() {
        return problemLabel;
    }

    public void setProblemLabel(String problemLabel) {
        this.problemLabel = problemLabel;
    }

}
