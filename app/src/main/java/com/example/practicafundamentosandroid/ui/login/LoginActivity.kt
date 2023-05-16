package com.example.practicafundamentosandroid.ui.login

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import  com.example.practicafundamentosandroid.R
import  com.example.practicafundamentosandroid.data.repository.User
import  com.example.practicafundamentosandroid.databinding.ActivityLoginConstraintBinding
import  com.example.practicafundamentosandroid.ui.main.MainActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private var startTime = 0L
    private lateinit var binding: ActivityLoginConstraintBinding
    private val viewModel: LoginViewModel by viewModels()

    companion object {
        const val TAG_TIME = "TAG_TIME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginConstraintBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setObservers()
        setListeners()
        if (!User.getToken(this).isNullOrBlank()) goToMainActivity()
    }

    private fun setListeners() {
        with(binding) {
            butttonLogin.setOnClickListener {
                viewModel.login(editTextUser.text.toString(), editTextPassword.text.toString())
            }
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { loginState ->
                when (loginState) {
                    is LoginViewModel.LoginActivityState.LoginSuccess -> {
                        showLoading(false)
                        User.updateToken(loginState.token, this@LoginActivity)
                        goToMainActivity()
                    }
                    is LoginViewModel.LoginActivityState.Loading ->
                        showLoading(true)
                    is LoginViewModel.LoginActivityState.Error -> {
                        showLoading(false)
                        Toast.makeText(this@LoginActivity, loginState.message, Toast.LENGTH_LONG).show()
                    }
                    is LoginViewModel.LoginActivityState.InvalidUser ->
                        Toast.makeText(this@LoginActivity, getString(R.string.invalid_user), Toast.LENGTH_LONG).show()
                    is LoginViewModel.LoginActivityState.InvalidPassword ->
                        Toast.makeText(this@LoginActivity, getString(R.string.invalid_password), Toast.LENGTH_LONG).show()
                    LoginViewModel.LoginActivityState.Idle -> Unit
                }
            }
        }
    }

    private fun showLoading(show: Boolean){

        binding.pbLoading?.visibility = if (show)  View.VISIBLE else View.GONE
    }

    override fun onStart() {
        super.onStart()
        startTimeTracker()
    }

    override fun onStop() {
        endTimeTracker()
        super.onStop()
    }

    private fun startTimeTracker() {
        startTime = System.currentTimeMillis()
    }

    private fun endTimeTracker() {
        val exitTime = System.currentTimeMillis()
        val inAppTime = exitTime - startTime

        with(getPreferences(Context.MODE_PRIVATE)) {
            val accTime = getLong(TAG_TIME, 0L)
            val preferencesEditable = edit()
            preferencesEditable.putLong(TAG_TIME, inAppTime + accTime)
            preferencesEditable.apply()
        }
    }

    private fun goToMainActivity() {
        MainActivity.start(this@LoginActivity)
        finish()
    }

}