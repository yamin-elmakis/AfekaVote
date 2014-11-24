package com.afekavote.memory;

import java.util.HashMap;

import android.R.integer;

import com.afekavote.application.Program;

/**
 * This class is the interface for the program data, it keeps the names and the
 * pointers for the data, and implements the method that can be received from
 * it.
 */
public class DataManager {

	private DB sDataBase;
	private MemoryPref memoryPref;
	private static DataManager instance;
	
	private DataManager() {
		sDataBase = DB.getInstance(Program.getAppContext());
		memoryPref = MemoryPref.getInstance();
	}
	
	public static DataManager getInstance(){
		if (instance == null)
			instance = new DataManager();
		return instance;
	}

	public void saveApp(String appName, HashMap<String, Integer> grades){
		memoryPref.saveAppCategories(appName, grades);
	}
	
	public HashMap<String, Integer> getAppCategories(String appName){
		return memoryPref.getAppCategories(appName);
	}
	
	/**
	 * getAppCategory
	 * @param appKey format : appName_category
	 * */
	public int getAppCategory(String appKey){
		return memoryPref.getAppCategory(appKey);
	}
	// **************************************************************

	

}
