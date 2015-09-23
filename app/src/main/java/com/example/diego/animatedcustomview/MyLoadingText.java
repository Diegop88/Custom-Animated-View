package com.example.diego.animatedcustomview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class MyLoadingText extends View {

    String text;
    Paint pText, pDot;
    int currentDot;
    float centerX, centerY;

    public MyLoadingText(Context context) {
        this(context, null);
    }

    public MyLoadingText(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray values = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyLoadingText, 0, 0);

        try {
            setValues(values);
        } finally {
            values.recycle();
        }

        removeCallbacks(animator);
        post(animator);
    }

    private void setValues(TypedArray values) {
        float textSize;
        int textColor;

        currentDot = 1;

        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());
        text = values.getString(R.styleable.MyLoadingText_loading_text);
        if(text == null)
            text = "Loading";

        textColor = values.getColor(R.styleable.MyLoadingText_loading_text_color, Color.BLACK);
        textSize = values.getDimension(R.styleable.MyLoadingText_loading_text_size, textSize);

        pText = new Paint();
        pText.setColor(textColor);
        pText.setTextSize(textSize);
        pText.setAntiAlias(true);
        pText.setTextAlign(Paint.Align.CENTER);

        pDot = new Paint();
        pDot.setColor(Color.LTGRAY);
        pDot.setStyle(Paint.Style.FILL);
        pDot.setAntiAlias(true);
    }

    private Runnable animator = new Runnable() {
        @Override
        public void run() {
            updateValues();
            postDelayed(this, 500);
            invalidate();
        }
    };

    private void updateValues() {
        currentDot++;
        if(currentDot == 4)
            currentDot = 1;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        centerX = w / 2;
        centerY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawText(text, centerX, centerY, pText);
        drawDots(canvas);
    }

    private void drawDots(Canvas canvas) {
        canvas.drawCircle(centerX, centerY + 25, 10, pDot);
        canvas.drawCircle(centerX - 45, centerY + 25, 10, pDot);
        canvas.drawCircle(centerX + 45, centerY + 25, 10, pDot);

        switch (currentDot) {
            case 1:
                canvas.drawCircle(centerX - 45, centerY + 25, 10, pText);
                break;
            case 2:
                canvas.drawCircle(centerX, centerY + 25, 10, pText);
                break;
            case 3:
                canvas.drawCircle(centerX + 45, centerY + 25, 10, pText);
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getMyMeasure(widthMeasureSpec), getMyMeasure(heightMeasureSpec));
    }

    private int getMyMeasure(int measureSpec) {
        int MINVAL = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());

        int dimen = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);

        if(mode == MeasureSpec.EXACTLY)
            return dimen;
        else if (mode == MeasureSpec.AT_MOST)
            return Math.min(dimen, MINVAL);
        else
            return MINVAL;
    }
}
