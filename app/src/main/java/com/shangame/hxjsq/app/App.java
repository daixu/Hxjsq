package com.shangame.hxjsq.app;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.shangame.hxjsq.BuildConfig;
import com.shangame.hxjsq.storage.db.DaoMaster;
import com.shangame.hxjsq.storage.db.DaoSession;
import com.shangame.hxjsq.utils.FakeCrashLibrary;
import com.shangame.hxjsq.utils.ReleaseTree;

import timber.log.Timber;

public class App extends Application {

    public static App instance;
    private DaoMaster.DevOpenHelper dbHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ReleaseTree());
        }
        initDatabase();
    }

    private void initDatabase() {
        //这里之后会修改，关于升级数据库
        dbHelper = new DaoMaster.DevOpenHelper(this, "HXJSQ.db", null);
        db = dbHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private static class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, @NonNull String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            FakeCrashLibrary.log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                    FakeCrashLibrary.logError(t);
                } else if (priority == Log.WARN) {
                    FakeCrashLibrary.logWarning(t);
                }
            }
        }
    }

    public DaoSession getSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
