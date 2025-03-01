package com.carissac.learninp3k.view.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.carissac.learninp3k.R
import com.google.android.material.textfield.TextInputLayout

class EditTextEmail @JvmOverloads constructor(
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
        hint = context.getString(R.string.hint_email)

        addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(a: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                validateEmail(s.toString().trim())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validateEmail(email: String) {
        textInputLayout?.let { layout ->
            val errorColor = ContextCompat.getColorStateList(context, R.color.red)

            when {
                email.isEmpty() -> {
                    layout.helperText = "Email wajib diisi"
                    layout.setHelperTextColor(errorColor)
                }

                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    layout.helperText = "Format email tidak valid"
                    layout.setHelperTextColor(errorColor)
                }

                else -> {
                    layout.helperText = null
                }
            }
        }
    }
}