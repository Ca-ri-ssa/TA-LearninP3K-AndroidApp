package com.carissac.learninp3k.view.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Glide
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.databinding.ActivitySettingBinding
import com.carissac.learninp3k.view.auth.AuthViewModel
import com.carissac.learninp3k.view.auth.AuthViewModelFactory
import com.carissac.learninp3k.view.profile.EditProfileActivity
import com.carissac.learninp3k.view.welcome.WelcomeActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(Injection.provideUserRepository(this))
    }

    private val settingViewModel: SettingViewModel by viewModels {
        SettingViewModelFactory(ThemePreference.getInstance(application.dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply{
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.setting) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = systemBars.top
                leftMargin = systemBars.left
                bottomMargin = systemBars.bottom
                rightMargin = systemBars.right
            }

            WindowInsetsCompat.CONSUMED
        }

        theme()
        setContent()

        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnEmergency.setOnClickListener {
            val phoneNum = "119"
            MaterialAlertDialogBuilder(this)
                .setTitle("Panggilan Darurat")
                .setMessage("Apakah Anda ingin melakukan panggilan darurat ($phoneNum)?")
                .setPositiveButton("Iya") { _, _ ->
                    val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:$phoneNum")
                    }
                    startActivity(dialIntent)
                }
                .setNegativeButton("Tidak", null)
                .show()
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun setContent() {
        binding.apply {
            Glide.with(this@SettingActivity)
                .load("https://lh3.googleusercontent.com/d/1HQ0A-bBV9WuHMiprKpHCGR1_W0Os6Gk2=s1000?authuser=0")
                .centerCrop()
                .into(ivSettingUser)
        }
    }

    private fun theme() {
        val switchTheme = binding.toggleDarkMode

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }
    }

    private fun logout() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Keluar")
            .setMessage("Apakah Anda ingin keluar?")
            .setPositiveButton("Iya") { _, _ ->
                authViewModel.logout()

                val intent = Intent(this@SettingActivity, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}