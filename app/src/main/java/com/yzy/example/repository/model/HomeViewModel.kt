package com.yzy.example.repository.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.request
import com.yzy.example.http.ListDataUiState
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.BannerBean


class HomeViewModel(var state: SavedStateHandle) : BaseViewModel<GankRepository>() {
    private val homeListKey = "homeListKey"
    fun setHomeListValue(value: ListDataUiState<ArticleDataBean>) = state.set(homeListKey, value)

    private var page = 0

    //首页列表数据
    var homeDataState: MutableLiveData<ListDataUiState<ArticleDataBean>> = MutableLiveData()


    fun getData(isRefresh: Boolean) {
        if (isRefresh) {
            page = 0
        }
        request({ repository.getHomeData(page) }, success = { homeData ->
            if (page == 0) {
                homeDataState.postValue(
                    ListDataUiState(
                        isSuccess = true,
                        isEmpty = homeData.isEmpty(),
                        isRefresh = isRefresh,
                        listData = homeData.datas
                    )
                )
                page++
            } else {
                homeDataState.postValue(
                    ListDataUiState(
                        isSuccess = true,
                        listData = homeData.datas,
                        hasMore = homeData.hasMore()
                    )
                )
            }
        }, emptyView = {
            homeDataState.postValue(ListDataUiState(isSuccess = false, isEmpty = true))
        }, error = {
            homeDataState.postValue(
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.message ?: "",
                    errCode = it.code
                )
            )
        })
    }

    fun loadData() {
        page += 1
        getData(false)
    }
}

