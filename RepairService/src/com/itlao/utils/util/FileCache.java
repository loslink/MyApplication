package com.itlao.utils.util;

import java.io.File;
import android.content.Context;

/**
 * @author KeJianLong
 * @creatTime 2013-6-28	����04:30:12
 * @param:
 */

public class FileCache {
	
	public static final int BROADCAST = 0;//ͼƬ���ͣ�����ͼ
	public static final int STAGE = 1;
	public static final int MESSAGE = 2;
	
	private File cacheDir;
	private File imageCache;
	private File broadcastCache;
	private File stageCache;
	private File msgCache;
	
	private FileClearProcessListener fileClearProcessListener;
	
    public FileCache(Context context) {  
        // �����SD������SD���н�һ��LazyList��Ŀ¼��Ż����ͼƬ  
        // û��SD���ͷ���ϵͳ�Ļ���Ŀ¼��  
    	String packageName = context.getPackageName();
    	String appName = packageName.substring(packageName.lastIndexOf(".")+1,
    				packageName.length());
    	String path = "/image_cache/";
    	String broadcastPath = "/image_cache/broadcast";//����ͼ
    	String stagePath = "/image_cache/stage";//����ͼ
    	String msgPath = "/image_cache/message";
        if (android.os.Environment.getExternalStorageState().equals(  
                android.os.Environment.MEDIA_MOUNTED))  
            cacheDir = new File(  
                    android.os.Environment.getExternalStorageDirectory(),  
                    appName);  
        else  
            cacheDir = context.getCacheDir();  
        
        imageCache = new File(cacheDir, path);
        if(!imageCache.exists()){
        	imageCache.mkdirs();
        }
        broadcastCache = new File(cacheDir, broadcastPath);
        if(!broadcastCache.exists()){
        	broadcastCache.mkdirs();
        } 
        stageCache = new File(cacheDir, stagePath);
        if(!stageCache.exists()){
        	stageCache.mkdirs();
        } 
        
        msgCache = new File(cacheDir, msgPath);
        if(!msgCache.exists()){
        	msgCache.mkdirs();
        } 
        
    }  
    /**
     * @param type ��0:����ͼ��
     * */
    public File getFile(String imageId, int type) {  
        // ��url��hashCode��Ϊ������ļ��ｿ 
    	File temp;
    	if(BROADCAST == type){
    		temp = broadcastCache;
    	}else if(STAGE == type){
    		temp = stageCache;
    	}else if(MESSAGE == type){
    		temp = msgCache;
    	}else{
    		temp = imageCache;//Ĭ��·��
    	}
        File f = new File(temp, imageId + ".jpg");  
        return f;  
  
    }  
    
  
    public void clear() {  
        File[] files = cacheDir.listFiles();  
        fileClearProcessListener.initTotalSize(cacheDir.length());
        if (files == null)  
            return;  
        long doneSize = 0l;
        for (File f : files) {
        	reClearFile(doneSize, f);
        }
        fileClearProcessListener.clearDone();      
    } 
    
    private void reClearFile(Long doneSize, File file){
    	if(file.isDirectory()){
    		for (File f : file.listFiles()) {
            	reClearFile(doneSize, f);
            }
    	}else {
    		fileClearProcessListener.clearingProcess(doneSize += file.length());
    		file.delete();
    	}
    	
    }
    
	/**
	 * �ļ�ɾ���Ƚӿ�
	 * 
	 * @author 
	 */
    public interface FileClearProcessListener {  
    	void clearDone();
    	/**
         * �ϴ��� 
         * @param uploadSize 
         */  
        void clearingProcess(Long clearSize);  
        /** 
         * ׼���ϴ� 
         * @param fileSize 
         */  
        void initTotalSize(Long fileSize);  
    }
    
    public void setFileClearProcessListener(FileClearProcessListener fileClearProcessListener) {
		this.fileClearProcessListener = fileClearProcessListener;
	}
	public FileClearProcessListener getFileClearProcessListener() {
		return fileClearProcessListener;
	}

}
