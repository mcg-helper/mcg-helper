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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcg.common.Constants;
import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.entity.flow.FlowBase;
import com.mcg.entity.flow.FlowStruct;
import com.mcg.entity.flow.sequence.FlowSequence;
import com.mcg.entity.flow.sequence.FlowSequences;
import com.mcg.entity.flow.text.FlowText;
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
	
	private static Logger logger = LoggerFactory.getLogger(DataConverter.class);
	
	public static FlowStruct xmlToflowStruct(String flowXml) {
		FlowStruct flowStruct = null;
		if(flowXml != null && !"".equals(flowXml)) {
	        try {  
	            JAXBContext context = JAXBContext.newInstance(FlowStruct.class);  
	            Unmarshaller unmarshaller = context.createUnmarshaller();  
	            flowStruct = (FlowStruct)unmarshaller.unmarshal(new StringReader(flowXml));  
	        } catch (JAXBException e) {  
	        	logger.error("流程xml数据转换FlowStruct出错，xml数据：{}，异常信息：{}", flowXml, e.getMessage());
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
            
            if(flowStruct.getFlowElementMap() != null) {
	            for(String elementId: flowStruct.getFlowElementMap().keySet()) {
	            	WebElement webElement = new WebElement();
	            	for(Object object : flowStruct.getFlowElementMap().get(elementId)) {

	            		JSONObject jsonObject = (JSONObject)object;
	            		for(EletypeEnum eletypeEnum : EletypeEnum.values()) {
	            			if(eletypeEnum.getValue().equals(jsonObject.getString("eletype"))) {
	    	            		Object flowElement = JSON.toJavaObject(jsonObject, eletypeEnum.getClasses());
	    				/*
	    						BeanCopier beanCopier = BeanCopier.create(object.getClass(),
	    						WebElement.class, false); beanCopier.copy(object, webElement, null);
	    				*/
	    	            		webElementList.add(setValue((FlowBase)flowElement, webElement));
	    	            		CachePlugin.putFlowEntity(flowId, webElement.getId(), flowElement);
	            			}
	            		}

	            	}
	            }
	            
	            webStruct.setWebElement(webElementList);
	            
	            if(flowStruct.getFlowSequences() != null && flowStruct.getFlowSequences().getFlowSequences().size() > 0) {
	                List<FlowSequence> flowSequenceList = flowStruct.getFlowSequences().getFlowSequences();
	                for(FlowSequence flowSequence : flowSequenceList) {
	                    WebConnector webConnector = new WebConnector();
	                    webConnector.setConnectorId(flowSequence.getSourceId() + flowSequence.getTargetId());
	                    webConnector.setSourceId(flowSequence.getSourceId());
	                    webConnector.setTargetId(flowSequence.getTargetId());
	                    webConnectorList.add(webConnector);
	                }
	                webStruct.setWebConnector(webConnectorList);
	            }
            }
        }
        return webStruct;
    }
    
    public static WebElement setValue(FlowBase flowBase, WebElement webElement) {
    	webElement.setId(flowBase.getId());
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
            
            HashMap<String, List<Object>> flowElementMap = new HashMap<>();
            List<WebElement> webElementList = webStruct.getWebElement();
            for(WebElement webElement : webElementList) {
                Object obj = CachePlugin.getFlowEntity(webStruct.getFlowId(), webElement.getId());
                for (EletypeEnum eletypeEnum : EletypeEnum.values()) {
                	if(eletypeEnum.getValue().equals(webElement.getEletype())) {
                		if(flowElementMap.get(eletypeEnum.getValue()) == null) {
                			List<Object> elementList = new ArrayList<>();
                			flowElementMap.put(webElement.getId(), elementList);
                		}
                        BeanCopier beanCopier = BeanCopier.create(webElement.getClass(), obj.getClass(), false);
                        beanCopier.copy(webElement, obj, null);
                        
                		flowElementMap.get(webElement.getId()).add(obj);
                	}
                }
            }
            
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
            
            flowStruct.setFlowElementMap(flowElementMap);
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
            logger.error("FlowStruct对象转换xml出错，对象数据：{}，异常信息：{}", JSON.toJSONString(flowStruct), e.getMessage());
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
      } catch (Exception e) {
          logger.error("mcgGlobal转换xml出错，mcgGlobal对象数据：{}，异常信息：{}", JSON.toJSONString(mcgGlobal), e.getMessage());
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
	            logger.error("xml数据转换McgGlobal出错，xml数据：{}，异常信息：{}", mcgGlobalXml, e.getMessage());
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
	@SuppressWarnings("unchecked")
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
            logger.error("流程变量中对引用全局变量（flowStart）的值进行替换出错，参数：{}，flowObject数据：{}，异常信息：{}", JSON.toJSONString(param), JSON.toJSONString(flowObject), e.getMessage());
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
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for(List<Order> orderList : executeStruct.getOrders().getOrder()) {
			for(Order order : orderList) {
				if(elementId.equals(order.getElementId())) {
					List<String> ids = order.getPid();
					for(int i=0; i<ids.size(); i++) {
						
					    if(executeStruct.getOrders().getOrder().get(0).get(0).getElementId().equals(ids.get(i))) {
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
        RunResult flowStartRunResult = (RunResult)executeStruct.getRunResultMap().get(executeStruct.getOrders().getOrder().get(0).get(0).getElementId());      
        
        if(flowStartRunResult == null) {
        	return new JSONObject();
        }
        
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

		JSONObject allParam = new JSONObject();
		
		for(List<Order> orderList : executeStruct.getOrders().getOrder()) {
			for(Order order : orderList) {
				if(elementId.equals(order.getElementId())) {
					List<String> ids = order.getPid();
					if(!CollectionUtils.isEmpty(ids)) {
						for(int i=0; i<ids.size(); i++) {
							if(executeStruct.getRunResultMap().get(ids.get(i)) != null && executeStruct.getRunResultMap().get(ids.get(i)).getJsonVar() != null && !"".equals(executeStruct.getRunResultMap().get(ids.get(i)).getJsonVar())) {
								JSONObject param = JSONObject.parseObject(executeStruct.getRunResultMap().get(ids.get(i)).getJsonVar());
								allParam.putAll(param);
							}
							
						}
		
					}
				}
			}
		}
		
		return  (JSON)allParam;
	}	
	
	/**
	 * 获取所有父级控件的运行值，将所有父级控件的key去掉，将value进行合并，若value中的key有相同的，则由后面控件运行值将覆盖前面控件运行值
	 * @param elementId 当前组件的id
	 * @param executeStruct 当前已执行的流程的组件
	 * @return
	 */
	public static JSON getParentRunResultByValue(String elementId, ExecuteStruct executeStruct) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		
		for(List<Order> orderList : executeStruct.getOrders().getOrder()) {
			for(Order order : orderList) {
				if(elementId.equals(order.getElementId())) {
					List<String> ids = order.getPid();
					if(!CollectionUtils.isEmpty(ids)) {
						for(int i=0; i<ids.size(); i++) {
							if(executeStruct.getRunResultMap().get(ids.get(i)) != null && executeStruct.getRunResultMap().get(ids.get(i)).getJsonVar() != null && !"".equals(executeStruct.getRunResultMap().get(ids.get(i)).getJsonVar())) {
								String jsonValue = executeStruct.getRunResultMap().get(ids.get(i)).getJsonVar().substring(1, executeStruct.getRunResultMap().get(ids.get(i)).getJsonVar().length()-1 );
								if(StringUtils.isNotEmpty(jsonValue)) {
									int start = jsonValue.indexOf("{") + 1;
									int end = jsonValue.lastIndexOf("}")-1;
									if(start <= end) {
										sb.append(jsonValue.substring(start, end));
									} else {
										sb.append("");
									}
									sb.append(",");
								}
							}
						}
						if(sb.length() >= 2) {
						    sb.deleteCharAt(sb.length()-1);
						}
					}
				}
			}
		}
		
		sb.append("}");		
		return  (JSON)JSON.parse(sb.toString());
	}
}