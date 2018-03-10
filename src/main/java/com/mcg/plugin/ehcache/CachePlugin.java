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

package com.mcg.plugin.ehcache;

import com.mcg.common.Constants;
import com.mcg.common.SpringContextHelper;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class CachePlugin {

    private static CacheManager cacheManager = ((CacheManager)SpringContextHelper.getSpringBean("cacheManager"));
    
    public static Cache addCache(String cacheName) {
        cacheManager.addCache(cacheName);
        Cache cache = cacheManager.getCache(cacheName);
        cache.getCacheConfiguration().setEternal(true);
        return cache;
    }    
    
    public static void put(String key, Object value) {
        Cache cache = cacheManager.getCache(Constants.CACHE_NAME);
        if (cache != null) {
            Element el = new Element(key, value);
            cache.put(el);
        }
    }

    public static Object get(String key) {
        Element element = cacheManager.getCache(Constants.CACHE_NAME).get(key);
        if (element == null) {
            return null;
        }
        return element.getObjectValue();
    }

    public static void removeCache() {
        cacheManager.removeCache(Constants.CACHE_NAME);
    }

    public static void removeCacheObject(String key) {
        cacheManager.getCache(Constants.CACHE_NAME).remove(key);
    }

    public static void removeCacheObjectAll() {
        cacheManager.getCache(Constants.CACHE_NAME).removeAll();
    }    
}
