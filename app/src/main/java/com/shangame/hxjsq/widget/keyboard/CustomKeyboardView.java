package com.shangame.hxjsq.widget.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import com.shangame.hxjsq.R;

import java.util.List;

import androidx.annotation.DrawableRes;

/**
 * 从写这个类，是为了改变部分按钮的背景颜色
 */
public class CustomKeyboardView extends KeyboardView {

    private Context context;
    private Paint mPaint;

    public CustomKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(46);
        mPaint.setColor(Color.WHITE);
        this.context = context;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        List<Keyboard.Key> keys = getKeyboard().getKeys();

        for (Keyboard.Key key : keys) {
            drawSpecialKey(canvas, key);
        }
    }

    private void drawSpecialKey(Canvas canvas, Keyboard.Key key) {
        if (key.codes[0] == -3) { // 隐藏
            drawBackground(R.drawable.bg_gray_key_selector, canvas, key);
            drawIcon(canvas, key);
        } else if (key.codes[0] == -5) { // 删除
            drawBackground(R.drawable.bg_gray_key_selector, canvas, key);
            drawIcon(canvas, key);
        }
    }

    private void drawBackground(@DrawableRes int drawableId, Canvas canvas, Keyboard.Key key) {
        Drawable drawable = context.getResources().getDrawable(drawableId);
        int[] state = key.getCurrentDrawableState();
        if (key.codes[0] != 0) {
            drawable.setState(state);
        }
        drawable.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
        drawable.draw(canvas);
    }

    private void drawIcon(Canvas canvas, Keyboard.Key key) {
        //绘制设置了icon的按钮
        key.icon.setBounds(key.x + (key.width - key.icon.getIntrinsicWidth()) / 2,
                key.y + (key.height - key.icon.getIntrinsicHeight()) / 2,
                key.x + (key.width - key.icon.getIntrinsicWidth()) / 2 + key.icon.getIntrinsicWidth(),
                key.y + (key.height - key.icon.getIntrinsicHeight()) / 2 + key.icon.getIntrinsicHeight());
        key.icon.draw(canvas);
    }
}
