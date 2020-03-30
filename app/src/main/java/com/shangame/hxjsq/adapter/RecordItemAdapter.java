package com.shangame.hxjsq.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.shangame.hxjsq.R;
import com.shangame.hxjsq.pojo.ResultBean;

import java.util.List;

public class RecordItemAdapter extends BaseQuickAdapter<ResultBean.ResultDataBean, BaseViewHolder> {
    public RecordItemAdapter(int layoutResId, List<ResultBean.ResultDataBean> data) {
        super(layoutResId, data);

        addChildClickViewIds(R.id.img_del);
    }

    @Override
    protected void convert(BaseViewHolder helper, ResultBean.ResultDataBean dataBean) {
        helper.setText(R.id.text_number, "第" + dataBean.number + "局");
        TextView textName1 = helper.getView(R.id.text_name_1);
        TextView textName2 = helper.getView(R.id.text_name_2);
        TextView textName3 = helper.getView(R.id.text_name_3);
        ImageView imgName = helper.getView(R.id.img_name);
        if (TextUtils.isEmpty(dataBean.name)) {
            textName1.setVisibility(View.GONE);
        } else {
            textName1.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(dataBean.name2)) {
            textName2.setVisibility(View.GONE);
        } else {
            textName2.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(dataBean.name3)) {
            textName3.setVisibility(View.GONE);
        } else {
            textName3.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(dataBean.name) || TextUtils.isEmpty(dataBean.name2) || TextUtils.isEmpty(dataBean.name3)) {
            imgName.setVisibility(View.GONE);
        } else {
            imgName.setVisibility(View.VISIBLE);
        }

        textName1.setText(dataBean.name);
        textName2.setText(dataBean.name2);
        textName3.setText(dataBean.name3);

        TextView textAccumulative1 = helper.getView(R.id.text_accumulative_1);
        TextView textAccumulative2 = helper.getView(R.id.text_accumulative_2);
        TextView textAccumulative3 = helper.getView(R.id.text_accumulative_3);

        TextView textScore1 = helper.getView(R.id.text_score_1);
        TextView textScore2 = helper.getView(R.id.text_score_2);
        TextView textScore3 = helper.getView(R.id.text_score_3);

        textAccumulative1.setText(checkPoint(dataBean.accumulative + ""));
        textAccumulative2.setText(checkPoint(dataBean.accumulative2 + ""));
        textAccumulative3.setText(checkPoint(dataBean.accumulative3 + ""));
        setAccumulativeColor(dataBean, textAccumulative1, textAccumulative2, textAccumulative3);

        textScore1.setText(checkPoint(dataBean.score + ""));
        textScore2.setText(checkPoint(dataBean.score2 + ""));
        textScore3.setText(checkPoint(dataBean.score3 + ""));
        setScoreColor(dataBean, textScore1, textScore2, textScore3);
    }

    private String checkPoint(String num) {
        if (!num.contains(".")) {
            return num;
        }
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String integerNum = num.substring(0, num.indexOf("."));
            String decimals = num.substring(num.indexOf("."));
            float decimalsNum = Float.valueOf(decimals);
            if (decimalsNum > 0) {
                stringBuilder = stringBuilder.append(integerNum).append(decimals);
            } else {
                stringBuilder = stringBuilder.append(integerNum);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return String.valueOf(stringBuilder);
    }

    private void setAccumulativeColor(ResultBean.ResultDataBean dataBean, TextView textAccumulative1, TextView textAccumulative2, TextView textAccumulative3) {
        if (dataBean.accumulative > 0) {
            textAccumulative1.setTextColor(ContextCompat.getColor(getContext(), R.color.numberBlack));
        } else {
            textAccumulative1.setTextColor(ContextCompat.getColor(getContext(), R.color.numberRed));
        }

        if (dataBean.accumulative2 > 0) {
            textAccumulative2.setTextColor(ContextCompat.getColor(getContext(), R.color.numberBlack));
        } else {
            textAccumulative2.setTextColor(ContextCompat.getColor(getContext(), R.color.numberRed));
        }

        if (dataBean.accumulative3 > 0) {
            textAccumulative3.setTextColor(ContextCompat.getColor(getContext(), R.color.numberBlack));
        } else {
            textAccumulative3.setTextColor(ContextCompat.getColor(getContext(), R.color.numberRed));
        }
    }

    private void setScoreColor(ResultBean.ResultDataBean dataBean, TextView textScore1, TextView textScore2, TextView textScore3) {
        if (dataBean.score > 0) {
            textScore1.setTextColor(ContextCompat.getColor(getContext(), R.color.numberBlack));
        } else {
            textScore1.setTextColor(ContextCompat.getColor(getContext(), R.color.numberRed));
        }

        if (dataBean.score2 > 0) {
            textScore2.setTextColor(ContextCompat.getColor(getContext(), R.color.numberBlack));
        } else {
            textScore2.setTextColor(ContextCompat.getColor(getContext(), R.color.numberRed));
        }

        if (dataBean.score3 > 0) {
            textScore3.setTextColor(ContextCompat.getColor(getContext(), R.color.numberBlack));
        } else {
            textScore3.setTextColor(ContextCompat.getColor(getContext(), R.color.numberRed));
        }
    }
}
