package com.xile.script.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xile.script.bean.FileInfo;
import com.xile.script.utils.common.StringUtil;
import com.yzy.example.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：赵小飞<br>
 * 时间 2017/3/11.
 */

public class PlayAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    List<FileInfo> strList = new ArrayList<>();

    public PlayAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return strList.size();
    }

    @Override
    public FileInfo getItem(int position) {
        return strList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_play, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews(getItem(position).getName(), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(String name, ViewHolder holder) {

        if (!StringUtil.isNullOrEmpty(name)) {
            holder.play_name.setText(name);
        }

    }

    protected class ViewHolder {
        private TextView play_name;

        public ViewHolder(View view) {
            play_name = view.findViewById(R.id.play_name);
        }
    }

    public void setObjects(List<FileInfo> objects) {
        if (objects != null && objects.size() > 0) {
            this.strList = objects;
        }
    }
}
