package me.daei.touchlistenerconflict.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import me.daei.touchlistenerconflict.R;
import me.daei.touchlistenerconflict.ScreenUtils;


/**
 * Created by su on 2016/5/10.
 */
public class ViewWithSign extends LinearLayout {

    private Path path3;
    private int dis;
    private int disY;

    public ViewWithSign(Context context) {
        super(context);
        addDrawText(getCont());
    }

    public ViewWithSign(Context context, AttributeSet attrs) {
        super(context, attrs);
        addDrawText(getCont());
    }

    private String cont = ""; //提示内容
    private Paint textPaint = new Paint();
    private int paintMiddle;
    private int startDis;
    private int textSize = 12;
    private int width;

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRect(canvas);
    }



    public void addDrawText(String string) {
        if (!TextUtils.isEmpty(string)) {
            setCont(string);
        }else{
            setCont("");
        }
        invalidate();
    }


    private void drawRect(Canvas canvas) {
        if (TextUtils.isEmpty(getCont())) {
            return;
        }
        textPaint.setAntiAlias(true);  //抗锯齿
        path3 = new Path();
        path3.moveTo(width - startDis, 0);
        path3.lineTo(width - startDis - paintMiddle, 0);
        path3.lineTo(width, startDis + paintMiddle);
        path3.lineTo(width, startDis);
        path3.close();
        textPaint.setColor(getResources().getColor(R.color.signColor));//ea5f61
        canvas.drawPath(path3, textPaint);
        canvas.save();
        textPaint.setStrokeWidth(3);
        textPaint.setTextSize(textSize* ScreenUtils.getDensity(getContext()));
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        dis = (paintMiddle/2  + startDis) /2;
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        disY = dis - (fontMetrics.bottom + fontMetrics.top) / 2;
        canvas.rotate(45, width-dis, dis);
        canvas.drawText(cont, width - dis, disY, textPaint);
        canvas.rotate(-45, dis, dis);
        canvas.restore();
    }

    @SuppressWarnings("unused")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // For simple implementation, or internal size is always 0.
        // We depend on the container to specify the layout size of
        // our view. We can't really know what it is since we will be
        // adding and removing different arbitrary views and do not
        // want the layout to change as this happens.
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

        // Children are just made to fill our space.
        int childWidthSize = getMeasuredWidth();
        int childHeightSize = getMeasuredHeight();

        //高度和宽度一样
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getWidth();
        paintMiddle = width / 5;
        startDis = width / 4;
    }
}
