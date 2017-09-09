package me.ricky.fileexplorer.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import me.ricky.fileexplorer.FileExplorer;
import me.ricky.fileexplorer.R;
import me.ricky.fileexplorer.entity.FileEntity;
import me.ricky.fileexplorer.fragment.SpecificTypeFragment;
import me.ricky.fileexplorer.util.Utils;


public class SpecificFileAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<FileEntity> mFiles;
    private Activity mActivity;

    public SpecificFileAdapter(Activity activity, List<FileEntity> fileEntities) {
        mActivity = activity;
        mFiles = fileEntities;
        mInflater = LayoutInflater.from(activity);
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
            switch (FileExplorer.getInstance().getFileType()) {
                case FileExplorer.FileType.IMAGE:
                    convertView = mInflater.inflate(R.layout.item_grid_file_img, parent, false);
                    break;
                case FileExplorer.FileType.TEXT:
                    convertView = mInflater.inflate(R.layout.item_grid_file_text, parent, false);
                    break;
                case FileExplorer.FileType.MEDIA:
                    convertView = mInflater.inflate(R.layout.item_grid_file_img, parent, false);
                    break;
                case FileExplorer.FileType.SINGLE_TYPE:
                default:
                    convertView = mInflater.inflate(R.layout.item_grid_file_text, parent, false);
                    break;

            }

            //TODO 判断 自定义后缀名的 所属的 类型

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

            setIcon(holder, file);

            holder.mTvName.setText(file.mName);

            if (file.isSelected) {
                holder.mCbMultiSelected.setChecked(true);
            } else {
                holder.mCbMultiSelected.setChecked(false);
            }
        }
        return convertView;
    }

    private void setIcon(ViewHolder holder, FileEntity file) {
        int width = 0;
        switch (FileExplorer.getInstance().getFileType()) {
            case FileExplorer.FileType.IMAGE:
                width = getWidth(SpecificTypeFragment.IMAGE_NUMCOLUMNS);
                holder.mIvIcon.setLayoutParams(new RelativeLayout.LayoutParams(width, width));
                holder.mIvIcon.setImageURI(Uri.parse(file.mPath));
                break;
            case FileExplorer.FileType.MEDIA:
                width = getWidth(SpecificTypeFragment.MEDIA_NUMCOLUMNS);
                holder.mIvIcon.setLayoutParams(new RelativeLayout.LayoutParams(width, width));
                holder.mIvIcon.setImageBitmap(getMediaThumbnail(file.mPath));
                break;
            case FileExplorer.FileType.TEXT:
                holder.mIvIcon.setImageResource(R.drawable.ic_file);
                break;
            default:
                break;
        }
    }

    private static class ViewHolder {
        private TextView mTvName;
        private ImageView mIvIcon;
        private CheckBox mCbMultiSelected;
    }

    private int getWidth(int scale) {
        int sWidth = Utils.getScreentWH(mActivity)[0];
        return sWidth / scale;
    }


    private Bitmap getMediaThumbnail(String path) {
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND);
        //裁剪大小
//        bitmap = ThumbnailUtils.extractThumbnail(bitmap, (int)(100*metrics.density), (int)(100*metrics.density));
        return bitmap;
    }

//    private int getHeight() {
//        int sHeight = Utils.getScreentWH(mActivity)[1];
//    }


}
