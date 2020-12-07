package com.xile.script.base.fragment

import android.view.View
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.databinding.FragmentPlayBinding
import com.yzy.example.repository.model.PlayViewModel
import kotlinx.android.synthetic.main.fragment_record.*

class RecordFragment : CommFragment<PlayViewModel, FragmentPlayBinding>() {
    override fun initContentView() {
        TODO("Not yet implemented")
        tv_record.setOnClickListener(this)
    }

    override fun getLayoutId(): Int {
        TODO("Not yet implemented")
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

    /* fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_record, container, false)
        tl_title_layout.setTitle("录制")
        tl_title_layout.setLeftBtnVisiable(false)
        tv_record = view.findViewById(R.id.tv_record)
        tv_record.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_record -> {
                RecordFloatView.bigFloatState = RecordFloatView.RECORD
                activity!!.startService(Intent(activity, FloatingService::class.java))
                AppUtil.runToBackground(activity)
                ScriptApplication.getService()
                    .execute { CMDUtil.execShell("pkill main_exec") }
            }
        }
    }*/
}