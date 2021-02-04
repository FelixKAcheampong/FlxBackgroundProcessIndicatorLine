package com.felinkotech.flxbackgroundprocessindicatorline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.felinkotech.flxbgprocessindicatorline.FlxBackgroundProcessIndicatorLine;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private FlxBackgroundProcessIndicatorLine indicatorLine ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        indicatorLine = findViewById(R.id.indicator) ;
        findViewById(R.id.start).setOnClickListener(this) ;
        findViewById(R.id.stop).setOnClickListener(this) ;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.start){
            indicatorLine.start();
            return ;
        }
        indicatorLine.destroy();
    }
}