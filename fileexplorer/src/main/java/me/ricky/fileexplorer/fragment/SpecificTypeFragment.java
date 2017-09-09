package me.ricky.fileexplorer.fragment;


import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.ricky.fileexplorer.FileExplorer;
import me.ricky.fileexplorer.R;
import me.ricky.fileexplorer.adapter.SpecificFileAdapter;
import me.ricky.fileexplorer.bus.FileBus;
import me.ricky.fileexplorer.callback.CallbackManager;
import me.ricky.fileexplorer.entity.FileEntity;

public class SpecificTypeFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    public static final int LIMINT_SELECTED = 9;
    public static final int IMAGE_NUMCOLUMNS = 3;
    public static final int TEXT_NUMCOLUMNS = 1;
    public static final int MEDIA_NUMCOLUMNS = 3;

    private GridView mGvFiles;
    private LinearLayout mLlBtnsContainer;

    private SpecificFileAdapter mSpecificFileAdapter;
    private List<FileEntity> mEntityList;
    private List<File> mSelectedFiles = new ArrayList<>(LIMINT_SELECTED);


    public SpecificTypeFragment() {
    }

    public static SpecificTypeFragment newInstance() {
        return new SpecificTypeFragment();
    }


    @Override
    protected int setContentView() {
        return R.layout.fragment_specific_type;
    }

    @Override
    protected void findViews() {
        mGvFiles = findViewById(R.id.gv_files);
        mLlBtnsContainer = findViewById(R.id.ll_multi_select);

        findViewById(R.id.btn_cancel_multi_select).setOnClickListener(this);
        findViewById(R.id.btn_confirm_multi_select).setOnClickListener(this);
    }

    @Override
    protected void initDatas() {
        setGridviewColumns();
        mEntityList = FileBus.getInstance().getFiles(FileExplorer.getInstance().getFileType());

        mSpecificFileAdapter = new SpecificFileAdapter(getActivity(), mEntityList);
        mGvFiles.setAdapter(mSpecificFileAdapter);
        mGvFiles.setOnItemClickListener(this);

    }

    private void setGridviewColumns() {
        switch (FileExplorer.getInstance().getFileType()) {
            case FileExplorer.FileType.IMAGE:
                mGvFiles.setNumColumns(IMAGE_NUMCOLUMNS);
                break;
            case FileExplorer.FileType.MEDIA:
                mGvFiles.setNumColumns(MEDIA_NUMCOLUMNS);
                break;
            case FileExplorer.FileType.TEXT:
                mGvFiles.setNumColumns(TEXT_NUMCOLUMNS);
                break;
            default:
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //选择多个
        if (mLlBtnsContainer.getVisibility() == View.GONE) {
            mLlBtnsContainer.setVisibility(View.VISIBLE);
        }

        FileEntity fileEntity = mEntityList.get(position);

        if (FileExplorer.getInstance().isSelectOne()) {
            cancelAllSelect();
        }


        if (fileEntity.isSelected) {
            if (FileExplorer.getInstance().isSelectOne()) {
                cancelAllSelect();
            }

            fileEntity.isSelected = false;
            mSpecificFileAdapter.notifyDataSetChanged();

            if (mSelectedFiles.contains(fileEntity.mFile)) {
                mSelectedFiles.remove(fileEntity);
            }
        } else {

            if (FileExplorer.getInstance().isSelectOne()) {
                cancelAllSelect();
            }

            fileEntity.isSelected = true;
            mSpecificFileAdapter.notifyDataSetChanged();

            if (mSelectedFiles.contains(fileEntity.mFile)) {
                return;
            }

            mSelectedFiles.add(fileEntity.mFile);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_cancel_multi_select) {
            if (FileExplorer.getInstance().isSelectOne()) {
                CallbackManager.getInstance().dispatchCancelOneSelect();
            } else {
                CallbackManager.getInstance().dispatchCancelMultiSelect();
            }
            getActivity().finish();

        } else if (i == R.id.btn_confirm_multi_select) {
            if (FileExplorer.getInstance().isSelectOne()) {
                CallbackManager.getInstance().dispatchOneSelect(mSelectedFiles.get(0));
            } else {
                CallbackManager.getInstance().dispatchMultiSelect(mSelectedFiles);
            }

            getActivity().finish();

        }
    }


    public void onBackPressed() {
        if (FileExplorer.getInstance().isSelectOne()) {
            CallbackManager.getInstance().dispatchCancelOneSelect();
        } else {
            CallbackManager.getInstance().dispatchCancelMultiSelect();
        }
        getActivity().finish();
    }

    private void cancelAllSelect() {
        mSelectedFiles.clear();

        for (FileEntity f : mEntityList) {
            if (f.isSelected) {
                f.isSelected = false;
            }
        }
    }

}
