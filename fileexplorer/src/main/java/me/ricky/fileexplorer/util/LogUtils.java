package me.ricky.fileexplorer.util;

import android.util.Log;

/**
 * log 工具类.
 *
 * @author RickyLee <a href="mailto:liteng@haima.me">Contact me.</a>
 * @version 1.0
 * @since 17/9/8 17:47
 */
public class LogUtils {
    private static final String TAG = "LOG-TAG";

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

}
