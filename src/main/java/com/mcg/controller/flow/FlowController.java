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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.mcg.entity.flow.git.FlowGit;
import com.mcg.entity.flow.java.FlowJava;
import com.mcg.entity.flow.json.FlowJson;
import com.mcg.entity.flow.linux.FlowLinux;
import com.mcg.entity.flow.loop.FlowLoop;
import com.mcg.entity.flow.process.FlowProcess;
import com.mcg.entity.flow.python.FlowPython;
import com.mcg.entity.flow.script.FlowScript;
import com.mcg.entity.flow.sftp.FlowSftp;
import com.mcg.entity.flow.sqlexecute.FlowSqlExecute;
import com.mcg.entity.flow.sqlquery.FlowSqlQuery;
import com.mcg.entity.flow.start.FlowStart;
import com.mcg.entity.flow.text.FlowText;
import com.mcg.entity.flow.web.WebStruct;
import com.mcg.entity.flow.wonton.FlowWonton;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunStatus;
import com.mcg.entity.global.McgGlobal;
import com.mcg.entity.global.datasource.McgDataSource;
import com.mcg.entity.message.Message;
import com.mcg.entity.message.NotifyBody;
import com.mcg.plugin.ehcache.CachePlugin;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.service.DbService;
import com.mcg.service.FlowService;
import com.mcg.service.GlobalService;
import com.mcg.util.DataConverter;
import com.mcg.util.FlowInstancesUtils;
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
	
	private static Logger logger = LoggerFactory.getLogger(FlowController.class);
	
	@Autowired
    private FlowService flowService;
	@Autowired
	private GlobalService globalService;
    @Autowired
    private DbService dbService;
    
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
            CachePlugin.putFlowEntity(flowStart.getFlowId(), flowStart.getStartId(), flowStart);
            notifyBody.setContent("开始控件保存成功！");
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
            CachePlugin.putFlowEntity(flowSqlQuery.getFlowId(), flowSqlQuery.getId(), flowSqlQuery);
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
            CachePlugin.putFlowEntity(flowSqlExecute.getFlowId(), flowSqlExecute.getId(), flowSqlExecute);
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
            CachePlugin.putFlowEntity(flowJson.getFlowId(), flowJson.getId(), flowJson);
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
            CachePlugin.putFlowEntity(flowText.getFlowId(), flowText.getTextId(), flowText);
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
            CachePlugin.putFlowEntity(flowScript.getFlowId(), flowScript.getScriptId(), flowScript);
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
            CachePlugin.putFlowEntity(flowJava.getFlowId(), flowJava.getId(), flowJava);
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
            CachePlugin.putFlowEntity(flowPython.getFlowId(), flowPython.getId(), flowPython);
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
            CachePlugin.putFlowEntity(flowLinux.getFlowId(), flowLinux.getId(), flowLinux);
            notifyBody.setContent("Linux控件保存成功！");
            notifyBody.setType(LogTypeEnum.SUCCESS.getValue());            
        }

        message.setBody(notifyBody);
        MessagePlugin.push(session.getId(), message);        
		return mcgResult;
	}	
	
	@RequestMapping(value="saveWonton", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public McgResult saveWonton(@Valid @RequestBody FlowWonton flowWonton, BindingResult result, HttpSession session) {
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY);
        NotifyBody notifyBody = new NotifyBody();
        McgResult mcgResult = new McgResult();
        
        if(Tools.validator(result, mcgResult, notifyBody)) {
        	flowWonton.setName(flowWonton.getWontonProperty().getName());
            CachePlugin.putFlowEntity(flowWonton.getFlowId(), flowWonton.getId(), flowWonton);
            notifyBody.setContent("混沌控件保存成功！");
            notifyBody.setType(LogTypeEnum.SUCCESS.getValue());
        }

        message.setBody(notifyBody);
        MessagePlugin.push(session.getId(), message);
		return mcgResult;
	}
	
	@RequestMapping(value="saveProcess", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public McgResult saveProcess(@Valid @RequestBody FlowProcess flowProcess, BindingResult result, HttpSession session) {
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY);
        NotifyBody notifyBody = new NotifyBody();
        McgResult mcgResult = new McgResult();
        
        if(Tools.validator(result, mcgResult, notifyBody)) {
        	flowProcess.setName(flowProcess.getProcessProperty().getName());
            CachePlugin.putFlowEntity(flowProcess.getFlowId(), flowProcess.getId(), flowProcess);
            notifyBody.setContent("子流程控件保存成功！");
            notifyBody.setType(LogTypeEnum.SUCCESS.getValue());
        }

        message.setBody(notifyBody);
        MessagePlugin.push(session.getId(), message);
		return mcgResult;
	}
	
	@RequestMapping(value="saveLoop", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public McgResult saveLoop(@Valid @RequestBody FlowLoop flowLoop, BindingResult result, HttpSession session) {
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY);
        NotifyBody notifyBody = new NotifyBody();
        McgResult mcgResult = new McgResult();
        
        if(Tools.validator(result, mcgResult, notifyBody)) {
        	flowLoop.setName(flowLoop.getLoopProperty().getName());
            CachePlugin.putFlowEntity(flowLoop.getFlowId(), flowLoop.getId(), flowLoop);
            notifyBody.setContent("循环控件保存成功！");
            notifyBody.setType(LogTypeEnum.SUCCESS.getValue());
        }

        message.setBody(notifyBody);
        MessagePlugin.push(session.getId(), message);
		return mcgResult;
	}
	
	@RequestMapping(value="saveGit", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public McgResult saveGit(@Valid @RequestBody FlowGit flowGit, BindingResult result, HttpSession session) {
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY);
        NotifyBody notifyBody = new NotifyBody();
        McgResult mcgResult = new McgResult();
        
        if(Tools.validator(result, mcgResult, notifyBody)) {
        	flowGit.setName(flowGit.getGitProperty().getName());
            CachePlugin.putFlowEntity(flowGit.getFlowId(), flowGit.getId(), flowGit);
            notifyBody.setContent("GIT控件保存成功！");
            notifyBody.setType(LogTypeEnum.SUCCESS.getValue());
        }

        message.setBody(notifyBody);
        MessagePlugin.push(session.getId(), message);
		return mcgResult;
	}
	
	@RequestMapping(value="saveSftp", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public McgResult saveGit(@Valid @RequestBody FlowSftp flowSftp, BindingResult result, HttpSession session) {
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY);
        NotifyBody notifyBody = new NotifyBody();
        McgResult mcgResult = new McgResult();
        
        if(Tools.validator(result, mcgResult, notifyBody)) {
        	flowSftp.setName(flowSftp.getSftpProperty().getName());
            CachePlugin.putFlowEntity(flowSftp.getFlowId(), flowSftp.getId(), flowSftp);
            notifyBody.setContent("sftp控件保存成功！");
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
            CachePlugin.putFlowEntity(flowData.getFlowId(), flowData.getId(), flowData);
            notifyBody.setContent("Data控件保存成功！");
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
            CachePlugin.putFlowEntity(flowEnd.getFlowId(), flowEnd.getEndId(), flowEnd);
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
        if(flowService.testConnect(mcgDataSource)) {
        	mcgResult.setStatusCode(1);
        	mcgResult.setStatusMes("执行成功");
        } else {
        	mcgResult.setStatusCode(0);
        	mcgResult.setStatusMes("执行失败");        	
        }
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
            McgGlobal mcgGlobal = (McgGlobal)dbService.query(Constants.GLOBAL_KEY, McgGlobal.class);
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

    @RequestMapping(value="stop", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public McgResult stop(String flowId, HttpSession session) {
    	McgResult mcgResult = new McgResult();
    	try {
    		
    		String flowInstanceId = Tools.genFlowInstanceId(session.getId(), flowId);
	    	ExecuteStruct executeStruct = FlowInstancesUtils.executeStructMap.get(flowInstanceId);
	    	if(executeStruct != null && executeStruct.getRunStatus() != null) {
	    		executeStruct.getRunStatus().setInterrupt(true);
	    	}
	    	stopFlow(executeStruct.getChildExecuteStruct());
	    	
	    	Thread.sleep(2000L);
	    	
	    	if(executeStruct.getFlowTaskFutureList().size() > 0 ) {
		    	for(int i=executeStruct.getFlowTaskFutureList().size() -1 ; i>=0; i--) {
		    		Future<RunStatus> flowFuture = executeStruct.getFlowTaskFutureList().get(i);
		    		if(!flowFuture.isDone() ) {
		    			flowFuture.cancel(true);
		    		}
		    	}
	    	}

    	} catch (Exception e) {
    		mcgResult.setStatusCode(0);
		}
    	return mcgResult;
    }
    
    private boolean stopFlow(ExecuteStruct executeStruct) {
    	if(executeStruct != null && executeStruct.getRunStatus() != null) {
    		executeStruct.getRunStatus().setInterrupt(true);
    		logger.debug("正在中断子流程：{}", executeStruct.getTopology().getName());    
    	} else {
    		logger.debug("中断所有子流程完毕！");    		
    		return false;
    	}
    	return stopFlow(executeStruct.getChildExecuteStruct());
    }
    
    @RequestMapping(value="generate", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public McgResult generate(@Valid @RequestBody WebStruct webStruct, BindingResult result, HttpSession session) throws ClassNotFoundException, IOException, InterruptedException, ExecutionException {

        McgResult mcgResult = new McgResult();
        NotifyBody notifyBody = new NotifyBody();
        
        if(Tools.validator(result, mcgResult, notifyBody)) {
            if(flowService.saveFlow(webStruct, session) ) {
                flowService.generate(webStruct, session, false, null, null);
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
        dbService.delete(flowId);
    	notifyBody.setContent("清空流程成功！");
    	notifyBody.setType(LogTypeEnum.SUCCESS.getValue());
        messageComplete.setBody(notifyBody);
        MessagePlugin.push(session.getId(), messageComplete);
        
    	return mcgResult;
    }
}