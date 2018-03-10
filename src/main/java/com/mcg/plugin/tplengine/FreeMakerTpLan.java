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

package com.mcg.plugin.tplengine;

import java.io.StringReader;
import java.io.StringWriter;

import com.alibaba.fastjson.JSON;
import com.mcg.common.Constants;
import com.mcg.util.McgFileUtils;

import freemarker.template.Template;

public class FreeMakerTpLan implements TplLan {

	@Override
	public String generate(JSON json, String ftl, String outFileName, String outFilePath) throws Exception {
		String result = null;
        Template t;
/*		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFilePath+outFileName), "UTF-8"));
		t = new Template(null, new StringReader(ftl), null);
		t.process(json, out);
        out.flush();
        out.close();*/
        
        StringWriter stringWriter = new StringWriter();
        t = new Template(null, new StringReader(ftl), null);
        t.process(json, stringWriter);
        result = stringWriter.toString();
        stringWriter.close();    
        McgFileUtils.writeByteArrayToFile(outFilePath, outFileName, result, Constants.CHARSET.toString());
		return result;
	}

	@Override
    public String generate(JSON json, String source) throws Exception {
	    String result = null;
        Template t;
        StringWriter stringWriter = new StringWriter();
        t = new Template(null, new StringReader(source), null);
        t.process(json, stringWriter);
        result = stringWriter.toString();
        stringWriter.close();
        return result;
    }

}