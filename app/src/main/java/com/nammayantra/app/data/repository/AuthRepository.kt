package com.nammayantra.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.nammayantra.app.data.model.UserProfile
import kotlinx.coroutines.tasks.await

object AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun signIn(email: String, password: String): UserProfile {
        auth.signInWithEmailAndPassword(email, password).await()
        val uid = auth.currentUser?.uid ?: error("No user after sign in")
        return db.collection("profiles").document(uid).get().await()
            .toObject(UserProfile::class.java)?.copy(id = uid)
            ?: error("Profile not found")
    }

    suspend fun signUp(email: String, password: String, name: String, role: String): UserProfile {
        auth.createUserWithEmailAndPassword(email, password).await()
        val uid = auth.currentUser?.uid ?: error("No user after sign up")
        val profile = UserProfile(id = uid, name = name, role = role)
        db.collection("profiles").document(uid).set(profile).await()
        return profile
    }

    suspend fun signInWithGoogle(idToken: String): UserProfile {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await()
        val uid = auth.currentUser?.uid ?: error("No user after Google sign in")
        val displayName = auth.currentUser?.displayName ?: "User"
        val docRef = db.collection("profiles").document(uid)
        val existing = docRef.get().await().toObject(UserProfile::class.java)
        if (existing == null) {
            val profile = UserProfile(id = uid, name = displayName, role = "Farmer")
            docRef.set(profile).await()
            return profile
        }
        return existing.copy(id = uid)
    }

    suspend fun signOut() {
        auth.signOut()
    }

    suspend fun currentProfile(): UserProfile? {
        val uid = auth.currentUser?.uid ?: return null
        return try {
            db.collection("profiles").document(uid).get().await()
                .toObject(UserProfile::class.java)?.copy(id = uid)
        } catch (e: Exception) {
            null
        }
    }
}
