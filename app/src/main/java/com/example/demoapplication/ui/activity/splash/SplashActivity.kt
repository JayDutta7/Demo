package com.example.demoapplication.ui.activity.splash

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.demoapplication.BuildConfig
import com.example.demoapplication.R
import com.example.demoapplication.ui.BaseActivity
import com.example.demoapplication.ui.activity.home.HomeActivity
import com.example.demoapplication.viewModel.splash.SplashViewModel
import timber.log.Timber

class SplashActivity : BaseActivity() {

    private lateinit var animation: Animation

    private lateinit var splashViewModel: SplashViewModel

    private lateinit var nameApp:AppCompatTextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nameApp = findViewById(R.id.nameApp)

        setAnimation()

        splashViewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)

        splashViewModel.showAnimation()
        splashViewModel.viewAnimationLiveData.observe(this, Observer {
            when (it) {
                is SplashViewModel.ViewAnimation.AnimationView -> {
                    setAnimation()
                }
            }
        })


        splashViewModel.delayIntent()
        splashViewModel.intentLiveData.observe(this, Observer {
            when (it) {
                is SplashViewModel.GotoNextPage.DashBoardActivity -> {
                    Timber.e(BuildConfig.CONSUMER_KEY)
                    if(!TextUtils.isEmpty(BuildConfig.CONSUMER_KEY)) {
                        goToMainActivity()
                    }else{
                        errorMessage()
                    }
                }
            }
        })

    }

    override fun setContentView(): Int? {
        return R.layout.activity_splash
    }

    private fun setAnimation() {
        animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
        nameApp.animation = animation
    }


    private fun goToMainActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }

    private fun errorMessage() {
        Toast.makeText(this, "Something wrong !! please uninstall and download the application again", Toast.LENGTH_SHORT).show()
    }


}
