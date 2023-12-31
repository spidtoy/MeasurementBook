package com.example.measurementbook.ui.singleui.customerdetailscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.measurementbook.network.services.LogService
import com.example.measurementbook.network.services.Storage
import com.example.measurementbook.ui.sharedui.CustomerUiState
import com.example.measurementbook.ui.sharedui.toCustomerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CustomerDetailScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val logService: LogService,
    private val storageService: Storage
) : ViewModel() {

    var customerUiState by mutableStateOf(CustomerUiState())

    var showDialog by mutableStateOf(false)

    fun resetShowDialog(){
        showDialog = false
    }

    private val customerDetailErrorExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logService.logNonFatalCrash(throwable)
    }

    init{
        val customerId: String = checkNotNull(savedStateHandle[CustomerDetailsDestination.customerIdArg])
        viewModelScope.launch(customerDetailErrorExceptionHandler) {
            customerUiState = storageService.getCustomer(customerId)?.toCustomerUiState() ?: CustomerUiState()
        }
    }


    fun onDeleteButtonClicked() {
        showDialog = true
    }

    fun onConfirmDelete(){
        viewModelScope.launch(customerDetailErrorExceptionHandler){
            storageService.delete(customerUiState.id)
        }
    }
}