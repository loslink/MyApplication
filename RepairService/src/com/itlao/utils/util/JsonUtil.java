package com.itlao.utils.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.itlao.utils.util.mappings.JSONObjectToObject;

public class JsonUtil {
	private static ObjectMapper mapper=new ObjectMapper(){{
		setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		 configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES , false);
		 configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
		configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
	}};

	public static <T extends Object> List<T> readList(String jsonString,Class<T> clazz){
		List<T> list=new ArrayList<T>();
	    try {
//	    	JSONArray array = new JSONArray(jsonString);
//	    	for (int i = 0; i < array.length(); i++) {
//	    		JSONObject jo = array.getJSONObject(i);
//	    		T object = (T) JsonUtil.json2Object(jo, clazz);
//	    		list.add(object);
//	    	}
	    	JsonFactory f = new JsonFactory();
			JsonParser jp = f.createJsonParser(jsonString);
			jp.nextToken();
			// and then each time, advance to opening start_object
			while (jp.nextToken() == JsonToken.START_OBJECT) {
				T t = mapper.readValue(jp, clazz);
				list.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	 public static <T extends Object> T json2Object(String jsonString,Class<T> clazz) throws Exception{
		 try {
//			 DeserializationProblemHandler hs=new DeserializationProblemHandler(){
//					@Override
//					public boolean handleUnknownProperty(
//							DeserializationContext ctxt,
//							JsonDeserializer<?> deserializer,
//							Object beanOrClass, String propertyName)
//							throws IOException, JsonProcessingException {
//
//							return super.handleUnknownProperty(ctxt, deserializer,
//									beanOrClass, propertyName);
//					}
//				};
//			 mapper.getDeserializationConfig().addHandler(hs);
			 if(jsonString==null)return null;
			 T t=mapper.readValue(jsonString, clazz);
//			JSONObject jo=new JSONObject(jsonString);
//			JSONObjectToObject converter=JSONObjectToObject.getInstance();
//			T t=converter.castByGivenObject(jo, clazz);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return null;
	 }
	 public static <T extends Object> T json2Object(JSONObject jo,Class<T> clazz) throws Exception{
		 try {
			JSONObjectToObject converter=JSONObjectToObject.getInstance();
			T t=converter.castByGivenObject(jo, clazz);
			 return t;
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 return null;
	 }

	 public static String toJson(Object obj){
		 try {
			String json = mapper.writeValueAsString(obj);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return null;
	 }
	 public static String getString(JSONObject order, String key){
			String result = "";
			try {
				if(order.getString(key) != null){
					result = order.getString(key);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return result;
		}

		public static int getInt(JSONObject order, String key){
			int result = 0;
			try {
				if(order.getString(key) != null){
					result = order.getInt(key);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return result;
		}

		public static long getLong(JSONObject order, String key){
			long result = 0l;
			try {
				result = order.getLong(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return result;
		}
}
