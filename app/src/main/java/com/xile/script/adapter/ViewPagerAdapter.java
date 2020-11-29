package com.xile.script.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

	List<Fragment> list;
	public ViewPagerAdapter(List<Fragment> list) {
		this.list = list;
	}

	public Fragment getItem(int arg0) {

		return list.get(arg0);
	}

	public int getCount() {
		return list.size();
	}

	@Override
	public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
		return false;
	}

}
