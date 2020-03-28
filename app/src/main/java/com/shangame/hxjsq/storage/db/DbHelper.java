package com.shangame.hxjsq.storage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

/**
 * Create by Speedy on 2018/8/16
 */
public class DbHelper extends DaoMaster.OpenHelper {
    private static final String TAG = "DbHelper";

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade: oldVersion = " + oldVersion + " newVersion = " + newVersion);
        switch (oldVersion) {
            case 1:
                DaoMaster.dropAllTables(db, false);
                DaoMaster.createAllTables(db, false);
                break;
            default:
                break;
        }
    }
}
