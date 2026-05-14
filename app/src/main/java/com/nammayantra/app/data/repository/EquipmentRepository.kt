package com.nammayantra.app.data.repository

import android.content.Context
import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.nammayantra.app.data.model.Equipment
import kotlinx.coroutines.tasks.await

object EquipmentRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("equipment")
    private val storage = FirebaseStorage.getInstance()

    suspend fun listAll(): List<Equipment> {
        return collection
            .whereEqualTo("isAvailable", true)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get().await()
            .documents.mapNotNull { it.toObject(Equipment::class.java) }
    }

    suspend fun listByCategory(category: String): List<Equipment> {
        return collection
            .whereEqualTo("isAvailable", true)
            .whereEqualTo("category", category)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get().await()
            .documents.mapNotNull { it.toObject(Equipment::class.java) }
    }

    suspend fun listByOwner(ownerId: String): List<Equipment> {
        return collection
            .whereEqualTo("ownerId", ownerId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get().await()
            .documents.mapNotNull { it.toObject(Equipment::class.java) }
    }

    suspend fun addListing(equipment: Equipment): Equipment {
        val ref = collection.document()
        val withId = equipment.copy(id = ref.id, createdAt = System.currentTimeMillis())
        ref.set(withId).await()
        return withId
    }

    suspend fun deleteListing(id: String) {
        collection.document(id).delete().await()
    }

    suspend fun uploadImages(context: Context, ownerId: String, uris: List<Uri>): List<String> {
        return uris.mapIndexed { index, uri ->
            val path = "listings/$ownerId/${System.currentTimeMillis()}_$index.jpg"
            val ref = storage.reference.child(path)
            val stream = context.contentResolver.openInputStream(uri)!!
            val bytes = stream.readBytes()
            stream.close()
            ref.putBytes(bytes).await()
            ref.downloadUrl.await().toString()
        }
    }
}
