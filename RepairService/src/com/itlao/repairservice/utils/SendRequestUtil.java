package com.itlao.repairservice.utils;

import java.util.Map;

import com.itlao.utils.http.Request;
import com.itlao.utils.http.handler.RequestHandler;

//发送请求的封装类
public class SendRequestUtil {

	    public static void post(String url,Map<String, Object> params,RequestHandler handler)
	    {
	    	try {
				// url="http://172.27.35.1:8080/RepairServer/views/data/Test.do?action=main";
	    		//String url2 = "http://172.27.35.1:8080/RepairServer/android/"+url;
	    		//String url2 = "http://192.168.191.1:8080/RepairServer/android/"+url;
	    		//String url2 = "http://192.168.191.1:8080/RepairServer/android/"+url;
	    		String url2 = "http://192.168.168.113:8080/RepairAdmin/android/"+url;

				Request.post(url2, params, handler, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    }
	  
}
