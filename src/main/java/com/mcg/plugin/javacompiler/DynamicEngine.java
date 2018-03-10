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

package com.mcg.plugin.javacompiler;
 
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import com.mcg.common.Constants;
 
public class DynamicEngine {
    private static DynamicEngine ourInstance = new DynamicEngine();
 
    public static DynamicEngine getInstance() {
        return ourInstance;
    }
    private URLClassLoader parentClassLoader;
    private String classpath;
    private DynamicEngine() {
        this.parentClassLoader = (URLClassLoader) this.getClass().getClassLoader();
        this.buildClassPath();
    }
    private void buildClassPath() {
        this.classpath = null;
        StringBuilder sb = new StringBuilder();
        for (URL url : this.parentClassLoader.getURLs()) {
            String p = url.getFile();
            sb.append(p).append(File.pathSeparator);
        }
        this.classpath = sb.toString();
    }
    
    /**
     * 调用类方法
     * @param cls 类
     * @param methodName 方法名
     * @param paramsCls 方法参数类型
     * @param params 方法参数
     * @return
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     */
    public Object invoke(Class<?> cls, String methodName, Class<?>[] paramsCls, Object[] params) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        Object result = null;
        Method method = cls.getDeclaredMethod(methodName, paramsCls);
        Object obj = cls.newInstance();
        result = method.invoke(obj, params);
        return result;
    }    
    
    public Object execute(String fullClassName, String javaCode, String method, Class<?>[] paramsCls,Object[] params) throws Exception {
        Object result = null;
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        ClassFileManager fileManager = new ClassFileManager(compiler.getStandardFileManager(diagnostics, null, null));
 
        List<JavaFileObject> jfiles = new ArrayList<JavaFileObject>();
        jfiles.add(new CharSequenceJavaFileObject(fullClassName, javaCode));
 
        List<String> options = new ArrayList<String>();
        options.add("-encoding");
        options.add(Constants.CHARSET.toString());
        options.add("-classpath");
        options.add(this.classpath);
 
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, jfiles);
        boolean success = task.call();
 
        if (success) {
            JavaClassObject jco = fileManager.getJavaClassObject();
            DynamicClassLoader dynamicClassLoader = new DynamicClassLoader(this.parentClassLoader);
            Class<?> clazz = dynamicClassLoader.loadClass(fullClassName,jco);
            result = invoke(clazz, method, paramsCls, params);
            
  //        instance = clazz.newInstance();
			dynamicClassLoader.close();
        } else {
            StringBuilder error = new StringBuilder();
            for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
            	error.append(compilePrint(diagnostic));
            }
            throw new Exception(error.toString());
        }

        return result;
    }
 
    private String compilePrint(Diagnostic<?> diagnostic) {
        StringBuilder res = new StringBuilder();
        res.append("Code:[" + diagnostic.getCode() + "]\n");
        res.append("Kind:[" + diagnostic.getKind() + "]\n");
        res.append("Position:[" + diagnostic.getPosition() + "]\n");
        res.append("Start Position:[" + diagnostic.getStartPosition() + "]\n");
        res.append("End Position:[" + diagnostic.getEndPosition() + "]\n");
        res.append("Source:[" + diagnostic.getSource() + "]\n");
        res.append("Message:[" + diagnostic.getMessage(null) + "]\n");
        res.append("LineNumber:[" + diagnostic.getLineNumber() + "]\n");
        res.append("ColumnNumber:[" + diagnostic.getColumnNumber() + "]\n");
        return res.toString();
    }
}

