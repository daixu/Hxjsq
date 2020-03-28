package com.shangame.hxjsq.view.setting;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.shangame.hxjsq.R;
import com.shangame.hxjsq.utils.RxSPTool;
import com.shangame.hxjsq.utils.StatusBarUtil;
import com.suke.widget.SwitchButton;

import androidx.appcompat.app.AppCompatActivity;
import timber.log.Timber;

import static com.shangame.hxjsq.common.PreferenceKeys.SHOW_NAME;

/**
 * 设置
 * @author hhh
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener, SwitchButton.OnCheckedChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        StatusBarUtil.setTranslucentForImageView(this, 0, null);

        initView();
        initListener();
    }

    private void initListener() {
        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.img_copy).setOnClickListener(this);
        findViewById(R.id.img_copy_qq).setOnClickListener(this);
    }

    private void initView() {
        SwitchButton switchButton = findViewById(R.id.switch_show_name);

        boolean isShowName = RxSPTool.getBoolean(this, SHOW_NAME, true);
        switchButton.setChecked(isShowName);

        switchButton.setOnCheckedChangeListener(this);
    }

    private void copy(String copyStr) {
        //获取剪贴板管理器
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", copyStr);
        // 将ClipData内容放到系统剪贴板里。
        if (null != cm) {
            cm.setPrimaryClip(mClipData);
        }
        Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_copy:
                copy("sshd123456");
                break;
            case R.id.img_copy_qq:
                copy("281584439");
                break;
            case R.id.img_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
        Timber.e("isChecked= " + isChecked);
        RxSPTool.putBoolean(SettingActivity.this, SHOW_NAME, isChecked);
    }
}
