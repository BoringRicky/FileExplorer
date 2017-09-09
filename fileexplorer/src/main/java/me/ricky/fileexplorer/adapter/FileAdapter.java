package me.ricky.fileexplorer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.ricky.fileexplorer.FileExplorer;
import me.ricky.fileexplorer.R;
import me.ricky.fileexplorer.entity.FileEntity;


public class FileAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<FileEntity> mFiles;

    public FileAdapter(Context context, List<FileEntity> fileEntities) {
        mFiles = fileEntities;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int count = 0;
        if (mFiles != null && !mFiles.isEmpty()) {
            count = mFiles.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        FileEntity file = null;
        if (mFiles != null && !mFiles.isEmpty()) {
            file = mFiles.get(position);
        }
        return file;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_list_file, parent, false);
            holder = new ViewHolder();
            holder.mIvIcon = (ImageView) convertView.findViewById(R.id.iv_file_icon);
            holder.mTvName = (TextView) convertView.findViewById(R.id.tv_file_name);
            holder.mCbMultiSelected = (CheckBox) convertView.findViewById(R.id.cb_file_selected);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mFiles != null && !mFiles.isEmpty()) {

            FileEntity file = mFiles.get(position);
            if (file.mFile.isDirectory()) {
                holder.mIvIcon.setImageResource(R.drawable.ic_folder);
            } else if (file.mFile.isFile()) {

                if (file.mName.endsWith(".mp3") || file.mName.endsWith("wav")) {
                    holder.mIvIcon.setImageResource(R.drawable.ic_music);
                } else {
                    holder.mIvIcon.setImageResource(R.drawable.ic_file);
                }
            }

            holder.mTvName.setText(file.mName);


            if (FileExplorer.getInstance().getFileType() == FileExplorer.FileType.EXPLORER) {
                holder.mCbMultiSelected.setVisibility(View.GONE);
            } else {

                if (FileExplorer.getInstance().isSelectOne()) {
                    holder.mCbMultiSelected.setVisibility(View.GONE);
                } else {
                    if (file.mFile.isDirectory()) {
                        holder.mCbMultiSelected.setVisibility(View.GONE);
                    } else {
                        holder.mCbMultiSelected.setVisibility(View.VISIBLE);
                        if (file.isSelected) {
                            holder.mCbMultiSelected.setChecked(true);
                        } else {
                            holder.mCbMultiSelected.setChecked(false);
                        }
                    }
                }
            }

        }

        return convertView;
    }


    private static class ViewHolder {
        private TextView mTvName;
        private ImageView mIvIcon;
        private CheckBox mCbMultiSelected;
    }


}
