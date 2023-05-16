package com.example.practicafundamentosandroid.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import  com.example.practicafundamentosandroid.utils.BASE_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Credentials
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request


class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<LoginActivityState>(LoginActivityState.Idle)
    val uiState: StateFlow<LoginActivityState> = _uiState

    fun login(user: String, password: String) {
        if (checkUserValidity(user) && checkPasswordValidity(password)) launchLoginRequest(user, password)
    }

    private fun checkPasswordValidity(password: String) =
        if (password.isBlank() && password.length < 3) {
            _uiState.value = LoginActivityState.InvalidPassword
            false
        } else
            true


    private fun launchLoginRequest(user: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val url = "${BASE_URL}auth/login"
            val credentials = Credentials.basic("verserper@hotmail.com", "Qwe123!")
            val formBody = FormBody.Builder() // Esto hace que la request sea de tipo POST
                .build()
            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", credentials)
                .post(formBody)
                .build()
            val call = client.newCall(request)
            val response = call.execute()
            _uiState.value =  if (response.isSuccessful)
                response.body?.let {
                    LoginActivityState.LoginSuccess(it.string())
                } ?: LoginActivityState.Error("Empty Token")
            else
                LoginActivityState.Error(response.message)
        }
    }

    private fun checkUserValidity(user: String) =
        if (user.isBlank()) {
            _uiState.value = LoginActivityState.InvalidUser
            false
        } else
            true

    sealed class LoginActivityState {
        data class LoginSuccess(val token: String) : LoginActivityState()
        data class Error(val message : String): LoginActivityState()
        object Idle : LoginActivityState()
        object Loading : LoginActivityState()
        object InvalidUser : LoginActivityState()
        object InvalidPassword : LoginActivityState()
    }
}