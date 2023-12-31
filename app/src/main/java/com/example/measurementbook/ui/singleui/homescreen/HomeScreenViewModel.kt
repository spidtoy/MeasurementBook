package com.example.measurementbook.ui.singleui.homescreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.measurementbook.network.services.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val accountService: AccountService
) : ViewModel() {

    val thereIsCurrentUser: Boolean
        get() = accountService.isThereCurrentUser()

    var signedOut by mutableStateOf(false)
        private set

    val userDisplayName: String
        get() = accountService.userDisplayName ?: "Guest"

    fun resetSignedOut(){
        signedOut = false
    }

    val isEmailVerified: Boolean
        get() = accountService.isEmailVerified ?: false

    fun signOut() {
        viewModelScope.launch{
            accountService.signOut()
            signedOut = true
        }
    }

}