package me.ricky.fileexplorer.callback;

import java.io.File;
import java.util.List;

/**
 * 选择多个文件的回调接口.
 *
 * @author RickyLee <a href="mailto:liteng@haima.me">Contact me.</a>
 * @version 1.0
 * @since 17/9/6 16:46
 */
public interface OnMultiSelectListener {

    void onSelect(List<File> file);

    void onCancel();
}
