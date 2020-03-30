package com.shangame.hxjsq.view.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.shangame.hxjsq.R;
import com.shangame.hxjsq.adapter.MainNameAdapter;
import com.shangame.hxjsq.app.App;
import com.shangame.hxjsq.storage.db.ResultEntityDao;
import com.shangame.hxjsq.storage.db.ScoreRecordEntityDao;
import com.shangame.hxjsq.storage.model.ResultEntity;
import com.shangame.hxjsq.storage.model.ScoreRecordEntity;
import com.shangame.hxjsq.utils.RxSPTool;
import com.shangame.hxjsq.utils.StatusBarUtil;
import com.shangame.hxjsq.view.record.RecordActivity;
import com.shangame.hxjsq.view.record.ScoreRecordActivity;
import com.shangame.hxjsq.view.setting.SettingActivity;
import com.shangame.hxjsq.widget.CustomDialog;
import com.shangame.hxjsq.widget.DecimalInputTextWatcher;
import com.shangame.hxjsq.widget.keyboard.KeyboardViewManager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

import static com.shangame.hxjsq.common.PreferenceKeys.SHOW_NAME;

/**
 * 首页
 *
 * @author hhh
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String SP_KEY_SEARCH = "history_search";
    private static final String SP_NAME = "name_history";
    private static final String SP_SEPARATOR = ":-P";
    private static final int MAX_HISTORY_COUNT = 4;
    private static final String SP_EMPTY_TAG = "玩家1";
    private ResultEntityDao dao;
    private ScoreRecordEntityDao recordEntityDao;
    private KeyboardViewManager manager;
    private AutoCompleteTextView mEditName1;
    private AutoCompleteTextView mEditName2;
    private AutoCompleteTextView mEditName3;
    private TextView mTextNumber;
    private EditText mEditBureau1;
    private EditText mEditBureau2;
    private EditText mEditBureau3;
    private TextView mTextAccumulative1;
    private TextView mTextAccumulative2;
    private TextView mTextAccumulative3;
    private TextView mTextScore1;
    private TextView mTextScore2;
    private TextView mTextScore3;
    private EditText mEditMagnification;
    private FrameLayout rootView;
    private LinearLayout mLayoutName;
    private LinearLayout mLayoutAccumulative;
    private LinearLayout mLayoutBureau;
    private LinearLayout mLayoutScore;

    private ImageView mImgClear;
    private ImageView mImgQueryRecord;
    private ImageView mImgCancel;

    private ImageView mImgNextGame;
    private ImageView mImgEndGame;
    private ImageView mImgConfirm;

    private BigDecimal mBureau1 = new BigDecimal("0");
    private BigDecimal mBureau2 = new BigDecimal("0");
    private BigDecimal mBureau3 = new BigDecimal("0");

    private BigDecimal mAccumulative1 = new BigDecimal("0");
    private BigDecimal mAccumulative2 = new BigDecimal("0");
    private BigDecimal mAccumulative3 = new BigDecimal("0");

    private BigDecimal mScore1 = new BigDecimal("0");
    private BigDecimal mScore2 = new BigDecimal("0");
    private BigDecimal mScore3 = new BigDecimal("0");
    private CustomDialog mDialog;

    private MainNameAdapter mAdapter;

    private long mExitTime;

    private long mNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
        dao = App.getInstance().getSession().getResultEntityDao();
        recordEntityDao = App.getInstance().getSession().getScoreRecordEntityDao();

        recordEntityDao.deleteAll();

        initView();
        initListener();
    }

    private void initView() {
        rootView = findViewById(R.id.rootView);
        mEditMagnification = findViewById(R.id.edit_magnification);

        mTextNumber = findViewById(R.id.text_number);

        mLayoutName = findViewById(R.id.layout_name);
        mLayoutAccumulative = findViewById(R.id.layout_accumulative);
        mLayoutBureau = findViewById(R.id.layout_bureau);
        mLayoutScore = findViewById(R.id.layout_score);

        mEditName1 = findViewById(R.id.edit_name1);
        mEditName2 = findViewById(R.id.edit_name2);
        mEditName3 = findViewById(R.id.edit_name3);

        mEditBureau1 = findViewById(R.id.edit_bureau1);
        mEditBureau2 = findViewById(R.id.edit_bureau2);
        mEditBureau3 = findViewById(R.id.edit_bureau3);

        mImgClear = findViewById(R.id.img_clear);
        mImgQueryRecord = findViewById(R.id.img_query_record);
        mImgCancel = findViewById(R.id.img_cancel);

        mImgNextGame = findViewById(R.id.img_next_game);
        mImgEndGame = findViewById(R.id.img_end_game);
        mImgConfirm = findViewById(R.id.img_confirm);

        SpannableString ss = new SpannableString(getString(R.string.input_score));
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14, true);
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mEditBureau1.setHint(new SpannedString(ss));
        mEditBureau2.setHint(new SpannedString(ss));
        mEditBureau3.setHint(new SpannedString(ss));

        mTextAccumulative1 = findViewById(R.id.text_accumulative1);
        mTextAccumulative2 = findViewById(R.id.text_accumulative2);
        mTextAccumulative3 = findViewById(R.id.text_accumulative3);

        mTextScore1 = findViewById(R.id.text_score1);
        mTextScore2 = findViewById(R.id.text_score2);
        mTextScore3 = findViewById(R.id.text_score3);

        String[] searchHistoryArray = getHistoryArray(SP_KEY_SEARCH);
        List<String> arrList = new ArrayList<>(Arrays.asList(searchHistoryArray));
        mAdapter = new MainNameAdapter(this, arrList);
        mEditName1.setAdapter(mAdapter);
        mEditName2.setAdapter(mAdapter);
        mEditName3.setAdapter(mAdapter);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("第");
        stringBuilder.append("\n");
        stringBuilder.append(1);
        stringBuilder.append("\n");
        stringBuilder.append("局");
        mTextNumber.setText(stringBuilder);
    }

    private void initListener() {
        findViewById(R.id.img_record).setOnClickListener(this);
        findViewById(R.id.img_setting).setOnClickListener(this);

        findViewById(R.id.img_clear).setOnClickListener(this);
        findViewById(R.id.img_next_game).setOnClickListener(this);
        findViewById(R.id.img_query_record).setOnClickListener(this);
        findViewById(R.id.img_end_game).setOnClickListener(this);

        findViewById(R.id.img_cancel).setOnClickListener(this);
        findViewById(R.id.img_confirm).setOnClickListener(this);

        mEditMagnification.addTextChangedListener(new DecimalInputTextWatcher(4, 1, this, 1));
        mEditBureau1.addTextChangedListener(new DecimalInputTextWatcher(4, 1, this));
        mEditBureau2.addTextChangedListener(new DecimalInputTextWatcher(4, 1, this));
        mEditBureau3.addTextChangedListener(new DecimalInputTextWatcher(4, 1, this));

        mEditMagnification.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEditName1.setInputType(InputType.TYPE_CLASS_TEXT);
        mEditName2.setInputType(InputType.TYPE_CLASS_TEXT);
        mEditName3.setInputType(InputType.TYPE_CLASS_TEXT);
        initManager();
    }

    private String[] getHistoryArray(String key) {
        String[] array = getHistoryFromSharedPreferences(key).split(SP_SEPARATOR);
        // 最多只提示最近的5条历史记录
        String[] newArray;
        if (array.length > MAX_HISTORY_COUNT) {
            newArray = new String[MAX_HISTORY_COUNT];
            // 实现数组间的内容复制
            System.arraycopy(array, 0, newArray, 0, MAX_HISTORY_COUNT);
        } else {
            newArray = new String[array.length];
            System.arraycopy(array, 0, newArray, 0, array.length);
        }
        return newArray;
    }

    private void initManager() {
        manager = KeyboardViewManager
                .builder()
                .bindEditText(mEditBureau1, mEditBureau2, mEditBureau3)
                .showSystemKeyboard(mEditMagnification)
                .showSystemNameKeyboard(mEditName1, mEditName2, mEditName3)
                .bindEditTextCallBack(mEditBureau1, new KeyboardViewManager.onSureClickListener() {

                    @Override
                    public void onShowKeyboard() {
                        boolean isShowName = RxSPTool.getBoolean(MainActivity.this, SHOW_NAME, true);
                        if (isShowName) {
                            checkName();
                        }
                    }

                    @Override
                    public void onSureClick() {
                        mEditBureau1.clearFocus();
                        mEditBureau2.requestFocus();
                    }
                })
                .bindEditTextCallBack(mEditBureau2, new KeyboardViewManager.onSureClickListener() {
                    @Override
                    public void onShowKeyboard() {
                        boolean isShowName = RxSPTool.getBoolean(MainActivity.this, SHOW_NAME, true);
                        if (isShowName) {
                            checkName();
                        }
                    }

                    @Override
                    public void onSureClick() {
                        mEditBureau2.clearFocus();
                        mEditBureau3.requestFocus();
                    }
                })
                .bindEditTextCallBack(mEditBureau3, new KeyboardViewManager.onSureClickListener() {
                    @Override
                    public void onShowKeyboard() {
                        boolean isShowName = RxSPTool.getBoolean(MainActivity.this, SHOW_NAME, true);
                        if (isShowName) {
                            checkName();
                        }
                    }

                    @Override
                    public void onSureClick() {
                    }
                })
                .build(this)
                .addKeyboardView(rootView);
    }

    private String getHistoryFromSharedPreferences(String key) {
        SharedPreferences sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        return sp.getString(key, SP_EMPTY_TAG);
    }

    private void checkName() {
        String name1 = mEditName1.getText().toString();
        String name2 = mEditName2.getText().toString();
        String name3 = mEditName3.getText().toString();

        if (TextUtils.isEmpty(name1) || TextUtils.isEmpty(name2) || TextUtils.isEmpty(name3)) {
            dialogPromptInput(getString(R.string.prompt_user_name_empty));
            return;
        }
        if (name1.equals(name2) || name1.equals(name3) || name2.equals(name3)) {
            dialogPromptInput(getString(R.string.prompt_user_name_exist));
            return;
        }
        Timber.i("name1= " + name1 + " ,name2= " + name2 + " ,name3= " + name3);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (null != manager && manager.isShow()) {
                manager.hideSoftKeyboard(mEditMagnification, 0);
            } else {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Toast.makeText(this, R.string.app_exit_prompt, Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                    return true;
                } else {
                    finish();
                }
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isShowName = RxSPTool.getBoolean(this, SHOW_NAME, true);
        if (isShowName) {
            mLayoutName.setVisibility(View.VISIBLE);
        } else {
            mLayoutName.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_record: {
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.img_setting: {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.img_end_game: {
                endGame();
            }
            break;
            case R.id.img_clear: {
                clearView();
                recordEntityDao.deleteAll();
            }
            break;
            case R.id.img_next_game: {
                checkInput();
                saveHistory();
            }
            break;
            case R.id.img_query_record: {
                Intent intent = new Intent(MainActivity.this, ScoreRecordActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.img_cancel: {
                cancelOption();
            }
            break;
            case R.id.img_confirm: {
                insert();
                resetView();
                recordEntityDao.deleteAll();
            }
            break;
            default:
                break;
        }
    }

    private void endGame() {
        String value1 = mEditBureau1.getText().toString();
        String value2 = mEditBureau2.getText().toString();
        String value3 = mEditBureau3.getText().toString();

        BigDecimal accumulative1;
        BigDecimal accumulative2;
        BigDecimal accumulative3;
        if (!TextUtils.isEmpty(value1)) {
            BigDecimal bureau1 = new BigDecimal(value1);
            accumulative1 = mAccumulative1.add(bureau1);
        } else {
            accumulative1 = mAccumulative1;
        }
        if (!TextUtils.isEmpty(value2)) {
            BigDecimal bureau2 = new BigDecimal(value2);
            accumulative2 = mAccumulative2.add(bureau2);
        } else {
            accumulative2 = mAccumulative2;
        }
        if (!TextUtils.isEmpty(value3)) {
            BigDecimal bureau3 = new BigDecimal(value3);
            accumulative3 = mAccumulative3.add(bureau3);
        } else {
            accumulative3 = mAccumulative3;
        }

        BigDecimal bigDecimal = new BigDecimal(100);
        int result1 = accumulative1.compareTo(bigDecimal);
        int result2 = accumulative2.compareTo(bigDecimal);
        int result3 = accumulative3.compareTo(bigDecimal);

        if (result1 < 0 && result2 < 0 && result3 < 0) {
            dialogScore(getString(R.string.prompt_score_small));
        } else {
            calcScore();
            saveHistory();
        }
    }

    private void dialogScore(String content) {
        mDialog = new CustomDialog.Builder(this)
                .setMessage(content)
                .setNegativeButton(R.drawable.img_confirm_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                        calcScore();
                        saveHistory();
                    }
                })
                .setPositiveButton(R.drawable.img_clear_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                    }
                }).create();
        mDialog.show();
    }

    private void calcScore() {
        if (check()) {
            String value1 = mEditBureau1.getText().toString();
            String value2 = mEditBureau2.getText().toString();
            String value3 = mEditBureau3.getText().toString();

            String magnification = mEditMagnification.getText().toString();
            if (TextUtils.isEmpty(magnification)) {
                dialogPromptInput(getString(R.string.input_game_magnification));
                return;
            }
            BigDecimal magnifi = new BigDecimal(magnification);

            if (!TextUtils.isEmpty(value1)) {
                mBureau1 = new BigDecimal(value1);
                mAccumulative1 = mAccumulative1.add(mBureau1);
            }
            if (!TextUtils.isEmpty(value2)) {
                mBureau2 = new BigDecimal(value2);
                mAccumulative2 = mAccumulative2.add(mBureau2);
            }
            if (!TextUtils.isEmpty(value3)) {
                mBureau3 = new BigDecimal(value3);
                mAccumulative3 = mAccumulative3.add(mBureau3);
            }

            mLayoutAccumulative.setVisibility(View.VISIBLE);

            mTextAccumulative1.setText(checkPoint(mAccumulative1 + ""));
            mTextAccumulative2.setText(checkPoint(mAccumulative2 + ""));
            mTextAccumulative3.setText(checkPoint(mAccumulative3 + ""));

            setAccumulativeColor();

            mScore1 = mAccumulative1.multiply(new BigDecimal(2)).subtract(mAccumulative2.add(mAccumulative3)).multiply(magnifi);
            mScore2 = mAccumulative2.multiply(new BigDecimal(2)).subtract(mAccumulative1.add(mAccumulative3)).multiply(magnifi);
            mScore3 = mAccumulative3.multiply(new BigDecimal(2)).subtract(mAccumulative1.add(mAccumulative2)).multiply(magnifi);

            mLayoutBureau.setVisibility(View.GONE);
            mLayoutScore.setVisibility(View.VISIBLE);

            mTextScore1.setText(checkPoint(mScore1 + ""));
            mTextScore2.setText(checkPoint(mScore2 + ""));
            mTextScore3.setText(checkPoint(mScore3 + ""));

            manager.hideSoftKeyboard(mEditMagnification, 1);

            setScoreColor();

            mImgCancel.setVisibility(View.VISIBLE);
            mImgConfirm.setVisibility(View.VISIBLE);

            mImgClear.setVisibility(View.GONE);
            mImgNextGame.setVisibility(View.GONE);
            mImgEndGame.setVisibility(View.GONE);
            mImgQueryRecord.setVisibility(View.GONE);
        }
    }

    private void saveHistory() {
        saveHistory(mEditName1, SP_KEY_SEARCH);
        saveHistory(mEditName2, SP_KEY_SEARCH);
        saveHistory(mEditName3, SP_KEY_SEARCH);
    }

    private void clearView() {
        mLayoutBureau.setVisibility(View.VISIBLE);
        mLayoutScore.setVisibility(View.GONE);
        mLayoutAccumulative.setVisibility(View.GONE);

        mImgEndGame.setVisibility(View.GONE);
        mImgQueryRecord.setVisibility(View.GONE);

        mEditBureau1.setText("");
        mEditBureau2.setText("");
        mEditBureau3.setText("");

        mEditBureau1.clearFocus();
        mEditBureau2.clearFocus();
        mEditBureau3.clearFocus();

        mAccumulative1 = new BigDecimal("0");
        mAccumulative2 = new BigDecimal("0");
        mAccumulative3 = new BigDecimal("0");
        mTextAccumulative1.setText("");
        mTextAccumulative2.setText("");
        mTextAccumulative3.setText("");

        mScore1 = new BigDecimal("0");
        mScore2 = new BigDecimal("0");
        mScore3 = new BigDecimal("0");
        mTextScore1.setText("");
        mTextScore2.setText("");
        mTextScore3.setText("");

        mNumber = 1;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("第");
        stringBuilder.append("\n");
        stringBuilder.append(1);
        stringBuilder.append("\n");
        stringBuilder.append("局");
        mTextNumber.setText(stringBuilder);

        mEditName1.setText("");
        mEditName2.setText("");
        mEditName3.setText("");

        mEditName1.requestFocus();
        manager.hideSoftKeyboard(mEditMagnification, 1);
    }

    private void checkInput() {
        String name1 = mEditName1.getText().toString();
        String name2 = mEditName2.getText().toString();
        String name3 = mEditName3.getText().toString();

        boolean isShowName = RxSPTool.getBoolean(this, SHOW_NAME, true);
        if (isShowName && (TextUtils.isEmpty(name1) || TextUtils.isEmpty(name2) || TextUtils.isEmpty(name3))) {
            dialogPromptInput(getString(R.string.prompt_user_name_empty));
            return;
        }

        if (isShowName && (name1.equals(name2) || name1.equals(name3) || name2.equals(name3))) {
            dialogPromptInput(getString(R.string.prompt_user_name_exist));
            return;
        }

        String value1 = mEditBureau1.getText().toString();
        String value2 = mEditBureau2.getText().toString();
        String value3 = mEditBureau3.getText().toString();
        if (TextUtils.isEmpty(value1) || TextUtils.isEmpty(value2) || TextUtils.isEmpty(value3)) {
            dialogPromptInput(getString(R.string.prompt_score_error));
            return;
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
                return;
            }

            if (dValue1 < 0 && dValue2 < 0 && dValue3 < 0) {
                dialogPrompt(getString(R.string.error_prompt_2));
                return;
            }
            saveRecord(value1, value2, value3);
            saveScoreRecord(name1, name2, name3, dValue1, dValue2, dValue3);
            if (mNumber < 99) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("第");
                stringBuilder.append("\n");
                stringBuilder.append(++mNumber);
                stringBuilder.append("\n");
                stringBuilder.append("局");
                mTextNumber.setText(stringBuilder);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("第");
                stringBuilder.append("\n");
                stringBuilder.append("99+");
                stringBuilder.append("\n");
                stringBuilder.append("局");
                mTextNumber.setText(stringBuilder);
            }

            mImgQueryRecord.setVisibility(View.VISIBLE);
            mImgEndGame.setVisibility(View.VISIBLE);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            dialogPrompt(getString(R.string.error_score));
        }

    }

    private void cancelOption() {
        mLayoutBureau.setVisibility(View.VISIBLE);
        mLayoutScore.setVisibility(View.GONE);

        mEditBureau2.clearFocus();
        mEditBureau3.clearFocus();
        mEditBureau1.requestFocus();

        mImgClear.setVisibility(View.VISIBLE);
        mImgEndGame.setVisibility(View.VISIBLE);
        mImgQueryRecord.setVisibility(View.VISIBLE);
        mImgNextGame.setVisibility(View.VISIBLE);

        mImgCancel.setVisibility(View.GONE);
        mImgConfirm.setVisibility(View.GONE);

        mAccumulative1 = mAccumulative1.subtract(mBureau1);
        mAccumulative2 = mAccumulative2.subtract(mBureau2);
        mAccumulative3 = mAccumulative3.subtract(mBureau3);

        int accumulative1 = mAccumulative1.compareTo(BigDecimal.ZERO);
        int accumulative2 = mAccumulative2.compareTo(BigDecimal.ZERO);
        int accumulative3 = mAccumulative3.compareTo(BigDecimal.ZERO);

        if (accumulative1 == 0 && accumulative2 == 0 && accumulative3 == 0) {
            mLayoutAccumulative.setVisibility(View.GONE);
        } else {
            mLayoutAccumulative.setVisibility(View.VISIBLE);
        }

        mTextAccumulative1.setText(checkPoint(mAccumulative1 + ""));
        mTextAccumulative2.setText(checkPoint(mAccumulative2 + ""));
        mTextAccumulative3.setText(checkPoint(mAccumulative3 + ""));

        if (accumulative1 > 0) {
            mTextAccumulative1.setTextColor(ContextCompat.getColor(this, R.color.numberBlack));
        } else {
            mTextAccumulative1.setTextColor(ContextCompat.getColor(this, R.color.numberRed));
        }

        if (accumulative2 > 0) {
            mTextAccumulative2.setTextColor(ContextCompat.getColor(this, R.color.numberBlack));
        } else {
            mTextAccumulative2.setTextColor(ContextCompat.getColor(this, R.color.numberRed));
        }

        if (accumulative3 > 0) {
            mTextAccumulative3.setTextColor(ContextCompat.getColor(this, R.color.numberBlack));
        } else {
            mTextAccumulative3.setTextColor(ContextCompat.getColor(this, R.color.numberRed));
        }
    }

    private void insert() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        String strDate = simpleDateFormat.format(date);
        List<ResultEntity> list = dao.queryBuilder().where(ResultEntityDao.Properties.Date.eq(strDate)).orderDesc(ResultEntityDao.Properties.Number).list();
        ResultEntity entity = new ResultEntity();

        String name1 = mEditName1.getText().toString();
        String name2 = mEditName2.getText().toString();
        String name3 = mEditName3.getText().toString();

        boolean isShowName = RxSPTool.getBoolean(this, SHOW_NAME, true);
        if (isShowName && (TextUtils.isEmpty(name1) || TextUtils.isEmpty(name2) || TextUtils.isEmpty(name3))) {
            dialogPromptInput(getString(R.string.prompt_user_name_empty));
            return;
        }

        if (isShowName && (name1.equals(name2) || name1.equals(name3) || name2.equals(name3))) {
            dialogPromptInput(getString(R.string.prompt_user_name_exist));
            return;
        }

        if (isShowName) {
            entity.setName(name1);
            entity.setName2(name2);
            entity.setName3(name3);
        }

        entity.setDuplicate(1);
        entity.setAccumulative(mAccumulative1.doubleValue());
        entity.setScore(mScore1.doubleValue());

        entity.setDuplicate2(1);
        entity.setAccumulative2(mAccumulative2.doubleValue());
        entity.setScore2(mScore2.doubleValue());

        entity.setDuplicate3(1);
        entity.setAccumulative3(mAccumulative3.doubleValue());
        entity.setScore3(mScore3.doubleValue());
        long datetime = System.currentTimeMillis();
        entity.setDatetime(datetime);

        long count = 0;
        if (null != list && !list.isEmpty()) {
            count = list.get(0).getNumber();
        }
        entity.setNumber(++count);

        entity.setDate(simpleDateFormat.format(date));
        dao.insert(entity);
        Toast.makeText(this, getString(R.string.prompt_save_success), Toast.LENGTH_SHORT).show();
    }

    private void resetView() {
        mLayoutBureau.setVisibility(View.VISIBLE);
        mLayoutScore.setVisibility(View.GONE);
        mLayoutAccumulative.setVisibility(View.GONE);

        mImgCancel.setVisibility(View.GONE);
        mImgConfirm.setVisibility(View.GONE);

        mImgEndGame.setVisibility(View.GONE);
        mImgQueryRecord.setVisibility(View.GONE);

        mImgClear.setVisibility(View.VISIBLE);
        mImgNextGame.setVisibility(View.VISIBLE);

        mEditBureau1.setText("");
        mEditBureau2.setText("");
        mEditBureau3.setText("");

        mEditBureau2.clearFocus();
        mEditBureau3.clearFocus();
        mEditBureau1.requestFocus();

        mAccumulative1 = new BigDecimal("0");
        mAccumulative2 = new BigDecimal("0");
        mAccumulative3 = new BigDecimal("0");
        mTextAccumulative1.setText("");
        mTextAccumulative2.setText("");
        mTextAccumulative3.setText("");

        mScore1 = new BigDecimal("0");
        mScore2 = new BigDecimal("0");
        mScore3 = new BigDecimal("0");
        mTextScore1.setText("");
        mTextScore2.setText("");
        mTextScore3.setText("");

        mNumber = 1;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("第");
        stringBuilder.append("\n");
        stringBuilder.append(1);
        stringBuilder.append("\n");
        stringBuilder.append("局");
        mTextNumber.setText(stringBuilder);
    }

    private boolean check() {
        String name1 = mEditName1.getText().toString();
        String name2 = mEditName2.getText().toString();
        String name3 = mEditName3.getText().toString();

        if (TextUtils.isEmpty(name1) || TextUtils.isEmpty(name2) || TextUtils.isEmpty(name3)) {
            dialogPromptInput(getString(R.string.prompt_user_name_empty));
            return false;
        }

        if (name1.equals(name2) || name1.equals(name3) || name2.equals(name3)) {
            dialogPromptInput(getString(R.string.prompt_user_name_exist));
            return false;
        }

        String value1 = mEditBureau1.getText().toString();
        String value2 = mEditBureau2.getText().toString();
        String value3 = mEditBureau3.getText().toString();
        boolean isZero = (mAccumulative1.compareTo(BigDecimal.ZERO) == 0 && mAccumulative2.compareTo(BigDecimal.ZERO) == 0 && mAccumulative3.compareTo(BigDecimal.ZERO) == 0);
        boolean isEmpty = (TextUtils.isEmpty(value1) || TextUtils.isEmpty(value2) || TextUtils.isEmpty(value3));
        if (isZero && isEmpty) {
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

    private void setAccumulativeColor() {
        int accumulative1 = mAccumulative1.compareTo(BigDecimal.ZERO);
        if (accumulative1 > 0) {
            mTextAccumulative1.setTextColor(ContextCompat.getColor(this, R.color.numberBlack));
        } else {
            mTextAccumulative1.setTextColor(ContextCompat.getColor(this, R.color.numberRed));
        }

        int accumulative2 = mAccumulative2.compareTo(BigDecimal.ZERO);
        if (accumulative2 > 0) {
            mTextAccumulative2.setTextColor(ContextCompat.getColor(this, R.color.numberBlack));
        } else {
            mTextAccumulative2.setTextColor(ContextCompat.getColor(this, R.color.numberRed));
        }

        int accumulative3 = mAccumulative3.compareTo(BigDecimal.ZERO);
        if (accumulative3 > 0) {
            mTextAccumulative3.setTextColor(ContextCompat.getColor(this, R.color.numberBlack));
        } else {
            mTextAccumulative3.setTextColor(ContextCompat.getColor(this, R.color.numberRed));
        }
    }

    private void setScoreColor() {
        int score1 = mScore1.compareTo(BigDecimal.ZERO);
        if (score1 > 0) {
            mTextScore1.setTextColor(ContextCompat.getColor(this, R.color.numberBlack));
        } else {
            mTextScore1.setTextColor(ContextCompat.getColor(this, R.color.numberRed));
        }

        int score2 = mScore2.compareTo(BigDecimal.ZERO);
        if (score2 > 0) {
            mTextScore2.setTextColor(ContextCompat.getColor(this, R.color.numberBlack));
        } else {
            mTextScore2.setTextColor(ContextCompat.getColor(this, R.color.numberRed));
        }

        int score3 = mScore3.compareTo(BigDecimal.ZERO);
        if (score3 > 0) {
            mTextScore3.setTextColor(ContextCompat.getColor(this, R.color.numberBlack));
        } else {
            mTextScore3.setTextColor(ContextCompat.getColor(this, R.color.numberRed));
        }
    }

    private void saveHistory(AutoCompleteTextView view, String key) {
        String text = view.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            return;
        }

        // 获取SP中保存的历史记录
        String oldText = getHistoryFromSharedPreferences(key);
        StringBuilder sb;
        if (SP_EMPTY_TAG.equals(oldText)) {
            sb = new StringBuilder();
        } else {
            sb = new StringBuilder(oldText);
        }
        // 使用逗号来分隔每条历史记录
        sb.append(text).append(SP_SEPARATOR);

        // 判断搜索内容是否已存在于历史文件中，已存在则不再添加
        if (!oldText.contains(text + SP_SEPARATOR)) {
            // 实时保存历史记录
            saveHistoryToSharedPreferences(key, sb.toString());
            mAdapter.add(text);
            Timber.e("saved: ");
        } else {
            Timber.e("existed: ");
        }
    }

    private void dialogPrompt(String content) {
        mDialog = new CustomDialog.Builder(this)
                .setMessage(content)
                .setNegativeButton(R.drawable.img_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                        mEditBureau1.setText("");
                        mEditBureau2.setText("");
                        mEditBureau3.setText("");

                        mEditBureau3.clearFocus();
                        mEditBureau1.requestFocus();
                    }
                }).create();
        mDialog.show();
    }

    private void saveRecord(String value1, String value2, String value3) {
        mLayoutAccumulative.setVisibility(View.VISIBLE);

        mAccumulative1 = mAccumulative1.add(new BigDecimal(value1));
        mAccumulative2 = mAccumulative2.add(new BigDecimal(value2));
        mAccumulative3 = mAccumulative3.add(new BigDecimal(value3));

        mTextAccumulative1.setText(checkPoint(mAccumulative1 + ""));
        mTextAccumulative2.setText(checkPoint(mAccumulative2 + ""));
        mTextAccumulative3.setText(checkPoint(mAccumulative3 + ""));

        int accumulative1 = mAccumulative1.compareTo(BigDecimal.ZERO);
        if (accumulative1 > 0) {
            mTextAccumulative1.setTextColor(ContextCompat.getColor(this, R.color.numberBlack));
        } else {
            mTextAccumulative1.setTextColor(ContextCompat.getColor(this, R.color.numberRed));
        }

        int accumulative2 = mAccumulative2.compareTo(BigDecimal.ZERO);
        if (accumulative2 > 0) {
            mTextAccumulative2.setTextColor(ContextCompat.getColor(this, R.color.numberBlack));
        } else {
            mTextAccumulative2.setTextColor(ContextCompat.getColor(this, R.color.numberRed));
        }

        int accumulative3 = mAccumulative3.compareTo(BigDecimal.ZERO);
        if (accumulative3 > 0) {
            mTextAccumulative3.setTextColor(ContextCompat.getColor(this, R.color.numberBlack));
        } else {
            mTextAccumulative3.setTextColor(ContextCompat.getColor(this, R.color.numberRed));
        }

        mEditBureau1.setText("");
        mEditBureau2.setText("");
        mEditBureau3.setText("");

        mEditBureau3.clearFocus();
        mEditBureau1.requestFocus();
    }

    private void saveScoreRecord(String name1, String name2, String name3, double value1, double value2, double value3) {
        ScoreRecordEntity entity = new ScoreRecordEntity();
        boolean isShowName = RxSPTool.getBoolean(this, SHOW_NAME, true);

        if (isShowName) {
            entity.setName(name1);
            entity.setName2(name2);
            entity.setName3(name3);
        }

        entity.setScore(value1);
        entity.setScore2(value2);
        entity.setScore3(value3);

        entity.setNumber(mNumber);
        recordEntityDao.insertOrReplace(entity);
    }

    private void saveHistoryToSharedPreferences(String key, String history) {
        SharedPreferences sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, history);
        editor.apply();
    }
}
