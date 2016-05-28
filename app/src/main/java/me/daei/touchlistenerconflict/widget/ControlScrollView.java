package me.daei.touchlistenerconflict.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import me.daei.touchlistenerconflict.MainActivity;
import me.daei.touchlistenerconflict.ScreenUtils;

/**
 * Created by su on 2016/5/28.
 */
public class ControlScrollView  extends ScrollView {

    DragGridView grid;
    private boolean isInControl = true;
    private  int moveSpeed = 15;
    private final int msgWhat = 1;
    private final int time = 40;

    public ControlScrollView(Context context) {
        super(context);
    }

    public void setGrid(DragGridView grid) {
        this.grid = grid;
        init();
    }

    public ControlScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:/**/
                if (!isInControl) {
                    if (ev.getY() < 0) {
                        if (!myHandler.hasMessages(msgWhat)) {
                            Message msg = new Message();
                            msg.arg1 = -1;
                            msg.what = msgWhat;
                            myHandler.sendMessageDelayed(msg, time);
                        }
                        return super.dispatchTouchEvent(ev);
                    } else if (ev.getY() > getHeight()) {
                        if (!myHandler.hasMessages(msgWhat)) {
                            Message msg = new Message();
                            msg.arg1 = 1;
                            msg.what = msgWhat;
                            myHandler.sendMessageDelayed(msg, time);
                        }
                        return super.dispatchTouchEvent(ev);
                    }else{
                        myHandler.removeMessages(msgWhat);
                    }
                }else{
                    myHandler.removeMessages(msgWhat);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                grid.stopDrag();
                myHandler.removeMessages(msgWhat);
                requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }



    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        myHandler.removeMessages(msgWhat);
        grid.stopDrag();

    }


    private void init() {
        moveSpeed = ScreenUtils.dip2px(getContext(), moveSpeed);
        grid.setOnDragStartListener(new DragGridView.OnDragStartListener() {
            @Override
            public void onDragStart() {
                requestDisallowInterceptTouchEvent(true);
                isInControl = false;
                ((MainActivity) getContext()).setViewpagerNoSCroll(true);
            }
        });
        grid.setOnDragEndListener(new DragGridView.OnDragEndListener() {
            @Override
            public void onDragEnd() {
                requestDisallowInterceptTouchEvent(false);
                isInControl = true;
                ((MainActivity) getContext()).setViewpagerNoSCroll(false);
                grid.postInvalidate();
            }
        });
    }



    private Handler myHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            smoothScrollBy(0, moveSpeed * (msg.arg1 > 0 ? 1 : -1));
            Message msg1 = new Message();
            msg1.what = msg.what;
            msg1.arg1 = msg.arg1;
            myHandler.sendMessageDelayed(msg1, time);
        }
    };

}