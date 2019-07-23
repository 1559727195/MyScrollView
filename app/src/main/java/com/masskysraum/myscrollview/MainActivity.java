package com.masskysraum.myscrollview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
//
//        final TextView tv = (TextView) findViewById(R.id.tv);
//        tv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                int measuredWidth = tv.getMeasuredWidth();
//                int measuredHeight = tv.getMeasuredHeight();
//            }
//        });

        btn = (Button) findViewById(R.id.btn);

    }


    private void myclick() {

    }
}
