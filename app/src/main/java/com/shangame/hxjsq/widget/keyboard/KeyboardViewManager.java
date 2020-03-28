package com.shangame.hxjsq.widget.keyboard;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.shangame.hxjsq.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

public class KeyboardViewManager implements KeyboardView.OnKeyboardActionListener {
    //数字键盘标记
    public static Integer NUMBER_XML = R.xml.keyboard_numbers;
    private static List<EditText> showSystem;
    private static List<EditText> showSystemName;
    private static List<EditText> showCustomKeyboard;
    private static Map<EditText, onSureClickListener> editList;
    private static Context mContext;
    //键盘的根布局
    private final FrameLayout frameLayout;
    private final Keyboard keyboardNumber;
    private final KeyboardView mKeyboardView;
    private EditText currentEditText;
    private EditText focusReplace;
    private FrameLayout rootView;
    private boolean hiding = true;

    private KeyboardViewManager() {
        //创建打气筒
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        //把键盘布局解析成对象
        frameLayout = (FrameLayout) layoutInflater.inflate(R.layout.layout_content_keyboard, null);
        mKeyboardView = frameLayout.findViewById(R.id.keyboardView);
        //创建keyboard
        keyboardNumber = new Keyboard(mContext, NUMBER_XML);
        //把创建的键盘布局设置给控件
        mKeyboardView.setKeyboard(keyboardNumber);
        //给键盘设置监听
        mKeyboardView.setOnKeyboardActionListener(this);
        for (final EditText key : editList.keySet()) {
            key.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                        if (showSystem.contains(view)) {
                            hideSoftKeyboard(key, 0);
                            currentEditText = (EditText) view;
                        } else if (showSystemName.contains(view)) {
                            hideSoftKeyboard(key, 0);
                            currentEditText = (EditText) view;
                            currentEditText.setFocusableInTouchMode(true);
                            currentEditText.selectAll();
                        } else {
                            currentEditText = (EditText) view;
                            currentEditText.setCursorVisible(true);
                            SystemSoftKeyUtils.hideSoftInput(mContext, view);
                            showSoftKeyboard();
                            currentEditText.setHint("");
                        }
                    } else {
                        if (showCustomKeyboard.contains(view)) {
                            SpannableString ss = new SpannableString("请输入胡息");
                            AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14, true);
                            ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            EditText editText = (EditText) view;
                            editText.setHint(new SpannedString(ss));
                        }
                    }
                }
            });
        }
    }

    /**
     * 隐藏键盘
     */
    public void hideSoftKeyboard(final EditText editText, final int type) {
        if (frameLayout.getVisibility() == View.VISIBLE && hiding) {
            hiding = false;
            Object tag = rootView.getTag();
            if (tag != null) {
                //遍历所有的子View，让其向上移动改移动的高度
                for (int i = 0; i < rootView.getChildCount(); i++) {
                    if (rootView.getChildAt(i) != frameLayout) {
                        ObjectAnimator.ofFloat(rootView.getChildAt(i), "translationY", 0).setDuration(200).start();
                    }
                }
            }
            //设置隐藏动画
            Animation hide = AnimationUtils.loadAnimation(mContext, R.anim.up_to_hide);
            frameLayout.startAnimation(hide);
            hide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // currentEditText.setCursorVisible(false);
                    currentEditText.clearFocus();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    frameLayout.setVisibility(View.GONE);
                    hiding = true;
                    if (type == 0) {
                        editText.requestFocus();
                        focusReplace.setInputType(editText.getInputType());
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    /**
     * 显示键盘
     */
    public void showSoftKeyboard() {
        //根据设置的输入类型，动态切换键盘
        mKeyboardView.setKeyboard(keyboardNumber);

        //如果遮挡住，计算需要上移的距离，进行上移
        // isCover();
        //键盘显示和隐藏
        if (frameLayout.getVisibility() == View.GONE) {
            Animation show = AnimationUtils.loadAnimation(mContext, R.anim.down_to_up);
            frameLayout.startAnimation(show);
            show.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    frameLayout.setVisibility(View.VISIBLE);
                    if (null != editList && !editList.isEmpty()) {
                        if (null != editList.get(currentEditText)) {
                            editList.get(currentEditText).onShowKeyboard();
                        }
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    //===========================Builder模式==============================================
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 软键盘展示状态
     */
    public boolean isShow() {
        if (null == frameLayout) {
            return false;
        }
        return frameLayout.getVisibility() == View.VISIBLE;
    }

    /**
     * 添加键盘到布局中去，这里应该去调用隐藏系统键盘
     *
     * @param rootView
     * @return
     */
    public KeyboardViewManager addKeyboardView(FrameLayout rootView) {
        this.rootView = rootView;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM);
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(0, 0, Gravity.BOTTOM);
        rootView.addView(frameLayout, params);
        focusReplace = new EditText(mContext);
        rootView.addView(focusReplace, params2);
        return this;
    }

    //================================键盘监听事件回调==============================================
    @Override
    public void onPress(int i) {
        Timber.e("onPress");
    }

    @Override
    public void onRelease(int i) {
        Timber.e("onRelease");
    }

    @Override
    public void onKey(int primaryCode, int[] ints) {
        //获取文本内容
        Editable editable = currentEditText.getText();
        //  获取光标位置
        int start = currentEditText.getSelectionStart();
        switch (primaryCode) {
            // 加号
            case -2: {
                String text = currentEditText.getText().toString();
                if (text.contains("-")) {
                    editable.delete(0, 1);
                }
                Timber.e("text= " + text);
            }
            break;
            // 减号
            case 45: {
                String text = currentEditText.getText().toString();
                if (!text.contains("-")) {
                    editable.insert(0, Character.toString((char) primaryCode));
                }
                Timber.e("text= " + text);
            }
            break;
            // 下一栏
            case -4: {
                if (editList.get(currentEditText) != null) {
                    Objects.requireNonNull(editList.get(currentEditText)).onSureClick();
                }
            }
            break;
            // 删除
            case -5: {
                if (!TextUtils.isEmpty(editable)) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            }
            break;
            // 隐藏
            case -3: {
                hideSoftKeyboard(currentEditText, 1);
            }
            break;
            default://普通的按键就直接去把字符串设置到EditText上即可
                //在光标处插入字符
                editable.insert(start, Character.toString((char) primaryCode));
                break;
        }
    }

    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    /**
     * 如果输入框呗遮挡就向下移动
     *
     * @return
     */
    public void isCover() {
        //获取传递过来的跟布局的宽高
        Rect rect = new Rect();
        frameLayout.getWindowVisibleDisplayFrame(rect);
        //计算当前获取焦点的输入框在屏幕中的位置
        int[] vLocation = new int[2];
        currentEditText.getLocationOnScreen(vLocation);
        int keyboardTop = vLocation[1] + currentEditText.getHeight() / 2 + currentEditText.getPaddingBottom() + currentEditText.getPaddingTop();
        //输入框或基线View的到屏幕的距离 + 键盘高度 如果 超出了屏幕的承载范围, 就需要移动.
        int moveHeight = rect.bottom - keyboardTop - mKeyboardView.getHeight();
        moveHeight = moveHeight > 0 ? 0 : moveHeight;
        if (moveHeight != 0) {
            rootView.setTag("move");
            //遍历所有的子View，让其向上移动改移动的高度
            for (int i = 0; i < rootView.getChildCount(); i++) {
                if (rootView.getChildAt(i) != frameLayout) {
                    rootView.getChildAt(i).setTranslationY(moveHeight);
                    ObjectAnimator.ofFloat(rootView.getChildAt(i), "translationY", 0, moveHeight).setDuration(200).start();
                }
            }
        }
    }

    //===================================点击确定回调==============================================
    public interface onSureClickListener {
        void onShowKeyboard();

        void onSureClick();
    }

    public static final class Builder {

        private Builder() {
            editList = new HashMap<>();
            showSystem = new ArrayList<>();
            showCustomKeyboard = new ArrayList<>();
            showSystemName = new ArrayList<>();
        }

        //如果页面有EditText，解决键盘冲突，这个方法必须写
        public Builder bindEditText(EditText... editText) {
            showCustomKeyboard = Arrays.asList(editText);
            for (int i = 0; i < editText.length; i++) {
                //隐藏系统软键盘冲突，需要配合清单文件一起使用: android:windowSoftInputMode="stateHidden|stateUnchanged"
                SystemSoftKeyUtils.hideSystemSoftKeyboard(editText[i]);
                editList.put(editText[i], null);
            }
            return this;
        }

        public Builder bindEditTextCallBack(EditText editText, onSureClickListener onSureListener) {
            editList.put(editText, onSureListener);
            return this;
        }

        public Builder showSystemNameKeyboard(EditText... editTexts) {
            showSystemName = Arrays.asList(editTexts);
            for (int i = 0; i < editTexts.length; i++) {
                editList.put(editTexts[i], null);
            }
            return this;
        }

        public Builder showSystemKeyboard(EditText... editTexts) {
            showSystem = Arrays.asList(editTexts);
            for (int i = 0; i < editTexts.length; i++) {
                editList.put(editTexts[i], null);
            }
            return this;
        }

        public KeyboardViewManager build(Context context) {
            mContext = context;
            return new KeyboardViewManager();
        }
    }
}
