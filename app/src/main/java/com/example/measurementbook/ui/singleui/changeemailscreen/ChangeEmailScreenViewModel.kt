package com.example.measurementbook.ui.singleui.changeemailscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.measurementbook.generalutilities.uiutilities.isEmailValid
import com.example.measurementbook.network.services.AccountService
import com.example.measurementbook.network.services.ChangeEmailState
import com.example.measurementbook.network.services.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeEmailScreenViewModel @Inject constructor(
    private val accountService: AccountService,
    private val logService: LogService
) : ViewModel() {
    var oldEmail by mutableStateOf("")
        private set

    var newEmail by mutableStateOf("")
        private set

    var changeEmailState: MutableStateFlow<ChangeEmailState> = accountService.changeEmailState
        private set

    var displayNotUserEmail by mutableStateOf(false)
        private set

    var oldEmailError by mutableStateOf(false)
        private set

    var newEmailError by mutableStateOf(false)
        private set

    fun onOldEmailChange(newText: String){
        if(displayNotUserEmail) { displayNotUserEmail = false }
        if(oldEmailError) { oldEmailError = false }
        oldEmail = newText
    }

    fun onNewEmailChange(newText: String) {
        if(newEmailError){ newEmailError = false }
        newEmail = newText
    }

    private val showErrorExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        accountService.changeEmailState.value = ChangeEmailState.Error(throwable)
        logService.logNonFatalCrash(throwable)
    }

    fun onChangeEmailButtonClicked() {
        if(!oldEmail.isEmailValid()){
            oldEmailError = true
        }else if(!newEmail.isEmailValid()){
            newEmailError = true
        } else if(accountService.userEmail != oldEmail){
            displayNotUserEmail = true
        } else {
            viewModelScope.launch(showErrorExceptionHandler) {
                accountService.updateEmail(newEmail)
                accountService.changeEmailState.value = ChangeEmailState.ChangeEmailSuccess
            }
        }
    }

    fun onTryAgainClick(){
        accountService.resetChangeEmailState()
    }
}