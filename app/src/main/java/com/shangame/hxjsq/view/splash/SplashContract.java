package com.shangame.hxjsq.view.splash;

import com.shangame.hxjsq.base.BasePresenter;
import com.shangame.hxjsq.base.BaseView;

public class SplashContract {

    interface Presenter extends BasePresenter {
        void takeView(SplashContract.View view);

        void loadSplash();
    }

    interface View extends BaseView<Presenter> {
        void loadSplashSuccess(Object resp);

        void loadSplashFailure();
    }
}
