package com.example.capstone.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SplashActivityViewModelFactory(): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SplashActivityViewModel::class.java)){
            return SplashActivityViewModel() as T
        }
        throw IllegalArgumentException ("UnknownViewModel")
    }

}