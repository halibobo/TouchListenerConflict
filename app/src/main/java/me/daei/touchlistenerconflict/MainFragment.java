package me.daei.touchlistenerconflict;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.daei.touchlistenerconflict.adapter.CommonAdapter;
import me.daei.touchlistenerconflict.adapter.ViewHolder;
import me.daei.touchlistenerconflict.bean.GridCutItem;
import me.daei.touchlistenerconflict.widget.ControlScrollView;
import me.daei.touchlistenerconflict.widget.DragGridView;
import me.daei.touchlistenerconflict.widget.ViewWithSign;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MainFragment extends LazyLoadFragment {
    @Bind(R.id.grid)
    DragGridView grid;
    @Bind(R.id.scroller)
    ControlScrollView scroller;

    private ArrayList<GridCutItem> mDatas = new ArrayList<>();
    private CommonAdapter mAdapter;
    private boolean isPrepared;


    public MainFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        initView();
    }

    ViewWithSign viewWithSign ;
    private void initView() {
        scroller.smoothScrollTo(0, 0);
        mDatas.clear();
        for(int i = 0; i< GridDatas.gridDatas.length; i++) {
            GridCutItem gridCutItem = new GridCutItem(GridDatas.gridDatas[i][0], GridDatas.gridDatas[i][1]);
            mDatas.add(gridCutItem);
        }

        grid.setAdapter(mAdapter = new CommonAdapter<GridCutItem>(mContext, mDatas, R.layout.adapter_grid_item) {
            @Override
            public void convert(ViewHolder helper, final GridCutItem item, int position) {
                helper.setText(R.id.tv_item, item.getName());
                viewWithSign = helper.getView(R.id.icon);
                viewWithSign.addDrawText(item.getTip());
                if (position == mAdapter.getCount() -1) {
                    helper.setImageResource(R.id.iv_item, R.drawable.add_more);
                }
            }
        });

        //设置拖拽数据交换
        grid.setOnChangeListener(new DragGridView.OnChangeListener() {
            @Override
            public void onChange(int from, int to) {
                GridCutItem temp = mDatas.get(from);
                if (from < to) {
                    for (int i = from; i < to; i++) {
                        Collections.swap(mDatas, i, i + 1);
                    }
                } else if (from > to) {
                    for (int i = from; i > to; i--) {
                        Collections.swap(mDatas, i, i - 1);
                    }
                }
                mDatas.set(to, temp);
                mAdapter.notifyDataSetChanged();
            }
        });
        mAdapter.notifyDataSetChanged();
        scroller.setScrollState(new ControlScrollView.ScrollState() {
            @Override
            public void stopTouch() {
                grid.stopDrag();
            }

            @Override
            public void isCanDrag(boolean isCanDrag) {
                grid.setCanDrag(isCanDrag);
            }
        });

        grid.setOnDragStartListener(new DragGridView.OnDragStartListener() {
            @Override
            public void onDragStart() {
                scroller.requestDisallowInterceptTouchEvent(true);
                scroller.setInControl(false);
                ((MainActivity) getContext()).setViewpagerNoSCroll(true);
            }
        });
        grid.setOnDragEndListener(new DragGridView.OnDragEndListener() {
            @Override
            public void onDragEnd() {
                scroller.requestDisallowInterceptTouchEvent(false);
                scroller.setInControl(true);
                ((MainActivity) getContext()).setViewpagerNoSCroll(false);
                grid.postInvalidate();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
