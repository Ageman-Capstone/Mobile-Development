package com.example.capstoneproject.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.capstoneproject.data.pref.UserModel
import com.example.capstoneproject.databinding.ActivityLoginBinding
import com.example.capstoneproject.ui.MainActivity
import com.example.capstoneproject.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding
    private var emailValid = false
    private var passwordValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setEmailEditText()
        setPasswordEditText()
        setButtonEnable()
        loginAction()
    }

    private fun setEmailEditText() {
        binding.etEmail.isValidCallback {
            emailValid = it
            setButtonEnable()
        }
    }

    private fun setPasswordEditText() {
        binding.etPassword.isValidCallback {
            passwordValid = it
            setButtonEnable()
        }
    }

    private fun setButtonEnable() {
        binding.btnLogin.isEnabled = emailValid && passwordValid
    }

    private fun loginAction() {
        binding.btnLogin.setOnClickListener {
            //Loading
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            showLoading(true)
            lifecycleScope.launch {
                val isSuccess = viewModel.logIn(email, password)
                if (!isSuccess) {
                    viewModel.errorMessage.observe(this@LoginActivity) { message ->
                        Log.e("Failed", message)
                        showToast(message)
                    }
                } else {
                    viewModel.successResponse.observe(this@LoginActivity) { user ->
                        viewModel.saveSession(UserModel(user.user.id, user.user.fullName, user.user.email, user.user.photo, user.token, user.user.role))
                        Log.d("Success", user.toString())
                        navigateToMainActivity()
                        showToast("Login Success")
                    }
                }
                showLoading(false)
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}