/**
 *
 */
package com.itlao.utils.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.Toast;

/**
 *
 * <p>
 * Title: ����ȷ�϶Ի���
 * </p>
 * <p>
 * Description: �����ϸ�����ｿ
 * </p>
 * <p>
 * Copyright: 2012 . All rights reserved.
 * </p>
 * <p>
 * Company: Eshine
 * </p>
 * <p>
 * CreateDate:2012-7-9
 * </p>
 *
 * @author qiushuzhong
 * @version 1.0
 *
 *          <p>
 *          -------------------------------------------------------------------
 *          </p>
 *          <p>
 *          Date Mender Reason
 *          </p>
 *          <p>
 *          2012-7-9 qiushuzhong add
 *          </p>
 */
public class DialogUtil {
	// ����һ����ʾ��Ϣ�ĶԻ���,closeSelfΪ�Ƿ�رյ�ǰ���ｿ
	public static void showDialog(final Context ctx, String msg,
			boolean closeSelf) {
		// ����һ��AlertDialog.Builder����
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx).setMessage(
				msg).setCancelable(false);
		if (closeSelf) {
			builder.setPositiveButton("ȷ��", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// ����ǰActivity
					((Activity) ctx).finish();
				}
			});
		} else {
			builder.setPositiveButton("ȷ��", null);
		}
		builder.create().show();
	}

	// ����һ����ʾָ������ĶԻ��ｿ
	/**
	 *
	 * ����˵�� ����ϸ��˵��
	 *
	 * @param propertyName
	 *            the property name.
	 * @return true if there are ore or more listeners for the given property
	 * @see �����Ŀ���������ｿ
	 * @since 1.1
	 *
	 *        <p>
	 *        ----------------------------------------------------------------
	 *        </p>
	 *        <p>
	 *        Date Mender Reason
	 *        </p>
	 *        <p>
	 *        2012-7-9 qiushuzhong
	 *        </p>
	 */
	public static void showDialog(Context ctx, View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
				.setView(view).setCancelable(false)
				.setPositiveButton("ȷ��", null);
		builder.create().show();
	}

	public static void showDialog(Context ctx,String title,String msg){
		new AlertDialog.Builder(ctx)
		.setTitle(title)
		.setMessage(msg)
		.setPositiveButton("ȷ��", null)
		.show();
	}


	/**
	 *
	 * ��ʾ�ı�
	 *
	 * @param
	 * @return
	 * @see �����Ŀ���������ｿ
	 * @since 1.1
	 *
	 *        <p>
	 *        ----------------------------------------------------------------
	 *        </p>
	 *        <p>
	 *        Date Mender Reason
	 *        </p>
	 *        <p>
	 *        2012-8-10 qiushuzhong
	 *        </p>
	 */
	public static void showTips(Context ctx, String message) {
		Toast.makeText(ctx, message, 5000).show();
	}

	public static ProgressDialog progressDlg(Context ctx, String prompt) {
		ProgressDialog	progressDialog = ProgressDialog.show(ctx, "���Ե�...",
				prompt, true);
		progressDialog.setCancelable(true);
		return progressDialog;
	}
}
