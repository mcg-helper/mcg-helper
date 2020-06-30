package com.mcg.entity.common;

/**
 * 
 * 循环控件的执行状态
 *
 */
public class LoopStatus {

	/* 循环的剩余次数 */
	private Integer count;
	/* 循环开关    true：开  false：关*/
	private Boolean swicth;
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Boolean getSwicth() {
		return swicth;
	}
	public void setSwicth(Boolean swicth) {
		this.swicth = swicth;
	}
	
	
}
