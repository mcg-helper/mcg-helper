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
 
import java.net.URLClassLoader;
import java.net.URL;
 
public class DynamicClassLoader extends URLClassLoader {
    public DynamicClassLoader(ClassLoader parent) {
        super(new URL[0], parent);
    }
 
    public Class<?> findClassByClassName(String className) throws ClassNotFoundException {
        return this.findClass(className);
    }
 
    public Class<?> loadClass(String fullName, JavaClassObject jco) {
        byte[] classData = jco.getBytes();
        return this.defineClass(fullName, classData, 0, classData.length);
    }
}

