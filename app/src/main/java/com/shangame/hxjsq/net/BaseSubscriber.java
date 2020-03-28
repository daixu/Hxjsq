package com.shangame.hxjsq.net;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public abstract class BaseSubscriber<T> implements Subscriber<T> {

    @Override
    public void onError(Throwable e) {
        hideDialog();
        if(e instanceof ExceptionHandle.ResponseThrowable){
            onError((ExceptionHandle.ResponseThrowable)e);
        } else {
            onError(new ExceptionHandle.ResponseThrowable(e, ExceptionHandle.ERROR.UNKNOWN));
        }
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
        showDialog();
    }

    protected abstract void hideDialog();

    protected abstract void showDialog();

    @Override
    public void onComplete() {
        hideDialog();
    }

    public abstract void onError(ExceptionHandle.ResponseThrowable e);
}