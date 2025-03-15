package com.carissac.learninp3k.view.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Glide
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private var selectedAvatarId: Int? = null
    private var selectedAvatarImg: String? = null

    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(Injection.provideUserRepository(this))
    }

    private val chooseAvatarLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                selectedAvatarId = result.data?.getIntExtra(AVATAR_ID, -1) ?: -1
                val selectedAvatarImg = result.data?.getStringExtra(AVATAR_IMG)

                if (selectedAvatarId != -1 && !selectedAvatarImg.isNullOrEmpty()) {
                    Glide.with(this)
                        .load(selectedAvatarImg)
                        .centerCrop()
                        .into(binding.ivProfileUser)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply{
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.editProfile) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = systemBars.top
                leftMargin = systemBars.left
                bottomMargin = systemBars.bottom
                rightMargin = systemBars.right
            }

            WindowInsetsCompat.CONSUMED
        }

        selectedAvatarId = intent.getIntExtra(AVATAR_ID, -1)
        selectedAvatarImg = intent.getStringExtra(AVATAR_IMG) ?: ""

        profileViewModel.getProfile()

        profileViewModel.profileResult.observe(this) { result ->
            result.onSuccess { profile ->
                binding.edtUsername.setText(profile.name)
                binding.edtEmail.setText(profile.email)

                if (selectedAvatarId == null || selectedAvatarId == -1 || selectedAvatarImg.isNullOrEmpty()) {
                    selectedAvatarImg = profile.avatarImg
                }

                Glide.with(this)
                    .load(selectedAvatarImg)
                    .centerCrop()
                    .into(binding.ivProfileUser)
            }.onFailure { error ->
                showToast("Gagal mengambil data profil")
            }
        }

        binding.btnChooseAvatar.setOnClickListener {
            val intent = Intent(this, ChooseAvatarActivity::class.java)
            chooseAvatarLauncher.launch(intent)
        }

        binding.btnUpdateProfile.setOnClickListener {
            val name = binding.edtUsername.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()

            if(name.isEmpty() || email.isEmpty())
            {
                showToast("Semua harus terisi dan avatar terpilih")
                return@setOnClickListener
            }

            val isAvatarSelected = if (selectedAvatarId == -1) null else selectedAvatarId

            profileViewModel.updateProfile(name, email, isAvatarSelected)
        }

        profileViewModel.updateProfileResult.observe(this) { event ->
            event.getContentIfNotHandled()?.onSuccess {
                showToast("Profil berhasil diperbarui")
                finish()
            }?.onFailure {
                showToast("Gagal memperbarui profil, Silahkan coba kembali")
            }
        }

        profileViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
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

    companion object {
        const val AVATAR_ID = "avatar_id"
        const val AVATAR_IMG = "avatar_img"
    }
}