package com.carissac.learninp3k.view.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.carissac.learninp3k.R
import com.google.android.material.textfield.TextInputLayout

class EditTextPassword @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var textInputLayout: TextInputLayout? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        textInputLayout = parent.parent as? TextInputLayout
    }

    init {
        setupView()
    }

    private fun setupView() {
        hint = context.getString(R.string.hint_password)

        addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(a: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                validatePassword(s.toString().trim())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validatePassword(password: String) {
        textInputLayout?.let { layout ->
            when {
                password.isEmpty() -> {
                    layout.helperText = "Password wajib diisi"
                    layout.setHelperTextTextAppearance(R.style.HelperTextStyle_Error)
                }
                password.length < 8 -> {
                    layout.helperText = "Password harus minimal 8 karakter\nTambahkan lebih banyak karakter"
                    layout.setHelperTextTextAppearance(R.style.HelperTextStyle_Error)
                }
                isStrongPassword(password) -> {
                    layout.helperText = "Password kuat"
                    layout.setHelperTextTextAppearance(R.style.HelperTextStyle_Success)
                }
                isMediumPassword(password) -> {
                    layout.helperText = "Password kurang kuat\nTambahkan karakter spesial (!@#\\\$%^&*)"
                    layout.setHelperTextTextAppearance(R.style.HelperTextStyle_Warning)
                }
                else -> {
                    layout.helperText = "Password lemah\nKombinasikan huruf, angka, dan karakter spesial"
                    layout.setHelperTextTextAppearance(R.style.HelperTextStyle_Error)
                }
            }
        }
    }

    private fun isMediumPassword(password: String): Boolean {
        val hasLetters = password.any { it.isLetter() }
        val hasDigits = password.any { it.isDigit() }
        return password.length >= 8 && hasLetters && hasDigits
    }

    private fun isStrongPassword(password: String): Boolean {
        val hasLetters = password.any { it.isLetter() }
        val hasDigits = password.any { it.isDigit() }
        val hasSpecial = password.any { !it.isLetterOrDigit() }
        return password.length >= 8 && hasLetters && hasDigits && hasSpecial
    }
}