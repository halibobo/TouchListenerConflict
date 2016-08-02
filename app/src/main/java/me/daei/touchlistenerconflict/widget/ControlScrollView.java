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
public class ControlScrollView extends ScrollView {

    private boolean isInControl = true;
    private int moveSpeed = 5;
    private final int msgWhat = 1;
    private final int time = 10;
    private ScrollState scrollState;

    public ControlScrollView(Context context) {
        super(context);
        init();
    }

    public ControlScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
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
                        if (scrollState != null) {
                            scrollState.isCanDrag(false);
                        }
                        return super.dispatchTouchEvent(ev);
                    } else if (ev.getY() > getHeight()) {
                        if (!myHandler.hasMessages(msgWhat)) {
                            Message msg = new Message();
                            msg.arg1 = 1;
                            msg.what = msgWhat;
                            myHandler.sendMessageDelayed(msg, time);
                        }
                        if (scrollState != null) {
                            scrollState.isCanDrag(false);
                        }
                        return super.dispatchTouchEvent(ev);
                    } else {
                        if (scrollState != null) {
                            scrollState.isCanDrag(true);
                        }
                        myHandler.removeMessages(msgWhat);
                    }
                } else {
                    myHandler.removeMessages(msgWhat);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (scrollState != null) {
                    scrollState.stopTouch();
                }
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
    }


    private void init() {
        moveSpeed = ScreenUtils.dip2px(getContext(), moveSpeed);
    }

    public boolean isInControl() {
        return isInControl;
    }

    public ScrollState getScrollState() {
        return scrollState;
    }

    public void setScrollState(ScrollState scrollState) {
        this.scrollState = scrollState;
    }

    public void setInControl(boolean inControl) {
        isInControl = inControl;
    }

    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            smoothScrollBy(0, moveSpeed * (msg.arg1 > 0 ? 1 : -1));
            Message msg1 = new Message();
            msg1.what = msg.what;
            msg1.arg1 = msg.arg1;
            myHandler.sendMessageDelayed(msg1, time);
        }
    };

    public interface ScrollState {
        void stopTouch();
        void isCanDrag(boolean isCanDrag);
    }

}