package me.ricky.fileexplorer.callback;

import java.io.File;

/**
 * 选择一个文件的回调接口.
 *
 * @author RickyLee <a href="mailto:liteng@haima.me">Contact me.</a>
 * @version 1.0
 * @since 17/9/6 16:46
 */
public interface OnOneSelectListener {

    void onSelect(File file);

    void onCancel();
}
