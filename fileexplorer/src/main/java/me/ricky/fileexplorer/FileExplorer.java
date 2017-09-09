package me.ricky.fileexplorer;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import me.ricky.fileexplorer.callback.CallbackManager;
import me.ricky.fileexplorer.callback.OnMultiSelectListener;
import me.ricky.fileexplorer.callback.OnOneSelectListener;

public class FileExplorer {

    private FileExplorer() {
    }

    private static class Inner {
        private static final FileExplorer INSTANCE = new FileExplorer();
    }

    public static FileExplorer getInstance() {
        return Inner.INSTANCE;
    }

    public interface FileType {
        int SINGLE_TYPE = -1;
        /*不限格式*/
        int ALL = 0;
        /* 图片文件：png,jpg,jpeg,gif，bmp */
        int IMAGE = 0x001;
        /* 文本文件：txt,pdf,doc */
        int TEXT = 0x002;
        /* 媒体文件：mp4,mp3,rmvb,3gp,mov,avi，wav */
        int MEDIA = 0x003;
        /*@hide*/
        int EXPLORER = 0x004;
    }


    private int mFileType = FileType.ALL;
    private boolean mSelectOne;
    private String mSuffix;

    public FileExplorer setType(@me.ricky.fileexplorer.annotation.FileType int fileType) {
        mFileType = fileType;
        return this;
    }


    public void selectOne(Context context, OnOneSelectListener selectOneListener) {
        if (mFileType == FileType.SINGLE_TYPE) {
            if (TextUtils.isEmpty(mSuffix)) {
                throw new RuntimeException("如果你需要选择单一后缀名的文件，务必调用 setSuffix(String suffix) 设置文件后缀");
            }
        }

        CallbackManager.getInstance().registerOneSelectListener(selectOneListener);
        mSelectOne = true;
        Intent intent = new Intent(context, FileListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public void selectMulti(Context context, OnMultiSelectListener multiSelectListener) {
        if (mFileType == FileType.SINGLE_TYPE) {
            if (TextUtils.isEmpty(mSuffix)) {
                throw new RuntimeException("如果你需要选择单一后缀名的文件，务必调用 setSuffix(String suffix) 设置文件后缀");
            }
        }

        mSelectOne = false;
        CallbackManager.getInstance().registerMultiSelectListener(multiSelectListener);
        Intent intent = new Intent(context, FileListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void openExplorer(Context context) {
        mFileType = FileType.EXPLORER;
        Intent intent = new Intent(context, FileListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public boolean isSelectOne() {
        return mSelectOne;
    }

    public int getFileType() {
        return mFileType;
    }

    public FileExplorer setSuffix(String suffix) {
        this.mSuffix = suffix;
        return this;
    }

    public String getSuffix() {
        return mSuffix;
    }
}



