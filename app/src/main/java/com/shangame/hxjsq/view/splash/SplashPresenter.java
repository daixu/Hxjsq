package com.shangame.hxjsq.view.splash;

import com.shangame.hxjsq.BuildConfig;
import com.shangame.hxjsq.net.ApiServer;
import com.shangame.hxjsq.net.BaseSubscriber;
import com.shangame.hxjsq.net.ExceptionHandle;
import com.shangame.hxjsq.net.RetrofitManager;
import com.shangame.hxjsq.utils.DeviceUtil;
import com.shangame.hxjsq.utils.RxUtil;

import timber.log.Timber;

public class SplashPresenter implements SplashContract.Presenter {
    private SplashContract.View mView;
    private ApiServer mApiServer;

    SplashPresenter() {
        mApiServer = RetrofitManager.getInstance().getApiServer();
    }

    @Override
    public void takeView(SplashContract.View view) {
        mView = view;
    }

    @Override
    public void loadSplash() {
        String device = DeviceUtil.getAndroidID();
        int channel = BuildConfig.CHANNEL_ID;
        mApiServer.setLoginLog(device, channel).compose(RxUtil.<Object>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER_BACK_PRESSURE))
                .compose(mView.bindToLife())
                .subscribe(new BaseSubscriber<Object>() {
                    @Override
                    public void onNext(Object resp) {
                        mView.loadSplashSuccess(resp);
                    }

                    @Override
                    protected void hideDialog() {

                    }

                    @Override
                    protected void showDialog() {

                    }

                    @Override
                    public void onError(ExceptionHandle.ResponseThrowable e) {
                        mView.loadSplashFailure();
                    }
                });
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        Timber.tag("SplashPresenter").e("unSubscribe");
        mView = null;
    }
}
