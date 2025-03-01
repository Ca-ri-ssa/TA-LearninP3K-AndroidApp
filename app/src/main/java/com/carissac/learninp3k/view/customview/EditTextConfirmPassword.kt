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
            override fun beforeTextChanged(a: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                validateConfirmPassword(s.toString().trim())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun setOriginalPasswordField(passwordField: EditTextPassword) {
        this.originalPasswordField = passwordField
    }

    private fun validateConfirmPassword(confirmPassword: String) {
        textInputLayout?.let { layout ->
            val errorColor = ContextCompat.getColorStateList(context, R.color.red)
            val successColor = ContextCompat.getColorStateList(context, R.color.green)

            val originalPassword = originalPasswordField?.text?.toString() ?: ""

            when {
                confirmPassword.isEmpty() -> {
                    layout.helperText = "Konfirmasi password wajib diisi"
                    layout.setHelperTextColor(errorColor)
                }
                confirmPassword != originalPassword -> {
                    layout.helperText = "Password tidak cocok"
                    layout.setHelperTextColor(errorColor)
                }
                else -> {
                    layout.helperText = "Password cocok"
                    layout.setHelperTextColor(successColor)
                }
            }
        }
    }
}