package com.avisio.dashboard.usecase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import com.avisio.dashboard.R
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val SPLASH_SCREEN_DELAY: Long = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        disableDarkMode()
        resumeAfterDelay()
    }

    private fun resumeAfterDelay() {
        Handler().postDelayed({
            resumeWithMainActivity()
        }, SPLASH_SCREEN_DELAY)
    }

    private fun resumeWithMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun disableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}