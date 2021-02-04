package com.felinkotech.flxbgprocessindicatorline;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FlxBackgroundProcessIndicatorLine extends LinearLayout {
    private Context context;
    private View viewIndicator;
    LinearLayout.LayoutParams params;
    int screenWidth = 0;
    private Disposable disposable;
    private float indicatorWidth;
    private float indicatorHeight;
    private int indicatorColor;
    private int indicatorSpeed ;

    public FlxBackgroundProcessIndicatorLine(Context context) {
        super(context);
        this.context = context;
        createIndicator(null);
    }

    public FlxBackgroundProcessIndicatorLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        createIndicator(attrs);
    }

    public FlxBackgroundProcessIndicatorLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        createIndicator(attrs);
    }

    private void processAttributes(AttributeSet attributeSet) {
        TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.FlxBackgroundProcessIndicatorLine, 0, 0);
        try {
            indicatorWidth = array.getDimension(R.styleable.FlxBackgroundProcessIndicatorLine_indicator_width, 20.0f);
            indicatorHeight = array.getDimension(R.styleable.FlxBackgroundProcessIndicatorLine_indicator_height, 2.0f);
            indicatorColor = array.getColor(R.styleable.FlxBackgroundProcessIndicatorLine_indicator_color, Color.BLACK);
            indicatorSpeed = array.getInt(R.styleable.FlxBackgroundProcessIndicatorLine_indicator_speed, 1);
        } finally {
            array.recycle();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FlxBackgroundProcessIndicatorLine(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        createIndicator(attrs);
    }

    private void createIndicator(AttributeSet attributeSet) {
        if (attributeSet != null) {
            processAttributes(attributeSet);
        }
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        removeAllViews();
        destroy();

        viewIndicator = new View(context);
        viewIndicator.setBackgroundColor(indicatorColor);

        params = new LinearLayout.LayoutParams((int) indicatorWidth, (int) indicatorHeight);
        viewIndicator.setLayoutParams(params);
        addView(viewIndicator);
        setVisibility(View.GONE);
    }

    public void start() {
        setVisibility(View.VISIBLE);
        moveForward();
    }

    public void destroy() {
        setVisibility(View.GONE);
        if (disposable != null) disposable.dispose();
    }

    private void moveForward() {
        disposable = Observable.intervalRange(0, screenWidth-((int)indicatorWidth), 0, indicatorSpeed, TimeUnit.MILLISECONDS, Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next -> {
                            if (next == null) return;
                            params.leftMargin = next.intValue();
                            viewIndicator.setLayoutParams(params);
                        }, err -> {
                        },
                        () -> {
                            moveBackward();
                            invalidate();
                            requestFocus();
                        });
    }

    private void moveBackward() {
        new CountDown((long) screenWidth, TimeUnit.MILLISECONDS) {

            @Override
            public void onTick(long tickValue) {
                params.leftMargin = (int) tickValue;
                viewIndicator.setLayoutParams(params);
            }

            @Override
            public void onFinish() {
                moveForward();
                invalidate();
                requestFocus();
            }
        }.start();
    }

    abstract class CountDown {

        private TimeUnit timeUnit;
        private Long startValue;

        public CountDown(Long startValue, TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
            this.startValue = startValue;
        }

        public abstract void onTick(long tickValue);

        public abstract void onFinish();

        public void start() {
            io.reactivex.Observable.zip(
                    io.reactivex.Observable.range(0, startValue.intValue()), io.reactivex.Observable.interval(indicatorSpeed, timeUnit), (integer, aLong) -> {
                        Long l = startValue - integer;
                        return l;
                    }
            ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Long>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable = d;
                        }

                        @Override
                        public void onNext(Long aLong) {
                            onTick(aLong);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {
                            onFinish();
                        }
                    });
        }
    }
}
