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

package com.mcg.listener;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * 
 * @ClassName:   McgHelperRunComplete   
 * @Description: TODO(系统成功启动完成后的操作) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午5:30:15  
 *
 */
public class McgHelperRunComplete extends HttpServlet {

    private static final long serialVersionUID = 3529712260551712017L;

    @Override
    public void init() throws ServletException {
        
        super.init();
        System.out.println();
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println();
        System.out.println("mcg-helper研发小助手启动完成，请访问http://localhost:8888/mcg");
        System.out.println();
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println();
        
    }

    
}
