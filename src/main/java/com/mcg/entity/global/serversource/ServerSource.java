package com.mcg.entity.global.serversource;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class ServerSource implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement
	private String id;
	/* 服务器名称 */
	@XmlElement
	private String name;	
	/* 服务器类型(linux、unix) */
    @XmlElement
    private String type;
    /* 服务器IP地址*/
    @XmlElement
    private String ip;
    /* 服务器端口*/
    @XmlElement
    private Integer port;
    /* 服务器用户名 */
    @XmlElement
    private String userName;
    /* 服务器密码 */
    @XmlElement
    private String pwd;
    /* 说明 */
    @XmlElement
    private String note;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
    
    
}
