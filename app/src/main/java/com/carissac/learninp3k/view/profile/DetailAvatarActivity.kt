package com.carissac.learninp3k.view.profile

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Glide
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.databinding.ActivityDetailAvatarBinding
import kotlin.getValue

class DetailAvatarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailAvatarBinding

    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(Injection.provideUserRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailAvatarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply{
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.detailAvatar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = systemBars.top
                leftMargin = systemBars.left
                bottomMargin = systemBars.bottom
                rightMargin = systemBars.right
            }

            WindowInsetsCompat.CONSUMED
        }

        val avatarId = intent.getIntExtra(AVATAR_ID, -1)
        if(avatarId != -1) {
            profileViewModel.getDetailAvatar(avatarId)
        }

        profileViewModel.avatarResult.observe(this) { result ->
            result.onSuccess { avatar ->
                binding.apply {
                    Glide.with(this@DetailAvatarActivity)
                        .load(avatar.avatarImg)
                        .centerCrop()
                        .into(ivAvatarChar)

                    tvAvatarName.text = avatar.avatarName
                    tvCharPersonality.text = avatar.avatarPersonality
                    tvCharDesc.text = avatar.avatarDesc
                }
            }.onFailure { error ->
                showToast(error.message)
            }
        }

        profileViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val AVATAR_ID = "avatar_id"
    }
}