/**
 * @fileName ICmd.java[v1.0]
 * @classes com.eshine.core.web.ICmd
 * @author qiushuzhong 
 * @crateAt 2012-7-11 ����9:31:39
 */
package com.itlao.utils.cmd;

import java.util.List;

import org.apache.http.message.BasicNameValuePair;

/**

 * <p>Copyright: 2012 . All rights reserved.</p>
 * <p>Company: Eshine</p>
 * <p>CreateDate:2012-7-11</p>
 * @author qiushuzhong 
 * @version 1.0
 *
 * <p>-------------------------------------------------------------------</p>
 * <p>Date              Mender              Reason</p>
 * <p>2012-7-11         qiushuzhong            add</p>
 */

public interface ICmd {
	public List<BasicNameValuePair> getParams(); 
}
