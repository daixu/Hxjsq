package com.shangame.hxjsq.utils;

import android.util.Log;

import timber.log.Timber;

public class ReleaseTree extends Timber.DebugTree {
    @Override
    protected boolean isLoggable(String tag, int priority) {
        return priority == Log.WARN
                || priority == Log.ERROR
                || priority == Log.ASSERT;
    }

    @Override
    protected String createStackElementTag(StackTraceElement element) {
        return super.createStackElementTag(element) + ":" + element.getLineNumber();
    }
}