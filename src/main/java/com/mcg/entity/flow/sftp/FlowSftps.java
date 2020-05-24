package com.mcg.entity.flow.sftp;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class FlowSftps implements Serializable {

	private static final long serialVersionUID = 1L;
	@XmlElement
	private List<FlowSftp> flowSftp;
	
	public List<FlowSftp> getFlowSftp() {
		return flowSftp;
	}
	public void setFlowSftp(List<FlowSftp> flowSftp) {
		this.flowSftp = flowSftp;
	}
	
}
