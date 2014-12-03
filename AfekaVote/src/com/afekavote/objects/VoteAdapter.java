package com.afekavote.objects;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.afekavote.memory.MemoryKeys;
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
			sbWeigth.setMax(MemoryKeys.MAX_GRADE);
			tvCatg = (TextView) convertView.findViewById(R.id.weightSB_tv_ctg_name);
			tvGrade = (TextView) convertView.findViewById(R.id.weightSB_tv_grade);
			tvCatg.setText(vote.getCategory());
			int grade = vote.getGrade();
			tvGrade.setText(""+grade);
			sbWeigth.setProgress(grade);
			setCustomColor(sbWeigth, tvGrade, position);
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


	private void setCustomColor(SeekBar sbWeigth, TextView tvGrade, int position) {
		tvGrade.setTextColor(MemoryKeys.FRONT_COLORS[position%MemoryKeys.FRONT_COLORS.length]);
	    ColorDrawable progressDrawable = new ColorDrawable(MemoryKeys.FRONT_COLORS[position%MemoryKeys.FRONT_COLORS.length]);
		
		//Custom seek bar progress drawable. Also allows you to modify appearance.
	    SeekBarProgressDrawable clipProgressDrawable = new SeekBarProgressDrawable(progressDrawable,Gravity.LEFT,ClipDrawable.HORIZONTAL, context);
	    Drawable[] drawables = new Drawable[]{clipProgressDrawable};

	    //Create layer drawables with android pre-defined ids
	    LayerDrawable layerDrawable = new LayerDrawable(drawables);
	    layerDrawable.setId(0, android.R.id.progress);

	    //Set to seek bar
	    sbWeigth.setProgressDrawable(layerDrawable);
	}

	@Override
	public void notifyDataSetInvalidated() {
		super.notifyDataSetInvalidated();
	}

	public class SeekBarProgressDrawable extends ClipDrawable {

		private Paint mPaint = new Paint();
		private float dy;
		private Rect mRect;


		public SeekBarProgressDrawable(Drawable drawable, int gravity, int orientation, Context ctx) {
		    super(drawable, gravity, orientation);
		    mPaint.setColor(Color.WHITE);
		    dy = ctx.getResources().getDimension(R.dimen.seekbar_dp);
		}

		@Override
		public void draw(Canvas canvas) {
		    if (mRect==null) {
		        mRect = new Rect(getBounds().left, (int)(getBounds().centerY() - dy / 2), getBounds().right, (int)(getBounds().centerY() + dy / 2));
		        setBounds(mRect);
		    }

		    super.draw(canvas);
		}


		@Override
		public void setAlpha(int i) {
		    mPaint.setAlpha(i);
		}

		@Override
		public void setColorFilter(ColorFilter colorFilter) {

		}

		@Override
		public int getOpacity() {
		    return PixelFormat.TRANSLUCENT;
		}
	}	
}
