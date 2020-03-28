package com.shangame.hxjsq.view.record;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.shangame.hxjsq.app.App;
import com.shangame.hxjsq.R;
import com.shangame.hxjsq.adapter.RecordAdapter;
import com.shangame.hxjsq.pojo.ResultBean;
import com.shangame.hxjsq.storage.db.ResultEntityDao;
import com.shangame.hxjsq.storage.model.ResultEntity;
import com.shangame.hxjsq.utils.StatusBarUtil;
import com.shangame.hxjsq.widget.CustomDialog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

/**
 * 记录
 * @author hhh
 */
public class RecordActivity extends AppCompatActivity implements View.OnClickListener {
    private ResultEntityDao dao;
    private List<ResultBean> mList = new ArrayList<>();
    private RecordAdapter mAdapter;
    private CustomDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        StatusBarUtil.setTranslucentForImageView(this, 0, null);

        dao = App.getInstance().getSession().getResultEntityDao();

        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.img_del).setOnClickListener(this);

        RecyclerView recyclerRecord = findViewById(R.id.recycler_record);
        recyclerRecord.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mAdapter = new RecordAdapter(R.layout.item_record, mList, dao);
        recyclerRecord.setAdapter(mAdapter);

        View notDataView = getLayoutInflater().inflate(R.layout.layout_empty_view, recyclerRecord, false);
        mAdapter.setEmptyView(notDataView);

        query(mAdapter);
    }

    private void query(RecordAdapter adapter) {
        long endTime = System.currentTimeMillis();
        long startTime = endTime - (7 * 24 * 60 * 60 * 1000);
        Timber.e("endTime= " + endTime);
        Timber.e("startTime= " + startTime);

        List<ResultEntity> list = dao.queryBuilder()
                .where(ResultEntityDao.Properties.Datetime.ge(startTime), ResultEntityDao.Properties.Datetime.le(endTime))
                .orderDesc(ResultEntityDao.Properties.Number).list();
        mList.clear();

        Set<String> set = new HashSet<>();
        for (ResultEntity entity : list) {
            set.add(entity.getDate());
        }

        for (String string : set) {
            ResultBean bean = new ResultBean();
            bean.date = string;
            List<ResultBean.ResultDataBean> dataBeans = new ArrayList<>();
            for (ResultEntity entity : list) {
                if (string.equals(entity.getDate())) {
                    ResultBean.ResultDataBean dataBean = new ResultBean.ResultDataBean();
                    dataBean.id = entity.getId();
                    dataBean.name = entity.getName();
                    dataBean.name2 = entity.getName2();
                    dataBean.name3 = entity.getName3();

                    dataBean.duplicate = entity.getDuplicate();
                    dataBean.duplicate2 = entity.getDuplicate2();
                    dataBean.duplicate3 = entity.getDuplicate3();

                    dataBean.accumulative = entity.getAccumulative();
                    dataBean.accumulative2 = entity.getAccumulative2();
                    dataBean.accumulative3 = entity.getAccumulative3();

                    dataBean.score = entity.getScore();
                    dataBean.score2 = entity.getScore2();
                    dataBean.score3 = entity.getScore3();

                    dataBean.datetime = entity.getDatetime();
                    dataBean.number = entity.getNumber();
                    dataBeans.add(dataBean);
                }
            }
            bean.resultData = dataBeans;
            mList.add(bean);
        }

        adapter.setNewData(mList);
        adapter.notifyDataSetChanged();
    }

    private void dialogPrompt(String content) {
        mDialog = new CustomDialog.Builder(this)
                .setMessage(content)
                .setNegativeButton(R.drawable.img_clear_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                    }
                })
                .setPositiveButton(R.drawable.img_confirm_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                        dao.deleteAll();
                        mList.clear();
                        mAdapter.notifyDataSetChanged();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back: {
                finish();
            }
            break;
            case R.id.img_del: {
                if (mList.size() == 0) {
                    Toast.makeText(RecordActivity.this, "暂无记录可供删除~", Toast.LENGTH_SHORT).show();
                } else {
                    dialogPrompt("确定要删除全部记录吗?");
                }
            }
            break;
            default:
                break;
        }
    }
}
