package com.afekavote.activitys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.afekavote.application.BaseFragment;
import com.afekavote.memory.MemoryKeys;
import com.afekavote.objects.GradeAdapter;
import com.afekavote.objects.SummaryGrade;
import com.afekavote.utils.Logger;
import com.gameframwork.R;

public class FragSummary extends BaseFragment{

	private static String TAG = "FragSummary";
	public static final String ARG_NAME = "Summary";
	private HashMap<String, Integer> results;
	private TextView tvFirst, tvSecond, tvThird;
	private GradeAdapter gradeAdapter;
	private ListView gradesList;
	private ArrayList<SummaryGrade> grades;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
//		Log.e(TAG, "onAttach");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_summary, container, false);
		view.setBackgroundResource(R.color.azure3);
//		Log.e(TAG, "onCreateView");
		getLayoutRes(view);
		initRes(view);
		return view;
	}

	private void getLayoutRes(View view) {
		tvFirst = (TextView) view.findViewById(R.id.summary_tv_first);
		tvSecond = (TextView) view.findViewById(R.id.summary_tv_second);
		tvThird = (TextView) view.findViewById(R.id.summary_tv_third);
		gradesList = (ListView) view.findViewById(R.id.summary_lv_grades);
	}

	private void initRes(View view) {
		grades = new ArrayList<SummaryGrade>();
		gradeAdapter = new GradeAdapter(getActivity(), 777, grades, R.layout.grade_item);
		gradesList.setAdapter(gradeAdapter);
		
		setView();
	}

	private void setView() {
		grades.clear();
		
		String res = "";
		for (Map.Entry<String, Integer> entry : results.entrySet()) {
			res += entry.getKey() +" : " + entry.getValue()+"\n";
			grades.add(new SummaryGrade(entry.getValue(), entry.getKey()));
		}
		Logger.log(TAG, res);
		
		Collections.sort(grades);
		gradeAdapter.notifyDataSetChanged();
		
		// get the names of the top 3
		if (grades.size() < 1)
			return;
		tvFirst.setText(grades.get(0).getAppName());
		if (grades.size() > 1)
			tvSecond.setText(grades.get(1).getAppName());
		if (grades.size() > 2)
			tvThird.setText(grades.get(2).getAppName());
	}
	

	public void setResult(HashMap<String, Integer> results) {
		this.results = results;
	}

	@Override
	public void setFragName(String appName) {	}

	@Override
	public String getFragName() {
		return ARG_NAME;
	}
	
	public void update(){
		Logger.log(TAG, "enter update");
		if (gradeAdapter != null){
			Logger.log(TAG, "update - not null");
			setView();
		}
			
	}
}
