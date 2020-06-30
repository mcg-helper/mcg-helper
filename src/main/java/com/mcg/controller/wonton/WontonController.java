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

package com.mcg.controller.wonton;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.github.kevinsawicki.http.HttpRequest;
import com.mcg.common.Constants;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.controller.base.BaseController;
import com.mcg.entity.common.McgResult;
import com.mcg.entity.global.wonton.WontonData;
import com.mcg.entity.message.Message;
import com.mcg.entity.message.NotifyBody;
import com.mcg.entity.wonton.WontonHeart;
import com.mcg.entity.wonton.WontonPublish;
import com.mcg.plugin.task.McgTask;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.service.DbService;
import com.mcg.service.WontonService;
import com.mcg.util.LevelDbUtil;
import com.mcg.util.PageData;

/**
 * 
 * @ClassName:   WontonController   
 * @Description: TODO(混沌工程功能服务) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2019年1月14日 下午6:00:06  
 *
 */

@Controller
@RequestMapping(value="/wonton")
public class WontonController extends BaseController {
	
	private static Logger logger = LoggerFactory.getLogger(WontonController.class);
	@Autowired
	private WontonService wontonService;
	@Autowired
	private DbService dbService;
	
	@RequestMapping(value="/index")
	public ModelAndView toIndex() throws Exception{
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("wonton/index");
		mv.addObject("version", Constants.VERSION);
		return mv;
	}
	
    @RequestMapping(value="getAll", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public McgResult getAll() throws ClassNotFoundException, IOException {
    	
    	McgResult mcgResult = new McgResult();
    	mcgResult.getResultMap().put("dataList", wontonService.getAll());
    	return mcgResult;
    }
	
    @RequestMapping(value="heartbeat", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public void heartbeat(@Valid @RequestBody WontonHeart wontonHeart) {
    	
        McgTask.wontonHeartCacheData.offer(wontonHeart);
	}

    @RequestMapping(value="getWontonPublish", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public McgResult getWontonPublish(String instancecode, HttpSession session) throws ClassNotFoundException, IOException {
    	
    	McgResult mcgResult = new McgResult();
    	
    	WontonPublish wontonPublish = null;
        if(!StringUtils.isBlank(instancecode)) {
        	wontonPublish = (WontonPublish)dbService.query(instancecode, WontonPublish.class);
        	mcgResult.getResultMap().put("wontonPublish", wontonPublish);
        }
    	return mcgResult;
    }
    
    @RequestMapping(value="/publishModal")
    public ModelAndView getPublishModal() throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        mv.addObject("modalId", pd.get("modalId"));
        mv.setViewName("wonton/publishModal");
        return mv;
    }
    
    @RequestMapping(value="/ruleModal")
    public ModelAndView getRuleModal() throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        mv.addObject("modalId", pd.get("modalId"));        
        mv.setViewName("wonton/ruleModal");
        return mv;
    }    
    
    @RequestMapping(value="publishRule", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public McgResult publishRule(@RequestBody WontonPublish wontonPublish, HttpSession session) {
    	
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY);
        NotifyBody notifyBody = new NotifyBody();
        McgResult mcgResult = new McgResult();    	
        
        try {
        	dbService.save(wontonPublish.getWontonHeart().getInstancecode(), wontonPublish);
        	
            boolean flag = false;
            StringBuilder sucessBuilder = new StringBuilder();
        	StringBuilder faultBuilder = new StringBuilder();
        	
        	String resetUrl = "http://" + wontonPublish.getWontonHeart().getInstancecode() + "/muxy/networkshape/_reset";
        	String resourcesUrl = "http://" + wontonPublish.getWontonHeart().getInstancecode() + "/stressng/stressor";
        	
        	if(wontonPublish.getNetRule() != null && wontonPublish.getNetRule() != null && wontonPublish.getNetRule().isSwitchState()) {
        		HttpRequest httpRequest = HttpRequest.put(resetUrl);
        		String result = httpRequest.send(JSON.toJSONString(wontonPublish.getNetRule()).getBytes())
        					.connectTimeout(Constants.REQUEST_WONTON_TIME_OUT).body();
        		if(result.endsWith("successfully.")) {
	        		flag = true;
	        		sucessBuilder.append("网络规则，");
        		} else {
        			flag = false;
        			faultBuilder.append("网络规则，");
        		}
        	}
        	
        	if(wontonPublish.getCpuRule() != null && wontonPublish.getCpuRule() != null && wontonPublish.getCpuRule().isSwitchState()) {
        		HttpRequest httpRequest = HttpRequest.post(resourcesUrl);
        		String result = httpRequest.send(JSON.toJSONString(wontonPublish.getCpuRule()).getBytes())
        				.connectTimeout(Constants.REQUEST_WONTON_TIME_OUT).body();
        		if(200 == JSON.parseObject(result).getIntValue("State")) {
        			flag = true;
        			sucessBuilder.append("cpu规则，");
        		} else {
        			flag = false;
        			faultBuilder.append("cpu规则，");
        		}
        	}
        	if(wontonPublish.getMemRule() != null && wontonPublish.getMemRule() != null && wontonPublish.getMemRule().isSwitchState()) {
        		HttpRequest httpRequest = HttpRequest.post(resourcesUrl);
        		String result = httpRequest.send(JSON.toJSONString(wontonPublish.getMemRule()).getBytes())
        				.connectTimeout(Constants.REQUEST_WONTON_TIME_OUT).body();
        		if(200 == JSON.parseObject(result).getIntValue("State")) {
        			flag = true;
        			sucessBuilder.append("内存规则，");
        		} else {
        			flag = false;
        			faultBuilder.append("内存规则，");
        		}
        	}
        	if(wontonPublish.getIoRule() != null && wontonPublish.getIoRule()!= null && wontonPublish.getIoRule().isSwitchState()) {
        		HttpRequest httpRequest = HttpRequest.post(resourcesUrl);
        		String result = httpRequest.send(JSON.toJSONString(wontonPublish.getIoRule()).getBytes())
        					.connectTimeout(Constants.REQUEST_WONTON_TIME_OUT).body();
        		if(200 == JSON.parseObject(result).getIntValue("State")) {
        			flag = true;
        			sucessBuilder.append("io规则，");
        		} else {
        			flag = false;
        			faultBuilder.append("io规则，");
        		}
        	}
        	
        	if(StringUtils.isEmpty(sucessBuilder.toString()) && StringUtils.isEmpty(faultBuilder.toString())) {
        		notifyBody.setContent("没有规则可发布！");
        		notifyBody.setType(LogTypeEnum.SUCCESS.getValue());
        	} else {
        		
    	    	if(flag) {
    	    		sucessBuilder.append("发布成功！");
    	    		notifyBody.setContent(sucessBuilder.toString());
    		        notifyBody.setType(LogTypeEnum.SUCCESS.getValue());
    	    	} else {
    	    		faultBuilder.append("发布失败！");
    	    		notifyBody.setContent(sucessBuilder.toString() + faultBuilder.toString());
    	    		notifyBody.setType(LogTypeEnum.ERROR.getValue());
    	    	}
        	}
        	message.setBody(notifyBody);
        } catch (Exception e) {
        	
        	String reason = wontonPublish.getWontonHeart().getInstancecode() + "请求混沌客户端异常，规则发布失败！";
        	logger.error(reason + "异常信息：{}", e.getMessage());
    		notifyBody.setContent(reason);
    		notifyBody.setType(LogTypeEnum.ERROR.getValue());
    		mcgResult.setStatusCode(0);
    		mcgResult.setStatusMes(reason);
		} finally {
			message.setBody(notifyBody);
	        MessagePlugin.push(wontonPublish.getMcgWebScoketCode(), session.getId(), message);
		}

        return mcgResult;
	}
    
    @RequestMapping(value="/recoveryNet")
    @ResponseBody
    public McgResult recoveryNet(String instanceCode, HttpSession session) throws Exception {

        McgResult mcgResult = new McgResult();
        
    	try {
    		
    		String url = String.format("http://%s/muxy/networkshape/_disable", instanceCode);
    		HttpRequest httpRequest = HttpRequest.put(url).connectTimeout(Constants.REQUEST_WONTON_TIME_OUT);
    		String result = httpRequest.body();
    		
    		if(StringUtils.isNotEmpty(result) && result.endsWith("successfully")) {
        		mcgResult.setStatusCode(1);
        		mcgResult.setStatusMes(instanceCode + "网络恢复成功！");
    		} else {
        		mcgResult.setStatusCode(0);
        		mcgResult.setStatusMes(instanceCode + "网络恢复失败！");
    		}
    	} catch (Exception e) {
    		String reason = instanceCode + "网络恢复失败！";
			logger.error(reason + "异常信息：{}", e.getMessage());
    		mcgResult.setStatusCode(0);
    		mcgResult.setStatusMes(reason);
		}

        return mcgResult;
    }
    
    @RequestMapping(value="/recoveryHardware")
    @ResponseBody
    public McgResult recoveryHardware(String instanceCode, HttpSession session) throws Exception {
        McgResult mcgResult = new McgResult();
        
    	try {
    		
    		String url = String.format("http://%s/stressng/stressors", instanceCode);
    		HttpRequest httpRequest = HttpRequest.delete(url).connectTimeout(Constants.REQUEST_WONTON_TIME_OUT);
    		String result = httpRequest.body();
    		
    		if(StringUtils.isNotEmpty(result) && result.endsWith("here\n")) {
        		mcgResult.setStatusCode(1);
        		mcgResult.setStatusMes(instanceCode + "没有启动cpu、io、内存规则，不用恢复！");
    		} else {
        		mcgResult.setStatusCode(0);
        		mcgResult.setStatusMes(instanceCode + "硬件恢复失败！");
    		}
    	} catch (Exception e) {
			
    		String reason = instanceCode + "禁用cpu、io、内存规则，恢复硬件资源性能失败！";
    		logger.error(reason + "异常信息：{}", e.getMessage());
    		mcgResult.setStatusCode(0);
    		mcgResult.setStatusMes(reason);
		}

        return mcgResult;
    }
    
    
    @RequestMapping(value="getRule", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public McgResult getRule(@Valid @RequestBody WontonHeart wontonHeart, HttpSession session) {
        McgResult mcgResult = new McgResult();
        
        try {
        	String rule = HttpRequest.get("http://" + wontonHeart.getInstancecode() + "/muxy/networkshapes")
        				.connectTimeout(Constants.REQUEST_WONTON_TIME_OUT).body();
        	mcgResult.addAttribute("rule", rule);
        } catch (Exception e) {
        	logger.error("获取混沌客户端规则出错，异常信息：{}", e.getMessage());
        	mcgResult.setStatusCode(0);
        	mcgResult.setStatusMes("获取混沌客户端规则异常！");
		}
    	return mcgResult;
	}    
    
    
    @RequestMapping(value="deleteInstance", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public McgResult deleteInstance(String instanceCode) {
        McgResult mcgResult = new McgResult();
        
        try {
        	WontonData wontonData = (WontonData)LevelDbUtil.getObject(Constants.WONTON_KEY, WontonData.class);
        	if(wontonData != null) {
        		McgTask.wontonInstanceMap.remove(instanceCode);
        		wontonData.setWontonHeartMap(McgTask.wontonInstanceMap);
        		LevelDbUtil.putObject(Constants.WONTON_KEY, wontonData);
        		mcgResult.setStatusMes("删除混沌客户端" + instanceCode + "成功！");
        	}
        	
        } catch (Exception e) {
        	logger.error("删除混沌客户端{}出错，异常信息：{}", instanceCode, e.getMessage());
        	mcgResult.setStatusCode(0);
        	mcgResult.setStatusMes("删除混沌客户端" + instanceCode + "发生异常，请重试！");
		}
    	return mcgResult;
	} 
}
