/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") +  you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.itlao.utils.util.mappings;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;

import com.itlao.utils.util.ESLog;


public class StructureMethodList {
	
	private static ESLog log=new ESLog(JSONObjectToObject.class.getName());
	
	private StructureMethodList() {}

	private static StructureMethodList instance = null;

	public static synchronized StructureMethodList getInstance() {
		if (instance == null) {
			instance = new StructureMethodList();
		}
		return instance;
	}	
	
	/*
	 * 
	 */

	public LinkedHashMap<String,LinkedHashMap<String,Object>> parseClassToMethodList(Class<?> targetClass){
		try {
			LinkedHashMap<String,LinkedHashMap<String,Object>> returnMap = new LinkedHashMap<String,LinkedHashMap<String,Object>>();
			
			for (Field field : targetClass.getDeclaredFields()) {
				String fieldName = field.getName();
				String capitalizedFieldName =capitalize(fieldName);
				String setterPre = "set";
				
				for (Method method : targetClass.getMethods()) {
					// check that method is declared in current class. 
					if(method.getName().equals(setterPre + capitalizedFieldName)){
						String methodName = method.getName();
						
						Class<?>[] paramTypes = method.getParameterTypes();
						//log.error("parseClassToMethodList methodName: "+methodName);
						if (methodName.startsWith("set")) {
							//Found setter get Attribute name
							if (returnMap.get(fieldName)!=null) {
								LinkedHashMap<String,Object> methodListMap = returnMap.get(fieldName);
								methodListMap.put("setter", methodName);
								methodListMap.put("setterParamTypes", paramTypes);
							} else {
								LinkedHashMap<String,Object> methodListMap = new LinkedHashMap<String,Object>();
								methodListMap.put("setter", methodName);
								returnMap.put(fieldName, methodListMap);
								methodListMap.put("setterParamTypes", paramTypes);
							}
						} else if (methodName.startsWith("is")) {
							//Found setter(boolean) get Attribute name
							if (returnMap.get(fieldName)!=null) {
								LinkedHashMap<String,Object> methodListMap = returnMap.get(fieldName);
								methodListMap.put("getter", methodName);
							} else {
								LinkedHashMap<String,Object> methodListMap = new LinkedHashMap<String,Object>();
								methodListMap.put("getter", methodName);
								returnMap.put(fieldName, methodListMap);
							}
						} else if (methodName.startsWith("get")) {
							//Found setter(boolean) get Attribute name
							if (returnMap.get(fieldName)!=null) {
								LinkedHashMap<String,Object> methodListMap = returnMap.get(fieldName);
								methodListMap.put("getter", methodName);
							} else {
								LinkedHashMap<String,Object> methodListMap = new LinkedHashMap<String,Object>();
								methodListMap.put("getter", methodName);
								returnMap.put(fieldName, methodListMap);
							}
						}
						break;
					}
				}
				
				
			}			
			
			return returnMap;
		} catch (Exception ex) {
			log.error("[parseClassToMethodList]",ex);
			return new LinkedHashMap<String,LinkedHashMap<String,Object>>();
		}
		
	}
	static String capitalize(String fieldName){
		Character c=fieldName.charAt(0);
		char[] chars=fieldName.toCharArray();
		chars[0]=c.toUpperCase(c);
		return new String(chars);
	}
}
