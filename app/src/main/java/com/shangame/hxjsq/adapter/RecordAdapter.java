package com.shangame.hxjsq.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.shangame.hxjsq.R;
import com.shangame.hxjsq.pojo.ResultBean;
import com.shangame.hxjsq.storage.db.ResultEntityDao;
import com.shangame.hxjsq.storage.model.ResultEntity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

public class RecordAdapter extends BaseQuickAdapter<ResultBean, BaseViewHolder> {
    private ResultEntityDao mDao;
    private List<ResultBean> mData;

    public RecordAdapter(int layoutResId, List<ResultBean> data) {
        super(layoutResId, data);
        mData = data;
    }

    public RecordAdapter(int layoutResId, List<ResultBean> data, ResultEntityDao dao) {
        super(layoutResId, data);
        mData = data;
        mDao = dao;
    }

    @Override
    protected void convert(BaseViewHolder helper, final ResultBean entity) {
        helper.setText(R.id.text_date, entity.date);
        RecyclerView recyclerView = helper.getView(R.id.recycler_record);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        if (null != entity.resultData) {
            final RecordItemAdapter adapter = new RecordItemAdapter(R.layout.item_record_item, entity.resultData);
            recyclerView.setAdapter(adapter);

            adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                    if (view.getId() == R.id.img_del) {
                        Timber.e("onItemChildClick " + position);
                        ResultBean.ResultDataBean dataBean = entity.resultData.get(position);
                        if (null != mDao) {
                            ResultEntity resultEntity = mDao.queryBuilder().orderDesc(ResultEntityDao.Properties.Number).where(ResultEntityDao.Properties.Id.eq(dataBean.id)).unique();
                            if (null != resultEntity) {
                                mDao.delete(resultEntity);
                                entity.resultData.remove(dataBean);

                                if (entity.resultData.isEmpty()) {
                                    mData.remove(entity);
                                }
                                notifyDataSetChanged();
                            }
                        }
                    }
                }
            });
        }
    }
}
