package com.afekavote.activitys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.afekavote.application.Program;

public class ErrorDialog extends AlertDialog implements DialogInterface.OnClickListener{
	
	private static final String TAG = "ErrorDialog";
	private String title = "Error";
	private static String standartError = "error in the response.\nplease try again";
	
	public ErrorDialog(String text) {
		super(Program.getCurActivity());
		setTitle(title);
		setMessage(text);
		setCanceledOnTouchOutside(true);
		setButton(BUTTON_POSITIVE, "OK", this);
	}
	
	public ErrorDialog(Context context) {
		super(context);
		setTitle(title);
		setCanceledOnTouchOutside(true);
		setButton(BUTTON_POSITIVE, "OK", this);
	}

	@Override
	public void setMessage(CharSequence message) {
		super.setMessage(message);
	}

	@Override
	public void setTitle(CharSequence title) {
		super.setTitle(title);
	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		dismiss();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		dismiss();
	}
	public static void showResponseError(){
		showError(standartError);
	}
	
	public static void showError(String text){
		ErrorDialog ed = new ErrorDialog(text);
		ed.show();
	}
}
