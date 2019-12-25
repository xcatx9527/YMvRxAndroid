package com.yzy.example.repository.service

import com.yzy.example.http.response.BaseResponse
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.BannerBean
import com.yzy.example.repository.bean.GankAndroidBean
import com.yzy.example.repository.bean.GankBaseBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


/**
 *description: Gank网络请求的Service.
 *@date 2019/7/15
 *@author: yzy.
 */
interface GankService {

    /**
     * 根据日期获取福利的接口
     */

    @Headers("urlName:ganKUrl")
    @GET("api/data/Android/{pageSize}/{page}")
     fun getAndroid(@Path("pageSize") pageSize: Int, @Path("page") page: Int):Observable<GankBaseBean<MutableList<GankAndroidBean>>>


//
//    @Headers("urlName:ganKUrl")
//    @GET("api/data/Android/{pageSize}/{page}")
//     fun getAndroidSuspend(@Path("pageSize") pageSize: Int, @Path("page") page: Int): GankBaseBean<GankAndroidBean>


    @GET("banner/json")
    fun banner(
            @Query("page") page: String,
            @Query("size") size: String
    ): Observable<BaseResponse<MutableList<BannerBean>>>

    @GET("article/list/{page}/json")
    fun article(
            @Path("page") page: String
    ): Observable<BaseResponse<ArticleDataBean>>
}