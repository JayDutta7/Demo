package com.example.demoapplication.viewModel.splash

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.demoapplication.repository.SplashRepository
import io.reactivex.CompletableObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

class SplashViewModel : ViewModel() {

    val intentLiveData: LiveData<GotoNextPage>
        get() = pageMutableLive
    val viewAnimationLiveData: LiveData<ViewAnimation>
        get() = animationLive

    private val animationLive = MutableLiveData<ViewAnimation>()
    private val pageMutableLive = MutableLiveData<GotoNextPage>()

    private var compositeDisposable: CompositeDisposable? = null

    private var splashRepository: SplashRepository? = null

    init {
        compositeDisposable = CompositeDisposable()
        splashRepository = SplashRepository()
    }

    @SuppressLint("CheckResult")
    fun showAnimation() {
        splashRepository?.showAnimation()?.doOnError {
            Timber.e(it)
        }?.subscribeWith(object : CompletableObserver {
            override fun onComplete() {
                animationLive.postValue(ViewAnimation.AnimationView)
            }
            override fun onSubscribe(d: Disposable) {
                compositeDisposable?.add(d)
            }
            override fun onError(e: Throwable) {
                Timber.e(e)
            }
        })
    }


    @SuppressLint("CheckResult")
    fun delayIntent() {
        splashRepository?.delayIntent()
            ?.doOnError {
                Timber.e(it)
            }?.subscribeWith(object : CompletableObserver {
                override fun onComplete() {
                    pageMutableLive.postValue(GotoNextPage.DashBoardActivity)
                }
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable?.add(d)
                }
                override fun onError(e: Throwable) {
                    Timber.e(e)
                }
            })
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable?.clear()
    }

    sealed class ViewAnimation {
        object AnimationView : ViewAnimation()
    }

    sealed class GotoNextPage {
        object DashBoardActivity : GotoNextPage()
    }

}