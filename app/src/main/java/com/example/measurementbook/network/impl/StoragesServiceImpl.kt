package com.example.measurementbook.network.impl

import com.example.measurementbook.network.services.AccountService
import com.example.measurementbook.network.services.LogService
import com.example.measurementbook.network.services.Storage
import com.example.measurementbook.ui.sharedui.Customer
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import com.example.measurementbook.network.services.Result
import com.example.measurementbook.ui.sharedui.netErrList
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val CUSTOMERS_COLLECTION = "customers"

class StoragesServiceImpl @Inject constructor(
    private val accountService: AccountService,
    private val logService: LogService,
    private val fireStore: FirebaseFirestore
) : Storage {
    private val customersRef: CollectionReference = fireStore.collection(CUSTOMERS_COLLECTION)

    override suspend fun getUserCustomers(userId: String): Flow<Result<List<Customer>>> =
        callbackFlow {
            val snapshotStateListener = customersRef
                .whereEqualTo("userId", userId)
                .orderBy("name")
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        logService.logKnownError(e.message ?: "unknown error")
                        logService.logNonFatalCrash(e)
                        trySend(Result.Failed(e.cause))
                        return@addSnapshotListener
                    }
                    if (value != null) {
                        val list = value.toObjects(Customer::class.java)
                        trySend(Result.Success(list = list))
                    } else {
                        trySend(Result.Success(list = netErrList))
                    }
                }
                awaitClose { snapshotStateListener.remove() }
            }

    override suspend fun getCustomer(customerId: String): Customer? =
        customersRef.document(customerId).get().await().toObject(Customer::class.java)

    override suspend fun save(customer: Customer) {
        val newRef = customersRef.document()
        val id = newRef.id
        val docData: HashMap<String, Any> = hashMapOf(
            "id" to id,
            "name" to customer.name,
            "phone" to customer.phone,
            "userId" to accountService.userId,
        )
        val mapList = customer.measurement.map {
            hashMapOf(
                "name" to it.name,
                "value" to it.value,
                "unit" to it.unit,
                "menuExpandedState" to it.menuExpandedState
            )
        }
        docData["measurement"] = mapList
        customersRef.document(id).set(docData).await()
    }

    override suspend fun update(customer: Customer) {
        customersRef.document(customer.id).set(customer).await()
    }

    override suspend fun delete(customerId: String) {
        customersRef.document(customerId).delete().await()
    }

}
