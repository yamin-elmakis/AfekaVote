package com.afekavote.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Base64;

public class ImageUtiles {

	public static Bitmap decodeSampledBitmapFromResource(String path) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		Bitmap bmp;
		options.inJustDecodeBounds = true;
		File file = new File(path);
		if (file.exists()) {
			BitmapFactory.decodeFile(path);

			// Calculate inSampleSize
			options.inSampleSize = 4;

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeFile(path, options);
			return bmp;
		}
		return null;

	}

	public static Bitmap decodeSampledBitmapFromResource(Bitmap pic) {

		Bitmap bmp;

		bmp = Bitmap.createScaledBitmap(pic, 2000, 2000, false);
		return bmp;

	}

	public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
		Bitmap sbmp;
		if (bmp.getWidth() != radius || bmp.getHeight() != radius)
			sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
		else
			sbmp = bmp;
		Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.RED);
		canvas.drawCircle(sbmp.getWidth() / 2 + 1f, 
				sbmp.getHeight() / 2 + 1f,
				sbmp.getWidth() / 2 + 0.5f, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(sbmp, rect, rect, paint);

//		Bitmap output = Bitmap.createBitmap(bmp.getWidth(), bmp
//	            .getHeight(), Config.ARGB_8888);
//	    Canvas canvas = new Canvas(output);
//
//	    final int color = 0xff424242;
//	    final Paint paint = new Paint();
//	    final Rect rect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
//	    final RectF rectF = new RectF(rect);
//	    final float roundPx = radius;
//
//	    paint.setAntiAlias(true);
//	    canvas.drawARGB(0, 0, 0, 0);
//	    paint.setColor(color);
//	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
//
//	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
//	    canvas.drawBitmap(bmp, rect, rect, paint);
		return output;
	}
}
