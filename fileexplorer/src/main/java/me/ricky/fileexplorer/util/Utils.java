package me.ricky.fileexplorer.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

/**
 * Created by liteng on 17/4/25.
 */

public class Utils {

    public static File getRootPath() {
        return Environment.getExternalStorageDirectory();
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static final int[] getScreentWH(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }

    public static void print(Object[] obj) {
        for (Object o : obj) {
            LogUtils.e(o.toString());
        }
    }

    public static void print(Collection c) {
        for (Object o : c) {
            LogUtils.e(o.toString());
        }
    }


    public static String getFileType(String filePath) {
        String type = null;
        String[] cmds = {"/bin/sh", "-c", "file -b " + filePath};
        try {
            Process process = Runtime.getRuntime().exec(cmds);
            String processResult = readInputStream(process.getInputStream());
            type = distinguishFileType(processResult);
        } catch (IOException e) {
            type = null;
        }
        return type;
    }

    public static String readInputStream(InputStream in) {
        String content = null;

        BufferedReader read = new BufferedReader(new InputStreamReader(in));
        StringBuilder builder = new StringBuilder();
        String line = null;
        try {
            while ((line = read.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
        }

        content = builder.toString();
        return content;
    }

    public static String distinguishFileType(String processResult) {
        if (TextUtils.isEmpty(processResult)) {
            return null;
        }
        /* 图片文件：png,jpg,jpeg,gif，bmp */
        /* 文本文件：txt,pdf,doc */
        /* 媒体文件：mp4,mp3,rmvb,3gp,mov,avi，wav */
        if (processResult.contains("text")) {
            return "文本";
        }

        if (processResult.contains("image")) {
            return "图片";
        }

        if (processResult.contains("Media") ||
                processResult.contains("Flash") ||
                processResult.contains("data")) {
            return "媒体文件";
        }

        return null;
    }

    public static Intent openApplicationSettings(String packageName) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + packageName));
            return intent;
        }
        return new Intent();
    }


    public static boolean isNull(Object obj) {
        return obj == null;
    }
}