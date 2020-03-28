package com.shangame.hxjsq.view.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.shangame.hxjsq.utils.StatusBarUtil;

public class FastStartAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            final Intent intent = getIntent();
            final String intentAction = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null
                    && intentAction.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
        startActivity(new Intent(this, SplashActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public void onBackPressed() {
        //屏蔽back键
    }
}
