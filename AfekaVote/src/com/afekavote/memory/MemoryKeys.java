package com.afekavote.memory;

import java.util.ArrayList;

import com.gameframwork.R;

public class MemoryKeys {
	
	public static int[] BACK_COLORS = {R.color.yellow, R.color.azure_clear, R.color.blue, R.color.azure, R.color.pink};
	public static int[] FRONT_COLORS = {R.color.yellow2, R.color.azure_clear2, R.color.blue2, R.color.azure2, R.color.pink2};
	public static String BACK_COLOR = "back_color";
	public static String FRONT_COLOR = "front_color";
	public static String USER = "user";
	public static String yaer = "2014";
	public static String semester = "summer";
	public static String className = "IOS";
	public static String phoneID;
	public static int MAX_GRADE = 10;
	public static int APPS_COUNT = 1;
	public static int VOTERS_COUNT = 1;
	
	public static ArrayList<String> CATEGORIES; 
	public static String[] APPS;
	
	static{
		CATEGORIES = new ArrayList<String>();
	}
	
	public static int BASE_ID = 821984; 
	
	// communication
	public final static String API_URL = "http://198.57.247.131/~webi/afeka_vote/";
	public final static String API_FILE = "vote.php";
	public static final String API_SUCCESS = "success";
	public static final String API_TAG = "tag";
	public static final String ERROR = "error";
	public static final String API_APP_NAME = "app_name";
	public static final String API_VOTES = "votes";
	
	public static final String API_VOTE_TAG = "vote_for_app";
	public static final String API_GET_GRADES = "get_apps_grades";
	public static final String API_CHECK_STATUS = "check_status";
	public static final String API_APPS_GRADES = "apps_grades";
	public static final String API_APPS_COUNT = "apps_count";
	public static final String API_VOTERS_COUNT = "voters_count";
}
