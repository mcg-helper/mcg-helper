package com.mcg.entity.global.wonton;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import com.mcg.entity.wonton.WontonHeart;

public class WontonData implements Serializable {

	private static final long serialVersionUID = 1L;

	private ConcurrentHashMap<String, WontonHeart> wontonHeartMap;

	public ConcurrentHashMap<String, WontonHeart> getWontonHeartMap() {
		return wontonHeartMap;
	}

	public void setWontonHeartMap(ConcurrentHashMap<String, WontonHeart> wontonHeartMap) {
		this.wontonHeartMap = wontonHeartMap;
	}
	
}
