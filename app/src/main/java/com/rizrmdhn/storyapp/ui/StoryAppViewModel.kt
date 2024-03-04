package com.rizrmdhn.storyapp.ui

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.rizrmdhn.core.common.Helpers
import com.rizrmdhn.core.data.Resource
import com.rizrmdhn.core.domain.usecase.StoryUseCase
import com.rizrmdhn.core.utils.FormValidator
import com.rizrmdhn.storyapp.ui.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoryAppViewModel(
    private val storyUseCase: StoryUseCase
) : ViewModel() {
    private val _initialRender: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val initialRender: StateFlow<Boolean> get() = _initialRender

    private val _darkMode: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val darkMode: StateFlow<Boolean> get() = _darkMode

    private val _token: MutableStateFlow<String> = MutableStateFlow("")
    val token: StateFlow<String> get() = _token

    private val _loginIsLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loginIsLoading: StateFlow<Boolean> get() = _loginIsLoading

    private val _registerIsLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val registerIsLoading: StateFlow<Boolean> get() = _registerIsLoading

    private val _locale: MutableStateFlow<String> = MutableStateFlow("en")
    val locale: StateFlow<String> get() = _locale

    private val _name: MutableStateFlow<String> = MutableStateFlow("")
    val name: MutableStateFlow<String> get() = _name

    private val _email: MutableStateFlow<String> = MutableStateFlow("")
    val email: MutableStateFlow<String> get() = _email

    private val _password: MutableStateFlow<String> = MutableStateFlow("")
    val password: MutableStateFlow<String> get() = _password

    private val _isNameValid: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isNameValid: MutableStateFlow<Boolean> get() = _isNameValid

    private val _isEmailValid: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isEmailValid: MutableStateFlow<Boolean> get() = _isEmailValid


    private val _isPasswordValid: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isPasswordValid: MutableStateFlow<Boolean> get() = _isPasswordValid

    private val _nameMessage: MutableStateFlow<String> = MutableStateFlow("")
    val nameMessage: MutableStateFlow<String> get() = _nameMessage

    private val _emailMessage: MutableStateFlow<String> = MutableStateFlow("")
    val emailMessage: MutableStateFlow<String> get() = _emailMessage

    private val _passwordMessage: MutableStateFlow<String> = MutableStateFlow("")
    val passwordMessage: MutableStateFlow<String> get() = _passwordMessage

    private val _initialEmail: MutableStateFlow<Boolean> = MutableStateFlow(true)

    private val _initialName: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val initialName: MutableStateFlow<Boolean> get() = _initialName
    val initialEmail: MutableStateFlow<Boolean> get() = _initialEmail

    private val _initialPassword: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val initialPassword: MutableStateFlow<Boolean> get() = _initialPassword

    fun getLocaleSetting(context: Context) {
        viewModelScope.launch {
            storyUseCase.getLocaleSetting().collect {
                _locale.value = it
                Helpers.localeSelection(
                    context = context,
                    localeTag = it
                )
            }
        }
    }

    fun setLocaleSetting(locale: String, context: Context) {
        viewModelScope.launch {
            storyUseCase.setLocaleSetting(locale)
            _locale.value = locale
            Helpers.localeSelection(
                context = context,
                localeTag = locale
            )
        }
    }


    fun getDarkMode() {
        viewModelScope.launch {
            storyUseCase.getDarkMode().collect {
                _darkMode.value = it
            }
        }
    }

    fun setDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            storyUseCase.setDarkMode(isDarkMode)
            _darkMode.value = isDarkMode
        }
    }

    fun login(email: String, password: String, context: Context) {
        viewModelScope.launch {
            if (email.isEmpty() || password.isEmpty()) {
                _emailMessage.value = "Email is required"
                _passwordMessage.value = "Password is required"
                _initialPassword.value = false
                _initialEmail.value = false
                return@launch
            } else if (FormValidator.isEmailValid(email).not()) {
                Toast.makeText(context, "Email is not valid", Toast.LENGTH_SHORT).show()
                _emailMessage.value = "Email is not valid"
                _initialPassword.value = false
                _initialEmail.value = false
                return@launch
            } else if (FormValidator.isPasswordValid(password).not()) {
                Toast.makeText(context, "Password must be at least 8 characters", Toast.LENGTH_SHORT)
                    .show()
                _passwordMessage.value = "Password must be at least 8 characters"
                _initialEmail.value = false
                _initialPassword.value = false
                return@launch
            }

            storyUseCase.login(email, password).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _loginIsLoading.value = true
                    }

                    is Resource.Success -> {
                        _token.value = result.data?.loginResult?.token ?: ""
                        setAccessToken(result.data?.loginResult?.token ?: "")
                        _loginIsLoading.value = false
                        _email.value = ""
                        _initialEmail.value = true
                        _password.value = ""
                        _initialPassword.value = true
                        Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
                    }

                    is Resource.Error -> {
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        _loginIsLoading.value = false
                    }
                }
            }
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        context: Context,
        navController: NavHostController
    ) {
        viewModelScope.launch {
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                _nameMessage.value = "Name is required"
                _emailMessage.value = "Email is required"
                _passwordMessage.value = "Password is required"
                _initialName.value = false
                _initialEmail.value = false
                _initialPassword.value = false
                return@launch
            } else if (FormValidator.isNameValid(name).not()) {
                Toast.makeText(context, "Name is not valid", Toast.LENGTH_SHORT).show()
                _nameMessage.value = "Name is not valid"
                _initialName.value = false
                _initialEmail.value = false
                _initialPassword.value = false
                return@launch
            } else if (FormValidator.isEmailValid(email).not()) {
                Toast.makeText(context, "Email is not valid", Toast.LENGTH_SHORT).show()
                _emailMessage.value = "Email is not valid"
                _initialName.value = false
                _initialEmail.value = false
                _initialPassword.value = false
                return@launch
            } else if (FormValidator.isPasswordValid(password).not()) {
                Toast.makeText(context, "Password must be at least 8 characters", Toast.LENGTH_SHORT)
                    .show()
                _passwordMessage.value = "Password must be at least 8 characters"
                _initialName.value = false
                _initialEmail.value = false
                _initialPassword.value = false
                return@launch
            }

            storyUseCase.register(name, email, password).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _registerIsLoading.value = true
                    }

                    is Resource.Success -> {
                        _registerIsLoading.value = false
                        Toast.makeText(
                            context,
                            result.data?.message ?: "User Created",
                            Toast.LENGTH_SHORT
                        ).show()
                        _email.value = ""
                        _initialEmail.value = true
                        _password.value = ""
                        _initialPassword.value = true
                        _name.value = ""
                        _initialName.value = true
                        _registerIsLoading.value = false
                        navController.navigate(
                            Screen.Login.route
                        )
                    }

                    is Resource.Error -> {
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        _registerIsLoading.value = false
                    }
                }
            }
        }
    }

    fun setName(name: String) {
        if (FormValidator.isNameValid(name)) {
            _isNameValid.value = true
            _nameMessage.value = ""
        } else {
            _isNameValid.value = false
            _nameMessage.value = "Name is not valid"
        }
        _name.value = name
        _initialName.value = false
    }

    fun setEmail(email: String) {
        if (FormValidator.isEmailValid(email)) {
            _isEmailValid.value = true
            _emailMessage.value = ""
        } else {
            _isEmailValid.value = false
            _emailMessage.value = "Email is not valid"
        }
        _email.value = email
        _initialEmail.value = false
    }

    fun setPassword(password: String) {
        if (FormValidator.isPasswordValid(password)) {
            _isPasswordValid.value = true
            _passwordMessage.value = ""
        } else {
            _isPasswordValid.value = false
            _passwordMessage.value = "Password must be at least 8 characters"
        }
        _password.value = password
        _initialPassword.value = false
    }

    fun setNameToEmpty() {
        _name.value = ""
        _initialName.value = true
    }

    fun setEmailToEmpty() {
        _email.value = ""
        _initialEmail.value = true
    }

    fun setPasswordToEmpty() {
        _password.value = ""
        _initialPassword.value = true
    }

    fun getAccessToken() {
        viewModelScope.launch {
            _initialRender.value = true
            storyUseCase.getAccessToken().collect {
                _token.value = it
                _initialRender.value = false
            }
        }
    }

    private fun setAccessToken(token: String) {
        viewModelScope.launch {
            storyUseCase.setAccessToken(token)
            _token.value = token
        }
    }

    fun logout(
        context: Context
    ) {
        viewModelScope.launch {
            storyUseCase.removeAccessToken()
            _token.value = ""
            Toast.makeText(context, "Logout Success", Toast.LENGTH_SHORT).show()
        }
    }
}