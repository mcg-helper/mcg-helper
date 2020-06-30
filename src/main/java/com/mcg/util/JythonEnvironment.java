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

import java.util.Properties;

import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

/**
 * Jython环境,生存python解释器
 * 
 */
public final class JythonEnvironment {
	private static JythonEnvironment INSTANCE = new JythonEnvironment();

	/**
	 * 私有构造方法
	 */
	private JythonEnvironment() {
	}

	/**
	 * 获取单例
	 * 
	 * @return JythonEnvironment
	 */
	public static JythonEnvironment getInstance() {
		return INSTANCE;
	}

	/**
	 * 获取python系统状态,可根据需要指定classloader/sys.stdin/sys.stdout等
	 * 
	 * @return PySystemState
	 */
	private PySystemState getPySystemState() {
		Properties props = new Properties();
		props.put("python.console.encoding", "UTF-8");
		props.put("sys.stdout.encoding", "UTF-8");
		props.put("python.security.respectJavaAccessibility", "false");
		props.put("python.import.site", "false");
		Properties preprops = System.getProperties();

		PySystemState.initialize(props, preprops);
		final PySystemState py = new PySystemState();
		py.setClassLoader(getClass().getClassLoader());
		return py;
	}

	/**
	 * 获取python解释器
	 * 
	 * @return PythonInterpreter
	 */
	public PythonInterpreter getPythonInterpreter() {
		PythonInterpreter inter = new PythonInterpreter(null, getPySystemState());
		return inter;
	}
}
