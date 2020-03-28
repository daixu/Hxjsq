package com.shangame.hxjsq.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.shangame.hxjsq.R;

public class CustomToolbar extends Toolbar {
    /**
     * 左侧Title
     */
    private TextView mTextLeftTitle;
    /**
     * 中间Title
     */
    private TextView mTextMiddleTitle;
    /**
     * 右侧Title
     */
    private TextView mTextRightTitle;

    public CustomToolbar(Context context) {
        this(context, null);
    }

    public CustomToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_custom_toolbar, this);
        mTextLeftTitle = findViewById(R.id.text_left_title);
        mTextMiddleTitle = findViewById(R.id.text_main_title);
        mTextRightTitle = findViewById(R.id.text_right_title);
    }

    //设置中间title的内容
    public void setCustomTitle(String text) {
        this.setTitle(" ");
        mTextMiddleTitle.setVisibility(View.VISIBLE);
        mTextMiddleTitle.setText(text);
    }

    //设置中间title的内容文字的颜色
    public void setCustomTitleColor(int color) {
        mTextMiddleTitle.setTextColor(color);
    }

    //设置title左边文字
    public void setLeftTitleText(String text) {
        mTextLeftTitle.setVisibility(View.VISIBLE);
        mTextLeftTitle.setText(text);
    }

    //设置title左边文字颜色
    public void setLeftTitleColor(int color) {
        mTextLeftTitle.setTextColor(color);
    }

    //设置title左边图标
    public void setLeftTitleDrawable(int res) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), res);
        if (null != drawable) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTextLeftTitle.setCompoundDrawables(drawable, null, null, null);
        }
    }

    //设置title左边点击事件
    public void setLeftTitleClickListener(OnClickListener onClickListener) {
        mTextLeftTitle.setOnClickListener(onClickListener);
    }

    public void setLeftTitleGone() {
        mTextLeftTitle.setVisibility(View.GONE);
    }

    //设置title右边文字
    public void setRightTitleText(String text) {
        mTextRightTitle.setVisibility(View.VISIBLE);
        mTextRightTitle.setText(text);
    }

    //设置title右边文字颜色
    public void setRightTitleColor(int color) {
        mTextRightTitle.setTextColor(color);
    }

    //设置title右边图标
    public void setRightTitleDrawable(int res) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), res);
        if (null != drawable) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTextRightTitle.setCompoundDrawables(null, null, drawable, null);
        }
    }

    //设置title右边点击事件
    public void setRightTitleClickListener(OnClickListener onClickListener) {
        mTextRightTitle.setOnClickListener(onClickListener);
    }

    public void setRightTitleGone() {
        mTextRightTitle.setVisibility(View.GONE);
    }
}
