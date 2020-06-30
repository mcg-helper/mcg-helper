/*
 * @Copyright (c) 2018 缪聪(mcg-helper@qq.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");  
 * you may not use this file except in compliance with the License.  
 * You may obtain a copy of the License at  
 *     
 *     http://www.apache.org/licenses/LICENSE-2.0  
 *     
 * Unless required by applicable law or agreed to in writing, software  
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
 * See the License for the specific language governing permissions and  
 * limitations under the License.
 */

package com.mcg.entity.flow.wonton;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class WontonNetRule implements Serializable {

	private static final long serialVersionUID = 1L;
	private String Device;
	private Integer Latency;
	private Integer LatencyJitter;
	private Integer LatencyCorrelation;
	private String LatencyDistribution;
	private Integer LatencyReorder;
	private Integer LatencyDuplicate;
	private Integer LatencyCorrupt;
	private Integer TargetBandWidth;
	private Integer PacketLoss;
	private List<String> TargetPorts;
	private List<String> TargetProtos;
	private List<String> TargetIps;
	private boolean switchState;
	
	@JSONField(name = "Device")
	public String getDevice() {
		return Device;
	}
	public void setDevice(String device) {
		Device = device;
	}
	@JSONField(name = "Latency")
	public Integer getLatency() {
		return Latency;
	}
	public void setLatency(Integer latency) {
		Latency = latency;
	}
	@JSONField(name = "LatencyJitter")
	public Integer getLatencyJitter() {
		return LatencyJitter;
	}
	public void setLatencyJitter(Integer latencyJitter) {
		LatencyJitter = latencyJitter;
	}
	@JSONField(name = "LatencyCorrelation")
	public Integer getLatencyCorrelation() {
		return LatencyCorrelation;
	}
	public void setLatencyCorrelation(Integer latencyCorrelation) {
		LatencyCorrelation = latencyCorrelation;
	}
	@JSONField(name = "LatencyDistribution")
	public String getLatencyDistribution() {
		return LatencyDistribution;
	}
	public void setLatencyDistribution(String latencyDistribution) {
		LatencyDistribution = latencyDistribution;
	}
	@JSONField(name = "LatencyReorder")
	public Integer getLatencyReorder() {
		return LatencyReorder;
	}
	public void setLatencyReorder(Integer latencyReorder) {
		LatencyReorder = latencyReorder;
	}
	@JSONField(name = "LatencyDuplicate")
	public Integer getLatencyDuplicate() {
		return LatencyDuplicate;
	}
	public void setLatencyDuplicate(Integer latencyDuplicate) {
		LatencyDuplicate = latencyDuplicate;
	}
	@JSONField(name = "LatencyCorrupt")
	public Integer getLatencyCorrupt() {
		return LatencyCorrupt;
	}
	public void setLatencyCorrupt(Integer latencyCorrupt) {
		LatencyCorrupt = latencyCorrupt;
	}
	@JSONField(name = "TargetBandWidth")
	public Integer getTargetBandWidth() {
		return TargetBandWidth;
	}
	public void setTargetBandWidth(Integer targetBandWidth) {
		TargetBandWidth = targetBandWidth;
	}
	@JSONField(name = "PacketLoss")
	public Integer getPacketLoss() {
		return PacketLoss;
	}
	public void setPacketLoss(Integer packetLoss) {
		PacketLoss = packetLoss;
	}
	@JSONField(name = "TargetPorts")
	public List<String> getTargetPorts() {
		return TargetPorts;
	}
	@JSONField(name = "TargetPorts")
	public void setTargetPorts(List<String> targetPorts) {
		TargetPorts = targetPorts;
	}
	@JSONField(name = "TargetProtos")
	public List<String> getTargetProtos() {
		return TargetProtos;
	}
	@JSONField(name = "TargetProtos")
	public void setTargetProtos(List<String> targetProtos) {
		TargetProtos = targetProtos;
	}
	@JSONField(name = "TargetIps")
	public List<String> getTargetIps() {
		return TargetIps;
	}
	@JSONField(name = "TargetIps")
	public void setTargetIps(List<String> targetIps) {
		TargetIps = targetIps;
	}
	public boolean isSwitchState() {
		return switchState;
	}
	public void setSwitchState(boolean switchState) {
		this.switchState = switchState;
	}
	
	
}
