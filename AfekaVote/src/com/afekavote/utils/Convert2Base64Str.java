package com.afekavote.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Base64;

public class Convert2Base64Str {

	public static String convertFileToString(File f) {

		String base64String = null;
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			byte[] fileByts = readFile(f);
			base64String = Base64.encodeToString(fileByts, Base64.DEFAULT);
		} catch (IOException e) {		}
		return base64String;
	}

	public static String convertBitmapToString(Bitmap pic) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		pic.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		String image64Str = Base64.encodeToString(byteArray, Base64.DEFAULT);
		return image64Str;
	}
	
	public static byte[] readFile(File file) throws IOException {
		// Open file
		RandomAccessFile f = new RandomAccessFile(file, "r");
		try {
			// Get and check length
			long longlength = f.length();
			int length = (int) longlength;
			if (length != longlength)
				throw new IOException("File size >= 2 GB");
			// Read file and return data
			byte[] data = new byte[length];
			f.readFully(data);
			return data;
		} finally {
			f.close();
		}
	}

	public static Boolean convertStringToFile(String str, String path) {

		byte[] fileByts = Base64.decode(str, Base64.DEFAULT);

		File file = new File(path);
		if (!file.exists()) {
			File f = Environment.getExternalStorageDirectory();
			file = new File(f.getAbsolutePath(), str);
		}

		try {
			FileOutputStream stream = new FileOutputStream(path);
			stream.write(fileByts);
		} catch (FileNotFoundException e1) {
			return false;
		} catch (IOException e) {
			return false;
		}

		return true;
	}

}
