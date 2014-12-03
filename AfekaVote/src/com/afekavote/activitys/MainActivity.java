package com.afekavote.activitys;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.afekavote.application.BaseFragment;
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

import dev.dworks.libs.actionbartoggle.ActionBarToggle;

public class MainActivity extends ActionBarActivity {

	// SlidingPanel
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarToggle mDrawerToggle;
    private CharSequence mDrawerTitle, mTitle;
    private String[] appsTitles;
    
    // Afeka Vote
	private static final String TAG = "MainActivity";
	private ArrayList<BaseFragment> voteFrags;
	private ImageView yamin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		Program.setCurActivity(this);
		
		MemoryKeys.phoneID = Program.getUserUinqId();
		Logger.log(TAG, "android_id: "+MemoryKeys.phoneID);
		setContentView(R.layout.activity_main);
		
		getLayoutRes();
		// 
 		yamin = (ImageView) findViewById(R.id.yamin123);
 		yamin.setVisibility(View.GONE);
// 		
// 		yamin.getLayoutParams().height = displayMetrics.heightPixels / 100 * 30;
// 		yamin.getLayoutParams().width = displayMetrics.widthPixels / 100 * 30;
// 		AnimatorSet as = new AnimatorSet();
//		as.play(
//				
//				ObjectAnimator.ofFloat(
//						yamin,
//						"translationY",
//						-20));
//		
//		as.setDuration(1000);
//		as.start();
 		//
        getAppsCatgs();
	}

	private void getAppsCatgs() {
		ClientInterface client = VollyClinet.getInstance();
		MyProgressDialog.showDialog("Loadong apps names and categories...");
		if (!client.getCategoriesApps(new initResponse(), new initError())){
			// no Network Connection
			MyProgressDialog.hideDialog();
		}
	}

	private void checkStatus() {
		ClientInterface client = VollyClinet.getInstance();
		MyProgressDialog.showDialog("Checking competition status...");
		if (!client.checkStatus(new StatusResponse())){
			// no Network Connection
			MyProgressDialog.hideDialog();
		}
	}
	
	private void getSummary() {
		ClientInterface client = VollyClinet.getInstance();
		MyProgressDialog.showDialog("Loading results...");
		if (!client.getAppsGrades(new gradesResponse())){
			// no Network Connection
			MyProgressDialog.hideDialog();
		}
	}

	private void getLayoutRes() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mTitle = mDrawerTitle = getTitle();
		mDrawerToggle = new ActionBarToggle(
                this,                  		/* host Activity */
                mDrawerLayout,         		/* DrawerLayout object */
                R.drawable.ic_drawer_light, /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  		/* "open drawer" description for accessibility */
                R.string.drawer_close  		/* "close drawer" description for accessibility */
                ) {
        	@Override
			public void closeView() {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
				super.closeView();
			}
        	
        	@SuppressLint("NewApi")
			@Override
			public void openView() {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
				super.openView();
			}
        };
	}
	
	private void initRes() {
		appsTitles =  MemoryKeys.APPS;
		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, appsTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        if (getIntent().getExtras() == null) {
            selectItem(0);
        }		 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (voteFrags.get(position).getFragName().equals(FragSummary.ARG_NAME)){
            	checkStatus();
            } else
            	selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
    	BaseFragment newFreg = voteFrags.get(position);
		String backStateName = newFreg.getFragName();
		
		FragmentManager manager = getSupportFragmentManager();
		boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
		
		// the summary frag got more arguments
		Bundle args = newFreg.getArguments() == null ? new Bundle() : newFreg.getArguments();
		args.putInt(MemoryKeys.BACK_COLOR, MemoryKeys.BACK_COLORS[position%MemoryKeys.BACK_COLORS.length]);
		try {
			newFreg.setArguments(args);
		} catch (Exception e) {
			mDrawerLayout.closeDrawer(mDrawerList);
//			if(newFreg.getFragName().equals(FragSummary.ARG_NAME)){
//				((FragSummary) newFreg).update();
//			}
			return;
		}
		
        // fragment not in back stack, create it.
		if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null) {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			
			ft.setCustomAnimations(R.anim.slide_left_to_right,R.anim.slide_right_to_left,
					 R.anim.slide_left_to_right, R.anim.slide_right_to_left);
			
			ft.replace(R.id.content_frame, newFreg);
			
			ft.commit();
		}

		Program.setCurFragment(newFreg);
		
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(appsTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }
    
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
//        getSupportActionBar().setTitle(mTitle);
    }
    
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
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
//						MemoryKeys.APPS = gson.fromJson(appsStr, new TypeToken<String[]>() {}.getType());
						String[] apps = gson.fromJson(appsStr, new TypeToken<String[]>() {}.getType());
						// +1 for the summary fragment
						MemoryKeys.APPS = new String [apps.length+1];
						voteFrags = new ArrayList<BaseFragment>();
						for (int i = 0; i < apps.length; i++) {
							MemoryKeys.APPS[i] = apps[i];
							VoteFrag temp = new VoteFrag();
							temp.setFragName(apps[i]);
							voteFrags.add(temp);
						}
						// add the name of the summary fragment
						FragSummary summary = new FragSummary();
						summary.setFragName(FragSummary.ARG_NAME);
						voteFrags.add(summary);
						MemoryKeys.APPS[apps.length] = FragSummary.ARG_NAME;
					}
					
					String ctgStr = jsonResponse.getString("categories");
					Logger.log("ctgStr: " + ctgStr);
					if (ctgStr != null) {
						MemoryKeys.CATEGORIES = gson.fromJson(ctgStr, new TypeToken<ArrayList<String>>() {}.getType());
					}
					if (isResponseOK()) {
						// all good
						initRes();
					}
				}
			} catch (JSONException e) {
				Log.e(TAG, "JSONException: " + e.getMessage());
				ErrorDialog.showResponseError();
			}
		}

		private boolean isResponseOK() {
			boolean isOK = true;
			String error = "";
			if (MemoryKeys.APPS.length < 1){
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
	
	private class StatusResponse implements Response.Listener<String>{

		@Override
		public void onResponse(String response) {
			MyProgressDialog.hideDialog();
			Logger.log("response: " + response);
			try {
				JSONObject jsonResponse = new JSONObject(response);
				if (jsonResponse.getBoolean(MemoryKeys.API_SUCCESS) && 
						(jsonResponse.getString(MemoryKeys.API_TAG).equals(MemoryKeys.API_CHECK_STATUS))){
					int votersCount = jsonResponse.getInt(MemoryKeys.API_VOTERS_COUNT);
					Logger.log("votersCount: " + votersCount);
					MemoryKeys.VOTERS_COUNT = votersCount;
					getSummary();
				}else{
					ErrorDialog.showError("the competition isn't over yet");
				}
			} catch (JSONException e) {
				Log.e(TAG, "JSONException: " + e.getMessage());
				ErrorDialog.showResponseError();
			}
		}
	}
	
	private class gradesResponse implements Response.Listener<String>{

		@Override
		public void onResponse(String response) {
			MyProgressDialog.hideDialog();
			Logger.log("response: " + response);
			try {
				JSONObject jsonResponse = new JSONObject(response);
				if (jsonResponse.getBoolean(MemoryKeys.API_SUCCESS) && 
						(jsonResponse.getString(MemoryKeys.API_TAG).equals(MemoryKeys.API_GET_GRADES))){
					Gson gson = new Gson();
					String appsGradesStr = jsonResponse.getString(MemoryKeys.API_APPS_GRADES);
					Logger.log("appsGradesStr: " + appsGradesStr);
					if (appsGradesStr != null) {
						HashMap<String, Integer> ans = gson.fromJson(appsGradesStr, new TypeToken<HashMap<String, Integer>>() {}.getType());
						Logger.log("ans size: " + ans.size());
						Logger.log("ans: "+ans.toString());
						
						BaseFragment frag = voteFrags.get(voteFrags.size()-1);
						if (frag instanceof FragSummary){
							Logger.log("gradesResponse", "instanceof FragSummary");
							((FragSummary) frag).setResult(ans);
							((FragSummary) frag).update();
							selectItem(voteFrags.size()-1);
						}else {
							ErrorDialog errorDialog = new ErrorDialog(Program.getCurActivity());
				        	errorDialog.setMessage("something went wrong.\nplease try again");
				        	errorDialog.show();
						}
					}
				}
			} catch (JSONException e) {
				Log.e(TAG, "JSONException: " + e.getMessage());
				ErrorDialog.showResponseError();
			}
		}
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
//		Log.e(TAG, "onOptionsItemSelected : "+ item.getTitle());
		if (item.getItemId() == R.id.action_refresh){
			if (mTitle.equals(FragSummary.ARG_NAME)){
				getSummary();
			}else
			{
				getAppsCatgs();
			}	
		}
		if (mDrawerToggle.onOptionsItemSelected(item)) {
           return true;
       }
       return super.onOptionsItemSelected(item);
	}
	
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_refresh).setVisible(!drawerOpen);
    	return super.onPrepareOptionsMenu(menu);
    }
	
	@Override
	public void onBackPressed() {
		AlertDialog.Builder ald = new AlertDialog.Builder(this);
		ald.setTitle("EXIT THE APP");
		ald.setMessage("are you sure?");
		ald.setNegativeButton(android.R.string.no, null);
		ald.setPositiveButton(android.R.string.yes,
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						moveTaskToBack(true);
					}
				});
		ald.create().show();
	}
}
