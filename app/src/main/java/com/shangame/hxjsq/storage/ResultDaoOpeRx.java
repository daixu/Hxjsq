package com.shangame.hxjsq.storage;

import android.content.Context;

import com.shangame.hxjsq.storage.manager.DbManager;
import com.shangame.hxjsq.storage.model.ResultEntity;

public class ResultDaoOpeRx {

    public static void insertData(Context context, ResultEntity entity) {
        DbManager.getDaoSession(context).getResultEntityDao().insert(entity);
    }
}
