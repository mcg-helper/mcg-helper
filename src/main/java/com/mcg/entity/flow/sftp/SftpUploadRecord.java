package com.mcg.entity.flow.sftp;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class SftpUploadRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	/* 源文件上传全路径 */
	@XmlElement
	private String filePath;
	
	/* 上传ftp路径 */
	@XmlElement
	private String uploadPath;
	
	/* 在SftpOperationTypeEnum中定义  */
	@XmlElement
	private String type;
	
	@XmlElement
	private String note;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}
	
}