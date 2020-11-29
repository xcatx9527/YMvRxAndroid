package com.xile.script.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.xile.script.bean.FeatureInfo;
import com.yzy.example.R;

import java.util.ArrayList;
import java.util.List;


/**
 * date: 2017/3/28 18:02
 *
 * @scene 功能菜单适配器
 */

public class FeaturesMenuAdapter extends RecyclerView.Adapter<FeaturesMenuAdapter.MenuHolder> {

    private List<FeatureInfo> objects = new ArrayList<FeatureInfo>();
    private Context context;
    private LayoutInflater layoutInflater;


    public FeaturesMenuAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MenuHolder holder = new MenuHolder(layoutInflater.inflate(R.layout.float_function_menu_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MenuHolder holder, final int position) {
        if (objects.get(position).getIcon() != null) {
            holder.img_feature.setImageDrawable(objects.get(position).getIcon());
        } else {
            holder.img_feature.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(objects.get(position).getFunction())) {
            holder.tv_feature.setText(objects.get(position).getFunction());
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


    public void setObjects(List<FeatureInfo> infos) {
        this.objects.clear();
        objects.addAll(infos);
    }


    class MenuHolder extends RecyclerView.ViewHolder {
        public ImageView img_feature;
        public TextView tv_feature;

        public MenuHolder(View rootView) {
            super(rootView);
            this.img_feature = rootView.findViewById(R.id.img_feature);
            this.tv_feature = rootView.findViewById(R.id.tv_feature);
        }

    }
}
