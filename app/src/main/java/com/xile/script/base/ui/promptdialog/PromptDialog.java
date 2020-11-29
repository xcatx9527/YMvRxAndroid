package com.xile.script.base.ui.promptdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yzy.example.R;


/**
 * Created by zhaoxiaofei on 2016/6/22.
 */
public class PromptDialog extends Dialog {
    private EditText dialog_edit_view;
    private TextView dialog_one_button, dialog_two_button;

    private TextView title;
    private LinearLayout linear_all;

    public PromptDialog(Context context) {
        super(context, R.style.dialog_bocop);
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_text_two_button, null);
        dialog_edit_view = mView.findViewById(R.id.dialog_edit_view);
        dialog_one_button = mView.findViewById(R.id.dialog_one_button);
        dialog_two_button = mView.findViewById(R.id.dialog_two_button);
        title = mView.findViewById(R.id.title);
        linear_all = mView.findViewById(R.id.linear_all);
        super.setContentView(mView);
    }

    public void setBackGround(Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(bitmap);
        linear_all.setBackground(drawable);
    }

    public View getEditText() {
        return dialog_edit_view;
    }

    public void setTitleText(Spanned titleStr) {
        title.setText(titleStr);
    }

    @Override
    public void setContentView(int layoutResID) {
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
    }

    @Override
    public void setContentView(View view) {
    }

    /**
     * 隐藏取消按钮
     *
     * @param visibility
     */
    public void setNegativeButtonVisibility(int visibility) {
        dialog_two_button.setVisibility(visibility);
    }

    /**
     * 设置第一个按钮背景
     *
     * @param color
     */
    public void setPositiveBtnBackBround(int color) {
        dialog_one_button.setBackgroundColor(color);
    }

    /**
     * 设置第二个按钮背景
     *
     * @param color
     */
    public void setNegativeBtnBackBround(int color) {
        dialog_two_button.setBackgroundColor(color);
    }

    /**
     * 设置第一个按钮文本
     *
     * @param text
     */
    public void setPositiveBtnText(String text) {
        dialog_one_button.setText(text);
    }

    /**
     * 设置第二个按钮文本
     *
     * @param text
     */
    public void setNegativeBtnText(String text) {
        dialog_two_button.setText(text);
    }

    /**
     * 确定键监听器
     *
     * @param listener
     */
    public void setOnPositiveListener(View.OnClickListener listener) {
        dialog_one_button.setOnClickListener(listener);
    }

    /**
     * 取消键监听器
     *
     * @param listener
     */
    public void setOnNegativeListener(View.OnClickListener listener) {
        dialog_two_button.setOnClickListener(listener);
    }
}
