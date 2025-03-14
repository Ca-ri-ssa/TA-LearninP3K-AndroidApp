package com.carissac.learninp3k.view.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.carissac.learninp3k.MainActivity
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.databinding.ActivitySignupBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(Injection.provideUserRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply{
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.signup) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = systemBars.top
                leftMargin = systemBars.left
                bottomMargin = systemBars.bottom
                rightMargin = systemBars.right
            }

            WindowInsetsCompat.CONSUMED
        }

        binding.btnSignup.isEnabled = false
        binding.edtConfirmPassword.setOriginalPasswordField(binding.edtPassword)
        editTextWatchers()

        binding.btnSignup.setOnClickListener {
            val username = binding.edtUsername.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val confirmPassword = binding.edtConfirmPassword.text.toString()

            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                authViewModel.userRegister(username, email, password)
            }
        }

        authViewModel.registerResult.observe(this) { result ->
            result.onSuccess {
                showRegisterSuccessResult()
            }.onFailure {
                showRegisterFailResult()
            }
        }

        authViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        binding.tvHaveAccountLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun editTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateSignupButtonState()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.edtUsername.addTextChangedListener(textWatcher)
        binding.edtEmail.addTextChangedListener(textWatcher)
        binding.edtPassword.addTextChangedListener(textWatcher)
        binding.edtConfirmPassword.addTextChangedListener(textWatcher)
    }

    private fun updateSignupButtonState() {
        val username = binding.edtUsername.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val confirmPassword = binding.edtConfirmPassword.text.toString().trim()

        val isPasswordsMatch = password.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword

        binding.btnSignup.isEnabled = username.isNotEmpty() &&
                email.isNotEmpty() &&
                isPasswordsMatch
    }

    private fun showRegisterSuccessResult() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Daftar akun berhasil!")
            .setMessage("Akun Anda telah berhasil dibuat. Silahkan login akun Anda")
            .setPositiveButton("Ok") { _,_ ->
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            .show()
    }

    private fun showRegisterFailResult() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Registrasi gagal!")
            .setMessage("Ada terjadi kesalahan. Silahkan mencoba kembali")
            .setPositiveButton("Ok") { _,_ -> }
            .show()
    }

    @Suppress("SameParameterValue")
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}