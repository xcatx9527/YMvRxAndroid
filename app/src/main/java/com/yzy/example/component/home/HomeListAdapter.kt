package com.yzy.example.component.home

import android.text.TextUtils
import android.widget.ImageView
import coil.transform.CircleCropTransformation
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yzy.example.R
import com.yzy.example.extention.loadUrl
import com.yzy.example.extention.toHtml
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.widget.CollectView

/**
 *   @auther : yzy
 *   time   : 2019/11/08
 */
class HomeListAdapter(data: MutableList<ArticleDataBean>?) :
    BaseDelegateMultiAdapter<ArticleDataBean, BaseViewHolder>(data), LoadMoreModule {
    private var mOnCollectViewClickListener: OnCollectViewClickListener? = null
    private val Ariticle = 1//文章类型
    private val Project = 2//项目类型 本来打算不区分文章和项目布局用统一布局的，但是布局完以后发现差异化蛮大的，所以还是分开吧
    private var showTag = false//是否展示标签 tag 一般主页才用的到

    constructor(data: MutableList<ArticleDataBean>?, showTag: Boolean) : this(data) {
        this.showTag = showTag
    }

    init {
//        setAdapterAnimion(2)
        // 第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<ArticleDataBean>() {
            override fun getItemType(data: List<ArticleDataBean>, position: Int): Int {
                //根据是否有图片 判断为文章还是项目，好像有点low的感觉。。。我看实体类好像没有相关的字段，就用了这个，也有可能是我没发现
                return if (TextUtils.isEmpty(data[position].envelopePic)) Ariticle else Project
            }
        })
        // 第二步，绑定 item 类型
        getMultiTypeDelegate()?.let {
            it.addItemType(Ariticle, R.layout.item_ariticle)
            it.addItemType(Project, R.layout.item_project)
        }
    }

    override fun convert(holder: BaseViewHolder, item: ArticleDataBean) {
        when (holder.itemViewType) {
            Ariticle -> {
                //文章布局的赋值
                item.run {
                    holder.setText(
                        R.id.item_home_author,
                        if (author.isNotEmpty()) author else shareUser
                    )
                    holder.setText(R.id.item_home_content, title.toHtml())
                    holder.setText(R.id.item_home_type2, "$superChapterName·$chapterName".toHtml())
                    holder.setText(R.id.item_home_date, niceDate)
                    holder.getView<CollectView>(R.id.item_home_collect).isChecked = collect
                    if (showTag) {
                        //展示标签
                        holder.setGone(R.id.item_home_new, !fresh)
                        holder.setGone(R.id.item_home_top, type != 1)
                        if (tags.isNotEmpty()) {
                            holder.setGone(R.id.item_home_type1, false)
                            holder.setText(R.id.item_home_type1, tags[0].name)
                        } else {
                            holder.setGone(R.id.item_home_type1, true)
                        }
                    } else {
                        //隐藏所有标签
                        holder.setGone(R.id.item_home_top, true)
                        holder.setGone(R.id.item_home_type1, true)
                        holder.setGone(R.id.item_home_new, true)
                    }
                }
                holder.getView<CollectView>(R.id.item_home_collect)
                    .setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                        override fun onClick(v: CollectView) {
                            mOnCollectViewClickListener?.onClick(item, v, holder.layoutPosition)
                        }
                    })
            }
            Project -> {
                //项目布局的赋值
                item.run {
                    holder.setText(
                        R.id.item_project_author,
                        if (author.isNotEmpty()) author else shareUser
                    )
                    holder.setText(R.id.item_project_title, title.toHtml())
                    holder.setText(R.id.item_project_content, desc.toHtml())
                    holder.setText(
                        R.id.item_project_type,
                        "$superChapterName·$chapterName".toHtml()
                    )
                    holder.setText(R.id.item_project_date, niceDate)
                    if (showTag) {
                        //展示标签
                        holder.setGone(R.id.item_project_new, !fresh)
                        holder.setGone(R.id.item_project_top, type != 1)
                        if (tags.isNotEmpty()) {
                            holder.setGone(R.id.item_project_type1, false)
                            holder.setText(R.id.item_project_type1, tags[0].name)
                        } else {
                            holder.setGone(R.id.item_project_type1, true)
                        }
                    } else {
                        //隐藏所有标签
                        holder.setGone(R.id.item_project_top, true)
                        holder.setGone(R.id.item_project_type1, true)
                        holder.setGone(R.id.item_project_new, true)
                    }
                    holder.getView<CollectView>(R.id.item_project_collect).isChecked = collect
                    holder.getView<ImageView>(R.id.item_project_imageview).loadUrl(envelopePic)
//                    view.loadUrl(url,transformations = CircleCropTransformation())
//                    Glide.with(context.applicationContext).load(envelopePic)
//                        .transition(DrawableTransitionOptions.withCrossFade(500))
//                        .into()
                }
                holder.getView<CollectView>(R.id.item_project_collect)
                    .setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                        override fun onClick(v: CollectView) {
                            mOnCollectViewClickListener?.onClick(item, v, holder.layoutPosition)
                        }
                    })
            }
        }


    }


    interface OnCollectViewClickListener {
        fun onClick(item: ArticleDataBean, v: CollectView, position: Int)
    }


}


