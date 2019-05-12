package com.mcg.entity.message;

import java.util.List;

import com.mcg.entity.wonton.WontonHeart;

public class WontonHeartBody extends Body {

	private static final long serialVersionUID = 1L;
	
	private List<WontonHeart> dataList;

	public List<WontonHeart> getDataList() {
		return dataList;
	}

	public void setDataList(List<WontonHeart> dataList) {
		this.dataList = dataList;
	}
	


}
