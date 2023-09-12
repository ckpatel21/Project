package com.example.capstone.viewModel

import androidx.lifecycle.ViewModel
import com.example.capstone.model.User
import com.example.capstone.ui.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginActivityViewModel : ViewModel() {

    var mAuth = FirebaseAuth.getInstance()

    private val _userInfoState = MutableStateFlow(User())
    val userInfo : StateFlow<User> = _userInfoState.asStateFlow()

    fun loginActivity(){

    }
}