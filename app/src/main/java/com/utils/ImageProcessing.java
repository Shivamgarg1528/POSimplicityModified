package com.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.graphics.Bitmap;
import android.os.Environment;

public class ImageProcessing {

	public static final String FOLDER_NAME         = "/POSImages";
	public static final String BAR_CODE_IMAGE      = "/TSYSInfo";
	public static final String BAR_CODE_IAMGE_NAME = "xxx.jpg";

	public static void downLoadImageFromServerAndSavedInSdCard(final String imageUrlInDb,final String productId){/*

		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try{
					URL imageUrl           = new URL(imageUrlInDb);
					HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
					conn.setInstanceFollowRedirects(false);
					conn.setDoInput(true);
					conn.connect();

					InputStream is = conn.getInputStream();
					Bitmap itemImage  = BitmapFactory.decodeStream(is);
					saveImagesInSdCard(FOLDER_NAME,productId,itemImage);
				}
				catch(Exception ex){ ex.printStackTrace(); }
				return null;
			}
		}.execute();
	*/}
	public static void saveImagesInSdCard(String folderName,String imageName,Bitmap itemImage){

		try{
			File folder = new File(Environment.getExternalStorageDirectory()+ folderName);

			if (!folder.exists()) {
				folder.mkdir();
			}

			File fileObj         = new File(folder, imageName);
			FileOutputStream out = new FileOutputStream(fileObj);

			itemImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.close();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public static void deleteImagesAndFolderFromSdCard(String folderName){
		String baseDir  = Environment.getExternalStorageDirectory() + folderName;
		File file       = new File(baseDir); 
		if (file.exists()) {
			String deleteCmd = "rm -r " + baseDir;
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec(deleteCmd);
			} catch (IOException e) { 
				e.printStackTrace();
			}
		}
	}

	/*public static Bitmap getImageFromSdCard(String imageName,String folderName) {
		String baseDir  = Environment.getExternalStorageDirectory() + folderName;
		String fileName = imageName; 
		File f = new File(baseDir + File.separator + fileName); 
		if(f.exists()){ 
			Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
			return myBitmap;
		}
		return null;
	}*/

	public static String getImagePath(String folderName,String imageName){
		String baseDir  = Environment.getExternalStorageDirectory() + folderName;
		return baseDir + File.separator + imageName;	
	}

	public static boolean deleteAnImageFromSdCard(String folderName,String imageName){
		
		File file = new File(getImagePath(folderName, imageName));
		if(file.exists())
		{
			return file.delete();
		}
		else 
			return false;
	}
}
