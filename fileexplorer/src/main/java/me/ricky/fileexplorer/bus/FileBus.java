package me.ricky.fileexplorer.bus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.ricky.fileexplorer.FileExplorer;
import me.ricky.fileexplorer.entity.FileEntity;
import me.ricky.fileexplorer.util.LogUtils;
import me.ricky.fileexplorer.util.Utils;

/**
 * 文件相关逻辑操作.
 *
 * @author RickyLee <a href="mailto:liteng@haima.me">Contact me.</a>
 * @version 1.0
 * @since 17/9/7 17:43
 */
public class FileBus {

    public static final String[] TEXT_SUFFIXES = {".txt", ".log", ".html", ".xml"};
    public static final String[] IMAGE_SUFFIXES = {".png", ".bmp", ".jpg", ".jpeg", ".gif"};
    public static final String[] MEDIA_SUFFIXES = {".3gp", ".avi", ".flv", ".mov", ".mp4", ".mpeg", ".rmvb", ".wav", ".swf"};


    private FileBus() {
    }

    private static class Inner {
        private static final FileBus INSTANCE = new FileBus();
    }

    public static FileBus getInstance() {
        return Inner.INSTANCE;
    }


    List<FileEntity> mFileEntities;

    public List<FileEntity> getFiles(int type) {
        mFileEntities = new ArrayList<>();
        String[] suffixes = null;
        switch (type) {
            case FileExplorer.FileType.IMAGE:
                suffixes = IMAGE_SUFFIXES;
                break;
            case FileExplorer.FileType.TEXT:
                suffixes = TEXT_SUFFIXES;
                break;
            case FileExplorer.FileType.MEDIA:
                suffixes = MEDIA_SUFFIXES;
                break;
            case FileExplorer.FileType.SINGLE_TYPE:
                suffixes = new String[]{FileExplorer.getInstance().getSuffix()};
                break;
        }
        filterAllFiles(Utils.getRootPath(), suffixes);

        return mFileEntities;
    }


    private void filterAllFiles(File dir, String[] suffixes) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();

            for (File f : files) {
                // 如果不可以读，不可写；就抛弃
                if (!f.canRead() || !f.canWrite()) {
                    continue;
                }

                if (f.isDirectory()) {
                    filterAllFiles(f, suffixes);
                } else {
                    List<FileEntity> fileEntities = filterFiles(f, suffixes);
                    if (fileEntities != null && fileEntities.size() != 0) {
                        mFileEntities.addAll(fileEntities);
                    }
                }
            }
        }
    }


    private List<FileEntity> filterFiles(File file, String[] suffixes) {
        List<FileEntity> fileEntities = new ArrayList<>();
        String fileName = file.getName();
        for (String suffix : suffixes) {
            if (fileName.endsWith(suffix) ||
                    fileName.endsWith(suffix.toUpperCase()) ||
                    fileName.endsWith(suffix.toLowerCase())) {

                LogUtils.e(file.getAbsolutePath());

                FileEntity entity = new FileEntity();
                entity.mFile = file;
                entity.mName = fileName;
                entity.mPath = file.getAbsolutePath();
                entity.mUpdateTime = file.lastModified();
                fileEntities.add(entity);
            }
        }
        return fileEntities;
    }

}
