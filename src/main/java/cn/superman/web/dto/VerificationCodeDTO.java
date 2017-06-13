package cn.superman.web.dto;

public class VerificationCodeDTO {
	private String textContent;
	private String imageData;

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public String getImageData() {
		return imageData;
	}

	public void setImageData(String imageData) {
		this.imageData = imageData;
	}

}
