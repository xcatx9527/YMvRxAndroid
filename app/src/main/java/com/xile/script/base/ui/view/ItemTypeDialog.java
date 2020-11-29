package com.xile.script.base.ui.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yzy.example.R;

import java.util.ArrayList;

/**
 * 提示框
 *
 * @author chsh
 */
public class ItemTypeDialog extends Dialog implements
        DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {

    private Context mContext;
    private LayoutInflater inflater;
    private LayoutParams lp;
    private int percentageH = 4;
    private int percentageW = 8;

    private LinearLayout ll_layout = null;
    private LinearLayout ll_extra = null;
    private TextView dialog_item_1 = null;
    private TextView dialog_item_2 = null;
    private TextView dialog_item_3 = null;
    private TextView dialog_item_4 = null;
    private TextView dialog_item_5 = null;
    // private AnimationDrawable loadingAnimation;
    private String content1;
    private String content2;
    private String content3;
    private String content4;
    private String content5;


    ArrayList<OnCancelListener> m_arrCancelListeners = new ArrayList<OnCancelListener>();
    ArrayList<OnDismissListener> m_arrDismissListeners = new ArrayList<OnDismissListener>();
    private OnInputMileageChanged onChanged = null;
    private Spanned spanned;

    /**
     * @param context
     * @param onChanged
     */
    public ItemTypeDialog(Context context, String content1, String content2, OnInputMileageChanged onChanged) {
        super(context, R.style.Dialog);
        this.mContext = context;
        this.content1 = content1;
        this.content2 = content2;
        this.onChanged = onChanged;
    }


    /**
     * @param context
     * @param content1  管理平台
     * @param content2  优化平台
     * @param content3  转成横屏
     * @param content4  转成竖屏
     * @param content5  删除脚本
     * @param onChanged
     */
    public ItemTypeDialog(Context context, String content1, String content2, String content3, String content4, String content5, OnInputMileageChanged onChanged) {
        super(context, R.style.Dialog);
        this.mContext = context;
        this.content1 = content1;
        this.content2 = content2;
        this.content3 = content3;
        this.content4 = content4;
        this.content5 = content5;
        this.onChanged = onChanged;
    }


    public ItemTypeDialog(Context context, Spanned spanned, String btnConfirm,
                          String btnCancel, OnInputMileageChanged onChanged) {
        super(context, R.style.Dialog);

        this.mContext = context;
        this.onChanged = onChanged;
        this.spanned = spanned;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = inflater.inflate(R.layout.dialog_item_type, null);
        setContentView(mView);
        // 设置window属性
        lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.6f; // 去背景遮盖
        lp.alpha = 1.0f;
        int[] wh = initWithScreenWidthAndHeight(mContext);
        lp.width = wh[0] - wh[0] / percentageW;
        lp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(false);
        setOnDismissListener(this);
        setOnCancelListener(this);
        initView(mView);

    }

    private void initView(View mView) {
        ll_layout = mView.findViewById(R.id.diolog_message_ll);
        ll_extra = mView.findViewById(R.id.ll_extra);
        dialog_item_1 = mView.findViewById(R.id.dialog_item_1);
        dialog_item_2 = mView.findViewById(R.id.dialog_item_2);
        dialog_item_3 = mView.findViewById(R.id.dialog_item_3);
        dialog_item_4 = mView.findViewById(R.id.dialog_item_4);
        dialog_item_5 = mView.findViewById(R.id.dialog_item_5);


        // inputEdit.setText(curMileage+"");
        dialog_item_1.setText(content1 == null ? "" : content1);
        dialog_item_2.setText(content2 == null ? "" : content2);
        dialog_item_3.setText(content2 == null ? "" : content3);
        dialog_item_4.setText(content2 == null ? "" : content4);
        dialog_item_5.setText(content2 == null ? "" : content5);

        dialog_item_1.setOnClickListener(confimListener1);
        dialog_item_2.setOnClickListener(confimListener2);
        dialog_item_3.setOnClickListener(confimListener3);
        dialog_item_4.setOnClickListener(confimListener4);
        dialog_item_5.setOnClickListener(confimListener5);

        if (!TextUtils.isEmpty(content3)) {
            ll_extra.setVisibility(View.VISIBLE);
        }


    }

    private View.OnClickListener confimListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onDismiss();
            onChanged.onConfirm1();
        }
    };

    private View.OnClickListener confimListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onDismiss();
            onChanged.onConfirm2();
        }
    };


    private View.OnClickListener confimListener3 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onDismiss();
            onChanged.onConfirm3();
        }
    };

    private View.OnClickListener confimListener4 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onDismiss();
            onChanged.onConfirm4();
        }
    };


    private View.OnClickListener confimListener5 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onDismiss();
            onChanged.onConfirm5();
        }
    };


    private void ondismiss() {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (m_arrDismissListeners != null) {
            for (int x = 0; x < m_arrDismissListeners.size(); x++)
                m_arrDismissListeners.get(x).onDismiss(dialog);
        }
        ondismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (m_arrCancelListeners != null) {
            for (int x = 0; x < m_arrDismissListeners.size(); x++)
                m_arrCancelListeners.get(x).onCancel(dialog);
        }
    }


    private void onDismiss() {
        if (this.isShowing()) {
            this.dismiss();
        }

    }

    /**
     * 获取当前window width,height
     *
     * @param context
     * @return
     */
    private static int[] initWithScreenWidthAndHeight(Context context) {
        int[] wh = new int[2];
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        wh[0] = dm.widthPixels;
        wh[1] = dm.heightPixels;
        return wh;
    }

    public interface OnInputMileageChanged {
        void onConfirm1();

        void onConfirm2();

        void onConfirm3();

        void onConfirm4();

        void onConfirm5();

    }
}