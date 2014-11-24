package com.afekavote.memory;

import java.util.ArrayList;

public class MemoryKeys {
	
	public static String USER = "user";
	public static String yaer = "2014";
	public static String semester = "summer";
	public static String className = "IOS";
	public static String phoneID;
	
	public static ArrayList<String> CATEGORIES; 
	public static ArrayList<String> APPS;
	
	static{
		CATEGORIES = new ArrayList<String>();
		APPS = new ArrayList<String>();
	}
	
	public static int BASE_ID = 821984; 
	
	// communication
	public final static String API_URL = "http://198.57.247.131/~webi/afeka_vote/";
	public final static  String API_FILE = "vote.php";
	public static final String API_SUCCESS = "success";
	public static final String API_TAG = "tag";
	public static final String ERROR = "error";
	public static final String API_VOTE_TAG = "vote_for_app";
	public static final String API_APP_NAME = "app_name";
	public static final String API_VOTES = "votes";
}
