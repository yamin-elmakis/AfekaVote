package com.afekavote.application;

import java.io.UnsupportedEncodingException;

import com.afekavote.communication.volly.MyVolley;
import com.afekavote.memory.MemoryKeys;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class Program extends Application{
	
	private static Program instance;
	public static FragmentActivity curActivity;
	private static Fragment curFragment;
	
	@Override
	public void onCreate() {
		super.onCreate();
		init();	
	}
	
	 private void init() {
		 MyVolley.init(this);
		 instance = this;
	}

	 public static Context getAppContext(){
		 return instance.getApplicationContext();
	 }

	public static FragmentActivity getCurActivity() {
		return curActivity;
	}

	public static void setCurActivity(FragmentActivity curActivity) {
		Program.curActivity = curActivity;
	}

	public static Fragment getCurFragment() {
		return curFragment;
	}

	public static void setCurFragment(Fragment curFragment) {
		Program.curFragment = curFragment;
	}
	 
	public static String getUserUinqId() {
		String android_id = Secure.getString(getAppContext().getContentResolver(), Secure.ANDROID_ID);
		try {
			String user_id = toMD5Encoder(android_id);
			return user_id;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static String toMD5Encoder(String md5) throws UnsupportedEncodingException {
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes("UTF-8"));
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
						.substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}
}
