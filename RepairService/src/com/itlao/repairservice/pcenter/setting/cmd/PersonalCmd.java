
package com.itlao.repairservice.pcenter.setting.cmd;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;


import com.itlao.cmd.ICmd;

/**
 * package com.itlao.repairservice.pcenter.setting.cmd;
 * @Laoqilian
 * 2015-1-25 下午6:14:19
 */
public class PersonalCmd implements ICmd {

	
	public static String userName;
	public static String passWord;


	List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	

	@Override
	public List<BasicNameValuePair> getParams() {
		
		params.add(new BasicNameValuePair("userCode", this.userName));
		params.add(new BasicNameValuePair("password", this.passWord));
		
		
		return params;
	}

	
	
}
