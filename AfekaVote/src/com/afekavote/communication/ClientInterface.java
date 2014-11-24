package com.afekavote.communication;

import java.util.HashMap;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public interface ClientInterface {
	
	public boolean voteForApp(String appName, final HashMap<String, Integer> catgVotes,
			Listener<String> listener);
	
	public boolean getAppsNames(Listener<String> listener, ErrorListener err);

	public boolean getCategoriesNames(Listener<String> listener, ErrorListener err);

	public boolean getCategoriesApps(Listener<String> listener, ErrorListener err);

	
}
