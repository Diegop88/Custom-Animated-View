package com.example.diego.animatedcustomview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

public class MyLoadingView extends View {
    static String TAG = "MyLoadingView";

    float value;

    Rect rect;
    RectF rectF;

    Paint pBackRing, pRing;
    float strokeWidth;

    public MyLoadingView(Context context) {
        this(context, null);
    }

    public MyLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray values = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyLoadingView, 0, 0);
        try {
            createPaints(values);
        } finally {
            values.recycle();
        }

        removeCallbacks(animator);
        post(animator);
    }

    private void createPaints(TypedArray values) {
        int backRingColor;
        int valRingColor;
        float ringStrokeWidth;

        ringStrokeWidth = values.getDimension(R.styleable.MyLoadingView_ring_stroke_width, 4);
        backRingColor = values.getColor(R.styleable.MyLoadingView_back_ring_color, Color.CYAN);
        valRingColor = values.getColor(R.styleable.MyLoadingView_val_ring_color, Color.BLUE);

        strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ringStrokeWidth, getResources().getDisplayMetrics());

        pBackRing = new Paint();
        pBackRing.setStrokeWidth(strokeWidth);
        pBackRing.setColor(backRingColor);
        pBackRing.setStyle(Paint.Style.STROKE);
        pBackRing.setAntiAlias(true);

        pRing = new Paint();
        pRing.setStrokeWidth(strokeWidth);
        pRing.setColor(valRingColor);
        pRing.setStrokeCap(Paint.Cap.ROUND);
        pRing.setStyle(Paint.Style.STROKE);
        pRing.setAntiAlias(true);
    }

    private Runnable animator = new Runnable() {
        @Override
        public void run() {
            updateValues();
            postDelayed(this, 5);
            invalidate(rect);
        }
    };

    private void updateValues() {
        value++;
        if(value == 360)
            value = 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        rect = new Rect(getPaddingLeft(), getPaddingTop(), w - getPaddingRight(), h - getPaddingBottom());
        rectF = new RectF(rect.left + strokeWidth, rect.top + strokeWidth, rect.right - strokeWidth, rect.bottom - strokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackRing(canvas);
        drawLoading(canvas);
    }

    private void drawBackRing(Canvas canvas) {
        canvas.drawArc(rectF, 0, 360, false, pBackRing);
    }

    private void drawLoading(Canvas canvas) {
        canvas.drawArc(rectF, value, 1, false, pRing);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getMyMeasure(widthMeasureSpec), getMyMeasure(heightMeasureSpec));
    }

    private int getMyMeasure(int measureSpec) {
        int MINVAL = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());

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