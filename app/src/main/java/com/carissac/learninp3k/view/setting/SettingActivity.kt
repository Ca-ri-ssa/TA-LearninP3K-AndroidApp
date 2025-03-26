package com.carissac.learninp3k.view.setting

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Glide
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.databinding.ActivitySettingBinding
import com.carissac.learninp3k.view.auth.AuthViewModel
import com.carissac.learninp3k.view.auth.AuthViewModelFactory
import com.carissac.learninp3k.view.profile.EditProfileActivity
import com.carissac.learninp3k.view.profile.ProfileViewModel
import com.carissac.learninp3k.view.profile.ProfileViewModelFactory
import com.carissac.learninp3k.view.utils.formatDate
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

    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(Injection.provideUserRepository(this))
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
            Log.d("SettingActivity", "Edit Profile Button Clicked")
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnEmergency.setOnClickListener {
            val phoneNum = "112"
            MaterialAlertDialogBuilder(this)
                .setTitle("Panggilan Darurat")
                .setMessage("Apakah Anda ingin melakukan panggilan darurat ($phoneNum)?")
                .setPositiveButton("Iya") { _, _ ->
                    val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                        data = "tel:$phoneNum".toUri()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setContent() {
        profileViewModel.profileResult.observe(this) { result ->
            result.onSuccess { profile ->
                binding.tvSettingUsername.text = profile.name
                binding.tvSettingUserEmail.text = profile.email
                binding.tvSettingUserDate.text = formatDate(profile.createdAt ?: "")

                Glide.with(this@SettingActivity)
                    .load(profile.avatarImg)
                    .centerCrop()
                    .into(binding.ivSettingUser)
            }.onFailure {
                val msg = "Gagal mengambil profil, silahkan mencoba kembali"
                showToast(msg)
            }
        }

        profileViewModel.getProfile()

        profileViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun theme() {
        val switchTheme = binding.toggleDarkMode

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            val currentMode = AppCompatDelegate.getDefaultNightMode()
            val newMode = if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO

            if (currentMode != newMode) {
                AppCompatDelegate.setDefaultNightMode(newMode)
            }

            switchTheme.isChecked = isDarkModeActive
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @Suppress("SameParameterValue")
    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.getProfile()
    }
}