package com.afekavote.objects;

import java.util.ArrayList;
import java.util.List;

import com.afekavote.memory.MemoryKeys;
import com.gameframwork.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GradeAdapter extends ArrayAdapter<SummaryGrade>{

	private String TAG = "GradeAdapter";
	private Context context;
	private int layout, maxGrade;
	private List<SummaryGrade> grades;
	
	public GradeAdapter(Context context, int resource, List<SummaryGrade> list, int layout) {
		super(context, resource, list);
		this.context = context;
		this.grades = list; 
		this.layout = layout;
		for (SummaryGrade summaryGrade : list) {
			summaryGrade.setAdapter(this);
		}
		maxGrade = MemoryKeys.CATEGORIES.size() * MemoryKeys.MAX_GRADE * MemoryKeys.VOTERS_COUNT;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SummaryGrade gradeItem = grades.get(position);
		ViewHolder holder = null;
		
		if (convertView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layout, null);
			
			holder = new ViewHolder(0, gradeItem.getGrade());
			holder.grade = (TextView) convertView.findViewById(R.id.grade_tv_grade);
			holder.appName = (TextView) convertView.findViewById(R.id.grade_tv_app_name);
			holder.bar = (ProgressBar) convertView.findViewById(R.id.grade_pb_grade);
			holder.bar.setMax(maxGrade);
			holder.item = gradeItem;
			
			convertView.setTag(holder);
			holder.setDuration(1500);
			holder.bar.startAnimation(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
			holder.item = gradeItem;
			holder.bar.setProgress(gradeItem.getGrade());
		}

		holder.grade.setText(""+gradeItem.getGrade());
		holder.appName.setText(gradeItem.getAppName());
		
		return convertView;
	}
	
	@Override
	public void notifyDataSetInvalidated() {
		super.notifyDataSetInvalidated();
	}
	
	private class ViewHolder extends Animation{
		private ProgressBar bar;
		private TextView grade, appName;
		private SummaryGrade item;
		private int from, to;
		
		public ViewHolder(int from, int to) {
			super();
			this.from = from;
			this.to = to;
		}
		
		 @Override
		 protected void applyTransformation(float interpolatedTime, Transformation t) {
			 super.applyTransformation(interpolatedTime, t);
			 float value = from + (to - from) * interpolatedTime;
			 bar.setProgress((int) value);
		 }
	}
}
