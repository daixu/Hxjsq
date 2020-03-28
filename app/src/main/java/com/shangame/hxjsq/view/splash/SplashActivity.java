package com.shangame.hxjsq.view.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.shangame.hxjsq.R;
import com.shangame.hxjsq.utils.StatusBarUtil;
import com.shangame.hxjsq.view.main.MainActivity;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import timber.log.Timber;

/**
 * 启动页
 * @author hhh
 */
public class SplashActivity extends RxAppCompatActivity implements SplashContract.View {

    private SplashPresenter mPresenter;

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
        final View contentView = View.inflate(this, R.layout.activity_splash, null);
        setContentView(contentView);
        StatusBarUtil.setTranslucentForImageView(this, 0, null);

        initPresenter();
        mPresenter.loadSplash();

        Animation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
        alphaAnimation.setFillEnabled(true);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        contentView.startAnimation(alphaAnimation);
    }

    private void initPresenter() {
        mPresenter = new SplashPresenter();
        mPresenter.takeView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mPresenter) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public void onBackPressed() {
        //屏蔽back键
    }

    @Override
    public void loadSplashSuccess(Object resp) {
        Timber.tag("loadSplashSuccess").e("resp= " + resp.toString());
    }

    @Override
    public void loadSplashFailure() {
        Timber.tag("loadSplashFailure").e("loadSplashFailure");
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.<T>bindToLifecycle();
    }
}
