package com.example.measurementbook.network.services

import com.example.measurementbook.ui.sharedui.Customer
import kotlinx.coroutines.flow.Flow

interface Storage {
    suspend fun getUserCustomers(userId: String): Flow<Result<List<Customer>>>
    suspend fun getCustomer(customerId: String): Customer?
    suspend fun save(customer: Customer)
    suspend fun update(customer: Customer)
    suspend fun delete(customerId: String)
}

sealed class Result<T>(
    val list: T? = null,
    val throwable: Throwable? = null
){
    class Loading<T> : Result<T>()
    class Success<T>(list: T?) : Result<T>(list = list)
    class Failed<T>(throwable: Throwable?) : Result<T>(throwable = throwable)
}