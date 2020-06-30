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

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.ReadOptions;
import org.iq80.leveldb.Snapshot;
import org.iq80.leveldb.WriteBatch;
import org.iq80.leveldb.WriteOptions;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.mcg.common.Constants;

/**
 * 
 * @ClassName:   LevelDbUtil   
 * @Description: TODO(数据持久化服务) 
 * @author:      缪聪(mcg-helper@qq.com)
 * @date:        2018年3月9日 下午5:40:03  
 *
 */
public class LevelDbUtil {
	private static Logger logger = LoggerFactory.getLogger(LevelDbUtil.class);
    static DBFactory factory = Iq80DBFactory.factory;
    static DB db;
    
    /**
     * leveldb 写CURRENT文件（内容为当前使用的文件名）时以\n结尾，从github下载zip没有问题，
     * 但通过git拉取代码时CURRENT结尾变为\r\n，leveldb读取CURRENT文件全部数据再截掉\n，
     * 导致文件名多出\r，本方法纠错，若存在\r就删除掉。
     */
    public static void errorRecovery(String path) {
        File file = new File(path);  
        String fileContent = null;  
        try {  
        	if(file.exists()) {
        		fileContent = FileUtils.readFileToString(file, "UTF-8");
        	}
        	if(fileContent.contains("\r")) {
        		fileContent = fileContent.replaceAll("\r", "");
        		FileUtils.write(file, fileContent);
        	}
        } catch (IOException e) {  
            logger.error(e.getMessage());
        }     	
    }
    
    public static void init() throws IOException {
    	errorRecovery(Constants.DATA_PATH + File.separator + "CURRENT");
    	
        File dir = new File(Constants.DATA_PATH);
        Options options = new Options().createIfMissing(true);
        options.maxOpenFiles(100);
        db = factory.open(dir, options);
    }
    
    public static void clean() throws IOException {
        File dir = new File(Constants.DATA_PATH);
        //清除文件夹内的所有文件。
        factory.destroy(dir, null);
    }
    
    public static void batchPut() throws IOException {
        WriteBatch writeBatch = db.createWriteBatch();
        writeBatch.put("key-03".getBytes(Constants.CHARSET),"value-03".getBytes(Constants.CHARSET));
        //writeBatch.delete("key-01".getBytes(charset));
        db.write(writeBatch);
        writeBatch.close();        
    }
    
    public static void close() throws IOException {
        db.close();
    }
   
    public static void delete(String key) {
    	db.delete(key.getBytes(Constants.CHARSET));
    }
    
    public static byte[] get(byte[] key) {
        if(key != null) {
            return db.get(key);
        }
        return null;
    }
    
    public static void put(byte[] key, byte[] value) {
        if(key != null && value != null) {
            db.put(key, value);
        }
    }
    
    public synchronized static <T> void putObject(String key, T t) throws IOException {
    	
        if(key != null && !"".equals(key)) {
            WriteOptions writeOptions = new WriteOptions().sync(true);//write后立即进行磁盘同步写，且线程安全
            db.put(key.getBytes(Constants.CHARSET), JSON.toJSONBytes(t), writeOptions);
        }
    }
    
    public synchronized static <T> T getObject(String key, Class<T> classes) throws IOException, ClassNotFoundException {
        if(key != null && !"".equals(key)) {
            byte[] bytes = db.get(key.getBytes(Constants.CHARSET));
            if(bytes != null) {
            	//JSON.parseObject("", new TypeReference<T>(classes){});
                return JSON.parseObject(bytes, classes);
            }
        }
        return null;
    }

    public static void print() throws IOException {
        //iterator，遍历，顺序读
        //读取当前snapshot，快照，读取期间数据的变更，不会反应出来       
        Snapshot snapshot = db.getSnapshot();
        //读选项        
        ReadOptions readOptions = new ReadOptions();
        readOptions.fillCache(false);//遍历中swap出来的数据，不应该保存在memtable中。        
        readOptions.snapshot(snapshot);//默认snapshot为当前。        
        DBIterator iterator = db.iterator(readOptions);
        while (iterator.hasNext()) {
            Map.Entry<byte[],byte[]> item = iterator.next();
            String key = new String(item.getKey(), Constants.CHARSET);
            String value = new String(item.getValue(), Constants.CHARSET);
            logger.debug("key:{}，value:{}", key, value);
        }
        iterator.close();
    }
    

}