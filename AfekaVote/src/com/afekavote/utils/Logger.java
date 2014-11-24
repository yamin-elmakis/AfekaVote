package com.afekavote.utils;

import android.util.Log;
import android.widget.Toast;

import com.afekavote.application.Program;

public class Logger {

	private static boolean isLoggerOn = true;
	
	public static void log (String log){
		if (!isLoggerOn)
			return;
		if (log == null)
			log = "null";
		Log.e(Program.getCurActivity().getLocalClassName(), log);
	}
	
	public static void log (String tag, String log){
		if (!isLoggerOn)
			return;
		if (log == null)
			log = "null";
		Log.e(tag, log);
	}
	public static void shortToast(String text){
		Toast.makeText(Program.getCurActivity(), text,Toast.LENGTH_SHORT).show();
	}
	public static void longToast(String text){
		Toast.makeText(Program.getCurActivity(), text,Toast.LENGTH_LONG).show();
	}
}
