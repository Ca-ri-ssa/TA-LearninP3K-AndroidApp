package com.carissac.learninp3k.view.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.carissac.learninp3k.R
import com.google.android.material.textfield.TextInputLayout

class EditTextConfirmPassword @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var textInputLayout: TextInputLayout? = null
    private var originalPasswordField: EditTextPassword? = null

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
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                validateConfirmPassword(s.toString().trim())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                validateConfirmPassword(s.toString().trim())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun setOriginalPasswordField(passwordField: EditTextPassword) {
        this.originalPasswordField = passwordField

        originalPasswordField?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                validateConfirmPassword(text.toString().trim())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validateConfirmPassword(confirmPassword: String) {
        textInputLayout?.let { layout ->
            val originalPassword = originalPasswordField?.text?.toString() ?: ""

            when {
                confirmPassword.isEmpty() -> {
                    layout.helperText = "Konfirmasi password wajib diisi"
                    layout.setHelperTextTextAppearance(R.style.HelperTextStyle_Error)
                }
                confirmPassword != originalPassword -> {
                    layout.helperText = "Password tidak cocok"
                    layout.setHelperTextTextAppearance(R.style.HelperTextStyle_Error)
                }
                else -> {
                    layout.helperText = "Password cocok"
                    layout.setHelperTextTextAppearance(R.style.HelperTextStyle_Success)
                }
            }
        }
    }
}