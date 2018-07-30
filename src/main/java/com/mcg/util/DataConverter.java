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

package com.mcg.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.collections.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcg.common.Constants;
import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.entity.flow.FlowBase;
import com.mcg.entity.flow.FlowStruct;
import com.mcg.entity.flow.data.FlowData;
import com.mcg.entity.flow.data.FlowDatas;
import com.mcg.entity.flow.end.FlowEnd;
import com.mcg.entity.flow.gmybatis.FlowGmybatis;
import com.mcg.entity.flow.gmybatis.FlowGmybatises;
import com.mcg.entity.flow.java.FlowJava;
import com.mcg.entity.flow.java.FlowJavas;
import com.mcg.entity.flow.json.FlowJson;
import com.mcg.entity.flow.json.FlowJsons;
import com.mcg.entity.flow.linux.FlowLinux;
import com.mcg.entity.flow.linux.FlowLinuxs;
import com.mcg.entity.flow.model.FlowModel;
import com.mcg.entity.flow.model.FlowModels;
import com.mcg.entity.flow.python.FlowPython;
import com.mcg.entity.flow.python.FlowPythons;
import com.mcg.entity.flow.script.FlowScript;
import com.mcg.entity.flow.script.FlowScripts;
import com.mcg.entity.flow.sequence.FlowSequence;
import com.mcg.entity.flow.sequence.FlowSequences;
import com.mcg.entity.flow.sqlexecute.FlowSqlExecute;
import com.mcg.entity.flow.sqlexecute.FlowSqlExecutes;
import com.mcg.entity.flow.sqlquery.FlowSqlQuery;
import com.mcg.entity.flow.sqlquery.FlowSqlQuerys;
import com.mcg.entity.flow.start.FlowStart;
import com.mcg.entity.flow.text.FlowText;
import com.mcg.entity.flow.text.FlowTexts;
import com.mcg.entity.flow.text.TextCore;
import com.mcg.entity.flow.web.WebConnector;
import com.mcg.entity.flow.web.WebElement;
import com.mcg.entity.flow.web.WebStruct;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.Order;
import com.mcg.entity.generate.RunResult;
import com.mcg.entity.global.McgGlobal;
import com.mcg.plugin.ehcache.CachePlugin;
import com.mcg.plugin.tplengine.FreeMakerTpLan;
import com.mcg.plugin.tplengine.TplEngine;

/**
 * 
 * @ClassName:   DataConverter   
 * @Description: TODO(数据转换服务) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午5:34:33  
 *
 */
public class DataConverter {
	
	public static FlowStruct xmlToflowStruct(String flowXml) {
		FlowStruct flowStruct = null;
		if(flowXml != null && !"".equals(flowXml)) {
	        try {  
	            JAXBContext context = JAXBContext.newInstance(FlowStruct.class);  
	            Unmarshaller unmarshaller = context.createUnmarshaller();  
	            flowStruct = (FlowStruct)unmarshaller.unmarshal(new StringReader(flowXml));  
	        } catch (JAXBException e) {  
	            e.printStackTrace();  
	        }		
		}
		return flowStruct;
	}	
	
    public static WebStruct flowStructToWebStruct(String flowId) throws ClassNotFoundException, IOException {

    	FlowStruct flowStruct = (FlowStruct)LevelDbUtil.getObject(flowId, FlowStruct.class);
    	if(flowStruct == null) {
    		return null;
    	}
        WebStruct webStruct = new WebStruct();
        List<WebElement> webElementList = new ArrayList<WebElement>();
        List<WebConnector> webConnectorList = new ArrayList<WebConnector>();
        if(flowStruct != null) {
            webStruct.setFlowId(flowStruct.getMcgId());
            FlowStart flowStart = flowStruct.getFlowStart();
            if(flowStart != null) {
                WebElement webElement = new WebElement();
                FlowBase flowBase = flowStart;
                flowBase.setName("流程开始");
                webElement = setValue(flowBase, webElement);
                webElement.setId(flowStart.getStartId());
                webElementList.add(webElement);
                CachePlugin.put(flowStart.getStartId(), flowStart);
            }
            if(flowStruct.getFlowModels() != null && flowStruct.getFlowModels().getFlowModel() != null && flowStruct.getFlowModels().getFlowModel().size() >0) {
                List<FlowModel> flowModelList = flowStruct.getFlowModels().getFlowModel();
                for(FlowModel flowModel : flowModelList) {
                    WebElement webElement = new WebElement();
                    FlowBase flowBase = flowModel;
                    flowBase.setName(flowModel.getModelProperty().getModelName());
                    webElement = setValue(flowBase, webElement);
                    webElement.setId(flowModel.getModelId());
                    webElementList.add(webElement);
                    CachePlugin.put(flowModel.getModelId() , flowModel);
                }
            }
            if(flowStruct.getFlowJsons() != null && flowStruct.getFlowJsons().getFlowJson() != null && flowStruct.getFlowJsons().getFlowJson().size() >0) {
                List<FlowJson> flowJsonList = flowStruct.getFlowJsons().getFlowJson();
                for(FlowJson flowJson : flowJsonList) {
                    WebElement webElement = new WebElement();
                    FlowBase flowBase = flowJson;
                    flowBase.setName(flowJson.getJsonProperty().getName());
                    webElement = setValue(flowBase, webElement);
                    webElement.setId(flowJson.getId());
                    webElementList.add(webElement);
                    CachePlugin.put(flowJson.getId() , flowJson);
                }
            }
            if(flowStruct.getFlowSqlExecutes() != null && flowStruct.getFlowSqlExecutes().getFlowSqlExecute() != null && flowStruct.getFlowSqlExecutes().getFlowSqlExecute().size() >0) {
                List<FlowSqlExecute> flowSqlExecuteList = flowStruct.getFlowSqlExecutes().getFlowSqlExecute();
                for(FlowSqlExecute flowSqlExecute : flowSqlExecuteList) {
                    WebElement webElement = new WebElement();
                    FlowBase flowBase = flowSqlExecute;
                    flowBase.setName(flowSqlExecute.getSqlExecuteProperty().getName());
                    webElement = setValue(flowBase, webElement);
                    webElement.setId(flowSqlExecute.getId());
                    webElementList.add(webElement);
                    CachePlugin.put(flowSqlExecute.getId() , flowSqlExecute);
                }
            }            
            
            if(flowStruct.getFlowSqlQuerys() != null && flowStruct.getFlowSqlQuerys().getFlowSqlQuery() != null && flowStruct.getFlowSqlQuerys().getFlowSqlQuery().size() >0) {
                List<FlowSqlQuery> flowSqlQueryList = flowStruct.getFlowSqlQuerys().getFlowSqlQuery();
                for(FlowSqlQuery flowSqlQuery : flowSqlQueryList) {
                    WebElement webElement = new WebElement();
                    FlowBase flowBase = flowSqlQuery;
                    flowBase.setName(flowSqlQuery.getSqlQueryProperty().getName());
                    webElement = setValue(flowBase, webElement);
                    webElement.setId(flowSqlQuery.getId());
                    webElementList.add(webElement);
                    CachePlugin.put(flowSqlQuery.getId() , flowSqlQuery);
                }
            }             
            if(flowStruct.getFlowGmybatises() != null && flowStruct.getFlowGmybatises().getFlowGmybatis() != null && flowStruct.getFlowGmybatises().getFlowGmybatis().size() > 0) {
                List<FlowGmybatis> flowGmybatisList = flowStruct.getFlowGmybatises().getFlowGmybatis();
                for(FlowGmybatis flowGmybatis : flowGmybatisList) {
                    WebElement webElement = new WebElement();
                    FlowBase flowBase = flowGmybatis;
                    flowBase.setName(flowGmybatis.getGmybatisProperty().getName());
                    webElement = setValue(flowBase, webElement);
                    webElement.setId(flowGmybatis.getGmybatisId());
                    webElementList.add(webElement);  
                    CachePlugin.put(flowGmybatis.getGmybatisId() , flowGmybatis);
                }
            }
            if(flowStruct.getFlowDatas() != null && flowStruct.getFlowDatas().getFlowData() != null && flowStruct.getFlowDatas().getFlowData().size() > 0) {
                List<FlowData> flowDataList = flowStruct.getFlowDatas().getFlowData();
                for(FlowData flowData : flowDataList) {
                    WebElement webElement = new WebElement();
                    FlowBase flowBase = flowData;
                    flowBase.setName(flowData.getDataProperty().getName());
                    webElement = setValue(flowBase, webElement);
                    webElement.setId(flowData.getId());
                    webElementList.add(webElement);  
                    CachePlugin.put(flowData.getId() , flowData);
                }
            }            
            if(flowStruct.getFlowScripts() != null && flowStruct.getFlowScripts().getFlowScript() != null && flowStruct.getFlowScripts().getFlowScript().size() > 0) {
                List<FlowScript> flowScriptList = flowStruct.getFlowScripts().getFlowScript();
                for(FlowScript flowScript : flowScriptList) {
                    WebElement webElement = new WebElement();
                    FlowBase flowBase = flowScript;
                    flowBase.setName(flowScript.getScriptProperty().getScriptName());
                    webElement = setValue(flowBase, webElement);
                    webElement.setId(flowScript.getScriptId());
                    webElementList.add(webElement); 
                    CachePlugin.put(flowScript.getScriptId(), flowScript);
                }
            }
            if(flowStruct.getFlowJavas() != null && flowStruct.getFlowJavas().getFlowJava() != null && flowStruct.getFlowJavas().getFlowJava().size() > 0) {
                List<FlowJava> flowJavaList = flowStruct.getFlowJavas().getFlowJava();
                for(FlowJava flowJava : flowJavaList) {
                    WebElement webElement = new WebElement();
                    FlowBase flowBase = flowJava;
                    flowBase.setName(flowJava.getJavaProperty().getName());
                    webElement = setValue(flowBase, webElement);
                    webElement.setId(flowJava.getId());
                    webElementList.add(webElement); 
                    CachePlugin.put(flowJava.getId(), flowJava);
                }
            }
            if(flowStruct.getFlowPythons() != null && flowStruct.getFlowPythons().getFlowPython() != null && flowStruct.getFlowPythons().getFlowPython().size() > 0) {
                List<FlowPython> flowPythonList = flowStruct.getFlowPythons().getFlowPython();
                for(FlowPython flowPython : flowPythonList) {
                    WebElement webElement = new WebElement();
                    FlowBase flowBase = flowPython;
                    flowBase.setName(flowPython.getPythonProperty().getName());
                    webElement = setValue(flowBase, webElement);
                    webElement.setId(flowPython.getId());
                    webElementList.add(webElement); 
                    CachePlugin.put(flowPython.getId(), flowPython);
                }
            }
            if(flowStruct.getFlowLinuxs() != null && flowStruct.getFlowLinuxs().getFlowLinux() != null && flowStruct.getFlowLinuxs().getFlowLinux().size() > 0) {
                List<FlowLinux> flowLinuxList = flowStruct.getFlowLinuxs().getFlowLinux();
                for(FlowLinux flowLinux : flowLinuxList) {
                    WebElement webElement = new WebElement();
                    FlowBase flowBase = flowLinux;
                    flowBase.setName(flowLinux.getLinuxProperty().getName());
                    webElement = setValue(flowBase, webElement);
                    webElement.setId(flowLinux.getId());
                    webElementList.add(webElement); 
                    CachePlugin.put(flowLinux.getId(), flowLinux);
                }
            }
            if(flowStruct.getFlowTexts() != null && flowStruct.getFlowTexts().getFlowText() != null && flowStruct.getFlowTexts().getFlowText().size() > 0) {
                List<FlowText> flowTextList = flowStruct.getFlowTexts().getFlowText();
                for(FlowText flowText : flowTextList) {
                    WebElement webElement = new WebElement();
                    FlowBase flowBase = flowText;
                    flowBase.setName(flowText.getTextProperty().getName());
                    webElement = setValue(flowBase, webElement);
                    webElement.setId(flowText.getTextId());
                    webElementList.add(webElement);
                    CachePlugin.put(flowText.getTextId(), flowText);
                }
            }
            if(flowStruct.getFlowEnd() != null) {
                WebElement webElement = new WebElement();
                FlowEnd flowEnd = flowStruct.getFlowEnd();
                FlowBase flowBase = flowEnd;
                flowBase.setName("流程结束");
                webElement = setValue(flowBase, webElement);
                webElement.setId(flowEnd.getEndId());
                webElementList.add(webElement);
                CachePlugin.put(flowEnd.getEndId(), flowEnd);
            }
            
            webStruct.setWebElement(webElementList);
            
            if(flowStruct.getFlowSequences() != null && flowStruct.getFlowSequences().getFlowSequences().size() > 0) {
                List<FlowSequence> flowSequenceList = flowStruct.getFlowSequences().getFlowSequences();
                for(FlowSequence flowSequence : flowSequenceList) {
                    WebConnector webConnector = new WebConnector();
                    webConnector.setConnectorId(flowSequence.getSourceId()+flowSequence.getTargetId());
                    webConnector.setSourceId(flowSequence.getSourceId());
                    webConnector.setTargetId(flowSequence.getTargetId());
                    webConnectorList.add(webConnector);
                }
                webStruct.setWebConnector(webConnectorList);
            }
        }
        return webStruct;
    }
    
    public static WebElement setValue(FlowBase flowBase, WebElement webElement) {
        webElement.setClassname(flowBase.getClassname());
        webElement.setClone(flowBase.getClone());
        webElement.setEletype(flowBase.getEletype());
        webElement.setHeight(flowBase.getHeight());
        webElement.setLeft(flowBase.getLeft());
        webElement.setTop(flowBase.getTop());
        webElement.setWidth(flowBase.getWidth()); 
        webElement.setLabel(flowBase.getLabel());
        webElement.setName(flowBase.getName());
        webElement.setSign(flowBase.getSign());
        return webElement;
    }
    
    public static FlowStruct webStructToflowStruct(WebStruct webStruct) {
        FlowStruct flowStruct = null;
        if(webStruct != null && webStruct.getWebElement() != null && webStruct.getWebElement().size() > 0 && webStruct.getWebConnector() != null && webStruct.getWebConnector().size() > 0) {
            flowStruct = new FlowStruct();
            FlowModels flowModels = new FlowModels();
            List<FlowModel> flowModelList = new ArrayList<FlowModel>();
            FlowJsons flowJsons = new FlowJsons();
            List<FlowJson> flowJsonList = new ArrayList<FlowJson>();     
            FlowSqlQuerys flowSqlQuerys = new FlowSqlQuerys();
            List<FlowSqlQuery> flowSqlQueryList = new ArrayList<FlowSqlQuery>();
            FlowSqlExecutes flowSqlExecutes = new FlowSqlExecutes();
            List<FlowSqlExecute> flowSqlExecuteList = new ArrayList<FlowSqlExecute>();             
            FlowTexts flowTexts = new FlowTexts();
            List<FlowText> flowTextList = new ArrayList<FlowText>();
            FlowGmybatises flowGmybatises = new FlowGmybatises();
            List<FlowGmybatis> flowGmybatisList = new ArrayList<FlowGmybatis>();
            FlowDatas flowDatas = new FlowDatas();
            List<FlowData> flowDataList = new ArrayList<FlowData>();            
            FlowScripts flowScripts = new FlowScripts();
            List<FlowScript> flowScriptList = new ArrayList<FlowScript>();
            FlowJavas flowJavas = new FlowJavas();
            List<FlowJava> flowJavaList = new ArrayList<FlowJava>();            
            FlowPythons flowPythons = new FlowPythons();
            List<FlowPython> flowPythonList = new ArrayList<FlowPython>();   
            FlowLinuxs flowLinuxs = new FlowLinuxs();
            List<FlowLinux> flowLinuxList = new ArrayList<FlowLinux>();            
            
            List<WebElement> webElementList = webStruct.getWebElement();
            for(WebElement webElement : webElementList) {
                Object obj = CachePlugin.get(webElement.getId());
                if(webElement.getEletype().equals(EletypeEnum.START.getValue())) {
                    FlowStart flowStart = (FlowStart)obj;
                    flowStart.setLabel(webElement.getLabel());
                    flowStart.setWidth(webElement.getWidth());
                    flowStart.setHeight(webElement.getHeight());
                    flowStart.setClassname(webElement.getClassname());
                    flowStart.setEletype(webElement.getEletype());
                    flowStart.setClone(webElement.getClone());
                    flowStart.setLeft(webElement.getLeft());
                    flowStart.setTop(webElement.getTop());
                    flowStart.setSign(webElement.getSign());
                    flowStruct.setFlowStart(flowStart);
                } else if(webElement.getEletype().equals(EletypeEnum.MODEL.getValue())) {
                    FlowModel flowModel = (FlowModel)obj;
                    flowModel.setLabel(webElement.getLabel());
                    flowModel.setWidth(webElement.getWidth());
                    flowModel.setHeight(webElement.getHeight());
                    flowModel.setClassname(webElement.getClassname());
                    flowModel.setEletype(webElement.getEletype());
                    flowModel.setClone(webElement.getClone());
                    flowModel.setLeft(webElement.getLeft());
                    flowModel.setTop(webElement.getTop());
                    flowModel.setSign(webElement.getSign());
                    flowModelList.add(flowModel);
                } else if(webElement.getEletype().equals(EletypeEnum.JSON.getValue())) {
                    FlowJson flowJson = (FlowJson)obj;
                    flowJson.setLabel(webElement.getLabel());
                    flowJson.setWidth(webElement.getWidth());
                    flowJson.setHeight(webElement.getHeight());
                    flowJson.setClassname(webElement.getClassname());
                    flowJson.setEletype(webElement.getEletype());
                    flowJson.setClone(webElement.getClone());
                    flowJson.setLeft(webElement.getLeft());
                    flowJson.setTop(webElement.getTop());
                    flowJson.setSign(webElement.getSign());
                    flowJsonList.add(flowJson);
                } else if(webElement.getEletype().equals(EletypeEnum.SQLQUERY.getValue())) {
                    FlowSqlQuery flowSqlQuery = (FlowSqlQuery)obj;
                    flowSqlQuery.setLabel(webElement.getLabel());
                    flowSqlQuery.setWidth(webElement.getWidth());
                    flowSqlQuery.setHeight(webElement.getHeight());
                    flowSqlQuery.setClassname(webElement.getClassname());
                    flowSqlQuery.setEletype(webElement.getEletype());
                    flowSqlQuery.setClone(webElement.getClone());
                    flowSqlQuery.setLeft(webElement.getLeft());
                    flowSqlQuery.setTop(webElement.getTop());
                    flowSqlQuery.setSign(webElement.getSign());
                    flowSqlQueryList.add(flowSqlQuery);
                } else if(webElement.getEletype().equals(EletypeEnum.SQLEXECUTE.getValue())) {
                    FlowSqlExecute flowSqlExecute = (FlowSqlExecute)obj;
                    flowSqlExecute.setLabel(webElement.getLabel());
                    flowSqlExecute.setWidth(webElement.getWidth());
                    flowSqlExecute.setHeight(webElement.getHeight());
                    flowSqlExecute.setClassname(webElement.getClassname());
                    flowSqlExecute.setEletype(webElement.getEletype());
                    flowSqlExecute.setClone(webElement.getClone());
                    flowSqlExecute.setLeft(webElement.getLeft());
                    flowSqlExecute.setTop(webElement.getTop());
                    flowSqlExecute.setSign(webElement.getSign());
                    flowSqlExecuteList.add(flowSqlExecute);
                } else if(webElement.getEletype().equals(EletypeEnum.TEXT.getValue())) {
                	FlowText flowText = (FlowText)obj;
                	flowText.setLabel(webElement.getLabel());
                	flowText.setWidth(webElement.getWidth());
                	flowText.setHeight(webElement.getHeight());
                	flowText.setClassname(webElement.getClassname());
                	flowText.setEletype(webElement.getEletype());
                	flowText.setClone(webElement.getClone());
                	flowText.setLeft(webElement.getLeft());
                	flowText.setTop(webElement.getTop());
                	flowText.setSign(webElement.getSign());
                	flowTextList.add(flowText);
                } else if(webElement.getEletype().equals(EletypeEnum.GMYBATIS.getValue())) {
                	FlowGmybatis flowGmybatis = (FlowGmybatis)obj;
                	flowGmybatis.setLabel(webElement.getLabel());
                	flowGmybatis.setWidth(webElement.getWidth());
                	flowGmybatis.setHeight(webElement.getHeight());
                	flowGmybatis.setClassname(webElement.getClassname());
                	flowGmybatis.setEletype(webElement.getEletype());
                	flowGmybatis.setClone(webElement.getClone());
                	flowGmybatis.setLeft(webElement.getLeft());
                	flowGmybatis.setTop(webElement.getTop());
                	flowGmybatis.setSign(webElement.getSign());
                	flowGmybatisList.add(flowGmybatis);
                } else if(webElement.getEletype().equals(EletypeEnum.DATA.getValue())) {
                	FlowData flowData = (FlowData)obj;
                	flowData.setLabel(webElement.getLabel());
                	flowData.setWidth(webElement.getWidth());
                	flowData.setHeight(webElement.getHeight());
                	flowData.setClassname(webElement.getClassname());
                	flowData.setEletype(webElement.getEletype());
                	flowData.setClone(webElement.getClone());
                	flowData.setLeft(webElement.getLeft());
                	flowData.setTop(webElement.getTop());
                	flowData.setSign(webElement.getSign()); 
                	flowDataList.add(flowData);
                } else if(webElement.getEletype().equals(EletypeEnum.SCRIPT.getValue())) {
                	FlowScript flowScript = (FlowScript)obj;
                	flowScript.setLabel(webElement.getLabel());
                	flowScript.setWidth(webElement.getWidth());
                	flowScript.setHeight(webElement.getHeight());
                	flowScript.setClassname(webElement.getClassname());
                	flowScript.setEletype(webElement.getEletype());
                	flowScript.setClone(webElement.getClone());
                	flowScript.setLeft(webElement.getLeft());
                	flowScript.setTop(webElement.getTop());
                	flowScript.setSign(webElement.getSign()); 
                	flowScriptList.add(flowScript);
                } else if(webElement.getEletype().equals(EletypeEnum.JAVA.getValue())) {
                	FlowJava flowJava = (FlowJava)obj;
                	flowJava.setLabel(webElement.getLabel());
                	flowJava.setWidth(webElement.getWidth());
                	flowJava.setHeight(webElement.getHeight());
                	flowJava.setClassname(webElement.getClassname());
                	flowJava.setEletype(webElement.getEletype());
                	flowJava.setClone(webElement.getClone());
                	flowJava.setLeft(webElement.getLeft());
                	flowJava.setTop(webElement.getTop());
                	flowJava.setSign(webElement.getSign());
                	flowJavaList.add(flowJava);
                } else if(webElement.getEletype().equals(EletypeEnum.PYTHON.getValue())) {
                	FlowPython flowPython = (FlowPython)obj;
                	flowPython.setLabel(webElement.getLabel());
                	flowPython.setWidth(webElement.getWidth());
                	flowPython.setHeight(webElement.getHeight());
                	flowPython.setClassname(webElement.getClassname());
                	flowPython.setEletype(webElement.getEletype());
                	flowPython.setClone(webElement.getClone());
                	flowPython.setLeft(webElement.getLeft());
                	flowPython.setTop(webElement.getTop());
                	flowPython.setSign(webElement.getSign());
                	flowPythonList.add(flowPython);
                } else if(webElement.getEletype().equals(EletypeEnum.LINUX.getValue())) {
                	FlowLinux flowLinux = (FlowLinux)obj;
                	flowLinux.setLabel(webElement.getLabel());
                	flowLinux.setWidth(webElement.getWidth());
                	flowLinux.setHeight(webElement.getHeight());
                	flowLinux.setClassname(webElement.getClassname());
                	flowLinux.setEletype(webElement.getEletype());
                	flowLinux.setClone(webElement.getClone());
                	flowLinux.setLeft(webElement.getLeft());
                	flowLinux.setTop(webElement.getTop());
                	flowLinux.setSign(webElement.getSign());
                	flowLinuxList.add(flowLinux);
                } else if(webElement.getEletype().equals(EletypeEnum.END.getValue())) {
                    FlowEnd flowEnd = (FlowEnd)obj;
                    flowEnd.setLabel(webElement.getLabel());
                    flowEnd.setWidth(webElement.getWidth());
                    flowEnd.setHeight(webElement.getHeight());
                    flowEnd.setClassname(webElement.getClassname());
                    flowEnd.setEletype(webElement.getEletype());
                    flowEnd.setClone(webElement.getClone());
                    flowEnd.setLeft(webElement.getLeft());
                    flowEnd.setTop(webElement.getTop());
                    flowEnd.setSign(webElement.getSign());
                    flowStruct.setFlowEnd(flowEnd);
                } 
            }
            
            flowModels.setFlowModel(flowModelList);
            flowStruct.setFlowModels(flowModels);
            flowJsons.setFlowJson(flowJsonList);
            flowStruct.setFlowJsons(flowJsons);   
            flowSqlQuerys.setFlowSqlQuery(flowSqlQueryList);
            flowStruct.setFlowSqlQuerys(flowSqlQuerys);
            flowSqlExecutes.setFlowSqlExecute(flowSqlExecuteList);
            flowStruct.setFlowSqlExecutes(flowSqlExecutes);            
            flowGmybatises.setFlowGmybatis(flowGmybatisList);
            flowStruct.setFlowGmybatises(flowGmybatises);
            flowDatas.setFlowData(flowDataList);
            flowStruct.setFlowDatas(flowDatas);
            flowTexts.setFlowText(flowTextList);
            flowStruct.setFlowTexts(flowTexts);
            flowScripts.setFlowScript(flowScriptList);
            flowStruct.setFlowScripts(flowScripts);
            flowJavas.setFlowJava(flowJavaList);
            flowStruct.setFlowJavas(flowJavas);
            flowPythons.setFlowPython(flowPythonList);
            flowStruct.setFlowPythons(flowPythons);  
            flowLinuxs.setFlowLinux(flowLinuxList);
            flowStruct.setFlowLinuxs(flowLinuxs);             
            
            flowStruct.setTotalSize(webElementList.size());
            
            FlowSequences flowSequences = new FlowSequences();
            List<FlowSequence> flowSequenceList= new ArrayList<FlowSequence>();
            List<WebConnector> webConnectorList = webStruct.getWebConnector();
            for(WebConnector webConnector : webConnectorList) {
                FlowSequence flowSequence = new FlowSequence();
                flowSequence.setSequenceId(webConnector.getConnectorId());
                flowSequence.setSourceId(webConnector.getSourceId());
                flowSequence.setTargetId(webConnector.getTargetId());
                flowSequenceList.add(flowSequence);
            }
            flowSequences.setFlowSequences(flowSequenceList);
            flowStruct.setFlowSequences(flowSequences);
        }
        
        return flowStruct;
    }
    
    public static String flowStructToXml(FlowStruct flowStruct) {
    	String result = null;
        
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            JAXBContext context = JAXBContext.newInstance(FlowStruct.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);   
      //      m.setProperty(Marshaller.JAXB_ENCODING, "GBK"); //防止文件中文乱码  
            m.marshal(flowStruct, os);
            result = new String(os.toByteArray(), Constants.CHARSET);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    public static String mcgGlobalToXml(McgGlobal mcgGlobal) {
    	String result = null;
      
      try {
          ByteArrayOutputStream os = new ByteArrayOutputStream();
          JAXBContext context = JAXBContext.newInstance(McgGlobal.class);
          Marshaller m = context.createMarshaller();
          m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);   
    //      m.setProperty(Marshaller.JAXB_ENCODING, "GBK"); //防止文件中文乱码  
          m.marshal(mcgGlobal, os);
          result = new String(os.toByteArray(), Constants.CHARSET);
          os.close();
      } catch (JAXBException e) {
          e.printStackTrace();
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
    }
      
      return result;    	
    }
    
	public static McgGlobal xmlToMcgGlobal(String mcgGlobalXml) {
		McgGlobal mcgGlobal = null;
		if(mcgGlobalXml != null && !"".equals(mcgGlobalXml)) {
	        try {  
	            JAXBContext context = JAXBContext.newInstance(McgGlobal.class);  
	            Unmarshaller unmarshaller = context.createUnmarshaller();  
	            mcgGlobal = (McgGlobal)unmarshaller.unmarshal(new StringReader(mcgGlobalXml));  
	        } catch (JAXBException e) {  
	            e.printStackTrace();  
	        }		
		}
		
		return mcgGlobal;
	}

	/**
	 * 
	 * @Title:       flowOjbectRepalceGlobal   
	 * @Description: TODO(流程变量中对引用全局变量（flowStart）的值进行替换)   
	 * @param:       @param t 流程控件对象的实例
	 * @param:       @param executeStruct 执行的
	 * @param:       @return      
	 * @return:      T 流程对象的实例
	 * @throws
	 */
	public static <T> T flowOjbectRepalceGlobal(JSON param, T flowObject) {
	    TextCore textCore = null;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            JAXBContext context = JAXBContext.newInstance(flowObject.getClass());
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            if(flowObject instanceof FlowText) {
                FlowText flowText = (FlowText) flowObject;
                textCore = flowText.getTextCore();
                flowText.setTextCore(null);
            }

            m.marshal(flowObject, os);
            String xml = new String(os.toByteArray(), Constants.CHARSET);
            TplEngine tplEngine = new TplEngine(new FreeMakerTpLan());
            xml = tplEngine.generate(param, xml);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            flowObject = (T) unmarshaller.unmarshal(new StringReader(xml));
            if(flowObject instanceof FlowText) {
                ((FlowText) flowObject).setTextCore(textCore);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flowObject;
	}
	
	/**
	 * 获取所有父级组件的运行值，并指定组件类型，过滤其它的组件运行值
	 * @param elementId 当前组件的id
	 * @param executeStruct 当前已执行的流程的组件
	 * @param typeMap 指定的组件类型（typeMap.put(EletypeEnum.MODEL.getValue(), EletypeEnum.MODEL.getValue());）
	 * @return
	 */
	public static String getParentRunResult(String elementId, ExecuteStruct executeStruct, Map<String, String> typeMap) {
		List<Order> orderList = executeStruct.getOrders().getOrder();
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		
		for(Order order : orderList) {
			if(elementId.equals(order.getElementId())) {
				List<String> ids = order.getPid();
				for(int i=0; i<ids.size(); i++) {
				    if(executeStruct.getOrders().getOrder().get(0).getElementId().equals(ids.get(i))) {
				        continue ;
				    }
				    
					FlowBase flowBase = (FlowBase)executeStruct.getDataMap().get(ids.get(i));					
					if(typeMap.get(flowBase.getEletype()) != null) {}
					sb.append(executeStruct.getRunResultMap().get(ids.get(i)).getJsonVar().substring(1, executeStruct.getRunResultMap().get(ids.get(i)).getJsonVar().length()-1 ));
					if((i+1) != ids.size() ) {
						sb.append(",");
					}						
					
				}
			}
		}
		sb.append("}");		
		return sb.toString();
	}
	
	/**
	 * 
	 * @Title:       addFlowStartRunResult   
	 * @Description: TODO(在JSON参数中融入流程全局变量值（开始控件的运行值）)   
	 * @param:       @param param JSON对像参数
	 * @param:       @param executeStruct 运行状态对象
	 * @param:       @return      
	 * @return:      JSON      
	 * @throws
	 */
	public static JSON addFlowStartRunResult(JSON param, ExecuteStruct executeStruct) {
	    JSONObject newParam = (JSONObject)((JSONObject)param).clone();
        RunResult flowStartRunResult = (RunResult)executeStruct.getRunResultMap().get(executeStruct.getOrders().getOrder().get(0).getElementId());      
        String flowStartValue = flowStartRunResult.getJsonVar();
        JSONObject flowStartJot = JSON.parseObject(flowStartValue);
          
        JSONObject jot = (JSONObject)newParam;
        Set<String> set = flowStartJot.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            jot.put(key, flowStartJot.get(key));
        }
        return jot;
        
	}
	
	/**
	 * 获取所有父级组件的运行值
	 * @param elementId 当前组件的id
	 * @param executeStruct 当前已执行的流程的组件
	 * @return
	 */
	public static JSON getParentRunResult(String elementId, ExecuteStruct executeStruct) {
		List<Order> orderList = executeStruct.getOrders().getOrder();
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		
		for(Order order : orderList) {
			if(elementId.equals(order.getElementId())) {
				List<String> ids = order.getPid();
				if(!CollectionUtils.isEmpty(ids)) {
					for(int i=0; i<ids.size(); i++) {
						if(executeStruct.getRunResultMap().get(ids.get(i)).getJsonVar() != null && !"".equals(executeStruct.getRunResultMap().get(ids.get(i)).getJsonVar())) {
							sb.append(executeStruct.getRunResultMap().get(ids.get(i)).getJsonVar().substring(1, executeStruct.getRunResultMap().get(ids.get(i)).getJsonVar().length()-1 ));
							sb.append(",");
						}
						
					}
					if(sb.length() >= 2) {
					    sb.deleteCharAt(sb.length()-1);
					}
				}
			}
		}
		
		sb.append("}");		
		return  (JSON)JSON.parse(sb.toString());
	}	
	
    
}