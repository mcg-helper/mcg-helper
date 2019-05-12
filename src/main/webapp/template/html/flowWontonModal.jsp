<!-- 
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
 -->
 
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container-fluid">
	<div class="row">
		<div class="col-md-12">
			<ul class="nav nav-tabs" role="tablist" id="${modalId }_tab">
				<li role="presentation" class="active"><a href="#${modalId }_wontonProperty" data-toggle="tab">属性</a></li>
				<li role="presentation"><a href="#${modalId }_wontonNetRule" data-toggle="tab">网络</a></li>
				<li role="presentation"><a href="#${modalId }_wontonCpuRule" data-toggle="tab">CPU</a></li>
				<li role="presentation"><a href="#${modalId }_wontonMemRule" data-toggle="tab">IO</a></li>				
				<li role="presentation"><a href="#${modalId }_wontonIoRule" data-toggle="tab">内存</a></li>
			  	<li role="presentation"><a href="#${modalId }_explain" data-toggle="tab">说明</a></li>
			</ul>
		</div>
	</div>

	<div class="row margin_top">
		<div class="col-md-12">
			<form id="${modalId }_wontonForm" class="form-horizontal" role="form">
				<div class="form-body">
					<div id="myTabContent" class="tab-content">
						<div class="tab-pane fade in active" id="${modalId }_wontonProperty">
							<div class="form-group">
								<label class="col-sm-2 control-label">控件名称</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="hidden" id="${modalId }_modalId" name="id" value="${modalId }" />
										<input type="text" id="${modalId }_name" name="wontonProperty[name]" class="form-control"  />
									</div>
								</div>
								<label class="col-sm-1 control-label">控件KEY</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" id="${modalId }_key" name="wontonProperty[key]" class="form-control"  />
									</div>
								</div>									
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">混沌客户端</label>
								<div class="col-sm-9">
									<div class="fg-line">
							          	<select id="${modalId }_instancecode" name="wontonProperty[instancecode]" class="selectpicker">
							          		<option value="">请选择</option>
							          		<c:set var="state" value=""/>
						                	<c:forEach items="${wontons}" var="item">
												<c:choose>
												   <c:when test="${item.state == 'normal'}">
														<c:set var="state" value="存活"/>
												   </c:when>
												   <c:when test="${item.state == 'losed'}">  
														<c:set var="state" value="失联"/>
												   </c:when>
												   <c:otherwise> 
														<c:set var="state" value="未知"/>
												   </c:otherwise>
												</c:choose>
							              		<option value="${item.instancecode }">${item.instancecode }————状态：${state}————进程PID：${item.pid}————版本号：${item.version }</option>
							              	</c:forEach>
							          	</select>
									</div>
								</div>
																
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">描述</label>
								<div class="col-sm-9">
									<div class="fg-line">
										<textarea id="${modalId }_desc" name="wontonProperty[desc]" rows="5" cols="" class="form-control"></textarea>
									</div>
								</div>
							</div>								
						</div>
						
						<div class="tab-pane fade" id="${modalId }_wontonNetRule">
							<div class="form-group">
								<label class="col-sm-2 control-label">网络设备名称</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="wontonNetRule[Device]" class="form-control" placeholder="例：eth0，需要控制的网络设备名称" />
										<!-- <span class="help-block">需要控制的网络设备名称，例如eth0</span> -->
									</div>
								</div>
								
								<label class="col-sm-1 control-label">发送开关</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input id="ddd" name="wontonNetRule[switchState]" type="checkbox" />
									</div>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">延迟抖动</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="wontonNetRule[LatencyJitter]" class="form-control" placeholder="例：10，则延迟ms在[200-20, 200+20]区间" />
									</div>
								</div>
								
								<label class="col-sm-1 control-label">延迟率</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="wontonNetRule[LatencyCorrelation]" class="form-control" placeholder="例：25，表示当前packet的前后有25%的相关性"/>
									</div>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">网络延迟</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="wontonNetRule[Latency]" class="form-control" placeholder="例：200，发出的packet延迟200ms"/>
									</div>
								</div>	

								<label class="col-sm-1 control-label">分布规则</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<select name="wontonNetRule[LatencyDistribution]" class="selectpicker form-control">
											<option value="normal">normal</option>
											<option value="uniform">uniform</option>
											<option value="pareto">pareto</option>
											<option value="paretonormal">paretonormal</option>
										</select>
										<!-- <span class="help-block">随机延迟的ms数符合的分布规则</span> -->
									</div>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">包乱序率</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="wontonNetRule[LatencyReorder]" class="form-control" placeholder="例：25，则25%的packet发送顺序会改变"/>
									</div>
								</div>							
							
								<label class="col-sm-1 control-label">包重复率</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="wontonNetRule[LatencyDuplicate]" class="form-control" placeholder="例：20，则20%的packet会重复发送"/>
									</div>
								</div>
								
							</div>	
							
							<div class="form-group">
								<label class="col-sm-2 control-label">包损坏率</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="wontonNetRule[LatencyCorrupt]" class="form-control" placeholder="例：2，随机产生 2% 损坏的报文"/>
									</div>
								</div>		
													
								<label class="col-sm-1 control-label">网络带宽</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="wontonNetRule[TargetBandWidth]" class="form-control" placeholder="例：20，则表示出口带宽模拟为20kbit"/>
									</div>
								</div>
								
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">丢包率</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="wontonNetRule[PacketLoss]" class="form-control" placeholder="例：30，则30%的丢包率"/>
									</div>
								</div>
															
								<label class="col-sm-1 control-label">源端口</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" id="${modalId }_targetPorts" name="wontonNetRule[TargetPorts]" class="form-control" placeholder="例：5001,10090，对端口的网络连接进行控制"/>
									</div>
								</div>
								
							</div>	
							
							<div class="form-group">
								<label class="col-sm-2 control-label">目标协议</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" id="${modalId }_targetProtos" name="wontonNetRule[TargetProtos]" class="form-control" placeholder="例：tcp,icmp，对当前指定的协议进行网络模拟"/>
									</div>
								</div>
															
								<label class="col-sm-1 control-label">目标地址</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" id="${modalId }_targetIps" name="wontonNetRule[TargetIps]" class="form-control" placeholder="例：172.16.2.9,172.16.2.13，对指定的ip进行网络模拟"/>
									</div>
								</div>
								
							</div>
						</div>	
						
						<div class="tab-pane fade" id="${modalId }_wontonCpuRule">
							<div class="form-group">
								<label class="col-sm-2 control-label">类型</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<p class="form-control-static">cpu</p>
										<input type="hidden" name="wontonCpuRule[Stressor]" value="cpu"/>
									</div>
								</div>
								
								<label class="col-sm-1 control-label">发送开关</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input name="wontonCpuRule[switchState]" type="checkbox"/>
									</div>
								</div>						
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">Cpu负载</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="wontonCpuRule[CpuLoad]" class="form-control" placeholder="例：50，模拟CPU的load为50%"/>
									</div>
								</div>
								
								<label class="col-sm-1 control-label">任务数</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="wontonCpuRule[StressorCount]" class="form-control" placeholder="例：5，启动5个压力任务"/>
									</div>
								</div>								
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">开启监控</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<select name="wontonCpuRule[Metric]" class="selectpicker form-control">
											<option value="false">否</option>
											<option value="true">是</option>
										</select>
									</div>
								</div>
								
								<label class="col-sm-1 control-label">是否中断</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<select name="wontonCpuRule[Abort]" class="selectpicker form-control">
											<option value="true">是</option>
											<option value="false">否</option>
										</select>
									</div>
								</div>								
							</div>						
						
							<div class="form-group">
								<label class="col-sm-2 control-label">持续时间</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="wontonCpuRule[Timeout]" class="form-control" placeholder="例：60，压力持续时间为60，与时间单位配合使用"/>
									</div>
								</div>	
															
								<label class="col-sm-1 control-label">时间单位</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<select name="wontonCpuRule[TimeoutUnit]" class="selectpicker form-control">
											<option value="s">秒</option>
											<option value="m">分钟</option>
											<option value="h">小时</option>
										</select>
									</div>
								</div>
								
							</div>
						</div>
						
						<div class="tab-pane fade" id="${modalId }_wontonMemRule">
							<div class="form-group">
								<label class="col-sm-2 control-label">类型</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<p class="form-control-static">vm</p>
										<input type="hidden" name="wontonMemRule[Stressor]" value="vm"/>									
									</div>
								</div>
								
								<label class="col-sm-1 control-label">发送开关</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input name="wontonMemRule[switchState]" type="checkbox"/>
									</div>
								</div>								
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">是否中断</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<select name="wontonMemRule[Abort]" class="selectpicker form-control">
											<option value="true">是</option>
											<option value="false">否</option>
										</select>
									</div>
								</div>	
								
								<label class="col-sm-1 control-label">开启监控</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<select name="wontonMemRule[Metric]" class="selectpicker form-control">
											<option value="false">否</option>
											<option value="true">是</option>
										</select>									
									</div>
								</div>															
							</div>
							
							<div class="form-group">
								
								<label class="col-sm-2 control-label">持续时间</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="wontonMemRule[Timeout]" class="form-control" placeholder="例：60，压力持续时间为60，与时间单位配合使用"/>
									</div>
								</div>
								
								<label class="col-sm-1 control-label">时间单位</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<select name="wontonMemRule[TimeoutUnit]" class="selectpicker form-control">
											<option value="s">秒</option>
											<option value="m">分钟</option>
											<option value="h">小时</option>
										</select>
									</div>
								</div>
							</div>		
							
							<div class="form-group">	
								<label class="col-sm-2 control-label">任务数</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="wontonMemRule[StressorCount]" class="form-control" placeholder="例：5，启动5个压力任务"/>
									</div>
								</div>
							</div>
						</div>
						
						<div class="tab-pane fade" id="${modalId }_wontonIoRule">
							<div class="form-group">
								<label class="col-sm-2 control-label">类型</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<p class="form-control-static">iomix</p>
										<input type="hidden" name="wontonIoRule[Stressor]" value="iomix"/>									
									</div>
								</div>
															
								<label class="col-sm-1 control-label">发送开关</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input name="wontonIoRule[switchState]" type="checkbox"/>
									</div>
								</div>								
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">是否中断</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<select name="wontonIoRule[Abort]" class="selectpicker form-control">
											<option value="true">是</option>
											<option value="false">否</option>
										</select>
									</div>
								</div>	
															
								<label class="col-sm-1 control-label">开启监控</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<select name="wontonIoRule[Metric]" class="selectpicker form-control">
											<option value="false">否</option>
											<option value="true">是</option>
										</select>									
									</div>
								</div>
							</div>							
							
							<div class="form-group">
								<label class="col-sm-2 control-label">持续时间</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="wontonIoRule[Timeout]" class="form-control" placeholder="例：60，压力持续时间为60，与时间单位配合使用"/>
									</div>
								</div>	
															
								<label class="col-sm-1 control-label">时间单位</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<select name="wontonIoRule[TimeoutUnit]" class="selectpicker form-control">
											<option value="s">秒</option>
											<option value="m">分钟</option>
											<option value="h">小时</option>
										</select>
									</div>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">任务数</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="wontonIoRule[StressorCount]" class="form-control" placeholder="例：5，启动5个压力任务"/>
									</div>
								</div>							
							</div>
						</div>
						
						<div class="tab-pane fade" id="${modalId }_explain">
							<div class="form-group">
								<div class="col-sm-12">
									<div class="fg-line">
										
									</div>
								</div>
							</div>
						</div>	
												
					</div>
				</div>
			</form>
		</div>
	</div>
</div>	