package cn.superman.web.util.mail;

/**
 * 邮件附件类
 * 
 * @author 梁超人
 */
public class AttachBean {
	private String id;
	private String path;
	private String fileName;

	public AttachBean() {

	}

	public AttachBean(String id, String path, String fileName) {
		super();
		this.id = id;
		this.path = path;
		this.fileName = fileName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
