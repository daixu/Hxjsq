package com.shangame.hxjsq.widget;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.lxj.xpopup.core.CenterPopupView;
import com.shangame.hxjsq.R;
import com.shangame.hxjsq.storage.model.ScoreRecordEntity;

/**
 * @author hhh
 */
public class ModifyRecordPopupWindow extends CenterPopupView {
    private View.OnClickListener mSaveListener;
    private View.OnClickListener mCancelListener;
    private EditText mEditScore1;
    private EditText mEditScore2;
    private EditText mEditScore3;
    private ScoreRecordEntity mEntity;

    public ModifyRecordPopupWindow(@NonNull Context context) {
        this(context, null);
    }

    public ModifyRecordPopupWindow(@NonNull Context context, ScoreRecordEntity entity) {
        super(context);
        this.mEntity = entity;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_modify_record;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
        initListener();
    }

    private void initView() {
        mEditScore1 = findViewById(R.id.edit_score_1);
        mEditScore2 = findViewById(R.id.edit_score_2);
        mEditScore3 = findViewById(R.id.edit_score_3);

        if (null != mEntity) {
            mEditScore1.setText(checkPoint(mEntity.getScore() + ""));
            mEditScore2.setText(checkPoint(mEntity.getScore2() + ""));
            mEditScore3.setText(checkPoint(mEntity.getScore3() + ""));

            if (mEntity.getScore() > 0) {
                mEditScore1.setTextColor(ContextCompat.getColor(getContext(), R.color.numberBlack));
            } else {
                mEditScore1.setTextColor(ContextCompat.getColor(getContext(), R.color.numberRed));
            }

            if (mEntity.getScore2() > 0) {
                mEditScore2.setTextColor(ContextCompat.getColor(getContext(), R.color.numberBlack));
            } else {
                mEditScore2.setTextColor(ContextCompat.getColor(getContext(), R.color.numberRed));
            }

            if (mEntity.getScore3() > 0) {
                mEditScore3.setTextColor(ContextCompat.getColor(getContext(), R.color.numberBlack));
            } else {
                mEditScore3.setTextColor(ContextCompat.getColor(getContext(), R.color.numberRed));
            }
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

    private void initListener() {
        findViewById(R.id.img_save).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mSaveListener) {
                    mSaveListener.onClick(v);
                }
            }
        });

        findViewById(R.id.img_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCancelListener) {
                    mCancelListener.onClick(v);
                }
            }
        });
    }

    public String getScore1() {
        return mEditScore1.getText().toString();
    }

    public String getScore2() {
        return mEditScore2.getText().toString();
    }

    public String getScore3() {
        return mEditScore3.getText().toString();
    }

    public void setSaveClickListener(View.OnClickListener saveListener) {
        this.mSaveListener = saveListener;
    }

    public void setCancelListener(View.OnClickListener cancelListener) {
        this.mCancelListener = cancelListener;
    }
}
