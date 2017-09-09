package me.ricky.fileexplorer;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import me.ricky.fileexplorer.callback.CallbackManager;
import me.ricky.fileexplorer.fragment.AllFilesFragment;
import me.ricky.fileexplorer.fragment.SpecificTypeFragment;
import me.ricky.fileexplorer.util.Utils;

public class FileListActivity extends AppCompatActivity {

    public static final int PERMISSION_REQUEST_CODE = 0x123;
    private static final String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE};

    private FragmentManager mManager;

    private AllFilesFragment mAllFilesFragment;
    private SpecificTypeFragment mSpecificTypeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

        init();
    }

    private void init() {
        if (ContextCompat.checkSelfPermission(this, PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED) {
            requestFilePermission();
        } else {
            showFileList();
        }
    }

    private void showFileList() {
        mManager = getSupportFragmentManager();

        if (FileExplorer.getInstance().getFileType() == FileExplorer.FileType.ALL ||
                FileExplorer.getInstance().getFileType() == FileExplorer.FileType.EXPLORER) {
            mAllFilesFragment = AllFilesFragment.newInstance();
            mManager.beginTransaction().replace(R.id.fragment_container, mAllFilesFragment).commit();
        } else {
            mSpecificTypeFragment = SpecificTypeFragment.newInstance();
            mManager.beginTransaction().replace(R.id.fragment_container, mSpecificTypeFragment).commit();
        }
    }

    private void requestFilePermission() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showFileList();
                } else {
                    showAlertDialog();
                }
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                init();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        if (mAllFilesFragment != null) {
            mAllFilesFragment.onBackPressed();
        } else if (mSpecificTypeFragment != null) {
            mSpecificTypeFragment.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("您好，必须同意程序访问手机存储设备,否则无法获取文件列表");
        builder.setNegativeButton("就不", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!Utils.isNull(CallbackManager.getInstance().getOneSelectListener())) {
                    CallbackManager.getInstance().dispatchCancelOneSelect();
                }

                if (!Utils.isNull(CallbackManager.getInstance().getOnMultiSelectListener())) {
                    CallbackManager.getInstance().dispatchCancelMultiSelect();
                }

                finish();
            }
        });
        builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = Utils.openApplicationSettings(getPackageName());
                startActivityForResult(intent, PERMISSION_REQUEST_CODE);
            }
        });
        builder.show();
    }


}