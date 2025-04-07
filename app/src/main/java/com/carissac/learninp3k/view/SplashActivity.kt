package com.carissac.learninp3k.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import com.carissac.learninp3k.MainActivity
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.databinding.ActivitySplashBinding
import com.carissac.learninp3k.view.auth.AuthViewModel
import com.carissac.learninp3k.view.auth.AuthViewModelFactory
import com.carissac.learninp3k.view.welcome.WelcomeActivity
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(Injection.provideUserRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.splash) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = systemBars.top
                leftMargin = systemBars.left
                bottomMargin = systemBars.bottom
                rightMargin = systemBars.right
            }

            WindowInsetsCompat.CONSUMED
        }

        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launch {
                authViewModel.sessionToken.observe(this@SplashActivity) { token ->
                    if(!token.isNullOrEmpty()) {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    } else {
                        startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
                    }
                    finish()
                }
            }
        }, 2000)
    }
}