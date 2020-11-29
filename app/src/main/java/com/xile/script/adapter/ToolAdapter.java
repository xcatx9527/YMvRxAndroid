package com.xile.script.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xile.script.base.fragment.ToolFragment;
import com.xile.script.bean.AccountInfoBean;
import com.yzy.example.R;

import java.util.List;

/**
 * Created by chsh on 2017/3/28.
 */

public class ToolAdapter extends BaseAdapter {

    private Context mContext;
    private List<AccountInfoBean> listData;
    private LayoutInflater mInflater;

    public ToolAdapter(Context mContext, List<AccountInfoBean> listData, ToolFragment toolFragment) {
        this.mContext = mContext;
        this.listData = listData;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (listData == null)
            return 0;
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
        int viewType = super.getItemViewType(position);
        try {
            viewType = listData.get(position).getViewType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewType;
    }

    @Override
    public int getViewTypeCount() {
        return 20;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            switch (viewType) {
                case AccountInfoBean.ACTION_VIEWTYPE_0:
                case AccountInfoBean.ACTION_VIEWTYPE_1:
                case AccountInfoBean.ACTION_VIEWTYPE_2:
                case AccountInfoBean.ACTION_VIEWTYPE_3:
                case AccountInfoBean.ACTION_VIEWTYPE_4:
                case AccountInfoBean.ACTION_VIEWTYPE_5:
                case AccountInfoBean.ACTION_VIEWTYPE_6:
                case AccountInfoBean.ACTION_VIEWTYPE_7:
                case AccountInfoBean.ACTION_VIEWTYPE_8:
                case AccountInfoBean.ACTION_VIEWTYPE_9:
                case AccountInfoBean.ACTION_VIEWTYPE_10:
                case AccountInfoBean.ACTION_VIEWTYPE_11:
                    convertView = mInflater.inflate(R.layout.item_tool, null);
                    holder.img_tool = convertView.findViewById(R.id.img_tool);
                    holder.tv_tool_content = convertView.findViewById(R.id.tv_tool_content);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AccountInfoBean bean = listData.get(position);
        initData(holder, viewType, bean);
        return convertView;
    }

    private void initData(ViewHolder holder, int viewType, AccountInfoBean bean) {
        switch (viewType) {
            case AccountInfoBean.ACTION_VIEWTYPE_0:
            case AccountInfoBean.ACTION_VIEWTYPE_1:
            case AccountInfoBean.ACTION_VIEWTYPE_2:
            case AccountInfoBean.ACTION_VIEWTYPE_3:
            case AccountInfoBean.ACTION_VIEWTYPE_4:
            case AccountInfoBean.ACTION_VIEWTYPE_5:
            case AccountInfoBean.ACTION_VIEWTYPE_6:
            case AccountInfoBean.ACTION_VIEWTYPE_8:
            case AccountInfoBean.ACTION_VIEWTYPE_7:
            case AccountInfoBean.ACTION_VIEWTYPE_9:
            case AccountInfoBean.ACTION_VIEWTYPE_10:
            case AccountInfoBean.ACTION_VIEWTYPE_11:
                initItemContent(holder, bean);
                break;
        }
    }

    private void initItemContent(ViewHolder holder, AccountInfoBean bean) {
        holder.img_tool.setBackgroundResource(bean.getResId());
        holder.tv_tool_content.setText(bean.getSectionTitle());
    }

    class ViewHolder {
        ImageView img_tool;
        TextView tv_tool_content;
    }
}
