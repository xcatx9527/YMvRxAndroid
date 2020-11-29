package com.xile.script.base.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.xile.script.base.ScriptApplication;
import com.xile.script.base.ui.view.TitleLayout;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.service.FloatingService;
import com.xile.script.utils.AppUtil;
import com.xile.script.utils.CMDUtil;
import com.yzy.example.R;


public class RecordFragment extends Fragment implements View.OnClickListener {
    private TitleLayout tl_title_layout;
    private TextView tv_record;

    public RecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        tl_title_layout = view.findViewById(R.id.tl_title_layout);
        tl_title_layout.setTitle("录制");
        tl_title_layout.setLeftBtnVisiable(false);
        tv_record = view.findViewById(R.id.tv_record);
        tv_record.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_record:
                RecordFloatView.bigFloatState = RecordFloatView.RECORD;
                getActivity().startService(new Intent(getActivity(), FloatingService.class));
                AppUtil.runToBackground(getActivity());
                ScriptApplication.getService().execute(() -> CMDUtil.execShell("pkill main_exec"));
                /*getActivity().startService(new Intent(getActivity(), FloatingService.class));
                AppUtil.runToBackground(getActivity());*/
                break;
        }
    }


}
