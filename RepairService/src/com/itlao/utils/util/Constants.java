package com.itlao.utils.util;

import java.util.List;


public final class Constants {
	public static String servicePath=Config.getWebUrl();
	public static String key="CBBD46C0A3F21E3642257103D68706605F8DE923";
	public static String userCode;
	
	public static String mapFuncs;
	public static boolean offLine=false;
	
	public static final String ORDER_ACCEPT ="order_status2";
	public static final String ORDER_LEADER ="order_status0";
	public static final String ORDER_SEND ="order_status1";
	public static final String ORDER_AUDIT ="order_status-1";
	public static final String ORDER_REFUSE ="order_status-2";
	public static final String ORDER_REPLY ="order_status3";
	
	public static final String GEN_STORAGE = "generator_state4";
	public static final String GEN_CLAIM = "generator_state3";
	public static final String GEN_SEND = "generator_state2";
	public static final String GEN_BUSY = "generator_state1";
	public static final String GEN_FREE = "generator_state0";
	
	public static  boolean[] layer = { false, false,false };
	
}
 