package com.shangame.hxjsq.view.record;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shangame.hxjsq.R;
import com.shangame.hxjsq.adapter.ScoreRecordAdapter;
import com.shangame.hxjsq.app.App;
import com.shangame.hxjsq.storage.db.ScoreRecordEntityDao;
import com.shangame.hxjsq.storage.model.ScoreRecordEntity;
import com.shangame.hxjsq.utils.StatusBarUtil;
import com.shangame.hxjsq.view.setting.SettingActivity;

import java.util.List;

import timber.log.Timber;

/**
 * 小局记录
 */
public class ScoreRecordActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_record);
        StatusBarUtil.setTranslucentForImageView(this, 0, null);

        ScoreRecordEntityDao recordEntityDao = App.getInstance().getSession().getScoreRecordEntityDao();

        RecyclerView recyclerRecord = findViewById(R.id.recycler_record);
        List<ScoreRecordEntity> list = recordEntityDao.loadAll();
        Timber.e("list= " + list);
        ScoreRecordAdapter adapter = new ScoreRecordAdapter(R.layout.item_score_record, list);
        recyclerRecord.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        View notDataView = getLayoutInflater().inflate(R.layout.layout_empty_view, recyclerRecord, false);
        TextView textEmpty = notDataView.findViewById(R.id.text_empty);
        textEmpty.setText("暂时没有小局记录~");

        adapter.setEmptyView(notDataView);

        recyclerRecord.setAdapter(adapter);

        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.img_setting).setOnClickListener(this);
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
