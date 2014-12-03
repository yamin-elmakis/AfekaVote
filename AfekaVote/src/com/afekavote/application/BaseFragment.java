package com.afekavote.application;

import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment{
	
	public void onResume(){
		super.onResume();
	}
	
	public void onPause(){
		super.onPause();
	}
	
	public abstract void setFragName(String appName);
	public abstract String getFragName();
}