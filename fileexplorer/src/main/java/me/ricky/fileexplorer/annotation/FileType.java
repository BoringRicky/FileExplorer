package me.ricky.fileexplorer.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import me.ricky.fileexplorer.FileExplorer;

/**
 * 用来限制选择文件的格式
 *
 * @author RickyLee <a href="mailto:liteng@haima.me">Contact me.</a>
 * @version 1.0
 * @since 17/9/6 16:39
 */

@Retention(RetentionPolicy.RUNTIME)
@IntDef(flag = true, value = {FileExplorer.FileType.ALL, FileExplorer.FileType.TEXT, FileExplorer.FileType.IMAGE, FileExplorer.FileType.MEDIA})
public @interface FileType {
}
