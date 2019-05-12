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

<div class="container-fluid" style="height:410px;">
	<div class="row">
		<div class="col-md-12">
			<ul class="nav nav-tabs" role="tablist" id="${modalId }_tab">
				<li role="presentation" class="active"><a href="#${modalId }_wonton_publish_net_tab" data-toggle="tab">网络</a></li>
				<li role="presentation"><a href="#${modalId }_wonton_publish_cpu_tab" data-toggle="tab">CPU</a></li>
				<li role="presentation"><a href="#${modalId }_wonton_publish_mem_tab" data-toggle="tab">内存</a></li>
				<li role="presentation"><a href="#${modalId }_wonton_publish_io_tab" data-toggle="tab">IO</a></li>
			  	<li role="presentation"><a href="#${modalId }_wonton_publish_explain_tab" data-toggle="tab">说明</a></li>
			</ul>
		</div>
	</div>

	<div class="row margin_top">
		<div class="col-md-12">
			<form id="${modalId }_wonton_publish_form" class="form-horizontal" role="form">
				<div class="form-body">
					<div class="tab-content">
						<div class="tab-pane fade in active" id="${modalId }_wonton_publish_net_tab">
							<div class="form-group">
								<label class="col-sm-2 control-label">网络设备名称</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="hidden" id="${modalId }_modalId" name="id" value="${modalId }" />
										<input type="text" name="netRule[Device]" class="form-control" placeholder="例：eth0，需要控制的网络设备名称" />
										<!-- <span class="help-block">需要控制的网络设备名称，例如eth0</span> -->
									</div>
								</div>
								
								<label class="col-sm-1 control-label">发送开关</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input name="netRule[switchState]"  type="checkbox"/>
									</div>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">延迟抖动</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="netRule[LatencyJitter]" class="form-control" placeholder="例：10，则延迟ms在[200-20, 200+20]区间" />
									</div>
								</div>
								
								<label class="col-sm-1 control-label">延迟率</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="netRule[LatencyCorrelation]" class="form-control" placeholder="例：25，表示当前packet的前后有25%的相关性"/>
									</div>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">网络延迟</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="netRule[Latency]" class="form-control" placeholder="例：200，发出的packet延迟200ms"/>
									</div>
								</div>	

								<label class="col-sm-1 control-label">分布规则</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<select name="netRule[LatencyDistribution]" class="selectpicker form-control">
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
										<input type="text" name="netRule[LatencyReorder]" class="form-control" placeholder="例：25，则25%的packet发送顺序会改变"/>
									</div>
								</div>							
							
								<label class="col-sm-1 control-label">包重复率</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="netRule[LatencyDuplicate]" class="form-control" placeholder="例：20，则20%的packet会重复发送"/>
									</div>
								</div>
								
							</div>	
							
							<div class="form-group">
								<label class="col-sm-2 control-label">包损坏率</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="netRule[LatencyCorrupt]" class="form-control" placeholder="例：2，随机产生 2% 损坏的报文"/>
									</div>
								</div>		
													
								<label class="col-sm-1 control-label">网络带宽</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="netRule[TargetBandWidth]" class="form-control" placeholder="例：20，则表示出口带宽模拟为20kbit"/>
									</div>
								</div>
								
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">丢包率</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="netRule[PacketLoss]" class="form-control" placeholder="例：30，则30%的丢包率"/>
									</div>
								</div>
															
								<label class="col-sm-1 control-label">源端口</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="netRule[TargetPorts]" class="form-control" placeholder="例：5001,10090，对端口的网络连接进行控制"/>
									</div>
								</div>
								
							</div>	
							
							<div class="form-group">
								<label class="col-sm-2 control-label">目标协议</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="netRule[TargetProtos]" class="form-control" placeholder="例：tcp,icmp，对当前指定的协议进行网络模拟"/>
									</div>
								</div>
															
								<label class="col-sm-1 control-label">目标地址</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="netRule[TargetIps]" class="form-control" placeholder="例：172.16.2.9,172.16.2.13，对指定的ip进行网络模拟"/>
									</div>
								</div>
								
							</div>
							
						</div>
						<div class="tab-pane fade" id="${modalId }_wonton_publish_cpu_tab">
							<div class="form-group">
								<label class="col-sm-2 control-label">类型</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<p class="form-control-static">cpu</p>
										<input type="hidden" name="cpuRule[Stressor]" value="cpu"/>
									</div>
								</div>
								
								<label class="col-sm-1 control-label">发送开关</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input name="cpuRule[switchState]" type="checkbox"/>
									</div>
								</div>						
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">Cpu负载</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="cpuRule[CpuLoad]" class="form-control" placeholder="例：50，模拟CPU的load为50%"/>
									</div>
								</div>
								
								<label class="col-sm-1 control-label">任务数</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input type="text" name="cpuRule[StressorCount]" class="form-control" placeholder="例：5，启动5个压力任务"/>
									</div>
								</div>								
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">开启监控</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<select name="cpuRule[Metric]" class="selectpicker form-control">
											<option value="false">否</option>
											<option value="true">是</option>
										</select>
									</div>
								</div>
								
								<label class="col-sm-1 control-label">是否中断</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<select name="cpuRule[Abort]" class="selectpicker form-control">
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
										<input type="text" name="cpuRule[Timeout]" class="form-control" placeholder="例：60，压力持续时间为60，与时间单位配合使用"/>
									</div>
								</div>	
															
								<label class="col-sm-1 control-label">时间单位</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<select name="cpuRule[TimeoutUnit]" class="selectpicker form-control">
											<option value="s">秒</option>
											<option value="m">分钟</option>
											<option value="h">小时</option>
										</select>
									</div>
								</div>
								
							</div>
							
						</div>		
						<div class="tab-pane fade" id="${modalId }_wonton_publish_mem_tab">
							<div class="form-group">
								<label class="col-sm-2 control-label">类型</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<p class="form-control-static">vm</p>
										<input type="hidden" name="memRule[Stressor]" value="vm"/>									
									</div>
								</div>
								
								<label class="col-sm-1 control-label">发送开关</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input name="memRule[switchState]" type="checkbox"/>
									</div>
								</div>								
							</div>
							
							<div class="form-group">
								
								<label class="col-sm-2 control-label">是否中断</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<select name="memRule[Abort]" class="selectpicker form-control">
											<option value="true">是</option>
											<option value="false">否</option>
										</select>
									</div>
								</div>	
								
								<label class="col-sm-1 control-label">开启监控</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<select name="memRule[Metric]" class="selectpicker form-control">
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
										<input type="text" name="memRule[Timeout]" class="form-control" placeholder="例：60，压力持续时间为60，与时间单位配合使用"/>
									</div>
								</div>
								
								<label class="col-sm-1 control-label">时间单位</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<select name="memRule[TimeoutUnit]" class="selectpicker form-control">
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
										<input type="text" name="memRule[StressorCount]" class="form-control" placeholder="例：5，启动5个压力任务"/>
									</div>
								</div>
							</div>
						</div>
						<div class="tab-pane fade" id="${modalId }_wonton_publish_io_tab">
							
							<div class="form-group">
								<label class="col-sm-2 control-label">类型</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<p class="form-control-static">iomix</p>
										<input type="hidden" name="ioRule[Stressor]" value="iomix"/>									
									</div>
								</div>
															
								<label class="col-sm-1 control-label">发送开关</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<input name="ioRule[switchState]" type="checkbox"/>
									</div>
								</div>								
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">是否中断</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<select name="ioRule[Abort]" class="selectpicker form-control">
											<option value="true">是</option>
											<option value="false">否</option>
										</select>
									</div>
								</div>	
															
								<label class="col-sm-1 control-label">开启监控</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<select name="ioRule[Metric]" class="selectpicker form-control">
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
										<input type="text" name="ioRule[Timeout]" class="form-control" placeholder="例：60，压力持续时间为60，与时间单位配合使用"/>
									</div>
								</div>	
															
								<label class="col-sm-1 control-label">时间单位</label>
								<div class="col-sm-4">
									<div class="fg-line">
										<select name="ioRule[TimeoutUnit]" class="selectpicker form-control">
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
										<input type="text" name="ioRule[StressorCount]" class="form-control" placeholder="例：5，启动5个压力任务"/>
									</div>
								</div>							

							</div>							
							
						</div>						
						<div class="tab-pane fade" id="${modalId }_wonton_publish_explain_tab">
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