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

package com.mcg.controller.flow;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mcg.common.Constants;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.controller.base.BaseController;
import com.mcg.entity.common.McgResult;
import com.mcg.entity.flow.data.FlowData;
import com.mcg.entity.flow.end.FlowEnd;
import com.mcg.entity.flow.gmybatis.FlowGmybatis;
import com.mcg.entity.flow.java.FlowJava;
import com.mcg.entity.flow.json.FlowJson;
import com.mcg.entity.flow.linux.FlowLinux;
import com.mcg.entity.flow.model.FlowModel;
import com.mcg.entity.flow.python.FlowPython;
import com.mcg.entity.flow.script.FlowScript;
import com.mcg.entity.flow.sqlexecute.FlowSqlExecute;
import com.mcg.entity.flow.sqlquery.FlowSqlQuery;
import com.mcg.entity.flow.start.FlowStart;
import com.mcg.entity.flow.text.FlowText;
import com.mcg.entity.flow.web.WebStruct;
import com.mcg.entity.global.McgGlobal;
import com.mcg.entity.global.datasource.McgDataSource;
import com.mcg.entity.message.Message;
import com.mcg.entity.message.NotifyBody;
import com.mcg.plugin.ehcache.CachePlugin;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.service.FlowService;
import com.mcg.service.GlobalService;
import com.mcg.util.DataConverter;
import com.mcg.util.LevelDbUtil;
import com.mcg.util.Tools;

/**
 * 
 * @ClassName:   FlowController   
 * @Description: TODO(系统工作台功能服务) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午3:57:58  
 *
 */
@Controller
@RequestMapping(value="/flow")
public class FlowController extends BaseController {
	@Autowired
    private FlowService flowService;
	@Autowired
	private GlobalService globalService;
    
	@RequestMapping(value="/index")
	public ModelAndView toIndex()throws Exception{
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("flow/index");
		mv.addObject("version", Constants.VERSION);
		return mv;
	}
	
    @RequestMapping(value="saveStart", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public McgResult saveStart(@Valid @RequestBody FlowStart flowStart, BindingResult result, HttpSession session) {
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY);
        NotifyBody notifyBody = new NotifyBody();
        McgResult mcgResult = new McgResult();
        
        if(Tools.validator(result, mcgResult, notifyBody)) {
            flowStart.setName("流程开始");
            CachePlugin.put(flowStart.getStartId(), flowStart);
            notifyBody.setContent("开始控件保存成功！");
            notifyBody.setType(LogTypeEnum.SUCCESS.getValue());
        }
        
        message.setBody(notifyBody);
        MessagePlugin.push(session.getId(), message);        
        return mcgResult;
    }	
	
	@RequestMapping(value="saveModel", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public McgResult saveModel(@Valid @RequestBody FlowModel flowModel, BindingResult result, HttpSession session) {
        
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY);     
        NotifyBody notifyBody = new NotifyBody();       
        McgResult mcgResult = new McgResult();
        
        if(Tools.validator(result, mcgResult, notifyBody)) {
            flowModel.setName(flowModel.getModelProperty().getModelName());
            CachePlugin.put(flowModel.getModelId(), flowModel);
            notifyBody.setContent("Model控件保存成功！");
            notifyBody.setType(LogTypeEnum.SUCCESS.getValue());            
        }
        
        message.setBody(notifyBody);
        MessagePlugin.push(session.getId(), message);        
		return mcgResult;
	}
	
    @RequestMapping(value="saveSqlQuery", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public McgResult saveSqlQuery(@Valid @RequestBody FlowSqlQuery flowSqlQuery, BindingResult result, HttpSession session) {
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY);     
        NotifyBody notifyBody = new NotifyBody();       
        McgResult mcgResult = new McgResult();
        
        if(Tools.validator(result, mcgResult, notifyBody)) {
            flowSqlQuery.setName(flowSqlQuery.getSqlQueryProperty().getName());
            CachePlugin.put(flowSqlQuery.getId(), flowSqlQuery);
            notifyBody.setContent("Sql查询控件保存成功！");
            notifyBody.setType(LogTypeEnum.SUCCESS.getValue());
        }
        
        message.setBody(notifyBody);
        MessagePlugin.push(session.getId(), message);
        return mcgResult;
    }
    
    @RequestMapping(value="saveSqlExecute", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public McgResult saveSqlExecute(@Valid @RequestBody FlowSqlExecute flowSqlExecute, BindingResult result, HttpSession session) {
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY);     
        NotifyBody notifyBody = new NotifyBody();       
        McgResult mcgResult = new McgResult();
        if(Tools.validator(result, mcgResult, notifyBody)) {
            flowSqlExecute.setName(flowSqlExecute.getSqlExecuteProperty().getName());
            CachePlugin.put(flowSqlExecute.getId(), flowSqlExecute);
            notifyBody.setContent("Sql执行控件保存成功！");
            notifyBody.setType(LogTypeEnum.SUCCESS.getValue());
        }
        
        message.setBody(notifyBody);
        MessagePlugin.push(session.getId(), message);
        return mcgResult;
    }
    
	@RequestMapping(value="saveJson", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public McgResult saveJson(@Valid @RequestBody FlowJson flowJson, BindingResult result, HttpSession session) {
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY);     
        NotifyBody notifyBody = new NotifyBody();	    
	    McgResult mcgResult = new McgResult();
	    
	    if(Tools.validator(result, mcgResult, notifyBody)) {
            flowJson.setName(flowJson.getJsonProperty().getName());
            CachePlugin.put(flowJson.getId(), flowJson);
            notifyBody.setContent("Json控件保存成功！");
            notifyBody.setType(LogTypeEnum.SUCCESS.getValue());            
        }

        message.setBody(notifyBody);
        MessagePlugin.push(session.getId(), message);        
		return mcgResult;
	}	
    
	@RequestMapping(value="saveText", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public McgResult saveText(@Valid @RequestBody FlowText flowText, BindingResult result, HttpSession session) {
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY);     
        NotifyBody notifyBody = new NotifyBody();       
        McgResult mcgResult = new McgResult();
        
        if(Tools.validator(result, mcgResult, notifyBody)) {
            flowText.setName(flowText.getTextProperty().getName());
            CachePlugin.put(flowText.getTextId(), flowText);
            notifyBody.setContent("文本控件保存成功！");
            notifyBody.setType(LogTypeEnum.SUCCESS.getValue());            
        }

        message.setBody(notifyBody);
        MessagePlugin.push(session.getId(), message);        
		return mcgResult;
	}  
	
	@RequestMapping(value="saveScript", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public McgResult saveScript(@Valid @RequestBody FlowScript flowScript, BindingResult result, HttpSession session) {
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY);     
        NotifyBody notifyBody = new NotifyBody();       
        McgResult mcgResult = new McgResult();
        
        if(Tools.validator(result, mcgResult, notifyBody)) {
            flowScript.setName(flowScript.getScriptProperty().getScriptName());
            CachePlugin.put(flowScript.getScriptId(), flowScript);
            notifyBody.setContent("脚本控件保存成功！");
            notifyBody.setType(LogTypeEnum.SUCCESS.getValue());            
        }

        message.setBody(notifyBody);
        MessagePlugin.push(session.getId(), message);        
		return mcgResult;
	}  	
	
	@RequestMapping(value="saveJava", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public McgResult saveJava(@Valid @RequestBody FlowJava flowJava, BindingResult result, HttpSession session) {
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY);     
        NotifyBody notifyBody = new NotifyBody();       
        McgResult mcgResult = new McgResult();
        
        if(Tools.validator(result, mcgResult, notifyBody)) {
            flowJava.setName(flowJava.getJavaProperty().getName());
            CachePlugin.put(flowJava.getId(), flowJava);
            notifyBody.setContent("Java控件保存成功！");
            notifyBody.setType(LogTypeEnum.SUCCESS.getValue());            
        }

        message.setBody(notifyBody);
        MessagePlugin.push(session.getId(), message);        
		return mcgResult;
	}  	
	
	@RequestMapping(value="savePython", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public McgResult savePython(@Valid @RequestBody FlowPython flowPython, BindingResult result, HttpSession session) {
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY);     
        NotifyBody notifyBody = new NotifyBody();       
        McgResult mcgResult = new McgResult();
        
        if(Tools.validator(result, mcgResult, notifyBody)) {
            flowPython.setName(flowPython.getPythonProperty().getName());
            CachePlugin.put(flowPython.getId(), flowPython);
            notifyBody.setContent("Python控件保存成功！");
            notifyBody.setType(LogTypeEnum.SUCCESS.getValue());            
        }

        message.setBody(notifyBody);
        MessagePlugin.push(session.getId(), message);        
		return mcgResult;
	}
	
	@RequestMapping(value="saveLinux", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public McgResult saveLinux(@Valid @RequestBody FlowLinux flowLinux, BindingResult result, HttpSession session) {
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY);     
        NotifyBody notifyBody = new NotifyBody();       
        McgResult mcgResult = new McgResult();
        
        if(Tools.validator(result, mcgResult, notifyBody)) {
            flowLinux.setName(flowLinux.getLinuxProperty().getName());
            CachePlugin.put(flowLinux.getId(), flowLinux);
            notifyBody.setContent("Linux控件保存成功！");
            notifyBody.setType(LogTypeEnum.SUCCESS.getValue());            
        }

        message.setBody(notifyBody);
        MessagePlugin.push(session.getId(), message);        
		return mcgResult;
	}	
	
	@RequestMapping(value="saveData", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public McgResult saveData(@Valid @RequestBody FlowData flowData, BindingResult result, HttpSession session) {
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY);     
        NotifyBody notifyBody = new NotifyBody();	    
	    McgResult mcgResult = new McgResult();
        
	    if(Tools.validator(result, mcgResult, notifyBody)) {
            flowData.setName(flowData.getDataProperty().getName());
            CachePlugin.put(flowData.getId(), flowData);
            notifyBody.setContent("Data控件保存成功！");
            notifyBody.setType(LogTypeEnum.SUCCESS.getValue());            
        }
        
        message.setBody(notifyBody);
        MessagePlugin.push(session.getId(), message);         
		return mcgResult;
	} 		
	
	@RequestMapping(value="saveGmybatis", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public McgResult saveGmybatis(@Valid @RequestBody FlowGmybatis flowGmybatis, BindingResult result, HttpSession session) {
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY);     
        NotifyBody notifyBody = new NotifyBody();       
        McgResult mcgResult = new McgResult();
        
        if(Tools.validator(result, mcgResult, notifyBody)) {
            flowGmybatis.setName(flowGmybatis.getGmybatisProperty().getName());
            CachePlugin.put(flowGmybatis.getGmybatisId(), flowGmybatis);
            notifyBody.setContent("Gmybatis控件保存成功！");
            notifyBody.setType(LogTypeEnum.SUCCESS.getValue());            
        }

        message.setBody(notifyBody);
        MessagePlugin.push(session.getId(), message);         
		return mcgResult;
	} 	
	
    @RequestMapping(value="saveFlowEnd", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public McgResult saveFlowEnd(@Valid @RequestBody FlowEnd flowEnd, BindingResult result, HttpSession session) {
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY);     
        NotifyBody notifyBody = new NotifyBody();       
        McgResult mcgResult = new McgResult();
        
        if(Tools.validator(result, mcgResult, notifyBody)) {
            flowEnd.setName("流程结束");
            CachePlugin.put(flowEnd.getEndId(), flowEnd);
            notifyBody.setContent("结束控件保存成功！");
            notifyBody.setType(LogTypeEnum.SUCCESS.getValue());            
        }

        message.setBody(notifyBody);
        MessagePlugin.push(session.getId(), message);        
        return mcgResult;
    }	
    
    @RequestMapping(value="testConnect", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public McgResult testConnect(@RequestBody McgDataSource mcgDataSource) {
        
        McgResult mcgResult = new McgResult();
        mcgResult.setStatusCode(flowService.testConnect(mcgDataSource) ? 1 : 0);
        return mcgResult;
    }
    
    @RequestMapping(value="saveDataSource", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public McgResult saveDataSource(@Valid @RequestBody List<McgDataSource> flowDataSources, BindingResult result, HttpSession session) throws IOException, ClassNotFoundException {
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY);     
        NotifyBody notifyBody = new NotifyBody();       
        McgResult mcgResult = new McgResult();
        
        if(Tools.validator(result, mcgResult, notifyBody)) {
            McgGlobal mcgGlobal = (McgGlobal)LevelDbUtil.getObject(Constants.GLOBAL_KEY, McgGlobal.class);
            mcgGlobal.setFlowDataSources(flowDataSources);
            globalService.updateGlobal(mcgGlobal);   
            notifyBody.setContent("数据源控件保存成功！");
            notifyBody.setType(LogTypeEnum.SUCCESS.getValue());            
        }

        message.setBody(notifyBody);
        MessagePlugin.push(session.getId(), message);        
        return mcgResult;
    }    
	
    @RequestMapping(value="saveWebStruct", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public McgResult saveWebStruct(@Valid @RequestBody WebStruct webStruct, BindingResult result, HttpSession session) throws IOException {
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY);     
        NotifyBody notifyBody = new NotifyBody();       
        McgResult mcgResult = new McgResult();
        
        if(Tools.validator(result, mcgResult, notifyBody)) {
            flowService.saveFlow(webStruct, session);
            notifyBody.setContent("流程保存成功！");
            notifyBody.setType(LogTypeEnum.SUCCESS.getValue());            
        }

        message.setBody(notifyBody);
        MessagePlugin.push(session.getId(), message);          
        return mcgResult;
    }
    
    @RequestMapping(value="generate", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public McgResult generate(@Valid @RequestBody WebStruct webStruct, BindingResult result, HttpSession session) throws ClassNotFoundException, IOException {

        McgResult mcgResult = new McgResult();
        NotifyBody notifyBody = new NotifyBody();
        
        if(Tools.validator(result, mcgResult, notifyBody)) {
            if(flowService.saveFlow(webStruct, session) ) {
                flowService.generate(webStruct, session);
            }            
        } else {
            Message message = MessagePlugin.getMessage();
            message.getHeader().setMesType(MessageTypeEnum.NOTIFY); 
            
            message.setBody(notifyBody);
            MessagePlugin.push(session.getId(), message);
        }

        return mcgResult;
    }
    
    @RequestMapping(value="getFlowData", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebStruct getFlowData(String flowId, HttpSession session) throws ClassNotFoundException, IOException {
        WebStruct webStruct = null;
        if(StringUtils.isBlank(flowId)) {
            Message message = MessagePlugin.getMessage();
            message.getHeader().setMesType(MessageTypeEnum.NOTIFY);     
            NotifyBody notifyBody = new NotifyBody();       
            notifyBody.setContent("获取流程失败");
            notifyBody.setType(LogTypeEnum.ERROR.getValue()); 
            message.setBody(notifyBody);
            MessagePlugin.push(session.getId(), message);               
        } else {
            webStruct = DataConverter.flowStructToWebStruct(flowId);
        }
    	return webStruct;
    }
    
    @RequestMapping(value="clearFlowData", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public McgResult clearFlowData(String flowId, HttpSession session) {
    	
        McgResult mcgResult = new McgResult();
        Message messageComplete = MessagePlugin.getMessage();
        messageComplete.getHeader().setMesType(MessageTypeEnum.NOTIFY);
        NotifyBody notifyBody = new NotifyBody();
        LevelDbUtil.delete(flowId);
    	notifyBody.setContent("清空流程成功！");
    	notifyBody.setType(LogTypeEnum.SUCCESS.getValue());
/*        if(flowService.clearFileData(Constants.DATA_PATH + File.separator + flowId + Constants.EXTENSION)) {
        	notifyBody.setContent("清空流程成功！");
        	notifyBody.setType(LogTypeEnum.SUCCESS.getValue());
        } else {
        	notifyBody.setContent("清空流程失败！");
        	notifyBody.setType(LogTypeEnum.ERROR.getValue());        	
        }*/
        messageComplete.setBody(notifyBody);
        MessagePlugin.push(session.getId(), messageComplete);
        
    	return mcgResult;
    }
}