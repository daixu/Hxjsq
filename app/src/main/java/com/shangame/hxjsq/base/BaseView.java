package com.shangame.hxjsq.base;

import com.trello.rxlifecycle2.LifecycleTransformer;

public interface BaseView<T> {

    boolean isActive();

    <T> LifecycleTransformer<T> bindToLife();
}