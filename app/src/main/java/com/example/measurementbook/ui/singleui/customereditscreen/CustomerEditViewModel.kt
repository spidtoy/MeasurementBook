package com.example.measurementbook.ui.singleui.customereditscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.measurementbook.network.services.LogService
import com.example.measurementbook.network.services.Storage
import com.example.measurementbook.ui.sharedui.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val logService: LogService,
    private val storageService: Storage
) : ViewModel() {

    var customerUiState by mutableStateOf(CustomerUiState())

    var nameEmpty by mutableStateOf(false)

    val menuItems = listOf("in", "cm", "mm", "m")

    private val customerEditErrorExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logService.logNonFatalCrash(throwable)
    }

    init {
        val customerId: String = checkNotNull(savedStateHandle[CustomerEditsDestination.customerIdArg])

        viewModelScope.launch(customerEditErrorExceptionHandler){
            customerUiState = storageService.getCustomer(customerId)?.toCustomerUiState() ?: CustomerUiState()
        }
    }

    fun resetNameEmpty(){
        nameEmpty = false
    }

    fun updateCustomerUiState(newCustomerUiState: CustomerUiState) {
        if(nameEmpty){nameEmpty = false}
        customerUiState = newCustomerUiState.copy(actionEnabled = newCustomerUiState.isValid())
    }

    fun addAnotherMeasurement() {
        customerUiState.measurement.add(Measurement())
    }

    fun removeMeasurement(element: Measurement) {
        customerUiState.measurement.remove(element)
    }

    fun onSaveClick(gotoList: () -> Unit){
        if(!customerUiState.actionEnabled){
            nameEmpty = true
        } else {
            viewModelScope.launch(customerEditErrorExceptionHandler){
                storageService.update(customerUiState.toCustomer())
                gotoList()
            }
        }
    }
}