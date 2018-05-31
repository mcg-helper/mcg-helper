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

import com.alibaba.fastjson.JSON;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
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
            e.printStackTrace();  
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

    public static <T> void putObject(String key, T t) throws IOException {
        if(key != null && !"".equals(key)) {
            RuntimeSchema poSchema = RuntimeSchema.createFrom(t.getClass());
            WriteOptions writeOptions = new WriteOptions().sync(true);//write后立即进行磁盘同步写，且线程安全
            db.put(key.getBytes(Constants.CHARSET), ProtostuffIOUtil.toByteArray(t, poSchema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE)), writeOptions);
        }
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
    
    public static <T> Object getObject(String key, Class<T> classes) throws IOException, ClassNotFoundException {
        if(key != null && !"".equals(key)) {
            byte[] bytes = db.get(key.getBytes(Constants.CHARSET));
            if(bytes != null) {
                RuntimeSchema poSchema = RuntimeSchema.createFrom(classes);
                Object obj = poSchema.newMessage();
                ProtostuffIOUtil.mergeFrom(bytes, obj, poSchema);
                return obj;
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
            System.out.println(key + ":" + value);
        }
        iterator.close();//must be
    }
    
	public static void main(String[] args) throws IOException, ClassNotFoundException {
	    
	    LevelDbUtil.init();
	    LevelDbUtil.print();
//        McgUser mcg = new McgUser();
//        mcg.setUserKey("1314");     
        System.out.println(System.currentTimeMillis());
//        LevelDbUtil.testPut("he", mcg);
	    LevelDbUtil.putObject("he", "ccte");
        String cctv = (String)LevelDbUtil.getObject("he", String.class);
        System.out.println(System.currentTimeMillis());
        System.out.println(JSON.toJSONString(cctv));
        
/*		 boolean cleanup = true;
	        Charset charset = Charset.forName("utf-8");
	        String path = "E:/temp/data/leveldb";

	        //init
	        DBFactory factory = Iq80DBFactory.factory;
	        File dir = new File(Constants.DATA_PATH);  
	        //如果数据不需要reload，则每次重启，尝试清理磁盘中path下的旧数据。

	        Options options = new Options().createIfMissing(true);
	        //重新open新的db        
	        DB db = factory.open(dir,options);
	        
	   //   byte[] bytes = db.get("mcg".getBytes(charset));

	        McgUser mcg = new McgUser();
	        mcg.setUserKey("1314");
	        
	        ByteArrayOutputStream byt=new ByteArrayOutputStream();
	        ObjectOutputStream obj=new ObjectOutputStream(byt);
	        obj.writeObject(mcg);
	        byte[] bytes=byt.toByteArray();

	        db.put("mcg".getBytes(charset), bytes);
	        
	        ByteArrayInputStream byteInt=new ByteArrayInputStream(bytes);
	        ObjectInputStream objInt = new ObjectInputStream(byteInt);
	        McgUser mcgUser=(McgUser)objInt.readObject();	        
	        System.out.println(JSON.toJSONString(mcgUser));
	        //write
	        db.put("key-01".getBytes(charset),"value-01".getBytes(charset));
	        
	        //write后立即进行磁盘同步写
	        WriteOptions writeOptions = new WriteOptions().sync(true);//线程安全
	        db.put("key-02".getBytes(charset),"value-02".getBytes(charset),writeOptions);

	        //batch write
	        WriteBatch writeBatch = db.createWriteBatch();
	        writeBatch.put("key-03".getBytes(charset),"value-03".getBytes(charset));
	        writeBatch.put("key-04".getBytes(charset),"value-04".getBytes(charset));
	        //writeBatch.delete("key-01".getBytes(charset));
	        db.write(writeBatch);
	        writeBatch.close();

	        //read
	        byte[] bv = db.get("key-02".getBytes(charset));
	        if(bv != null && bv.length > 0) {
	            String value = new String(bv,charset);
	            System.out.println(value);
	        }



	        //delete
	        //db.delete("key-01".getBytes(charset));

	        //compaction，手动
//	        db.compactRange("key-".getBytes(charset),null);
	        db.close();*/
	}
}