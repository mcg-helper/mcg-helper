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
