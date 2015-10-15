package com.itlao.utils.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;




public class Config {
	private static final Properties properties=new Properties();
	private static boolean fileLoaded=false;
	private static boolean isDebug;
	private static final Map<String ,String> configProperties=new HashMap<String,String>();
	 
	static synchronized void  loadFile(){
		try {
			if(!fileLoaded||isDebug){
				properties.load(Config.class.getClassLoader().getResourceAsStream("config/config.properties"));
				fileLoaded=true;
				configProperties.clear();
				Enumeration em=properties.propertyNames();
				while(em.hasMoreElements()){
						String key=(String) em.nextElement();
						configProperties.put(key, properties.getProperty(key));
				}
				String pp=configProperties.get("debug");
				if(pp==null||pp.trim().length()==0)isDebug=false;
				else{
					isDebug=Integer.valueOf(pp)==1;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static  String getProperty(String name){
		if(!fileLoaded||isDebug)loadFile();
		try{
			String prop=configProperties.get(name);
			return  prop;
		}catch(Exception ex){
			ex.printStackTrace();
			return  null;
		}
	}
	public static String getLoginUrl(){
		return getProperty("web_url")+getProperty("login_url");
	}
	
	public static String getWebUrl(){
		return getProperty("web_url");
	}
	
	public static String getAddSosLocusUrl(){
		return getProperty("web_url")+getProperty("addSosLocus");
	}
	
	public static String getMsgWebSocketUrl(){
		return getProperty("msgWebSocket_url");
	}
	
	public static boolean isDebug(){
		String pp=getProperty("debug");
		if(pp==null||pp.trim().length()==0)return false;
		else{
			return Integer.valueOf(pp)==1;
		}
	}
	public static String getUrl(String key){
		return getWebUrl() + getProperty(key);
	}
	
	public static String getRawUrl(String key){
		return getProperty(key);
	}
	public static void main(String[] args){
		String cellAlpha=Config.getProperty("login_url");
		System.out.println(cellAlpha);
	}
}
