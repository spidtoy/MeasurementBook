package com.example.measurementbook.network.impl


import com.example.measurementbook.network.services.*
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
) : AccountService
{
    override var signUpState: MutableStateFlow<SignUpState> =
        MutableStateFlow(SignUpState.NotInitialized)

    override var signInState: MutableStateFlow<SignInState> =
        MutableStateFlow(SignInState.NotInitialized)

    override var changeEmailState: MutableStateFlow<ChangeEmailState> =
        MutableStateFlow(ChangeEmailState.NotInitialized)

    override var changePasswordState: MutableStateFlow<ChangePasswordState> =
        MutableStateFlow(ChangePasswordState.NotInitialized)

    override var resetPasswordState: MutableStateFlow<ResetPasswordState> =
        MutableStateFlow(ResetPasswordState.NotInitialized)

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override val userEmail: String?
        get() = currentUser?.email

    override val userId: String
        get() = currentUser?.uid ?: "guest"

    override val userDisplayName: String?
        get() = currentUser?.displayName

    override val isEmailVerified: Boolean?
        get() = currentUser?.isEmailVerified

    override fun sendVerificationEmail() {
        currentUser?.sendEmailVerification()
    }

    override fun resetSignUpState() {
        signUpState.value = SignUpState.NotInitialized
    }

    override fun resetSignInState() {
        signInState.value = SignInState.NotInitialized
    }

    override fun resetChangeEmailState(){
        changeEmailState.value = ChangeEmailState.NotInitialized
    }

    override fun resetChangePasswordState() {
        changePasswordState.value = ChangePasswordState.NotInitialized
    }

    override fun resetResetPasswordState() {
        resetPasswordState.value = ResetPasswordState.NotInitialized
    }

    override suspend fun getUserId(): String {
        return currentUser?.uid.orEmpty()
    }

    override fun isThereCurrentUser(): Boolean {
        return currentUser != null
    }

    private val signUpAuthStateListener = FirebaseAuth.AuthStateListener { auth ->
        val firebaseUser = auth.currentUser
        if(firebaseUser != null) {
            signUpState.value = SignUpState.SignUpSuccessful
            removeSignUpListener()
        }
    }

    private fun removeSignUpListener() {
        auth.removeAuthStateListener(signUpAuthStateListener)
    }

    override suspend fun createUser(email: String, password: String) {
        signUpState.value = SignUpState.SignUpInProgress
        auth.addAuthStateListener(signUpAuthStateListener)
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun setDisplayName(name: String) {
        val profileUpdates = userProfileChangeRequest{
            displayName = name
        }
        currentUser!!.updateProfile(profileUpdates).await()
    }

    override suspend fun signIn(email: String, password: String) {
        signInState.value = SignInState.SignInInProgress
        // auth.addAuthStateListener(signInAuthStateListener)
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun updateEmail(email: String){
        changeEmailState.value = ChangeEmailState.ChangeEmailInInProgress
        currentUser!!.updateEmail(email).await()
    }

    override suspend fun reAuthenticate(password: String) {
        changePasswordState.value = ChangePasswordState.ChangePasswordInInProgress
        val credential = EmailAuthProvider.getCredential(userEmail!!, password)
        currentUser!!.reauthenticate(credential).await()
    }

    override suspend fun updatePassword(password: String) {
        changePasswordState.value = ChangePasswordState.ChangePasswordInInProgress
        currentUser!!.updatePassword(password).await()
    }

    override suspend fun sendPasswordResetEmail(email: String) {
        resetPasswordState.value = ResetPasswordState.InProgress
        auth.sendPasswordResetEmail(email).await()
    }

    override fun signOut() {
        auth.signOut()
    }

}