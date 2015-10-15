
package com.itlao.utils.util.mappings;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.itlao.utils.util.ESLog;


/**
 * Class to cast any LinkedHashMap to its JavaBean repraesentant
 * the idiom is that the attribute name in the LinkedHashMap is the same as in the JavaBean/Pojo
 *
 * if the attribute's of the Bean are private (meaning it IS a Bean) then it will use the getters and setters
 * if the attribute's are public it will assign directly
 * if the attribute is final it will show an error in log
 *
 * if the HashMap contains an null for a primitive attribute it will not assign that value
 *
 * if the HashMap contains subelments nested as LinkedHashMap's it will add these Sub-Elements to the Main-Object
 * for an exmaple see:
 * http://openmeetings.googlecode.com/svn/branches/dev/xmlcrm/java/src/test/org/xmlcrm/utils/TestReflectionApi.java
 *
 * TODO:
 * If the Sub Item is not an Object but a Set (meaning a List of Object) this List must be
 * cast to Objects of the Bean too
 *
 * @author swagner
 *
 *
 */

public class JSONObjectToObject {

	private static ESLog log=new ESLog(JSONObjectToObject.class.getName());
	private JSONObjectToObject() {}

	private static JSONObjectToObject instance = null;

	public static synchronized JSONObjectToObject getInstance() {
		if (instance == null) {
			instance = new JSONObjectToObject();
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	public <T extends Object> T castByGivenObject(JSONObject values, Class<T> targetClass){
		try {
//			if (valuesObj.getClass().getClass().getName().equals(ObjectMap.class.getName())){
//				ObjectMap values = (ObjectMap) valuesObj;
//			} else if (valuesObj.getClass().getClass().getName().equals(LinkedHashMap.class.getName())){
//				LinkedHashMap values = (LinkedHashMap) valuesObj;
//			}
//			log.error("className:"+targetClass.getName());
			Object returnObject = targetClass.newInstance();
//			log.error("returnObject");
//			log.error(returnObject);
//			log.error( "class " + targetClass.getName() );
//			log.error (" number of declared fields: " + targetClass.getDeclaredFields().length );
			LinkedHashMap<String,LinkedHashMap<String,Object>> structuredMethodMap = StructureMethodList.getInstance().parseClassToMethodList(targetClass);

			for ( Field anyField : targetClass.getDeclaredFields() )  {
				String fieldName = anyField.getName();
				Class<?> fieldType = anyField.getType();
				String fieldTypeName = anyField.getType().getName();
				if(values.isNull(fieldName))continue;

				if (this.compareTypeNameToBasicTypes(fieldTypeName)) {
					//log.info("Found Type: " + fieldName);
					//Get value from  set

					Object t = values.get(fieldName);
					//log.info("fieldName Value: "+t);
					//log.info("fieldName Value: "+anyField.getModifiers());
					int mod = anyField.getModifiers();

					if (Modifier.isPrivate(mod) && !Modifier.isFinal(mod)){
						//log.info("is private so get setter method "+fieldName);
						LinkedHashMap<String,Object> methodSummery = structuredMethodMap.get(fieldName);
						if (methodSummery!=null) {
							if (methodSummery.get("setter")!=null) {

								String methodSetterName = methodSummery.get("setter").toString();
								Class<?>[] paramTypes = (Class[]) methodSummery.get("setterParamTypes");
								Method m = targetClass.getMethod(methodSetterName, paramTypes);

								Class<?> paramType = paramTypes[0];

								//try to cast the Given Object to the necessary Object
								if (fieldTypeName.equals("java.util.Date")&&t!=null && !paramType.getName().equals(t.getClass().getName())){
									Integer dateObj=(Integer)t;
									t=new Date(dateObj);
								}else if(t!=null && !paramType.getName().equals(t.getClass().getName())){
									for (Constructor<?> crt : paramType.getConstructors()) {
										if (crt.getParameterTypes()[0].getName().equals("java.lang.String")){
											t = crt.newInstance(t.toString());
										}
									}
								}
								if (paramType.isPrimitive() && t==null){
									//cannot cast null to primitve
								} else {
									Object[] arguments = new Object[]{ t };
									m.invoke(returnObject,arguments);
								}
							} else {
								log.error("could not find a setter-method from Structured table. Is there a setter-method for " + fieldName + " in Class " + targetClass.getName());
							}
						} else {
							log.error("could not find a method from Structured table. Is there a method for " + fieldName + " in Class " + targetClass.getName());
						}

					} else if (Modifier.isPublic(mod) && !Modifier.isFinal(mod)){
						if (t!=null && !anyField.getType().getName().equals(t.getClass().getName())){
							for (Constructor<?> crt : anyField.getType().getConstructors()) {
								if (crt.getParameterTypes()[0].getName().equals("java.lang.String")){
									t = crt.newInstance(t.toString());
								}
							}
							//Is public attribute so set it directly
							anyField.set(returnObject, t);
						}

					} else if (Modifier.isFinal(mod)) {
						log.error("Final attributes cannot be changed ");
					} else {
						log.error("Unhandled Modifier Type: " + mod);
					}

				} else {
					//This will cast nested Object to the current Object
					//it does not matter how deep it is nested
//					log.error("fieldType "+fieldType.getName());
					//Check if the Attribute in the bean is a List
					if (fieldType.getName().equals("java.util.Set")) {

						//Todo: Cast Set to Object

//						log.error("compareBeanTypeToAllowedListTypes true " + fieldType.getName());
//						log.error("compareBeanTypeToAllowedListTypes true " + fieldName);

						Object valueOfHashMap = values.get(fieldName);

						if (valueOfHashMap!=null){
//							log.error("compareBeanTypeToAllowedListTypes true " + valueOfHashMap.getClass().getName());
							String valueTypeOfHashMap = valueOfHashMap.getClass().getName();

							if (this.compareTypeNameToAllowedListTypes(fieldType.getName())) {
								Map<?, ?> m = (Map<?, ?>) valueOfHashMap;
								for (Iterator<?> it = m.keySet().iterator();it.hasNext();) {
									String key = it.next().toString();
//									log.error("key: "+key);
									@SuppressWarnings("unused")
									Object listObject = m.get(key);
//									log.error("listObject: "+listObject);
//									log.error("listObject: "+listObject.getClass().getName());

								}

							}
						}

					//otherwise do it as Object
					}else if (fieldType.getName().equals("java.util.List")) {
						try {
							JSONArray arrayValue = (JSONArray) values
									.get(fieldName);

							Type mapMainType = anyField.getGenericType();
							// 为锟斤拷确锟斤拷锟斤拷全转锟斤拷锟斤拷使锟斤拷instanceof
							if (mapMainType instanceof ParameterizedType) {
								// 执锟斤拷强锟斤拷锟斤拷锟斤拷转锟斤拷
								ParameterizedType parameterizedType = (ParameterizedType) mapMainType;
								// 锟斤拷取锟斤拷锟斤拷锟斤拷锟斤拷息锟斤拷锟斤拷Map
								Type basicType = parameterizedType.getRawType();
								System.out.println("锟斤拷锟斤拷锟斤拷为锟斤拷" + basicType);
								// 锟斤拷取锟斤拷锟斤拷锟斤拷锟酵的凤拷锟酵诧拷锟斤拷
								Type[] types = parameterizedType
										.getActualTypeArguments();
								Type type=types[0];
								for (int i = 0; i < types.length; i++) {
									System.out.println("锟斤拷" + (i + 1)
											+ "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟角ｏ拷" + types[i]);
								}
									LinkedHashMap<String,Object> methodSummery = structuredMethodMap.get(fieldName);
									if (methodSummery!=null) {
										List list=null;
										if (methodSummery.get("getter")!=null) {
											String methodGetterName = methodSummery.get("getter").toString();
											Method getMethod = targetClass.getMethod(methodGetterName);
											list=(List) getMethod.invoke(returnObject);
										}
										if (methodSummery.get("setter")!=null) {
											if(list==null)list=new ArrayList();
											String methodSetterName = methodSummery.get("setter").toString();
											Class<?>[] paramTypes = (Class[]) methodSummery.get("setterParamTypes");
											Method m = targetClass.getMethod(methodSetterName, paramTypes);

											Class<?> paramType = paramTypes[0];
											for(int i=0;i<arrayValue.length();i++){
												if (this.compareTypeNameToBasicTypes(type.getClass().getName())) {
													Object t=arrayValue.get(i);
													if (t!=null && !paramType.getName().equals(t.getClass().getName())){
														for (Constructor<?> crt : paramType.getConstructors()) {
															if (crt.getParameterTypes()[0].getName().equals("java.lang.String")){
																t = crt.newInstance(t.toString());
															}
														}
													}
													if (paramType.isPrimitive() && t==null){
														//cannot cast null to primitve
													} else {
														list.add(t);
													}
												}else if (this.compareTypeNameToAllowedListTypes(type.getClass().getName())) {

												}else{
													JSONObject jo=arrayValue.getJSONObject(i);
													Class<?> typeClass=getClazz(type,0);
													Object obj=this.castByGivenObject(jo,typeClass);
													if(obj!=null){
														list.add(obj);
													}
												}
											}
											Object[] arguments = new Object[]{list};
											m.invoke(returnObject,arguments);
										}
									}
							} else {
								System.out.println("锟斤拷取锟斤拷锟斤拷锟斤拷锟酵筹拷锟斤拷!");
							}
							//Todo: Cast Set to Object
							//						log.error("compareBeanTypeToAllowedListTypes true " + fieldType.getName());
							//						log.error("compareBeanTypeToAllowedListTypes true " + fieldName);
							Object valueOfHashMap = values.get(fieldName);
							if (valueOfHashMap != null) {
								//							log.error("compareBeanTypeToAllowedListTypes true " + valueOfHashMap.getClass().getName());
								String valueTypeOfHashMap = valueOfHashMap
										.getClass().getName();

								if (this.compareTypeNameToAllowedListTypes(valueTypeOfHashMap)) {
									Map<?, ?> m = (Map<?, ?>) valueOfHashMap;
									for (Iterator<?> it = m.keySet().iterator(); it
											.hasNext();) {
										String key = it.next().toString();
										//									log.error("key: "+key);
										@SuppressWarnings("unused")
										Object listObject = m.get(key);
										//									log.error("listObject: "+listObject);
										//									log.error("listObject: "+listObject.getClass().getName());

									}

								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					//otherwise do it as Object
					} else {

//						log.error("otherwise do it as Object "+fieldType.getName());

						JSONObject valueOfHashMap = values.getJSONObject(fieldName);
						if (valueOfHashMap!=null){
							String valueTypeOfHashMap = valueOfHashMap.getClass().getName();

							if (this.compareTypeNameToAllowedListTypes(valueTypeOfHashMap)) {

								log.error(valueTypeOfHashMap);
								log.error(fieldType.getName());

								//Get value from  set
								@SuppressWarnings("unchecked")
								Object t = this.castByGivenObject(valueOfHashMap, fieldType);
								int mod = anyField.getModifiers();

								if (Modifier.isPrivate(mod) && !Modifier.isFinal(mod)){

									//log.info("is private so get setter method "+fieldName);
									LinkedHashMap<String,Object> methodSummery = structuredMethodMap.get(fieldName);

									if (methodSummery!=null) {
										if (methodSummery.get("setter")!=null) {

											String methodSetterName = methodSummery.get("setter").toString();
											Class<?>[] paramTypes = (Class[]) methodSummery.get("setterParamTypes");
											Method m = targetClass.getMethod(methodSetterName, paramTypes);

											Class<?> paramType = paramTypes[0];
											//log.error("paramType: "+paramType.getName());
											if (paramType.isPrimitive() && t==null){
												//cannot cast null to primitve
											} else {
												Object[] arguments = new Object[]{ t };
												m.invoke(returnObject,arguments);
											}

										} else {
											log.error("could not find a setter-method from Structured table. Is there a setter-method for " + fieldName + " in Class " + targetClass.getName());
										}
									} else {
										log.error("could not find a method from Structured table. Is there a method for " + fieldName + " in Class " + targetClass.getName());
									}
								} else if (Modifier.isPublic(mod) && !Modifier.isFinal(mod)){

									//Is public attribute so set it directly
									anyField.set(returnObject, t);

								} else if (Modifier.isFinal(mod)) {
									log.error("Final attributes cannot be changed ");
								} else {
									log.error("Unhandled Modifier Type: " + mod);
								}

							}else{
								Object t = this.castByGivenObject(valueOfHashMap, fieldType);
									int mod = anyField.getModifiers();

								if (Modifier.isPrivate(mod) && !Modifier.isFinal(mod)){

									//log.info("is private so get setter method "+fieldName);
									LinkedHashMap<String,Object> methodSummery = structuredMethodMap.get(fieldName);

									if (methodSummery!=null) {
										if (methodSummery.get("setter")!=null) {

											String methodSetterName = methodSummery.get("setter").toString();
											Class<?>[] paramTypes = (Class[]) methodSummery.get("setterParamTypes");
											Method m = targetClass.getMethod(methodSetterName, paramTypes);

											Class<?> paramType = paramTypes[0];
											//log.error("paramType: "+paramType.getName());
											if (paramType.isPrimitive() && t==null){
												//cannot cast null to primitve
											} else {
												Object[] arguments = new Object[]{ t };
												m.invoke(returnObject,arguments);
											}

										} else {
											log.error("could not find a setter-method from Structured table. Is there a setter-method for " + fieldName + " in Class " + targetClass.getName());
										}
									} else {
										log.error("could not find a method from Structured table. Is there a method for " + fieldName + " in Class " + targetClass.getName());
									}
								} else if (Modifier.isPublic(mod) && !Modifier.isFinal(mod)){
									//Is public attribute so set it directly
									anyField.set(returnObject, t);

								}
							}
						} else {
							//There is no nested Object for that given
							log.error("There is no nested Object for that given: Attribute: " + fieldName + " Class " + targetClass.getName());
						}
					}

				}
			}
			return (T) returnObject;
		} catch (Exception ex) {
			log.error("[castByGivenObject]: " ,ex);
		}
		return null;
	}

	private boolean compareTypeNameToBasicTypes(String fieldTypeName) {
		try {

			for (Iterator<String> it = CastBasicTypes.getCompareTypesSimple().iterator();it.hasNext();) {
				if (fieldTypeName.equals(it.next())) return true;
			}

			return false;
		} catch (Exception ex) {
			log.error("[compareTypeNameToBasicTypes]",ex);
			return false;
		}
	}

	private boolean compareTypeNameToAllowedListTypes(String fieldTypeName) {
		try {
			//log.error("compareTypeNameToAllowedListTypes"+ fieldTypeName);
			for (Iterator<String> it = CastBasicTypes.getAllowedListTypes().iterator();it.hasNext();) {
				if (fieldTypeName.equals(it.next())) return true;
			}

			return false;
		} catch (Exception ex) {
			log.error("[compareTypeNameToBasicTypes]",ex);
			return false;
		}
	}
	public static Class getClazz(Type type, int i) {
        if (type instanceof ParameterizedType) { // 锟斤拷锟�锟斤拷锟斤拷锟斤拷
            return getGenericClass((ParameterizedType) type, i);
        } else if (type instanceof TypeVariable) {
            return getClazz(((TypeVariable) type).getBounds()[0], 0); // 锟斤拷锟�锟酵诧拷锟矫讹拷锟斤拷
        } else {// class锟斤拷锟斤拷也锟斤拷type锟斤拷强锟斤拷转锟斤拷
            return (Class) type;
        }
    } 

    private static Class getGenericClass(ParameterizedType parameterizedType, int i){
        Object genericClass = parameterizedType.getActualTypeArguments()[i];
        if (genericClass instanceof ParameterizedType) { 
        } else if (genericClass instanceof GenericArrayType) { 
            return (Class) ((GenericArrayType) genericClass).getGenericComponentType();
        } else if (genericClass instanceof TypeVariable) { 
            return getClazz(((TypeVariable) genericClass).getBounds()[0], 0);
        } else {
            return (Class) genericClass;
        }
        return null;
    }
}
