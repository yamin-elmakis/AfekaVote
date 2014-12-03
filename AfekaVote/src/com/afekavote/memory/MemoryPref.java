package com.afekavote.memory;

import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.content.SharedPreferences;

import com.afekavote.application.Program;
import com.afekavote.objects.User;
import com.afekavote.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MemoryPref {

	private static final String PREFS_NAME = "AfekaVotePrefsFile";
	private static final String TAG = "MemoryPref";
	private Context context;
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	private static MemoryPref memo;

	public static MemoryPref getInstance(){
		if (memo==null){
			memo = new MemoryPref(Program.getAppContext());
		}
		return memo;
	}
	
	private MemoryPref(Context context) {
		init(context);
	}

	private void init(Context context) {
		settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		editor = settings.edit();
	}

	private void add(String key, String value) {
		editor.putString(key, value);
		editor.commit();

	}

	private void add(String key, int value) {
		editor.putInt(key, value);
		editor.commit();

	}

	private String get(String key) {
		String get = settings.getString(key, "");
		if (get.equals(""))
			return null;
		return get;
	}

	public void clearAll() {
		editor.clear();
		editor.commit();
	}

	public void saveAppCategories(String appName, HashMap<String, Integer> grades){
		Iterator it = grades.entrySet().iterator();
	    while (it.hasNext()) {
	    	HashMap.Entry pairs = (HashMap.Entry)it.next();
	    	String key = appName+"_"+pairs.getKey();
	    	String value = pairs.getValue().toString();
	    	Logger.log(TAG,"saveAppCategories: "+ key + " = " + value);
	        add(key, value);
//	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}
	
	public HashMap<String, Integer> getAppCategories(String appName){
		MemoryKeys.CATEGORIES.size();
		HashMap<String, Integer> grades = null;
		for (String category : MemoryKeys.CATEGORIES) {
			int grade;
			try {
				grade = Integer.parseInt(get(appName+"_"+category));
			} catch (NumberFormatException e) {
				grade = 0;
			}
			grades.put(category, grade);
		}
		return grades;
	}
	public int getAppCategory(String appKey){
		int grade;
		try {
			grade = Integer.parseInt(get(appKey));
		} catch (NumberFormatException e) {
			grade = 0;
		}
		return grade;
	}
	
	public void saveUser(User user) {
		Gson gson = new Gson();
		String userSTR = gson.toJson(user);
		add(MemoryKeys.USER, userSTR);
	}

	public User getUser() {
		Gson gson = new Gson();
		String userStr = get(MemoryKeys.USER);
		if (userStr != null && !userStr.equals("")) {
			User user = gson.fromJson(userStr, new TypeToken<User>() {}.getType());
			return user;

		} else
			return null;
	}
}
