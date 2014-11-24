package com.afekavote.objects;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.afekavote.memory.DataManager;
import com.gameframwork.R;

public class VoteAdapter extends ArrayAdapter<WeightedVote> {

	private String TAG = "VoteAdapter";
	private Context context;
	private int layout;
	private List<WeightedVote> votes;
	
	public VoteAdapter(Context context, int resource, List<WeightedVote> list, int layout) {
		super(context, resource, list);
		this.context = context;
		this.votes = list; 
		this.layout = layout;
		for (WeightedVote weightedVote : list) {
			weightedVote.setAdapter(this);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layout, null);
		}
		final WeightedVote vote = votes.get(position);
		if (vote != null){
			TextView tvCatg;
			final TextView tvGrade;
			SeekBar sbWeigth;
			sbWeigth = (SeekBar) convertView.findViewById(R.id.weightSB_sb);
			tvCatg = (TextView) convertView.findViewById(R.id.weightSB_tv_ctg_name);
			tvGrade = (TextView) convertView.findViewById(R.id.weightSB_tv_grade);
			tvCatg.setText(vote.getCategory());
			int grade = vote.getGrade();
			tvGrade.setText(""+grade);
			sbWeigth.setProgress(grade);
			sbWeigth.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				
				@Override
				public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
					vote.setGrade(arg1);
					tvGrade.setText(""+arg1);
				}
				
				@Override
				public void onStopTrackingTouch(SeekBar arg0) {	
					notifyDataSetChanged();
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar arg0) {	}
			});
		}
		
		return convertView;
	}


	@Override
	public void notifyDataSetInvalidated() {
		super.notifyDataSetInvalidated();
	}

}
