package com.afekavote.activitys;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afekavote.application.Program;
import com.afekavote.communication.ClientInterface;
import com.afekavote.communication.volly.VollyClinet;
import com.afekavote.memory.DataManager;
import com.afekavote.memory.MemoryKeys;
import com.afekavote.objects.VoteAdapter;
import com.afekavote.objects.WeightedVote;
import com.afekavote.utils.Logger;
import com.android.volley.Response;
import com.gameframwork.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class VoteFrag extends Fragment implements OnClickListener{

	private static String TAG = "VoteFrag";
	private TextView tvAppName;
	private VoteAdapter voteAdapter;
	private Button bDone;
	private String appName;
	private DataManager manager;
	private ArrayList<WeightedVote> catgVote;
	private ListView lvVoteList;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
//		Log.e(TAG, "onAttach");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.vote_frag, container, false);
//		Log.e(TAG, "onCreateView");
		getLayoutRes(view);
		initRes(view);
		return view;
	}
	
	private void getLayoutRes(View view) {
		tvAppName = (TextView) view.findViewById(R.id.vote_tv_app_name);
		lvVoteList = (ListView) view.findViewById(R.id.vote_lv);
		bDone = (Button) view.findViewById(R.id.vote_b_done);
	}
	
	@SuppressLint("NewApi")
	private void initRes(View view) {
		if (!TextUtils.isEmpty(appName)) {
			tvAppName.setText(appName);
			bDone.setOnClickListener(this);

			manager = DataManager.getInstance();
			catgVote = new ArrayList<WeightedVote>();

			for (String category : MemoryKeys.CATEGORIES) {
				int grade = manager.getAppCategory(appName + "_" + category);
				WeightedVote viewChild = new WeightedVote(appName, category, grade);
				catgVote.add(viewChild);
			}
			voteAdapter = new VoteAdapter(Program.getAppContext(), 110, catgVote, R.layout.weigted_seek_bar);
			lvVoteList.setAdapter(voteAdapter);
		}
	}

	public void initFrag(String appName) {
		this.appName = appName;
		TAG += appName;
	}

	public String getFragAppName(){
		return appName;
	}
	
	@Override
	public void onClick(View v) {
		ClientInterface client = VollyClinet.getInstance();
		String text = appName +": ";
		HashMap<String, Integer> votes = new HashMap<String, Integer>();
		for (WeightedVote vote : catgVote) {
			text += vote.getCategory()+"- "+ vote.getGrade();
			votes.put(vote.getCategory(), vote.getGrade());
		}
		Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
		manager.saveApp(appName, votes);
		client.voteForApp(appName, votes, new voteResponse());
	}
	
	private class voteResponse implements Response.Listener<String>{

		@Override
		public void onResponse(String response) {
			Logger.log("response:" + response+"!!");
//			try {
//				JSONObject jsonResponse = new JSONObject(response);
//				if (jsonResponse.getBoolean(MemoryKeys.API_SUCCESS) && 
//						(jsonResponse.getString(MemoryKeys.API_TAG).equals(MemoryKeys.API_VOTE_TAG))){
//					Logger.longToast(appName +" saved to DB");
//				}
//			} catch (JSONException e) {
//				Logger.log("JSONException: " + e.getMessage());
//				ErrorDialog.showResponseError();
//			}
		}
	}

}
