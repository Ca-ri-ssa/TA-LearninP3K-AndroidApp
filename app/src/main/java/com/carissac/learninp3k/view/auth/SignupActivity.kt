package com.carissac.learninp3k.view.auth

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.carissac.learninp3k.MainActivity
import com.carissac.learninp3k.R
import com.carissac.learninp3k.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

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

        val passwordField = binding.edtPassword
        val confirmPasswordField = binding.edtConfirmPassword

        confirmPasswordField.setOriginalPasswordField(passwordField)

        binding.btnSignup.setOnClickListener {
            val username = binding.edtUsername.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val confirmPassword = binding.edtConfirmPassword.text.toString()

            val errorColor = ContextCompat.getColorStateList(this, R.color.red)

            if(username.isEmpty()) {
                binding.tilUsername.helperText = "Nama tidak boleh kosong"
                binding.tilUsername.setHelperTextColor(errorColor)
            } else {
                binding.tilUsername.helperText = null
            }

            if(email.isEmpty()) {
                binding.tilEmail.helperText = "Email tidak boleh kosong"
                binding.tilEmail.setHelperTextColor(errorColor)
            } else {
                binding.tilEmail.helperText = null
            }

            if(password.isEmpty()) {
                binding.tilPassword.helperText = "Password tidak boleh kosong"
                binding.tilPassword.setHelperTextColor(errorColor)

            } else {
                binding.tilPassword.helperText = null
            }

            if(confirmPassword.isEmpty()) {
                binding.tilConfirmPassword.helperText = "Konfirmasi password tidak boleh kosong"
                binding.tilConfirmPassword.setHelperTextColor(errorColor)

            } else {
                binding.tilPassword.helperText = null
            }

            if(username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                // TODO: Buat Backend Logic
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        binding.tvHaveAccountLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}