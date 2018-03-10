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
 
import java.io.IOException;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
 
public class ClassFileManager extends ForwardingJavaFileManager {
    public JavaClassObject getJavaClassObject() {
        return jclassObject;
    }
 
    private JavaClassObject jclassObject;
 
 
    public ClassFileManager(StandardJavaFileManager
        standardManager) {
        super(standardManager);
    }
 
 
    @Override
    public JavaFileObject getJavaFileForOutput(Location location,
        String className, JavaFileObject.Kind kind, FileObject sibling)
            throws IOException {
            jclassObject = new JavaClassObject(className, kind);
        return jclassObject;
    }
}

