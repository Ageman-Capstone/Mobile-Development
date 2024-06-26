package com.example.capstoneproject.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText

class CustomEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {
    private var inputValid = false
    private var validationCallback: ((Boolean) -> Unit)? = null

    init {
        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                validateInput()
            }

        })
    }

    private fun validateInput() {
        inputValid = text.toString().isNotBlank()
        validationCallback?.invoke(inputValid)
    }

    fun isValidCallback(callback: (Boolean) -> Unit) {
        validationCallback = callback
    }
}