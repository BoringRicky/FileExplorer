package me.ricky.fileexplorer.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 所有Fragment的基类
 *
 * @author RickyLee <a href="mailto:liteng@haima.me">Contact me.</a>
 * @version 1.0
 * @since 17/9/7 10:18
 */
public abstract class BaseFragment extends Fragment {

    private View mRootView;
    private LayoutInflater mInflater;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflater = inflater;
        if (mRootView == null) {
            int rootViewId = setContentView();
            mRootView = inflater.inflate(rootViewId, container, false);
        } else {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
        }

        findViews();

        initDatas();

        return mRootView;
    }


    protected abstract int setContentView();

    protected abstract void findViews();

    protected abstract void initDatas();

    protected <VT extends View> VT findViewById(@IdRes int id) {
        return (VT) mRootView.findViewById(id);
    }


}
