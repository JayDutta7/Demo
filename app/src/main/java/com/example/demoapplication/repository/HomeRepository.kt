package com.example.demoapplication.repository


import android.annotation.SuppressLint
import android.util.Log
import com.example.demoapplication.localDatabase.staticValue.StaticValue
import com.example.demoapplication.networking.ApiInterface
import com.example.demoapplication.networking.WebService
import com.example.demoapplication.networking.responsePojo.home.MainListResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber


class HomeRepository {
    private var dashboardRepoApiService: ApiInterface? = null
    init {
        dashboardRepoApiService = ApiInterface.CreateRetrofit().apiService(WebService.BaseUrl)
    }
    @SuppressLint("CheckResult")
    fun requestResponse(
        headerMap: HashMap<String, String>,
        action: String,
        pageNo: Int,
        timeZone:String,

    ): Single<MainListResponse>? {
        return dashboardRepoApiService?.fetchListing(
            headerMap,
            "",
            action.toRequestBody("text/plain".toMediaTypeOrNull()),
            pageNo.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            timeZone.toRequestBody("text/plain".toMediaTypeOrNull()),)
            ?.doOnError {
                Timber.e(it)
            }?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
    }

}