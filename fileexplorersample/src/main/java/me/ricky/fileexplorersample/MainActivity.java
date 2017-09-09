package me.ricky.fileexplorersample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import me.ricky.fileexplorer.FileExplorer;
import me.ricky.fileexplorer.callback.OnMultiSelectListener;
import me.ricky.fileexplorer.callback.OnOneSelectListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onFileExplorer(View view) {
        //打开一个文件管理器
        FileExplorer.getInstance().openExplorer(this);
    }


    public void onSelectOneFromAllFiles(View view) {
        //从所有文件中选择一项，不论格式
        FileExplorer.getInstance().selectOne(this, new OnOneSelectListener() {
            /**
             *  单选文件的回调方法
             * @param file 单选的文件
             */
            @Override
            public void onSelect(File file) {
                Toast.makeText(MainActivity.this, "" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            }

            /**
             * 取消单选
             */
            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "取消单选", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onSelectMultipleFromAllFiles(View view) {
        //从所有文件中选择多项，不论格式
        FileExplorer.getInstance().selectMulti(this, new OnMultiSelectListener() {
            /**
             *  单选文件的回调方法
             * @param files 多选的文件列表
             */
            @Override
            public void onSelect(List<File> files) {
                for (int i = 0; i < files.size(); i++) {
                    Log.e("==file==", files.get(i).getAbsolutePath());
                }
            }

            /**
             * 取消多选
             */
            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "取消多选", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onSelectSpecificOne(View view) {
        //从所有文件中选择一项，可以指定格式；给列表如下：
        /* 图片文件：png,jpg,jpeg,gif，bmp */
        /* 文本文件：txt,pdf,doc */
        /* 媒体文件：mp4,mp3,rmvb,3gp,mov,avi，wav */
        FileExplorer.getInstance().setType(FileExplorer.FileType.IMAGE).selectOne(this, new OnOneSelectListener() {
            @Override
            public void onSelect(File file) {
                Toast.makeText(MainActivity.this, "" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "取消单选", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onSelectSpecificMultiple(View view) {
        //从所有文件中选择多项，可以指定格式；给列表如下：
        /* 图片文件：png,jpg,jpeg,gif，bmp */
        /* 文本文件：txt,pdf,doc */
        /* 媒体文件：mp4,mp3,rmvb,3gp,mov,avi，wav */
        FileExplorer.getInstance().setType(FileExplorer.FileType.TEXT).selectMulti(this, new OnMultiSelectListener() {
            @Override
            public void onSelect(List<File> files) {
                for (int i = 0; i < files.size(); i++) {
                    Log.e("==file==", files.get(i).getAbsolutePath());
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "取消多选", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onSelectCustomSuffixMultiple(View view) {
        //从所有文件中选择多项，自定义后缀名
        FileExplorer.getInstance().setType(FileExplorer.FileType.SINGLE_TYPE).setSuffix("apk").selectMulti(this, new OnMultiSelectListener() {
            @Override
            public void onSelect(List<File> files) {

                for (int i = 0; i < files.size(); i++) {
                    Log.e("==file==", files.get(i).getAbsolutePath());
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "取消多选", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
