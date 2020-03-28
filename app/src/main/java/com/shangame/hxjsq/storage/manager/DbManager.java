package com.shangame.hxjsq.storage.manager;

import android.content.Context;

import com.shangame.hxjsq.storage.db.DaoMaster;
import com.shangame.hxjsq.storage.db.DaoSession;
import com.shangame.hxjsq.storage.db.DbHelper;

import org.greenrobot.greendao.database.Database;

/**
 * Create by Speedy on 2018/8/16
 */
public class DbManager {

    private static final String DATABASE_NAME = "HXJSQ.db";

    private static DaoSession daoSession;

    private static final Object mLock = new Object();

    /**
     * 获取
     * @param context
     * @return
     */
    public static DaoSession getDaoSession(Context context){
        if(daoSession == null) {
            synchronized (mLock) {
                if (daoSession == null) {
                    DbHelper helper = new DbHelper(context, DATABASE_NAME, null);
                    Database db = helper.getWritableDb();
                    daoSession = new DaoMaster(db).newSession();
                }
            }
        }
        return daoSession;
    }


    /**
     * 关闭
     */
    public static void close(){
        if(daoSession != null) {
            synchronized (mLock) {
                daoSession.clear();
                daoSession = null;
            }
        }
    }
}
