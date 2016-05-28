package me.daei.touchlistenerconflict;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class LazyLoadFragment extends Fragment {

    protected boolean isVisible;
    public Activity mActivity;
    protected boolean isLoaded = false;

    /**
     * 在这里实现Fragment数据的缓加载.
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            if (!isLoaded) {
                onVisible();
            }
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    public Context mContext;
    private FragmentManager mFragMgr;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        mContext = context;
        mFragMgr = getActivity().getSupportFragmentManager();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
        mFragMgr = null;
        mActivity = null;
    }


    protected void onVisible() {
        lazyLoad();
        isLoaded = true;
    }

    protected abstract void lazyLoad();

    protected void onInvisible() {

    }
}
