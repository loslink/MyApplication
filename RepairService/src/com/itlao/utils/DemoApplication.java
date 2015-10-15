package com.itlao.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

public class DemoApplication extends Application {

	String fileDirPath;
	String fileName ;
	
	@Override
	public void onCreate() {
		super.onCreate();
		// 鍦ㄤ娇鐢?SDK 鍚勭粍闂翠箣鍓嶅垵濮嬪寲 context 淇℃伅锛屼紶鍏?ApplicationContext
		SDKInitializer.initialize(this);
		
		fileDirPath = android.os.Environment  
	            .getExternalStorageDirectory().getAbsolutePath()// 得到外部存储卡的数据库的路径名  
	            + "/RepairService";// 我要存储的目录  
	     fileName = "flycatdeng.txt";// 要存储的文件名 
	     
	    createFile();// 创建目录及文件 
	}


	public List<String> getFilePaths() {

	    List<String> fileNames = null;

	    try {
		fileNames = Arrays.asList(getAssets().list("photo"));
	    } catch (IOException e) {
		e.printStackTrace();
	    }

	    return fileNames;
	}
	private void createFile() { 
		
		/*File f = new File("/res/raw");  
        File[] files = f.listFiles();// 列出所有文件 
        try {
			String[] files2 = getAssets().list("");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/

		List<String> listname=getFilePaths();
        
        try {
            File dir = new File(fileDirPath);// 目录路径 
            if (!dir.exists()) {// 如果不存在，则创建路径名 
                System.out.println("要存储的目录不存在");
                if (dir.mkdirs()) {// 创建该路径名，返回true则表示创建成功 
                    System.out.println("已经创建文件存储目录");
                } else {
                    System.out.println("创建目录失败");
                }
            }
            
        try { 
        	InputStream inputStream = null;
        	FileOutputStream fos = null;
        	for(int i=0;i<listname.size();i++) {
        		
        		String filePath = fileDirPath + "/" + listname.get(i);// 文件路径  
                //InputStreamReader inputReader = new InputStreamReader( getResources().getAssets().open("photo/"+listname.get(0)) ); 
            	inputStream = getResources().getAssets().open("photo/"+listname.get(i)) ; 
                //BufferedReader bufReader = new BufferedReader(inputReader);
                String line="";
                //String Result="";
                File file = new File(filePath);
                if(file.exists()) {
                	
                	file.delete();
                }
                fos = new FileOutputStream(filePath);
                byte[] buffer = new byte[8192];  
                int count = 0;// 循环写出  
    			while((count =inputStream.read(buffer))>0) {
                        	
    				fos.write(buffer, 0, count);
                    //Result += line;
                     }
    			//inputReader.close();
    			//bufReader.close();
    			
        	}
        	inputStream.close();
			fos.close();  
            } catch (Exception e) { 
                    e.printStackTrace(); 
            }
        
            /*// 目录存在，则将apk中raw中的需要的文档复制到该目录下 
            File file = new File(filePath); 
            if (!file.exists()) {// 文件不存在  
                System.out.println("要打开的文件不存在");
                InputStream ins = getResources().openRawResource(
                        R.raw.flycatdeng);// 通过raw得到数据资源  
                System.out.println("开始读入");
                FileOutputStream fos = new FileOutputStream(file);
                System.out.println("开始写出");
                byte[] buffer = new byte[8192];  
                int count = 0;// 循环写出  
                while ((count = ins.read(buffer)) > 0) {  
                    fos.write(buffer, 0, count);  
                }  
                System.out.println("已经创建该文件");  
                fos.close();// 关闭流  
                ins.close();  
            }  */
        
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
	
}