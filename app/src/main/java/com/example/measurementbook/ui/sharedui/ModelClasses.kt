package com.example.measurementbook.ui.sharedui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList


data class Customer(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val measurement: List<Measurement> = listOf(),
    val userId: String = ""
    )

val netErrList = listOf(Customer(name = "netErr", phone = "9999999"))
val viewErrList = listOf(Customer(name = "viewErr", phone = "9999999"))

data class Measurement(
    val name: String = "",
    val value: String = "",
    val unit : String = "in",
    val menuExpandedState: Boolean = false
    )

data class CustomerUiState(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val userId: String = "",
    val measurement: SnapshotStateList<Measurement> = mutableStateListOf(Measurement()),
    val actionEnabled: Boolean = false
)

fun CustomerUiState.isValid() : Boolean {
    return name.isNotBlank()
}


fun Customer.toCustomerUiState(actionEnabled: Boolean = false): CustomerUiState {
    val mutableStateList: SnapshotStateList<Measurement> = mutableStateListOf()
    measurement.forEach { listItem -> mutableStateList.add(listItem) }
        return CustomerUiState(
        id = id,
        name = name,
        phone = phone,
        userId = userId,
        measurement = mutableStateList,
        actionEnabled = actionEnabled
    )
}

fun CustomerUiState.toCustomer(): Customer = Customer(
    id = id,
    name = name,
    phone = phone,
    userId = userId,
    measurement = measurement.toList()
)