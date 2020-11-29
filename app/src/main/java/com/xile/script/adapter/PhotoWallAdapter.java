package com.xile.script.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xile.script.base.activity.PictureWallActivity;
import com.yzy.example.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GridView的适配器，负责异步从网络上下载图片展示在照片墙上。
 *
 * @author guolin
 */
public class PhotoWallAdapter extends BaseAdapter implements View.OnClickListener {


    /**
     * GridView的实例
     */
    private GridView mPhotoWall;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Object> listData = new ArrayList<>();
    private Map<Integer, Boolean> map = new HashMap<>();// 存放已被选中的CheckBox
    /**
     * 记录每个子项的高度。
     */
    private int mItemHeight = 0;

    private PhtoWallClickCallBack mCallBack;

    /**
     * 自定义回调接口
     */
    public interface PhtoWallClickCallBack {
        void ItemClick(View v, Integer position);
    }

    public PhotoWallAdapter(Context mContext,
                            GridView photoWall, PhtoWallClickCallBack callBack) {
        mPhotoWall = photoWall;
        this.mContext = mContext;
        this.mCallBack = callBack;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Object url = listData.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.itme_grid_pic, null);
            holder.imageView = convertView.findViewById(R.id.photo);
            holder.checkBox = convertView.findViewById(R.id.cb_xbox);
            holder.rl_pic_wall = convertView.findViewById(R.id.rl_pic_wall);
            holder.textView = convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Log.e("hahha", "getView: " + "file:///" + url);
        String name = url.toString().substring(url.toString().lastIndexOf("/") + 1);
        holder.textView.setText(name);
        Glide.with(mContext).load("file://" + url).into(holder.imageView);
        if (holder.imageView.getLayoutParams().height != mItemHeight) {
            holder.imageView.getLayoutParams().height = mItemHeight;
        }
        if (PictureWallActivity.pic_flag) {
            holder.checkBox.setChecked(false);
        }
        //处理checkBox复用混乱的逻辑
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    map.put(position, true);
                } else {
                    map.remove(position);
                }
            }
        });

        if (map != null && map.containsKey(position)) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        holder.rl_pic_wall.setTag(position);
        holder.rl_pic_wall.setOnClickListener(this);
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        CheckBox checkBox;
        TextView textView;
        RelativeLayout rl_pic_wall;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_pic_wall:
                Integer positon = (Integer) v.getTag();
                mCallBack.ItemClick(v, positon);
                break;
        }
    }

    /**
     * 设置item子项的高度。
     */
    public void setItemHeight(int height) {
        if (height == mItemHeight) {
            return;
        }
        mItemHeight = height;
        notifyDataSetChanged();
    }

    public void setObject(List<Object> listData) {
        this.listData = listData;
    }


    public void setMap(HashMap<Integer, Boolean> map) {
        if (map != null) {
            this.map = map;
        }
    }

}