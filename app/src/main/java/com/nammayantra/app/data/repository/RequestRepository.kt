package com.nammayantra.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.nammayantra.app.data.model.RentalRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

object RequestRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("rental_requests")

    suspend fun submit(request: RentalRequest) {
        val ref = collection.document()
        ref.set(request.copy(id = ref.id)).await()
    }

    suspend fun fetchForFarmer(farmerId: String): List<RentalRequest> {
        return collection.whereEqualTo("farmerId", farmerId).get().await()
            .documents.mapNotNull { it.toObject(RentalRequest::class.java) }
    }

    suspend fun fetchForOwner(ownerId: String): List<RentalRequest> {
        return collection.whereEqualTo("ownerId", ownerId).get().await()
            .documents.mapNotNull { it.toObject(RentalRequest::class.java) }
    }

    suspend fun updateStatus(id: String, status: String) {
        collection.document(id).update("status", status).await()
    }

    fun observeRequests(userId: String, isOwner: Boolean): Flow<List<RentalRequest>> = callbackFlow {
        val query = if (isOwner) {
            collection.whereEqualTo("ownerId", userId)
        } else {
            collection.whereEqualTo("farmerId", userId)
        }

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) return@addSnapshotListener
            val list = snapshot.documents.mapNotNull { it.toObject(RentalRequest::class.java) }
            trySend(list)
        }

        awaitClose { listener.remove() }
    }
}
