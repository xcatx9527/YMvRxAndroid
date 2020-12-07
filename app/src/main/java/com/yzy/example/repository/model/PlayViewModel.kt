package com.yzy.example.repository.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.request
import com.yzy.example.http.ListDataUiState
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.BannerBean


class PlayViewModel(var state: SavedStateHandle) : BaseViewModel<GankRepository>() {
    private val homeListKey = "homeListKey"
    fun setHomeListValue(value: ListDataUiState<ArticleDataBean>) = state.set(homeListKey, value)


    fun getData(isRefresh: Boolean) {

    }

    fun loadData() {
        getData(false)
    }
}

