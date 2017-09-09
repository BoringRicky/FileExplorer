package me.ricky.fileexplorer.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.ricky.fileexplorer.FileExplorer;
import me.ricky.fileexplorer.R;
import me.ricky.fileexplorer.adapter.FileAdapter;
import me.ricky.fileexplorer.callback.CallbackManager;
import me.ricky.fileexplorer.entity.FileEntity;
import me.ricky.fileexplorer.util.Utils;

/**
 * 显示所有文件的Fragment
 *
 * @author RickyLee <a href="mailto:liteng@haima.me">Contact me.</a>
 * @version 1.0
 * @since 17/9/7 10:18
 */
public class AllFilesFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView mLvFileList;

    private TextView mTvFilePath;
    private LinearLayout mLlMultiSelectBtns;

    private FileAdapter mFileAdapter;
    private List<FileEntity> mFileList;

    private File mRootPath;
    private File mCurrentPath;

    private List<File> mMultiSelectFiles = new ArrayList<>(16);

    public AllFilesFragment() {
    }

    public static AllFilesFragment newInstance() {
        return new AllFilesFragment();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_all_files;
    }

    @Override
    protected void findViews() {
        mLvFileList = findViewById(R.id.lv_file_list);
        mTvFilePath = findViewById(R.id.tv_file_path);
        mLlMultiSelectBtns = findViewById(R.id.ll_multi_select);
        findViewById(R.id.btn_cancel_multi_select).setOnClickListener(this);
        findViewById(R.id.btn_confirm_multi_select).setOnClickListener(this);

        mFileList = new ArrayList<>();
        mFileAdapter = new FileAdapter(getActivity(), mFileList);
        mLvFileList.setAdapter(mFileAdapter);

        mLvFileList.setOnItemClickListener(this);
        initDatas();
    }

    @Override
    protected void initDatas() {
        mRootPath = Utils.getRootPath();
        mFileList.addAll(getChildrenFiles(mRootPath));
        mFileAdapter.notifyDataSetChanged();
    }


    private List<FileEntity> getChildrenFiles(File file) {
        mCurrentPath = file;
        mTvFilePath.setText(mCurrentPath.getAbsolutePath());
        File[] files = file.listFiles();
        List<FileEntity> fileEnties = new ArrayList<>();

        for (File f : files) {
            FileEntity fileEntity = new FileEntity();
            fileEntity.mName = f.getName();
            fileEntity.mFile = f;
            fileEntity.mPath = f.getAbsolutePath();
            fileEntity.mUpdateTime = f.lastModified();
            fileEntity.isSelected = false;

            fileEnties.add(fileEntity);
        }

        return fileEnties;
    }

    public void onBackPressed() {
        if (mCurrentPath.getAbsolutePath().equals(mRootPath.getAbsolutePath())) {
            if (FileExplorer.getInstance().getFileType() == FileExplorer.FileType.EXPLORER) {
                getActivity().finish();
                return;
            }
            if (FileExplorer.getInstance().isSelectOne()) {
                CallbackManager.getInstance().dispatchCancelOneSelect();
            } else {
                CallbackManager.getInstance().dispatchCancelMultiSelect();
            }
            getActivity().finish();
        } else {
            mCurrentPath = mCurrentPath.getParentFile();
            refreshList(mCurrentPath);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_cancel_multi_select) {
            CallbackManager.getInstance().dispatchCancelMultiSelect();
            getActivity().finish();

        } else if (i == R.id.btn_confirm_multi_select) {
            CallbackManager.getInstance().dispatchMultiSelect(mMultiSelectFiles);
            getActivity().finish();

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FileEntity fileEntity = mFileList.get(position);
        if (fileEntity.mFile.isDirectory()) {
            refreshList(fileEntity.mFile);
        } else {
            if (FileExplorer.getInstance().getFileType() == FileExplorer.FileType.EXPLORER) {
                openFile(fileEntity);
                return;
            }

            // 只选一个文件
            if (FileExplorer.getInstance().isSelectOne()) {
                selectOneFile(fileEntity);
                return;
            }

            //选择多个
            if (mLlMultiSelectBtns.getVisibility() == View.GONE) {
                mLlMultiSelectBtns.setVisibility(View.VISIBLE);
            }

            if (fileEntity.isSelected) {
                fileEntity.isSelected = true;
            } else {
                fileEntity.isSelected = true;
            }

            mFileAdapter.notifyDataSetChanged();
            mMultiSelectFiles.add(fileEntity.mFile);
        }
    }

    private void openFile(FileEntity fileEntity) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String mimeType = null;
        if (fileEntity.mName.endsWith("jpg") || fileEntity.mName.endsWith("JPG") ||
                fileEntity.mName.endsWith("jpeg") || fileEntity.mName.endsWith("JPEG") ||
                fileEntity.mName.endsWith("gif") || fileEntity.mName.endsWith("GIF") ||
                fileEntity.mName.endsWith("png") || fileEntity.mName.endsWith("PNG") ||
                fileEntity.mName.endsWith("bmp") || fileEntity.mName.endsWith("BMP")) {
            mimeType = "image/*";
        } else if (fileEntity.mName.endsWith("mp4") || fileEntity.mName.endsWith("MP4") ||
                fileEntity.mName.endsWith("rmvb") || fileEntity.mName.endsWith("RMVB") ||
                fileEntity.mName.endsWith("3gp") || fileEntity.mName.endsWith("3GP") ||
                fileEntity.mName.endsWith("mov") || fileEntity.mName.endsWith("MOV") ||
                fileEntity.mName.endsWith("avi") || fileEntity.mName.endsWith("AVI")) {
            mimeType = "video/*";
        } else if (fileEntity.mName.endsWith("xml") || fileEntity.mName.endsWith("XML") ||
                fileEntity.mName.endsWith("html") || fileEntity.mName.endsWith("HTML") ||
                fileEntity.mName.endsWith("txt") || fileEntity.mName.endsWith("TXT") ||
                fileEntity.mName.endsWith(".sh") || fileEntity.mName.endsWith(".SH") ||
                fileEntity.mName.endsWith("log") || fileEntity.mName.endsWith("LOG")) {
            mimeType = "text/*";
        } else if (fileEntity.mName.endsWith("pdf") || fileEntity.mName.endsWith("PDF") ||
                fileEntity.mName.endsWith("doc") || fileEntity.mName.endsWith("DOC") ||
                fileEntity.mName.endsWith("xls") || fileEntity.mName.endsWith("xls") ||
                fileEntity.mName.endsWith("pdf") || fileEntity.mName.endsWith("pdf")) {
            mimeType = "application/*";
        } else if (fileEntity.mName.endsWith("m4a") || fileEntity.mName.endsWith("M4A") ||
                fileEntity.mName.endsWith("mp3") || fileEntity.mName.endsWith("MP3") ||
                fileEntity.mName.endsWith("mid") || fileEntity.mName.endsWith("MID") ||
                fileEntity.mName.endsWith("xmf") || fileEntity.mName.endsWith("XMF") ||
                fileEntity.mName.endsWith("ogg") || fileEntity.mName.endsWith("OGG") ||
                fileEntity.mName.endsWith("wav") || fileEntity.mName.endsWith("WAV")) {
            mimeType = "audio/*";
        } else if (fileEntity.mName.endsWith("apk") || fileEntity.mName.endsWith("APK")) {
            mimeType = "application/vnd.android.package-archive";
        }

        Uri uri = Uri.fromFile(fileEntity.mFile);
        intent.setDataAndType(uri, mimeType);
        startActivity(intent);
    }


    private void selectOneFile(FileEntity file) {
        CallbackManager.getInstance().dispatchOneSelect(file.mFile);
        getActivity().finish();
    }


    private void refreshList(File file) {
        if (!FileExplorer.getInstance().isSelectOne() && mMultiSelectFiles.size() > 0) {
            mMultiSelectFiles.clear();
        }

        mFileList.clear();
        mFileList.addAll(getChildrenFiles(file));
        mFileAdapter.notifyDataSetChanged();
    }

}
