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

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.google.common.base.CaseFormat;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.entity.common.McgResult;
import com.mcg.entity.message.NotifyBody;

/**
 * 
 * @ClassName:   Tools   
 * @Description: TODO(字符操作、hibernate validator) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午5:41:39  
 *
 */
public class Tools {

	/*
	 * 分割字符串"."，截取最后一段（如导入类型:java.utils.Date）截取最后的Date
	 */
	public static String splitLast(String data) {
		if(!notEmpty(data)) {
			return null;
		}
		String[] split = data.split("\\.");
		return split[split.length-1];
	}
	
	/*
	 * 字符串转换驼峰变量名
	 */
	public static String convertFieldName(String name) {
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
	}
	
	/*
	 * 字符串转换类名
	 */
	public static String convertClassName(String name) {
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name);
	}
	
	public static boolean notEmpty(String s) {
		return s != null && !"".equals(s) && !"null".equals(s);
	}

	public static boolean isEmpty(String s) {
		return s == null || "".equals(s) || "null".equals(s);
	}
	
	/* 验证页面提交的数据有效性 */
	public static boolean validator(BindingResult result, McgResult mcgResult, NotifyBody notifyBody) {
	    boolean flag = false;
        
        if(result.getFieldErrorCount() > 0) {
            StringBuilder sb = new StringBuilder();
            for(FieldError fieldError : result.getFieldErrors()) {
                sb.append(fieldError.getDefaultMessage() + "，");
            }
            sb.deleteCharAt(sb.length()-1);
            mcgResult.setStatusCode(0);
            notifyBody.setContent(sb.toString());
            notifyBody.setType(LogTypeEnum.ERROR.getValue());
        } else {
            flag = true;
        }
        
        return flag;
	}
}
