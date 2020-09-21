package com.example.demoapplication.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProviders
import com.example.demoapplication.R
import com.example.demoapplication.ui.BaseActivity
import com.example.demoapplication.ui.activity.home.HomeActivity
import com.example.demoapplication.viewModel.login.LoginViewModel

class LoginActivity : BaseActivity() {

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var loginButton:AppCompatButton
    private lateinit var skipLogin:AppCompatTextView
    private lateinit var userNameOrMobile:AppCompatEditText
    private lateinit var userPassword:AppCompatEditText
    private lateinit var myProgressbar:ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginButton = findViewById(R.id.loginButton)
        skipLogin = findViewById(R.id.skipLogin)
        userNameOrMobile = findViewById(R.id.userNameOrMobile)
        userPassword = findViewById(R.id.userPassword)
        myProgressbar = findViewById(R.id.my_progressBar)

        loginButton.setOnClickListener {
            when {
                TextUtils.isEmpty(userNameOrMobile.text) -> {
                    Toast.makeText(this, "Please enter user name", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(userPassword.text) -> {
                    Toast.makeText(this, "please enter password", Toast.LENGTH_SHORT).show()
                }
                else -> {

                    /*Hit Api*/
                    loginViewModel.loginApi(
                        email = userNameOrMobile.text.toString(),
                        password = userPassword.text.toString(),
                        action = "login"
                    )
                    myProgressbar.visibility = View.VISIBLE
                }
            }
        }


        skipLogin.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        /*ViewModel*/
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        /*Get-Data*/
        loginViewModel.mutableLiveData.observe(this, androidx.lifecycle.Observer {

            if(it.isSuccess!!){
                startActivity(Intent(this, HomeActivity::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                myProgressbar.visibility = View.GONE
            }else{
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                myProgressbar.visibility = View.GONE
            }
        })

    }

    override fun setContentView(): Int? {
        return R.layout.activity_login
    }
}
