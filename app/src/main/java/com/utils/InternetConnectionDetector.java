package com.utils;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
 
public class InternetConnectionDetector { 
 
    public static boolean isInternetAvailable(Context _context){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null) 
          {
              @SuppressWarnings("deprecation")
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null) 
                  for (int i = 0; i < info.length; i++) 
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      } 
          }
          return false;
    }
}
