package com.shangame.hxjsq.view.record;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.shangame.hxjsq.R;
import com.shangame.hxjsq.adapter.ScoreRecordAdapter;
import com.shangame.hxjsq.app.App;
import com.shangame.hxjsq.storage.db.ScoreRecordEntityDao;
import com.shangame.hxjsq.storage.model.ScoreRecordEntity;
import com.shangame.hxjsq.utils.StatusBarUtil;
import com.shangame.hxjsq.view.setting.SettingActivity;
import com.shangame.hxjsq.widget.CustomDialog;
import com.shangame.hxjsq.widget.keyboard.SystemSoftKeyUtils;

import java.util.List;

import timber.log.Timber;

/**
 * 小局记录
 */
public class ScoreRecordActivity extends AppCompatActivity implements View.OnClickListener {
    private List<ScoreRecordEntity> mList;
    private ScoreRecordEntityDao dao;
    private CustomDialog mDialog;
    private ScoreRecordAdapter mAdapter;
    private ConstraintLayout rootView;
    private ScoreRecordEntity mEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_record);
        StatusBarUtil.setTranslucentForImageView(this, 0, null);

        rootView = findViewById(R.id.rootView);
        dao = App.getInstance().getSession().getScoreRecordEntityDao();

        RecyclerView recyclerRecord = findViewById(R.id.recycler_record);
        mList = dao.queryBuilder().orderDesc(ScoreRecordEntityDao.Properties.Number).list();

        mAdapter = new ScoreRecordAdapter(R.layout.item_score_record, mList);
        recyclerRecord.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        View notDataView = getLayoutInflater().inflate(R.layout.layout_empty_view, recyclerRecord, false);
        TextView textEmpty = notDataView.findViewById(R.id.text_empty);
        textEmpty.setText("暂时没有小局记录~");

        mAdapter.setEmptyView(notDataView);

        recyclerRecord.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.img_modify) {
                    if (null != mList && !mList.isEmpty()) {
                        ScoreRecordEntity entity = mList.get(position);
                        Timber.e("modify");
                        modify(entity);
                    }
                }
            }
        });

        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.img_setting).setOnClickListener(this);
    }

    private void modify(ScoreRecordEntity entity) {
        mEntity = entity;
        Intent intent = new Intent(this, ModifyRecordActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("entity", entity);
        intent.putExtras(bundle);
        startActivityForResult(intent, 101);
    }

    private boolean check(String value1, String value2, String value3) {
        boolean isEmpty = (TextUtils.isEmpty(value1) || TextUtils.isEmpty(value2) || TextUtils.isEmpty(value3));
        if (isEmpty) {
            dialogPromptInput(getString(R.string.prompt_score_error));
            return false;
        }

        double dValue1 = 0;
        double dValue2 = 0;
        double dValue3 = 0;
        try {
            if (!TextUtils.isEmpty(value1)) {
                dValue1 = Double.valueOf(value1);
            }
            if (!TextUtils.isEmpty(value2)) {
                dValue2 = Double.valueOf(value2);
            }
            if (!TextUtils.isEmpty(value3)) {
                dValue3 = Double.valueOf(value3);
            }

            if ((dValue1 > 0 && dValue2 > 0) || (dValue1 > 0 && dValue3 > 0) || (dValue2 > 0 && dValue3 > 0)) {
                dialogPrompt(getString(R.string.error_prompt_1));
                return false;
            }

            if (dValue1 < 0 && dValue2 < 0 && dValue3 < 0) {
                dialogPrompt(getString(R.string.error_prompt_2));
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            dialogPrompt(getString(R.string.error_score));
            return false;
        }

        Timber.e("check");
        return true;
    }

    private void dialogPrompt(String content) {
        SystemSoftKeyUtils.hideSoftInput(this, rootView);
        mDialog = new CustomDialog.Builder(this)
                .setMessage(content)
                .setNegativeButton(R.drawable.img_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                    }
                }).create();
        mDialog.show();
    }

    private void dialogPromptInput(String content) {
        mDialog = new CustomDialog.Builder(this)
                .setMessage(content)
                .setPositiveButton(R.drawable.img_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                    }
                }).create();
        mDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mDialog) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                if (null != data){
                    String score1 = data.getStringExtra("score1");
                    String score2 = data.getStringExtra("score2");
                    String score3 = data.getStringExtra("score3");
                    if (check(score1, score2, score3)) {
                        double dScore1 = Double.valueOf(score1);
                        double dScore2 = Double.valueOf(score2);
                        double dScore3 = Double.valueOf(score3);

                        mEntity.setScore(dScore1);
                        mEntity.setScore2(dScore2);
                        mEntity.setScore3(dScore3);
                        dao.update(mEntity);
                        mList = dao.queryBuilder().orderDesc(ScoreRecordEntityDao.Properties.Number).list();
                        mAdapter.setNewData(mList);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back: {
                finish();
            }
            break;
            case R.id.img_setting: {
                Intent intent = new Intent(ScoreRecordActivity.this, SettingActivity.class);
                startActivity(intent);
            }
            break;
            default:
                break;
        }
    }
}
