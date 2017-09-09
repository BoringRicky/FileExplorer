package me.ricky.fileexplorer.callback;

import java.io.File;
import java.util.List;

import me.ricky.fileexplorer.FileExplorer;

/**
 * 文件管理器的回调接口的管理器
 *
 * @author RickyLee <a href="mailto:liteng@haima.me">Contact me.</a>
 * @version 1.0
 * @since 17/9/6 15:33
 */
public class CallbackManager {

    private CallbackManager() {
    }

    private static class Inner {
        private static final CallbackManager INSTANCE = new CallbackManager();
    }

    public static CallbackManager getInstance() {
        return Inner.INSTANCE;
    }

    private OnOneSelectListener mOneSelectListener;
    private OnMultiSelectListener mOnMultiSelectListener;

    public void registerOneSelectListener(OnOneSelectListener oneSelectListener) {
        this.mOneSelectListener = oneSelectListener;
    }

    public void dispatchOneSelect(File file) {
        this.mOneSelectListener.onSelect(file);
        FileExplorer.getInstance().setType(FileExplorer.FileType.ALL);
    }

    public void dispatchCancelOneSelect() {
        this.mOneSelectListener.onCancel();
        FileExplorer.getInstance().setType(FileExplorer.FileType.ALL);
    }


    public void registerMultiSelectListener(OnMultiSelectListener multiSelectListener) {
        this.mOnMultiSelectListener = multiSelectListener;
    }

    public void dispatchMultiSelect(List<File> files) {
        this.mOnMultiSelectListener.onSelect(files);
        FileExplorer.getInstance().setType(FileExplorer.FileType.ALL);
    }

    public void dispatchCancelMultiSelect() {
        this.mOnMultiSelectListener.onCancel();
        FileExplorer.getInstance().setType(FileExplorer.FileType.ALL);
    }

    public OnOneSelectListener getOneSelectListener() {
        return mOneSelectListener;
    }

    public OnMultiSelectListener getOnMultiSelectListener() {
        return mOnMultiSelectListener;
    }
}
