package cn.superman.web.dto;

import java.io.InputStream;

public class UploadProblemStandardFileDTO {
	private InputStream standardInputFileStream;
	private InputStream standardOutputFileStream;
	private Integer problemId;

	public InputStream getStandardInputFileStream() {
		return standardInputFileStream;
	}

	public void setStandardInputFileStream(InputStream standardInputFileStream) {
		this.standardInputFileStream = standardInputFileStream;
	}

	public InputStream getStandardOutputFileStream() {
		return standardOutputFileStream;
	}

	public void setStandardOutputFileStream(InputStream standardOutputFileStream) {
		this.standardOutputFileStream = standardOutputFileStream;
	}

	public Integer getProblemId() {
		return problemId;
	}

	public void setProblemId(Integer problemId) {
		this.problemId = problemId;
	}

}
