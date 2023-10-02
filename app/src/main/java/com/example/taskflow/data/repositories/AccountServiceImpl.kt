package com.example.taskflow.data.repositories

import android.util.Log
import android.widget.Toast
import com.example.taskflow.domain.entities.UserEntity
import com.example.taskflow.domain.repositories.AccountService
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AccountServiceImpl @Inject constructor(private val auth: FirebaseAuth): AccountService {
    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val hasUser: Boolean
        get() = auth.currentUser != null

    override val currentUser: FirebaseUser?
        get() = auth.currentUser
    override fun getUserInfo(){

    if (hasUser) {
        auth.currentUser?.providerData?.forEach{profile->
            Log.i("kpop","Sign-in provider: " + profile.providerId);
            Log.i("kpop","  Provider-specific UID: " + profile.uid);
            Log.i("kpop","  Name: " + profile.displayName);
            Log.i("kpop","  Email: " + profile.email);
            Log.i("kpop","  Photo URL: " + profile.photoUrl);
        }
    }
    }

    override val currentUserEntity: Flow<UserEntity>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let { UserEntity(
                        id = it.uid, isAnonymous =  it.isAnonymous, name = it.displayName,
                        email = it.email, photoUrl = it.photoUrl, providerData = it.providerData) } ?: UserEntity())
                }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    override suspend fun authenticate(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun sendRecoveryEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    override suspend fun createAnonymousAccount() {
        auth.signInAnonymously().await()
    }

    override suspend fun createAccountWithEmailAndPassword(email: String, password: String) {
        val credential = auth.createUserWithEmailAndPassword(email, password).await().credential
        if (credential != null) {
            auth.currentUser!!.linkWithCredential(credential).await()
        }
        auth.currentUser!!.sendEmailVerification()

    }
    override suspend fun createAccountWithGoogle(tokenId: String){
        val firebaseCredential = GoogleAuthProvider.getCredential(tokenId, null)
        auth.signInWithCredential(firebaseCredential).await()

    }
    override suspend fun updateProfile(userEntity: UserEntity){
        if (userEntity.name!=currentUser?.displayName || userEntity.photoUrl!=currentUser?.photoUrl) {
            val profileUpdates = userProfileChangeRequest {
                if (userEntity.name!=currentUser?.displayName) {
                    displayName = userEntity.name
                } else if (userEntity.photoUrl!=currentUser?.photoUrl) {
                    photoUri = userEntity.photoUrl
                }
            }
            auth.currentUser!!.updateProfile(profileUpdates).await()
        }
        if (userEntity.email!=null && userEntity.email!=currentUser?.email){
            auth.currentUser!!.updateEmail(userEntity.email).await()
        }
    }
//    todo remove?
    override suspend fun linkAccount(email: String, password: String) {
        val credential = EmailAuthProvider.getCredential(email, password)
        auth.currentUser!!.linkWithCredential(credential).await()
    }

    override suspend fun deleteAccount() {
        auth.currentUser!!.delete().await()
    }

    override suspend fun signOut() {
        if (auth.currentUser!!.isAnonymous) {
            auth.currentUser!!.delete()
        }
        auth.signOut()
    }

//    todo remove
    companion object {
        private const val LINK_ACCOUNT_TRACE = "linkAccount"
    }
}