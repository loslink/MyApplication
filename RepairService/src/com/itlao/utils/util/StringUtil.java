package com.itlao.utils.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import android.util.Log;

public class StringUtil {
	// ѹ��
	public static String compress(String str) throws IOException {
		long t1 = System.currentTimeMillis();
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(str.getBytes());
		gzip.close();
		String rs = out.toString("ISO-8859-1");
		long t2 = System.currentTimeMillis();
		System.out.println("compress String coast time:" + (t2 - t1));
		return rs;
	}

	// ��ѹ��
	public static String uncompress(String str) throws IOException {
		long t1 = System.currentTimeMillis();
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(
				str.getBytes("ISO-8859-1"));
		GZIPInputStream gunzip = new GZIPInputStream(in);
		byte[] buffer = new byte[256];
		int n;
		while ((n = gunzip.read(buffer)) >= 0) {
			out.write(buffer, 0, n);
		}
		// toString()ʹ��ƽ̨Ĭ�ϱ��룬Ҳ������ʽ��ָ����toString("GBK")
		String rs = out.toString();
		long t2 = System.currentTimeMillis();
		System.out.println("uncompress String coast time:" + (t2 - t1));
		return rs;
	}

	public static String uncompress(byte[] bytes) throws IOException {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		// System.out.println("bytes..length:"+bytes.length);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		GZIPInputStream gunzip = new GZIPInputStream(in);
		byte[] buffer = new byte[256];
		int n;
		while ((n = gunzip.read(buffer)) >= 0) {
			out.write(buffer, 0, n);
		}
		// toString()ʹ��ƽ̨Ĭ�ϱ��룬Ҳ������ʽ��ָ����toString("GBK")
		String rs = out.toString("GBK");
		return rs;
	}
	
	/*
	 * ����(��ֹ��������)
	 * */
	public static String encode(String value){
		String result = "";
		try {
			 result = java.net.URLEncoder.encode(value, "utf-8");
		} catch (Exception e) {
			Log.e("StringUtil", e.getMessage());
		}
		return result;
	}
}
