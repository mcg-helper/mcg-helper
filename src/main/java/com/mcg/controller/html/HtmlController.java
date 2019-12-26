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

package com.mcg.controller.html;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mcg.common.sysenum.FlowLinuxConnModeEnum;
import com.mcg.common.sysenum.FlowLoopTypeEnum;
import com.mcg.common.sysenum.FlowTextOutModeEnum;
import com.mcg.controller.base.BaseController;
import com.mcg.service.FlowService;
import com.mcg.service.GlobalService;
import com.mcg.service.WontonService;
import com.mcg.util.PageData;

/**
 * 
 * @ClassName:   HtmlController   
 * @Description: TODO(工作台中所有拖拽控件功能服务) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午4:04:29  
 *
 */
@Controller
@RequestMapping(value="/html")
public class HtmlController extends BaseController {

    @Autowired
    private FlowService flowService;
    @Autowired
    private GlobalService globalService;   
    @Autowired
    private WontonService wontonService;
    
	/* 流程节点悬浮工具层Modal */
	@RequestMapping(value="/flowSuspension")
	public ModelAndView getFlowElementSuspensionModal() throws Exception{
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("html/flowSuspension");
		return mv;
	}

	//acquire connector tool bar html
	@RequestMapping(value="/flowConnectorSuspen")
	public ModelAndView getFlowConnectorSuspensionModal() throws Exception{
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("html/flowConnectorSuspen");
		return mv;
	}

	@RequestMapping(value = "/Connector")
	public ModelAndView getConnectorModel()throws  Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("modalId",pd.get("modalId"));
		mv.setViewName("html/Connetor");
		return mv;
	}

	@RequestMapping(value = "/TextOutput")
	public ModelAndView getOnputHtml()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("modalId",pd.getString("modalId"));
		mv.setViewName("html/TextOutput");
		return mv;
	}
	
    /* 流程节点 开始_Modal */
    @RequestMapping(value="/flowStartModal")
    public ModelAndView getFlowStartModal() throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        mv.addObject("modalId", pd.get("modalId"));
        mv.setViewName("html/flowStartModal");
        return mv;
    }	
	
	/* 流程节点 Model_Modal */
	@RequestMapping(value="/flowModelModal")
	public ModelAndView getFlowElementModelModal() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("modalId", pd.get("modalId"));
		mv.setViewName("html/flowModelModal");
		return mv;
	}
	
	/* 流程节点 Json_Modal */
	@RequestMapping(value="/flowJsonModal")
	public ModelAndView getFlowJsonModal() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("modalId", pd.get("modalId"));
		mv.setViewName("html/flowJsonModal");
		return mv;
	}	
	
	/* 流程节点Data_Modal */
	@RequestMapping(value="/flowDataModal")
	public ModelAndView getFlowDataModal() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("modalId", pd.get("modalId"));
		mv.addObject("mcgDataSources", flowService.getMcgDataSources());
		mv.setViewName("html/flowDataModal");
		return mv;
	}
    
	/* 流程节点 Text_Modal */
	@RequestMapping(value="/flowTextModal")
	public ModelAndView getFlowTextModal() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("modalId", pd.get("modalId"));
		mv.addObject("outModes", FlowTextOutModeEnum.values());
		mv.setViewName("html/flowTextModal");
		return mv;
	}
	
	/* 流程节点 Java_Modal */
	@RequestMapping(value="/flowJavaModal")
	public ModelAndView getFlowJavaModal() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("modalId", pd.get("modalId"));
		mv.setViewName("html/flowJavaModal");
		return mv;
	}	
	
	/* 流程节点 Python_Modal */
	@RequestMapping(value="/flowPythonModal")
	public ModelAndView getFlowPythonModal() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("modalId", pd.get("modalId"));
		mv.setViewName("html/flowPythonModal");
		return mv;
	}
	
	/* 流程节点 Linux_Modal */
	@RequestMapping(value="/flowLinuxModal")
	public ModelAndView getFlowLinuxModal() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("modalId", pd.get("modalId"));
		mv.addObject("connMode", FlowLinuxConnModeEnum.values());
		mv.addObject("serverSources", globalService.getServerSources());		
		
		mv.setViewName("html/flowLinuxModal");
		return mv;
	}	
	
	/* 流程节点 Wonton_Modal */
	@RequestMapping(value="/flowWontonModal")
	public ModelAndView getFlowWontonModal() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("modalId", pd.get("modalId"));
		mv.addObject("wontons", wontonService.getAll());
		mv.setViewName("html/flowWontonModal");
		return mv;
	}	
	
    /* 流程节点 Java_Modal */
    @RequestMapping(value="/flowSqlQueryModal")
    public ModelAndView getFlowSqlQueryModal() throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        mv.addObject("mcgDataSources", flowService.getMcgDataSources());
        mv.addObject("modalId", pd.get("modalId"));
        mv.setViewName("html/flowSqlQueryModal");
        return mv;
    }
    
    /* 流程节点 Java_Modal */
    @RequestMapping(value="/flowSqlExecuteModal")
    public ModelAndView getFlowSqlExecuteModal() throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        mv.addObject("mcgDataSources", flowService.getMcgDataSources());
        mv.addObject("modalId", pd.get("modalId"));
        mv.setViewName("html/flowSqlExecuteModal");
        return mv;
    }
    
	/* 流程节点 DataSource_Modal */
	@RequestMapping(value="/flowDataSourceModal")
	public ModelAndView getDataSourceModal() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		
		mv.addObject("modalId", pd.get("modalId"));
		mv.setViewName("html/flowDataSourceModal");
		return mv;
	}
	
	/* 流程节点 Script_Modal */
	@RequestMapping(value="/flowScriptModal")
	public ModelAndView getFlowScriptModal() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("modalId", pd.get("modalId"));
		mv.setViewName("html/flowScriptModal");
		return mv;
	}	
	
	/* 流程节点 Process_Modal */
	@RequestMapping(value="/flowProcessModal")
	public ModelAndView getFlowProcessModal() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("modalId", pd.get("modalId"));
		mv.setViewName("html/flowProcessModal");
		return mv;
	}
	
	/* 流程节点 Loop_Modal */
	@RequestMapping(value="/flowLoopModal")
	public ModelAndView getFlowLoopModal() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("modalId", pd.get("modalId"));
		mv.addObject("loopTypes", FlowLoopTypeEnum.values());
		
		mv.setViewName("html/flowLoopModal");
		return mv;
	}
	
	/* 流程节点 Text_Modal */
	@RequestMapping(value="/flowEndModal")
	public ModelAndView getFlowEndModal() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("modalId", pd.get("modalId"));
		mv.setViewName("html/flowEndModal");
		return mv;
	}
	

}