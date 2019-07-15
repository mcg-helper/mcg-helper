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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

public class ThreadPoolUtils {

	/**
	 * 执行流程线程池
	 */
	public static final ExecutorService FLOW_WORK_EXECUTOR = createExecutorService(10, "flow-execute-pool-%d", 50);
	
	public static ExecutorService createExecutorService(int size,String threadNamingPattern, int capacity){
        if (size < 1){
            throw new IllegalArgumentException("线程池最小数量不能小于1");
        }

        if (capacity < 1){
            throw new IllegalArgumentException("线程任务队列最小数量不能小于1");
        }

        return new ThreadPoolExecutor(size,size * 10,100L, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<Runnable>(capacity),
                new BasicThreadFactory.Builder().namingPattern(threadNamingPattern).daemon(true).build(), new ThreadPoolExecutor.AbortPolicy());
    }
}
