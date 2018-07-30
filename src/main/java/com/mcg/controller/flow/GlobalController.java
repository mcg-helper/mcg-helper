package com.mcg.controller.flow;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mcg.common.Constants;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.common.McgResult;
import com.mcg.entity.global.McgGlobal;
import com.mcg.entity.global.serversource.ServerSource;
import com.mcg.entity.message.Message;
import com.mcg.entity.message.NotifyBody;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.service.GlobalService;
import com.mcg.util.LevelDbUtil;
import com.mcg.util.Tools;

/**
 * 
 * @ClassName:   GlobalController   
 * @Description: TODO(系统工作台功能服务) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年7月28日 下午22:57:38  
 *
 */
@Controller
@RequestMapping(value="/global")
public class GlobalController {

	@Autowired
	private GlobalService globalService;
	
    @RequestMapping(value="saveDataSource", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public McgResult saveDataSource(@Valid @RequestBody McgGlobal mcgGlobal, BindingResult result, HttpSession session) throws IOException, ClassNotFoundException {
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.NOTIFY);     
        NotifyBody notifyBody = new NotifyBody();       
        McgResult mcgResult = new McgResult();
        
        if(Tools.validator(result, mcgResult, notifyBody)) {
            McgGlobal lastMcgGlobal = (McgGlobal)LevelDbUtil.getObject(Constants.GLOBAL_KEY, McgGlobal.class);
            lastMcgGlobal.setFlowDataSources(mcgGlobal.getFlowDataSources());
            lastMcgGlobal.setServerSources(mcgGlobal.getServerSources());
            globalService.updateGlobal(lastMcgGlobal);
            notifyBody.setContent("数据源控件保存成功！");
            notifyBody.setType(LogTypeEnum.SUCCESS.getValue());            
        }

        message.setBody(notifyBody);
        MessagePlugin.push(session.getId(), message);        
        return mcgResult;
    }	
    
    @RequestMapping(value="/getServerSources")
    @ResponseBody
    public List<ServerSource> getServerSources() throws ClassNotFoundException, IOException {
    	return globalService.getServerSources();
    }   
    
    @RequestMapping(value="/getMcgGlobal")
    @ResponseBody
    public McgGlobal getMcgGlobal() throws ClassNotFoundException, IOException {
    	return globalService.getMcgGlobal();
    }
    
    @RequestMapping(value="testServerConnect", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public McgResult testServerConnect(@RequestBody ServerSource serverSource) {
        
        McgResult mcgResult = new McgResult();
        mcgResult.setStatusCode(globalService.isConnected(serverSource) ? 1 : 0);
        return mcgResult;
    }
}
