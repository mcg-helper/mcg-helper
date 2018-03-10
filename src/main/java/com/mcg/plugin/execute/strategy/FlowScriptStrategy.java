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

package com.mcg.plugin.execute.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.alibaba.fastjson.JSON;
import com.mcg.common.sysenum.EletypeEnum;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.common.sysenum.MessageTypeEnum;
import com.mcg.entity.flow.script.FlowScript;
import com.mcg.entity.generate.ExecuteStruct;
import com.mcg.entity.generate.RunResult;
import com.mcg.entity.message.FlowBody;
import com.mcg.entity.message.Message;
import com.mcg.plugin.build.McgProduct;
import com.mcg.plugin.execute.ProcessStrategy;
import com.mcg.plugin.generate.FlowTask;
import com.mcg.plugin.websocket.MessagePlugin;
import com.mcg.util.DataConverter;

public class FlowScriptStrategy implements ProcessStrategy {

	@Override
	public void prepare(ArrayList<String> sequence, McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		FlowScript flowScript = (FlowScript)mcgProduct;
		executeStruct.getRunStatus().setExecuteId(flowScript.getScriptId());
	}

	@Override
	public RunResult run(McgProduct mcgProduct, ExecuteStruct executeStruct) throws Exception {
		
		FlowScript flowScript = (FlowScript)mcgProduct;
		JSON parentParam = DataConverter.getParentRunResult(flowScript.getScriptId(), executeStruct);
		flowScript = DataConverter.flowOjbectRepalceGlobal(DataConverter.addFlowStartRunResult(parentParam, executeStruct) ,flowScript);		
		RunResult runResult = new RunResult();
        Message message = MessagePlugin.getMessage();
        message.getHeader().setMesType(MessageTypeEnum.FLOW);
        FlowBody flowBody = new FlowBody();
        flowBody.setEleType(EletypeEnum.SCRIPT.getValue());
        flowBody.setEleTypeDesc(EletypeEnum.SCRIPT.getName() + "--》" + flowScript.getScriptProperty().getScriptName());
        flowBody.setEleId(flowScript.getScriptId());
        flowBody.setComment("参数");
        if(parentParam == null) {
        	flowBody.setContent("{}");
        } else {
        	flowBody.setContent(JSON.toJSONString(parentParam, true));
        }
        flowBody.setLogType(LogTypeEnum.INFO.getValue());
        flowBody.setLogTypeDesc(LogTypeEnum.INFO.getName());
        message.setBody(flowBody);
        FlowTask flowTask = FlowTask.executeLocal.get();    
        MessagePlugin.push(flowTask.getHttpSessionId(), message); 		
		
		String dataJson = resolve(flowScript.getScriptCore().getSource(), parentParam);
		runResult.setElementId(flowScript.getScriptId());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(flowScript.getScriptProperty().getKey(), JSON.parse(dataJson));
		runResult.setJsonVar(JSON.toJSONString(map, true));
		executeStruct.getRunStatus().setCode("success");
		
		return runResult;
	}
	
	public String resolve(String script, JSON param) throws ScriptException, NoSuchMethodException {
		String dataJson = null;
	    ScriptEngineManager scriptEngineManager = new ScriptEngineManager();  
	    ScriptEngine engine = scriptEngineManager.getEngineByName("nashorn");
		engine.eval(script);
	    Invocable invocable = (Invocable) engine;
	    Object result = invocable.invokeFunction("main", param);
	    
	    dataJson = JSON.toJSONString(result);
	    return dataJson;
	}
	
/*
	private static void check(Object obj) {
	    if(obj instanceof JSONObject) {
	        JSONObject json = (JSONObject)obj;
            for (Map.Entry<String, Object> entry : json.entrySet()) {
                if (entry.getValue() instanceof Bindings) {
                    try {
                        final Class<?> cls = Class.forName("jdk.nashorn.api.scripting.ScriptObjectMirror");
                        if (cls.isAssignableFrom(entry.getValue().getClass())) {
                            final Method isArray = cls.getMethod("isArray");
                            final Object result = isArray.invoke(entry.getValue());
                            if (result != null && result.equals(true)) {
                                final Method values = cls.getMethod("values");
                                final Object vals = values.invoke(entry.getValue());
                                if (vals instanceof Collection<?>) {
                                    final Collection<?> coll = (Collection<?>) vals;
                                    entry.setValue(coll.toArray(new Object[0]));
                                }
                            }
                        }
                    } catch (ClassNotFoundException | NoSuchMethodException | SecurityException
                            | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}
                } else if(entry.getValue() instanceof Object[]) {
                    Object[] objectArray = (Object[])entry.getValue();
                    for(int i=0; i<objectArray.length; i++) {
                        check(objectArray[i]);
                    }                    
                } else if(entry.getValue() instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray)entry.getValue();
                    Iterator<Object> it = jsonArray.iterator();
                    while (it.hasNext()) {
                        check(it.next());
                    }
                }
                
                check(entry.getValue());
            }
        } else if(obj instanceof JSONArray) {
            JSONArray json = (JSONArray)obj;
            Iterator<Object> it = json.iterator();
            while (it.hasNext()) {
                check(it.next());
            }
        } else if(obj instanceof Object[]) {
            Object[] objectArray = (Object[])obj;
            for(int i=0; i<objectArray.length; i++) {
                check(objectArray[i]);
            }
        } else if (obj instanceof Bindings) {
            Class<?> cls;
            try {
                cls = Class.forName("jdk.nashorn.api.scripting.ScriptObjectMirror");
                Method resultMethod = cls.getMethod("entrySet", cls.getClasses());
                
                Set<Map.Entry<String, Object>> set = (Set<Map.Entry<String, Object>>)resultMethod.invoke(obj);
                Iterator<Map.Entry<String, Object>> itor = set.iterator();
                while (itor.hasNext()) {
                	Map.Entry<String, Object> me = itor.next();
                	
                    if (cls.isAssignableFrom(me.getValue().getClass())) {
                        final Method isArray = cls.getMethod("isArray");
                        final Object result = isArray.invoke(me.getValue());
                        if (result != null && result.equals(true)) {
                            final Method values = cls.getMethod("values");
                            final Object vals = values.invoke(me.getValue());
                            if (vals instanceof Collection<?>) {
                                final Collection<?> coll = (Collection<?>) vals;
                                me.setValue(coll.toArray(new Object[0]));
                                
                                
                            }
                        } else {
                        	check(me.getValue());
                        }
                    } else {
                        check(me.getValue());
                    }                    
                }                
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }       
	}
	
	private static Object convert(Object obj) {
	    check(obj);
     	        
	    return obj;
	}	
	*/
}
