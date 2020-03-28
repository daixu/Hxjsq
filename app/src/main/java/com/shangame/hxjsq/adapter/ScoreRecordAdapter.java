package com.shangame.hxjsq.adapter;

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
    }

    @Override
    protected void convert(BaseViewHolder helper, ScoreRecordEntity dataBean) {
        helper.setText(R.id.text_number, "第" + dataBean.getNumber() + "局");
        helper.setText(R.id.text_name_1, dataBean.getName());
        helper.setText(R.id.text_name_2, dataBean.getName2());
        helper.setText(R.id.text_name_3, dataBean.getName3());

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
