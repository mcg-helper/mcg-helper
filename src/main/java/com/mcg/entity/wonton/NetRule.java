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

package com.mcg.entity.wonton;

import java.util.List;

public class NetRule {

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
	
	public String getDevice() {
		return Device;
	}
	public void setDevice(String device) {
		Device = device;
	}
	public Integer getLatency() {
		return Latency;
	}
	public void setLatency(Integer latency) {
		Latency = latency;
	}
	public Integer getLatencyJitter() {
		return LatencyJitter;
	}
	public void setLatencyJitter(Integer latencyJitter) {
		LatencyJitter = latencyJitter;
	}
	public Integer getLatencyCorrelation() {
		return LatencyCorrelation;
	}
	public void setLatencyCorrelation(Integer latencyCorrelation) {
		LatencyCorrelation = latencyCorrelation;
	}
	public String getLatencyDistribution() {
		return LatencyDistribution;
	}
	public void setLatencyDistribution(String latencyDistribution) {
		LatencyDistribution = latencyDistribution;
	}
	public Integer getLatencyReorder() {
		return LatencyReorder;
	}
	public void setLatencyReorder(Integer latencyReorder) {
		LatencyReorder = latencyReorder;
	}
	public Integer getLatencyDuplicate() {
		return LatencyDuplicate;
	}
	public void setLatencyDuplicate(Integer latencyDuplicate) {
		LatencyDuplicate = latencyDuplicate;
	}
	public Integer getLatencyCorrupt() {
		return LatencyCorrupt;
	}
	public void setLatencyCorrupt(Integer latencyCorrupt) {
		LatencyCorrupt = latencyCorrupt;
	}
	public Integer getTargetBandWidth() {
		return TargetBandWidth;
	}
	public void setTargetBandWidth(Integer targetBandWidth) {
		TargetBandWidth = targetBandWidth;
	}
	public Integer getPacketLoss() {
		return PacketLoss;
	}
	public void setPacketLoss(Integer packetLoss) {
		PacketLoss = packetLoss;
	}
	public List<String> getTargetPorts() {
		return TargetPorts;
	}
	public void setTargetPorts(List<String> targetPorts) {
		TargetPorts = targetPorts;
	}
	public List<String> getTargetProtos() {
		return TargetProtos;
	}
	public void setTargetProtos(List<String> targetProtos) {
		TargetProtos = targetProtos;
	}
	public List<String> getTargetIps() {
		return TargetIps;
	}
	public void setTargetIps(List<String> targetIps) {
		TargetIps = targetIps;
	}
	public boolean getSwitchState() {
		return switchState;
	}
	public void setSwitchState(boolean switchState) {
		this.switchState = switchState;
	}
	
	
}
