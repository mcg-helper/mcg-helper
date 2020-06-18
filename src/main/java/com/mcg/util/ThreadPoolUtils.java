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
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

public class ThreadPoolUtils {

	/**
	 * 执行流程线程池
	 */
	public static final ExecutorService FLOW_WORK_EXECUTOR = createExecutorService(10, "flow-execute-pool-%d", 100);
	
	public static final ExecutorService WSSH_WORK_EXECUTOR = createCacheExecutorService(10, 1000, "wssh-execute-pool-%d");
	
	public static ExecutorService createExecutorService(int corePoolSize,String threadNamingPattern, int workQueue){
        if (corePoolSize < 1){
        	corePoolSize = 5;
        }

        if (workQueue < 1){
        	workQueue = 50;
        }

        return new ThreadPoolExecutor(corePoolSize, corePoolSize * 10, 100L, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<Runnable>(workQueue),
                new BasicThreadFactory.Builder().namingPattern(threadNamingPattern).daemon(true).build(), new ThreadPoolExecutor.AbortPolicy());
    }
	
	public static ExecutorService createCacheExecutorService(int corePoolSize, int maximumPoolSize, String threadNamingPattern) {
		if(corePoolSize < 1) {
			corePoolSize = 5;
		}
		if(maximumPoolSize < 1) {
			maximumPoolSize = 100;
		}
		return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 100L, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(),
				new BasicThreadFactory.Builder().namingPattern(threadNamingPattern).daemon(true).build(), new ThreadPoolExecutor.AbortPolicy());
	}
}
