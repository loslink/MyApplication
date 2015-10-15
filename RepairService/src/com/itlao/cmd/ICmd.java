package com.itlao.cmd;

import java.util.List;

import org.apache.http.message.BasicNameValuePair;

/**
 * <p>--------------------------------------------------------------</p>
 * <p>Date                            Reason</p>
 * <p>2012-7-11                    add</p>
 */

public interface ICmd {
	public List<BasicNameValuePair> getParams(); 

}
