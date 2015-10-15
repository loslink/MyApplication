package com.itlao.utils.util;


import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * @author KeJianLong
 * @creatTime 2013-6-28	下午04:27:20
 * @param:
 */

public class MemoryCache {
	 private static final String TAG = "MemoryCache";  
	    // 放入缓存时是个同步操作  
	    // LinkedHashMap构造方法的最后一个参数true代表这个map里的元素将按照最近使用次数由少到多排列，即LRU  
	    // 这样的好处是如果要将缓存中的元素替换，则先遍历出最近最少使用的元素来替换以提高效率  
	    private Map<String, Bitmap> cache = Collections  
	            .synchronizedMap(new LinkedHashMap<String, Bitmap>(8, 1.5f, true));  
	    // 缓存中图片所占用的字节，初始0，将通过此变量严格控制缓存所占用的堆内存  
	    private long size = 0;// current allocated size  
	    // 缓存只能占用的最大堆内存  
	    private long limit = 1000000;// max memory in bytes  
	  
	    public MemoryCache() {  
	        // use 33% of available heap size  
	    	Log.i(TAG, "MemoryCache total is " + Runtime.getRuntime().maxMemory() / 1024. / 1024. + "MB");  
	        setLimit(Runtime.getRuntime().maxMemory() / 3);  
	    }  
	  
	    public void setLimit(long new_limit) {   
	        limit = new_limit;  
	        Log.i(TAG, "MemoryCache will use up to " + limit / 1024. / 1024. + "MB");  
	    }  
	  
	    public Bitmap get(String id) {  
	        try {  
	            if (!cache.containsKey(id))  
	                return null;  
	            return cache.get(id);  
	        } catch (NullPointerException ex) {  
	            return null;  
	        }  
	    }  
	  
	    public void put(String id, Bitmap bitmap) {  
	        try {  
	            if (cache.containsKey(id))  
	                size -= getSizeInBytes(cache.get(id));  
	              
	            size += getSizeInBytes(bitmap);  
	            checkSize();  
	            cache.put(id, bitmap);
	        } catch (Throwable th) {  
	            th.printStackTrace();  
	        }  
	    }  
	  
	    /** 
	     * 严格控制堆内存，如果超过将首先替换最近最少使用的那个图片缓存 
	     *  
	     */  
	    private void checkSize() {  
	        Log.i(TAG, "cache size=" + size/ 1024. / 1024. + "MB" + " length=" + cache.size());  
	        if (size > limit) {  
	            // 先遍历最近最少使用的元素  
	            Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();  
	            while (iter.hasNext()) {  
	                Entry<String, Bitmap> entry = iter.next();  
	                size -= getSizeInBytes(entry.getValue());  
	                iter.remove();  
	                if (size <= limit)  
	                    break;  
	            }  
	            System.gc(); // 提醒系统及时回收
	            Log.i(TAG, "Clean cache. New size " + cache.size());  
	        }  
	    }  
	  
	    public void clear() { 
	        cache.clear();  
	    }  
	  
	    /** 
	     * 图片占用的内存 
	     *  
	     * @param bitmap 
	     * @return 
	     */  
	    long getSizeInBytes(Bitmap bitmap) {  
	        if (bitmap == null)  
	            return 0;  
	        return bitmap.getRowBytes() * bitmap.getHeight();  
	    } 
}
