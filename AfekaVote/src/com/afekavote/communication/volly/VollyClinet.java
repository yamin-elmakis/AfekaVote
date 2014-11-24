package com.afekavote.communication.volly;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.afekavote.activitys.ErrorDialog;
import com.afekavote.activitys.MyProgressDialog;
import com.afekavote.application.Program;
import com.afekavote.communication.ClientInterface;
import com.afekavote.memory.MemoryKeys;
import com.afekavote.utils.Logger;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

public class VollyClinet implements ClientInterface{

	private final String TAG = "VollyClinet";
	private static VollyClinet instance;
	
	private VollyClinet(){ }

	public static VollyClinet getInstance(){
		if (instance == null)
			instance = new VollyClinet();
		return instance;
	}
	
//	@Override
	public void sendUserInfo(final String id, final String apps,
			final String accunts, Listener<String> listener, ErrorListener err) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(MemoryKeys.API_TAG, "register");
		params.put("id", id);
		params.put("apps", apps);
		params.put("accunts", accunts);
//		sendStringRequest(Constans.API_USERS, params, listener, err);
	}
	
	@Override
	public boolean getAppsNames(Listener<String> listener, ErrorListener err) {
		Map<String, String> params = getBasicParams();
		params.put(MemoryKeys.API_TAG, "get_apps_names");
		return sendStringRequest(MemoryKeys.API_FILE, params, listener, err);
	}
	
	@Override
	public boolean getCategoriesNames(Listener<String> listener, ErrorListener err) {
		Map<String, String> params = getBasicParams();
		params.put(MemoryKeys.API_TAG, "get_categories_names");
		return sendStringRequest(MemoryKeys.API_FILE, params, listener, err);
	}
	
	@Override
	public boolean getCategoriesApps(Listener<String> listener, ErrorListener err) {
		Map<String, String> params = getBasicParams();
		params.put(MemoryKeys.API_TAG, "get_apps_catgs");
		return sendStringRequest(MemoryKeys.API_FILE, params, listener, err);
	}

	@Override
	public boolean voteForApp(String appName, HashMap<String, Integer> catgVotes, Listener<String> listener) {
		Map<String, String> params = getBasicParams();
		params.put(MemoryKeys.API_TAG, MemoryKeys.API_VOTE_TAG);
		params.put(MemoryKeys.API_APP_NAME, appName);
		Gson gson = new Gson();
		String votesSTR = gson.toJson(catgVotes);
		Log.e(TAG,"votesSTR: "+votesSTR );
		params.put(MemoryKeys.API_VOTES, votesSTR);
		return sendStringRequest(MemoryKeys.API_FILE, params, listener, createReqErrorListener());
	}

	private Map<String, String> getBasicParams() {
		Map<String, String> basicParams = new HashMap<String, String>();
		basicParams.put("phoneID", MemoryKeys.phoneID);
		basicParams.put("class", MemoryKeys.className);
		basicParams.put("semester", MemoryKeys.semester);
		basicParams.put("year", MemoryKeys.yaer);
		return basicParams;
	}

	private Response.ErrorListener createReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	Logger.log("VolleyError: " + error.getMessage());
            	MyProgressDialog.hideDialog();
            	ErrorDialog errorDialog = new ErrorDialog(Program.getCurActivity());
            	errorDialog.setMessage("something went wrong.\nplease try again");
            	errorDialog.show();
            }
        };
    }
	public StringRequest GetVolleyStringRequest(int method, String baseUrl, 
			final Map<String, String> params, Listener listener, ErrorListener err) {
		String url = baseUrl;

		if (method == Request.Method.GET) {
			StringBuilder queryString = new StringBuilder();
			String prefix = "?";
			for (String k : params.keySet()) {
				queryString.append(prefix);
				queryString.append(k);
				queryString.append("=");
				try {
					queryString.append(URLEncoder.encode(params.get(k), "utf-8"));
				} catch (UnsupportedEncodingException e) {
					Log.e(TAG, "UnsupportedEncodingException: "+e.getMessage());
				}
				prefix = "&";
			}
			url += queryString.toString();
		}

		StringRequest myStringReq = new StringRequest(method, url, listener, err) {

			protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
				return params;
			};
		};
		return myStringReq;
	}

    private static boolean isNetworkConnected() {
  	  ConnectivityManager cm = (ConnectivityManager)Program.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
  	  NetworkInfo ni = cm.getActiveNetworkInfo();
  	  if (ni == null) {
  		  // There are no active networks.
  		  Toast.makeText(Program.getAppContext(), "There is no internet connection", Toast.LENGTH_LONG).show();
  		  return false;
  	  } else
  		  return true;
    }

    public boolean sendStringRequest(String fileName, Map<String,String> params, Response.Listener<String> responseListener, ErrorListener errorListener){
    	if (!isNetworkConnected()){
			return false;
    	}
    	
    	String path = MemoryKeys.API_URL;
    	StringRequest sReq = GetVolleyStringRequest(Request.Method.POST, path + fileName, params, responseListener, errorListener);
    	RequestQueue queue = MyVolley.getRequestQueue();
		// change the retry policy- change the timeout to 8*default. and set the retries times to 0
//		sReq.setRetryPolicy(new DefaultRetryPolicy(8*DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    	queue.add(sReq);
    	return true;
    }
}
