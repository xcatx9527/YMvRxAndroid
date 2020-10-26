package com.yzy.example.component.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import com.blankj.utilcode.util.ConvertUtils
import com.yzy.baselibrary.extention.gone
import com.yzy.baselibrary.extention.inflate
import com.yzy.example.R
import com.yzy.example.component.MainFragmentDirections
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.databinding.FragmentHomeBinding
import com.yzy.example.extention.initFloatBtn
import com.yzy.example.extention.loadUrl
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.BannerBean
import com.yzy.example.repository.model.HomeViewModel
import com.yzy.example.widget.CycleViewPager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_banner.view.*
import kotlinx.android.synthetic.main.layout_comm_title.*
import com.yzy.example.widget.recyclerview.SpaceItemDecoration

class HomeFragment : CommFragment<HomeViewModel, FragmentHomeBinding>() {
    private val mAdapter by lazy {
        HomeListAdapter(
            mutableListOf(),
            true
        )
    }
    private lateinit var banner: CycleViewPager

    override fun initContentView() {
        binding.vm = viewModel
        main_toolbar.title = "首页"
        smRefresh.setEnableRefresh(true)
        smRefresh.autoRefresh()
        smRefresh.setOnRefreshListener {
            viewModel.getData(true)
        }
        with(rvHomeRecycler) {
            adapter = mAdapter
            addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f), false))
            initFloatBtn(floatbtn)
        }

        mAdapter.apply {
            loadMoreModule.setOnLoadMoreListener {
                viewModel.loadData()
            }
            setOnItemClickListener { adapter, v, position ->
                val bean: ArticleDataBean = adapter.data[position] as ArticleDataBean
//                Navigation.findNavController(v).navigate(MainFragmentDirections.actionMainFragmentToWebsiteDetailFragment(ariticleData = bean))
            }
        }

        viewModel.run {
            //监听首页文章列表请求的数据变化
            homeDataState.observe(viewLifecycleOwner, Observer {
                smRefresh.finishRefresh()
                if (it.isSuccess) {
                    //成功
                    when {
                        //是第一页
                        it.isRefresh -> {
                            mAdapter.setNewInstance(it.listData)
                            loadMore(it.isEmpty)
                        }
                        //不是第一页
                        else -> {
                            mAdapter.addData(it.listData ?: mutableListOf())
                            loadMore(it.isEmpty)
                        }
                    }
                } else {
                    loadMore(false)
                }
            })

        }
    }


    private fun loadMore(size: Boolean) {
        if (size) mAdapter.loadMoreModule.loadMoreEnd(true)
        else mAdapter.loadMoreModule.loadMoreComplete()
    }


    override fun getLayoutId(): Int = R.layout.fragment_home


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        viewModel.homeDataState.value?.let { viewModel.setHomeListValue(it) }
        super.onViewStateRestored(savedInstanceState)
    }
}
