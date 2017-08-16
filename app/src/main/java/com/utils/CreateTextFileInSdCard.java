package com.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import android.os.Environment;

public class CreateTextFileInSdCard {

	public static void onWrittenData(String data,String folderName,String fileName) {
		try {
			
			File folder = new File(Environment.getExternalStorageDirectory()+ folderName);
			if (!folder.exists()) {
				folder.mkdir();
			}
			File fileObj                   = new File(folder, fileName+".txt");
			FileOutputStream fOut          = new FileOutputStream(fileObj);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append(data);
			myOutWriter.close();
			fOut.close();			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
