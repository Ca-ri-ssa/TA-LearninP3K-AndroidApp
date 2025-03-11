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
import androidx.recyclerview.widget.LinearLayoutManager
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.databinding.ActivityChooseAvatarBinding

class ChooseAvatarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseAvatarBinding
    private lateinit var avatarAdapter: ChooseAvatarAdapter

    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(Injection.provideUserRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityChooseAvatarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply{
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.chooseAvatar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = systemBars.top
                leftMargin = systemBars.left
                bottomMargin = systemBars.bottom
                rightMargin = systemBars.right
            }

            WindowInsetsCompat.CONSUMED
        }

        avatarAdapter = ChooseAvatarAdapter()
        binding.rvChooseAvatar.apply {
            layoutManager = LinearLayoutManager(this@ChooseAvatarActivity)
            adapter = avatarAdapter
        }

        profileViewModel.listAvatarResult.observe(this) { result ->
            result.onSuccess { avatarList ->
                avatarAdapter.submitList(avatarList)
            }.onFailure { error ->
                showToast(error.message ?: "Gagal mengambil data avatar")
            }
        }

        profileViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        profileViewModel.getAllAvatar()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @Suppress("SameParameterValue")
    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}