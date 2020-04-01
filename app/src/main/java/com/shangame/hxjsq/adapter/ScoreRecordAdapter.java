package com.shangame.hxjsq.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.shangame.hxjsq.R;
import com.shangame.hxjsq.storage.model.ScoreRecordEntity;

import java.util.List;

public class ScoreRecordAdapter extends BaseQuickAdapter<ScoreRecordEntity, BaseViewHolder> {
    public ScoreRecordAdapter(int layoutResId, List<ScoreRecordEntity> data) {
        super(layoutResId, data);

        addChildClickViewIds(R.id.img_modify);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScoreRecordEntity dataBean) {
        helper.setText(R.id.text_number, "第" + dataBean.getNumber() + "局");
        TextView textName1 = helper.getView(R.id.text_name_1);
        TextView textName2 = helper.getView(R.id.text_name_2);
        TextView textName3 = helper.getView(R.id.text_name_3);
        ImageView imgName = helper.getView(R.id.img_name);
        if (TextUtils.isEmpty(dataBean.getName())) {
            textName1.setVisibility(View.GONE);
        } else {
            textName1.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(dataBean.getName2())) {
            textName2.setVisibility(View.GONE);
        } else {
            textName2.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(dataBean.getName3())) {
            textName3.setVisibility(View.GONE);
        } else {
            textName3.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(dataBean.getName()) || TextUtils.isEmpty(dataBean.getName2()) || TextUtils.isEmpty(dataBean.getName3())) {
            imgName.setVisibility(View.GONE);
        } else {
            imgName.setVisibility(View.VISIBLE);
        }

        textName1.setText(dataBean.getName());
        textName2.setText(dataBean.getName2());
        textName3.setText(dataBean.getName3());

        TextView textScore1 = helper.getView(R.id.text_score_1);
        TextView textScore2 = helper.getView(R.id.text_score_2);
        TextView textScore3 = helper.getView(R.id.text_score_3);

        textScore1.setText(checkPoint(dataBean.getScore() + ""));
        textScore2.setText(checkPoint(dataBean.getScore2() + ""));
        textScore3.setText(checkPoint(dataBean.getScore3() + ""));
        if (dataBean.getScore() > 0) {
            textScore1.setTextColor(ContextCompat.getColor(getContext(), R.color.numberBlack));
        } else {
            textScore1.setTextColor(ContextCompat.getColor(getContext(), R.color.numberRed));
        }

        if (dataBean.getScore2() > 0) {
            textScore2.setTextColor(ContextCompat.getColor(getContext(), R.color.numberBlack));
        } else {
            textScore2.setTextColor(ContextCompat.getColor(getContext(), R.color.numberRed));
        }

        if (dataBean.getScore3() > 0) {
            textScore3.setTextColor(ContextCompat.getColor(getContext(), R.color.numberBlack));
        } else {
            textScore3.setTextColor(ContextCompat.getColor(getContext(), R.color.numberRed));
        }
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
}
