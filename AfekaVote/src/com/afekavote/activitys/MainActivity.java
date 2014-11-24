package com.afekavote.activitys;

import java.util.ArrayList;
import java.util.HashSet;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.afekavote.application.Program;
import com.afekavote.communication.ClientInterface;
import com.afekavote.communication.volly.VollyClinet;
import com.afekavote.memory.MemoryKeys;
import com.afekavote.utils.Logger;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gameframwork.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends FragmentActivity implements OnClickListener{

	private static final String TAG = "MainActivity";
	private ArrayList<VoteFrag> voteFrags;
	private ArrayList<Button> bMenu;
	private LinearLayout llMenu;
	private int catCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Program.setCurActivity(this);
		MemoryKeys.phoneID = Program.getUserUinqId();
		Log.e("ID", "android_id: "+MemoryKeys.phoneID);
		setContentView(R.layout.activity_main);
		getAppsCatgs();
	}

	private void getAppsCatgs() {
		ClientInterface client = VollyClinet.getInstance();
		MyProgressDialog.showDialog();
		if (!client.getCategoriesApps(new initResponse(), new initError())){
			// no Network Connection
			MyProgressDialog.hideDialog();
		}
	}

	private void getLayoutRes() {
		llMenu = (LinearLayout) findViewById(R.id.main_ll_menu);
	}
	
	private void initRes() {
		bMenu = new ArrayList<Button>();
		voteFrags = new ArrayList<VoteFrag>();			
		catCount = MemoryKeys.APPS.size();
		
		for (int i = 0; i < catCount; i++) {
			VoteFrag frag = new VoteFrag();
			frag.initFrag(MemoryKeys.APPS.get(i));
			voteFrags.add(frag);
			
			Button bCat = new Button(MainActivity.this);
			bCat.setText(MemoryKeys.APPS.get(i));
			bCat.setId(MemoryKeys.BASE_ID + i);
			bCat.setOnClickListener(this);
			llMenu.addView(bCat);
		}
	}
	
	private void showFregment(VoteFrag newFreg) {
		String backStateName = newFreg.getFragAppName();

		FragmentManager manager = getSupportFragmentManager();
		boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

		// fragment not in back stack, create it.
		if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null) {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			
			ft.setCustomAnimations(R.anim.slide_left_to_right,
					R.anim.slide_right_to_left, R.anim.slide_left_to_right,
					R.anim.slide_right_to_left);
			
			ft.replace(R.id.main_vote_frame, newFreg);
			
			ft.commit();
		}

		Program.setCurFragment(newFreg);
	}
	
	public void addViews() {
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View btn) {
		for (int i = 0; i < catCount; i++) {
			if (btn.getId() == MemoryKeys.BASE_ID + i){
				showFregment(voteFrags.get(i));
			}
		}
	}

	private class initResponse implements Response.Listener<String>{

		@Override
		public void onResponse(String response) {
			MyProgressDialog.hideDialog();
			Logger.log("response: " + response);
			try {
				JSONObject jsonResponse = new JSONObject(response);
				if (jsonResponse.getBoolean(MemoryKeys.API_SUCCESS) && 
						(jsonResponse.getString(MemoryKeys.API_TAG).equals("get_apps_catgs"))){
					Gson gson = new Gson();
					String appsStr = jsonResponse.getString("apps");
					Logger.log("appsStr: " + appsStr);
					if (appsStr != null) {
						MemoryKeys.APPS = gson.fromJson(appsStr, new TypeToken<ArrayList<String>>() {}.getType());
					}
					String ctgStr = jsonResponse.getString("categories");
					Logger.log("ctgStr: " + ctgStr);
					if (ctgStr != null) {
						MemoryKeys.CATEGORIES = gson.fromJson(ctgStr, new TypeToken<ArrayList<String>>() {}.getType());
					}
					if (isResponseOK()) {
						// all good
						getLayoutRes();
						initRes();
						showFregment(voteFrags.get(0));
					}
				}
			} catch (JSONException e) {
				Logger.log("JSONException: " + e.getMessage());
				ErrorDialog.showResponseError();
			}
		}

		private boolean isResponseOK() {
			boolean isOK = true;
			String error = "";
			if (MemoryKeys.APPS.size() < 1){
				error = "there are no apps";
				isOK = false;
			}
			if (MemoryKeys.CATEGORIES.size() < 1){
				error = isOK ? "there are no categories" : "there no apps and no categories";
				isOK = false;
			}
			if (!isOK) {
				ErrorDialog errorDialog = new ErrorDialog(Program.getCurActivity());
				errorDialog.setMessage(error);
				errorDialog.show();
			}
			return isOK;
		}
	}
	
	private class initError implements Response.ErrorListener {

		@Override
		public void onErrorResponse(VolleyError error) {
			Log.e(TAG, "Volley initError: " + error.getMessage());
        	MyProgressDialog.hideDialog();
        	ErrorDialog errorDialog = new ErrorDialog(Program.getCurActivity());
        	errorDialog.setMessage("something went wrong.\nplease try again");
        	errorDialog.show();	
		}
    }
}
