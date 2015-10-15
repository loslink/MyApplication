package com.itlao.repairservice.utils;

import com.itlao.repairservice.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Send_Notification {

	private Context m_context;
	Class<?> toClass;
	Intent intent;

	public Send_Notification(Context context, Class<?> cls) {

		m_context = context;
		toClass = cls;
	}

	public void sendNotification(String contentTitle, String tickerText,
			String contentText) {

		/*
		 * String tickerText="哈哈哈"; String contentTitle="通知"; String
		 * contentText="您收到了一条消息";
		 */

		/*
		 * String m_contentTitle=contentTitle; String m_tickerText=tickerText;
		 * String m_contentText=contentText;
		 */
		int icon = R.drawable.logo;

		Notification notification = new Notification(icon, tickerText,
				System.currentTimeMillis());
		intent = new Intent(m_context, toClass);
		PendingIntent pendingIntent = PendingIntent.getActivity(m_context, 10,
				intent, 0);
		notification.setLatestEventInfo(m_context, contentTitle, contentText,
				pendingIntent);
		notification.defaults = Notification.DEFAULT_SOUND;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		NotificationManager manager = (NotificationManager) m_context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(100, notification);

	}

	public void sendUserNotification(String contentTitle, String tickerText,
			String contentText, Intent intent) {

		int icon = R.drawable.logo;

		Notification notification = new Notification(icon, tickerText,
				System.currentTimeMillis());
		// intent=new Intent(m_context,toClass);
		intent.setData(Uri.parse("custom://"+System.currentTimeMillis()));
		PendingIntent pendingIntent = PendingIntent.getActivity(m_context, 10,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(m_context, contentTitle, contentText,
				pendingIntent);
		notification.defaults = Notification.DEFAULT_SOUND;
		
		
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		NotificationManager manager = (NotificationManager) m_context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(100, notification);

	}

}
