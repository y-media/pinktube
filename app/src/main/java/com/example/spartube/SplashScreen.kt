package com.example.spartube

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.spartube.databinding.SplashScreenBinding
import com.example.spartube.main.MainActivity

class SplashScreen : AppCompatActivity() {

    private val binding by lazy { SplashScreenBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val animation = binding.splashLottie
        animation.setAnimation("animation_y_media.json")
        animation.loop(true)
        animation.playAnimation()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_up_enter, R.anim.slide_down_exit)
            finish()
        }, 3100)
    }
}