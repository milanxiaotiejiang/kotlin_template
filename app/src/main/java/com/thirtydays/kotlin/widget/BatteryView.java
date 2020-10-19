package com.thirtydays.kotlin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.thirtydays.kotlin.R;

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/6
 * desc   :
 * </pre>
 */
public class BatteryView extends View {

    private final Paint mPaint;
    /**
     * 电量 (0 - 100)
     */
    private int mBatteryPercent = 100;
    /**
     * 绘制颜色
     */
    private int mColor = getResources().getColor(R.color.color1D9ADD);

    public BatteryView(Context context) {
        this(context, null);
    }

    public BatteryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BatteryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BatteryView, 0, defStyleAttr);
        for (int i = 0; i < typedArray.length(); i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.BatteryView_battery_percent:
                    mBatteryPercent = typedArray.getInteger(attr, 50);
                    break;
                case R.styleable.BatteryView_battery_color:
                    mColor = typedArray.getColor(attr, ContextCompat.getColor(context, R.color.color1D9ADD));
                    break;
            }
        }
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        int width = 0, height = 0;
        if (specMode == MeasureSpec.EXACTLY) {
            width = specSize + getPaddingRight() + getPaddingLeft();
        }
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            height = specSize + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //圆角大小
        float round = getHeight() / 40f;
        //内部边距
        float inside = 0;
        //填充模式
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL);
        //整体宽高
        int wholeWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int wholeHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        //凸点的宽度
        float bumpWidget = wholeWidth / 4f;
        //将高度分为38分，三份为凸点，一份上空白，一份内部上空白，一份内部下空白，一份上线一份下线，三十份电量
        float bumpHeight = wholeHeight / 38f;

        float wholeWidthHalf = wholeWidth >> 1;
        float bumpWidthHalf = bumpWidget / 2;
        //凸点
        canvas.drawRoundRect(
                getPaddingLeft() + wholeWidthHalf - bumpWidthHalf,
                getPaddingTop(),
                getPaddingLeft() + wholeWidthHalf + bumpWidthHalf,
                getPaddingTop() + bumpHeight * 3, round, round, mPaint
        );

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(bumpHeight);
        //电池体
        canvas.drawRoundRect(
                getPaddingLeft(),
                getPaddingTop() + bumpHeight * 4,
                getPaddingLeft() + wholeWidth,
                getPaddingTop() + wholeHeight,
                round, round, mPaint
        );

        float insideHeight = bumpHeight * 30;
        //电量高度
        float batteryH = insideHeight * mBatteryPercent / 100;

        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(
                getPaddingLeft() + inside + bumpHeight,
                getPaddingTop() + bumpHeight * 4 + (insideHeight - batteryH) + bumpHeight,
                getPaddingLeft() + wholeWidth - bumpHeight - inside,
                getPaddingTop() + wholeHeight - bumpHeight,
                round, round, mPaint
        );

    }

    public void setBatteryPercent(int batteryPercent) {
        this.mBatteryPercent = batteryPercent;
        invalidate();
    }
}
