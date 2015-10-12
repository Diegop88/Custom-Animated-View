package com.example.diego.animatedcustomview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class LinesLoading extends View {
    static String TAG = "LinesLoading";

    Rect rect;
    float Xini, Yini, Xfin, Yfin, Xact;
    Paint pBack, pAnim;
    List<Integer> colorList;
    int act;

    public LinesLoading(Context context) {
        this(context, null);
    }

    public LinesLoading(Context context, AttributeSet attrs) {
        super(context, attrs);

        setUp();

        Runnable animator = new Runnable() {
            @Override
            public void run() {
                updateValues();
                postDelayed(this, 1);
                invalidate(rect);
            }
        };

        removeCallbacks(animator);
        post(animator);
    }

    private void setUp() {
        colorList = new ArrayList<>();
        colorList.add(Color.BLUE);
        colorList.add(Color.RED);
        colorList.add(Color.YELLOW);
        colorList.add(Color.GREEN);

        pBack = new Paint();
        pBack.setColor(Color.GREEN);
        pBack.setStyle(Paint.Style.STROKE);
        pBack.setStrokeCap(Paint.Cap.ROUND);
        pBack.setAntiAlias(true);

        pAnim = new Paint();
        pAnim.setColor(Color.BLUE);
        pAnim.setStyle(Paint.Style.STROKE);
        pAnim.setStrokeCap(Paint.Cap.ROUND);
        pAnim.setAntiAlias(true);
    }

    private void updateValues() {
        if(Xact >= Xfin){
            act++;
            if(act == 4)
                act = 0;
            Xact = Xini;
            pBack.setColor(pAnim.getColor());
            pAnim.setColor(colorList.get(act));
        } else {
            Xact+=3;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float myPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

        rect = new Rect(0, 0, w, h);
        Xini = getPaddingLeft() + myPadding;
        Yini = Yfin = h / 2;
        Xfin = w - getPaddingRight() - myPadding;

        pBack.setStrokeWidth(h - getPaddingTop() - getPaddingBottom());
        pAnim.setStrokeWidth(h - getPaddingTop() - getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackLine(canvas);
        drawAnimLine(canvas);
    }

    private void drawBackLine(Canvas canvas) { canvas.drawLine(Xini, Yini, Xfin, Yfin, pBack); }

    private void drawAnimLine(Canvas canvas) { canvas.drawLine(Xini, Yini, Xact, Yfin, pAnim); }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getMyMeasure(40, widthMeasureSpec), getMyMeasure(4, heightMeasureSpec));
    }

    private int getMyMeasure(int minVal, int measureSpec) {
        int MINVAL = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minVal, getResources().getDisplayMetrics());

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