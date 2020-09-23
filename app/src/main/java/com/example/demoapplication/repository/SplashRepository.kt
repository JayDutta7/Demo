package com.example.demoapplication.repository

import android.annotation.SuppressLint
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SplashRepository {

    @SuppressLint("CheckResult")
    fun showAnimation() : Completable {
      return  Completable.complete()
            .doOnError {
                Timber.e(it)
            }.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
    }

    @SuppressLint("CheckResult")
    fun delayIntent() : Completable {
      return Completable.complete()
            .delay(3, TimeUnit.SECONDS)
            .doOnError {
                Timber.e(it)
            }.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
    }

}