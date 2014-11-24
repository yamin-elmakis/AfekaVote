package com.afekavote.activitys;

import android.app.ProgressDialog;
import com.afekavote.application.Program;

public class MyProgressDialog {

	private static ProgressDialog pDialog;
	
	private MyProgressDialog() { /* no instances */ }

	public static void showDialog() {
		pDialog = new ProgressDialog(Program.getCurActivity());
		pDialog.setMessage("Please wait...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
	}
	
	public static void showDialog(String message) {
		pDialog = new ProgressDialog(Program.getCurActivity());
		pDialog.setMessage(message);
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
	}
	
	public static void hideDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}
}
