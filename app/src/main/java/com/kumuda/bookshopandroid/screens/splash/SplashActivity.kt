package com.kumuda.bookshopandroid.screens.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kumuda.bookshopandroid.MyApplication.Companion.context
import com.kumuda.bookshopandroid.R
import com.kumuda.bookshopandroid.constants.Constants
import com.kumuda.bookshopandroid.screens.home.HomeActivity
import com.kumuda.bookshopandroid.screens.login.LoginActivity
import com.kumuda.bookshopandroid.util.CommonUtils

class SplashActivity : AppCompatActivity() {
    private val TAG = "SplashActivity"
    private var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mContext = this@SplashActivity
    }

    override fun onResume() {
        super.onResume()
        navigateScreen()
    }

    private fun navigateScreen() {
        Thread(Runnable {
            Thread.sleep(3000)
            this@SplashActivity.runOnUiThread {
                if (CommonUtils.isUserLogin())
                    navigateToHomeScreen()
                else
                    navigateToLoginScreen()
            }
        }).start()
    }

    fun navigateToLoginScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    fun navigateToHomeScreen() {
        val intent = Intent(context, HomeActivity::class.java)
        intent.putExtra(Constants.SOURCE, Constants.SPLASH_SCREEN)
        startActivity(intent)
        finish()
    }

}