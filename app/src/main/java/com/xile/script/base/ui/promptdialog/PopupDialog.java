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
public class PopupDialog extends Dialog {
    private EditText dialog_edit_view;
    private TextView dialog_one_button, dialog_two_button, dialog_three_button, dialog_four_button, dialog_five_button, dialog_six_button;

    private TextView title;
    private LinearLayout linear_all;

    public PopupDialog(Context context, int buttonCount, boolean bottom) {
        super(context, R.style.dialog_bocop);
        setCustomDialog(buttonCount, bottom);
    }

    private void setCustomDialog(int buttonCount, boolean bottom) {
        View mView = null;
        if (bottom) {
            mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_assistance, null);
        } else {
            mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_two_button, null);
        }
        dialog_edit_view = mView.findViewById(R.id.dialog_edit_view);
        dialog_one_button = mView.findViewById(R.id.dialog_one_button);
        dialog_two_button = mView.findViewById(R.id.dialog_two_button);
        dialog_three_button = mView.findViewById(R.id.dialog_three_button);
        dialog_four_button = mView.findViewById(R.id.dialog_four_button);
        dialog_five_button = mView.findViewById(R.id.dialog_five_button);
        dialog_six_button = mView.findViewById(R.id.dialog_six_button);
        title = mView.findViewById(R.id.title);
        linear_all = mView.findViewById(R.id.linear_all);
        switch (buttonCount) {
            case 0:
                dialog_one_button.setVisibility(View.GONE);
                dialog_two_button.setVisibility(View.GONE);
                dialog_three_button.setVisibility(View.GONE);
                break;
            case 1:
                dialog_one_button.setVisibility(View.VISIBLE);
                dialog_two_button.setVisibility(View.GONE);
                dialog_three_button.setVisibility(View.GONE);
                break;
            case 2:
                dialog_one_button.setVisibility(View.VISIBLE);
                dialog_two_button.setVisibility(View.VISIBLE);
                dialog_three_button.setVisibility(View.GONE);
                break;
            case 3:
                dialog_one_button.setVisibility(View.VISIBLE);
                dialog_two_button.setVisibility(View.VISIBLE);
                dialog_three_button.setVisibility(View.VISIBLE);
                break;
            case 4:
                dialog_one_button.setVisibility(View.VISIBLE);
                dialog_two_button.setVisibility(View.VISIBLE);
                dialog_three_button.setVisibility(View.VISIBLE);
                dialog_four_button.setVisibility(View.VISIBLE);
                break;
            case 5:
                dialog_one_button.setVisibility(View.VISIBLE);
                dialog_two_button.setVisibility(View.VISIBLE);
                dialog_three_button.setVisibility(View.VISIBLE);
                dialog_four_button.setVisibility(View.VISIBLE);
                dialog_five_button.setVisibility(View.GONE);
                break;
            case 6:
                dialog_one_button.setVisibility(View.VISIBLE);
                dialog_two_button.setVisibility(View.VISIBLE);
                dialog_three_button.setVisibility(View.VISIBLE);
                dialog_four_button.setVisibility(View.VISIBLE);
                dialog_five_button.setVisibility(View.GONE);
                dialog_six_button.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
        super.setContentView(mView);
    }

    public void setBackGround(Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(bitmap);
        linear_all.setBackground(drawable);
    }

    public View getEditText() {
        return dialog_edit_view;
    }

    public void setTitle(String titleStr) {
        title.setText(titleStr);
    }

    public void setOneButtonText(String titleStr) {
        dialog_one_button.setText(titleStr);
    }

    public void setTwoButtonText(String titleStr) {
        dialog_two_button.setText(titleStr);
    }

    public void setThreeButtonText(String titleStr) {
        dialog_three_button.setText(titleStr);
    }

    public void setFourButtonText(String titleStr) {
        dialog_four_button.setText(titleStr);
    }

    public void setFiveButtonText(String titleStr) {
        dialog_five_button.setText(titleStr);
    }

    public void setSixButtonText(Spanned titleStr) {
        dialog_six_button.setText(titleStr);
    }

    public void setFiveButtonVisiable() {
        dialog_five_button.setVisibility(View.VISIBLE);
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

    /**
     * 第三按钮监听器
     *
     * @param listener
     */
    public void setOtherListener(View.OnClickListener listener) {
        dialog_three_button.setOnClickListener(listener);
    }

    /**
     * 第四按钮监听器
     *
     * @param listener
     */
    public void setCaptureListener(View.OnClickListener listener) {
        dialog_four_button.setOnClickListener(listener);
    }

    /**
     * 第五按钮监听器
     *
     * @param listener
     */
    public void setRechargeCaptureListener(View.OnClickListener listener) {
        dialog_five_button.setOnClickListener(listener);
    }

    /**
     * 第五按钮监听器
     *
     * @param listener
     */
    public void setRemarkListener(View.OnClickListener listener) {
        dialog_six_button.setOnClickListener(listener);
    }
}
