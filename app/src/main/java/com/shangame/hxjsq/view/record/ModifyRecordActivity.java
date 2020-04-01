package com.shangame.hxjsq.view.record;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.shangame.hxjsq.R;
import com.shangame.hxjsq.storage.model.ScoreRecordEntity;
import com.shangame.hxjsq.utils.StatusBarUtil;
import com.shangame.hxjsq.widget.CustomDialog;
import com.shangame.hxjsq.widget.DecimalInputTextWatcher;
import com.shangame.hxjsq.widget.keyboard.KeyboardViewManager;

import timber.log.Timber;

public class ModifyRecordActivity extends AppCompatActivity {

    private EditText mEditScore1;
    private EditText mEditScore2;
    private EditText mEditScore3;
    private ScoreRecordEntity mEntity;
    private CustomDialog mDialog;
    private FrameLayout rootView;
    private KeyboardViewManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_record);
        StatusBarUtil.setTranslucentForImageView(this, 0, null);

        Intent intent = getIntent();
        mEntity = (ScoreRecordEntity) intent.getSerializableExtra("entity");

        rootView = findViewById(R.id.rootView);
        mEditScore1 = findViewById(R.id.edit_score_1);
        mEditScore2 = findViewById(R.id.edit_score_2);
        mEditScore3 = findViewById(R.id.edit_score_3);

        mEditScore1.addTextChangedListener(new DecimalInputTextWatcher(4, 1, this));
        mEditScore2.addTextChangedListener(new DecimalInputTextWatcher(4, 1, this));
        mEditScore3.addTextChangedListener(new DecimalInputTextWatcher(4, 1, this));

        if (null != mEntity) {
            mEditScore1.setText(checkPoint(mEntity.getScore() + ""));
            mEditScore2.setText(checkPoint(mEntity.getScore2() + ""));
            mEditScore3.setText(checkPoint(mEntity.getScore3() + ""));

            if (mEntity.getScore() > 0) {
                mEditScore1.setTextColor(ContextCompat.getColor(this, R.color.numberBlack));
            } else {
                mEditScore1.setTextColor(ContextCompat.getColor(this, R.color.numberRed));
            }

            if (mEntity.getScore2() > 0) {
                mEditScore2.setTextColor(ContextCompat.getColor(this, R.color.numberBlack));
            } else {
                mEditScore2.setTextColor(ContextCompat.getColor(this, R.color.numberRed));
            }

            if (mEntity.getScore3() > 0) {
                mEditScore3.setTextColor(ContextCompat.getColor(this, R.color.numberBlack));
            } else {
                mEditScore3.setTextColor(ContextCompat.getColor(this, R.color.numberRed));
            }
        }

        initListener();
        initManager();
    }

    private void initManager() {
        manager = KeyboardViewManager
                .builder()
                .bindEditText(mEditScore1, mEditScore2, mEditScore3)
                .bindEditTextCallBack(mEditScore1, new KeyboardViewManager.onSureClickListener() {

                    @Override
                    public void onShowKeyboard() {

                    }

                    @Override
                    public void onSureClick() {
                    }
                })
                .bindEditTextCallBack(mEditScore2, new KeyboardViewManager.onSureClickListener() {
                    @Override
                    public void onShowKeyboard() {

                    }

                    @Override
                    public void onSureClick() {
                    }
                })
                .bindEditTextCallBack(mEditScore3, new KeyboardViewManager.onSureClickListener() {
                    @Override
                    public void onShowKeyboard() {
                    }

                    @Override
                    public void onSureClick() {
                    }
                })
                .build(this)
                .addKeyboardView(rootView);
        mEditScore1.requestFocus();
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
        findViewById(R.id.img_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String score1 = mEditScore1.getText().toString();
                String score2 = mEditScore2.getText().toString();
                String score3 = mEditScore3.getText().toString();
                if (check(score1, score2, score3)) {
                    Intent intent = new Intent();
                    intent.putExtra("score1", score1);
                    intent.putExtra("score2", score2);
                    intent.putExtra("score3", score3);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        findViewById(R.id.img_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    private void dialogPrompt(String content) {
        // SystemSoftKeyUtils.hideSoftInput(this, rootView);
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
}
