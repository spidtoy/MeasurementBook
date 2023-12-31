package com.example.measurementbook.ui.singleui.customerlistscreen

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.measurementbook.network.services.AccountService
import com.example.measurementbook.network.services.LogService
import com.example.measurementbook.network.services.Storage
import com.example.measurementbook.ui.sharedui.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CustomerListScreenViewModel @Inject constructor(
    private val accountService: AccountService,
    private val storageService: Storage,
    private val logService: LogService
) : ViewModel() {

    val menuItems = listOf("in", "cm", "mm", "m")

    var customerUiState by mutableStateOf(CustomerUiState())
        private set

    var addCustomerOn by mutableStateOf(false)
        private set

    var showMustSignUpScreen by mutableStateOf(false)

    var nameEmpty by mutableStateOf(false)

    var mainError by mutableStateOf(false)
        private set

    fun resetMainError(){
        mainError = false
    }

    var displayDropdownMenu by mutableStateOf(false)
        private set

    val isThereCurrentUser: Boolean
        get() = accountService.isThereCurrentUser()

    val userDisplayName: String
        get() = accountService.userDisplayName ?: "Guest"

    fun onMenuIconClicked() {
        displayDropdownMenu = !displayDropdownMenu
    }

    fun resetDisplayDropdownMenu() {
        displayDropdownMenu = false
    }

    fun onDismissRequest() {
        displayDropdownMenu = false
    }

    fun addAnotherMeasurement() {
        customerUiState.measurement.add(Measurement())
    }

    fun removeMeasurement(element: Measurement) {
        customerUiState.measurement.remove(element)
    }

    fun updateAddCustomerOn() {
        addCustomerOn = !addCustomerOn
    }
    fun resetShowMustSignUpScreen(){
        showMustSignUpScreen = false
    }

    fun resetNameEmpty(){
        nameEmpty = false
    }

    fun updateCustomerUiState(newCustomerUiState: CustomerUiState) {
        if(showMustSignUpScreen){ showMustSignUpScreen = false }
        if(nameEmpty){nameEmpty = false}
        customerUiState = newCustomerUiState.copy(actionEnabled = newCustomerUiState.isValid())
    }

    fun onBackPressed() {
        addCustomerOn = false
        customerUiState = CustomerUiState()
    }

    fun signOut() {
        accountService.signOut()
    }

    private val getCustomersErrorExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logService.logNonFatalCrash(throwable)
        mainError = true
    }

    private val saveCustomersErrorExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logService.logNonFatalCrash(throwable)
    }

    var customerList: List<Customer> by mutableStateOf(emptyList())

    fun getUserCustomers() {
        if(accountService.userId != "guest") {
            viewModelScope.launch(getCustomersErrorExceptionHandler) {
                storageService.getUserCustomers(accountService.userId).collect {
                    customerList = it.list ?: viewErrList
                }
            }
        }
    }

    private fun saveCustomer() {
        viewModelScope.launch(saveCustomersErrorExceptionHandler){
            storageService.save(customerUiState.toCustomer())
        }
    }

    fun onSaveClick() {
        if(accountService.userId == "guest") {
            showMustSignUpScreen = true
        } else if(!customerUiState.actionEnabled) {
            nameEmpty = true
        } else {
                addCustomerOn = false
                saveCustomer()
                customerUiState = CustomerUiState()
        }
    }
}
