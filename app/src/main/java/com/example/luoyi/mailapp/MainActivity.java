package com.example.luoyi.mailapp;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    private LinearLayout layout;
    private LinearLayout animLayout;
    private RelativeLayout leftLayout;
    private RelativeLayout rightLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opendoor);
        initViews();
    }

    private void initViews() {
        Button btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_start:
                        doOpenDoor();
                        break;
                    default:
                        break;
                }
            }

        });

        layout = (LinearLayout) findViewById(R.id.layout);
        animLayout = (LinearLayout) findViewById(R.id.animLayout);
        leftLayout = (RelativeLayout) findViewById(R.id.leftLayout);
        rightLayout = (RelativeLayout) findViewById(R.id.rightLayout);
    }

    private void doOpenDoor() {
        layout.setVisibility(View.GONE);
        animLayout.setVisibility(View.VISIBLE);
        Animation leftOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_left);
        Animation rightOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right);

        leftLayout.setAnimation(leftOutAnimation);
        rightLayout.setAnimation(rightOutAnimation);
        leftOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animLayout.setVisibility(View.GONE);
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
                overridePendingTransition(R.anim.zoom_out_enter, R.anim.zoom_out_exit);
            }
        });

    }
}
