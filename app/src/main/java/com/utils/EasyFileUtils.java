package com.utils;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.InputStream;

/**
 * This class has methods for file handling operations.
 *
 * @author sachin.gupta
 */
public class EasyFileUtils {

    private final static String LOG_TAG = EasyFileUtils.class.getSimpleName();

    /**
     * @param pInputStream
     * @return
     */
    public static String readInputStream(InputStream pInputStream) {
        ByteArrayOutputStream result = null;
        try {
            result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = pInputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeSafely(result);
        }
    }

    /**
     * @param pContext
     * @param pAssetFilePath
     * @return
     */
    public static String readAssetFile(Context pContext, String pAssetFilePath) {
        InputStream is = null;
        try {
            is = pContext.getAssets().open(pAssetFilePath);
            return readInputStream(is);
        } catch (Exception e) {
            return null;
        } finally {
            closeSafely(is);
        }
    }

    /**
     * @param pCloseable
     */
    public static void closeSafely(Closeable pCloseable) {
        if (pCloseable != null) {
            try {
                pCloseable.close();
            } catch (Exception e) {
                // ignore
            }
        }
    }
}
