package com.example.measurementbook.network.services


import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow

sealed class SignUpState {
    object NotInitialized : SignUpState()
    object SignUpInProgress : SignUpState()
    object SignUpSuccessful : SignUpState()
    class Error(val exception: Throwable?) : SignUpState()
}

sealed class SignInState {
    object NotInitialized : SignInState()
    object SignInInProgress : SignInState()
    object SignInSuccessfulToList : SignInState()
    object SignInSuccessfulToVerify: SignInState()
    class Error(val exception: Throwable?) : SignInState()
}

sealed class ChangeEmailState{
    object NotInitialized : ChangeEmailState()
    object ChangeEmailInInProgress : ChangeEmailState()
    object ChangeEmailSuccess : ChangeEmailState()
    class Error(val exception: Throwable?) : ChangeEmailState()
}

sealed class ChangePasswordState{
    object NotInitialized : ChangePasswordState()
    object ChangePasswordInInProgress : ChangePasswordState()
    object ChangePasswordSuccess : ChangePasswordState()
    class Error(val exception: Throwable?) : ChangePasswordState()
}

sealed class ResetPasswordState {
    object NotInitialized : ResetPasswordState()
    object InProgress : ResetPasswordState()
    object Success : ResetPasswordState()
    class Error(val exception: Throwable?) : ResetPasswordState()
}

interface AccountService {
    val signUpState: MutableStateFlow<SignUpState>

    val signInState: MutableStateFlow<SignInState>

    val changeEmailState: MutableStateFlow<ChangeEmailState>

    val changePasswordState: MutableStateFlow<ChangePasswordState>

    val resetPasswordState: MutableStateFlow<ResetPasswordState>

    val currentUser: FirebaseUser?

    val userEmail: String?

    val userId: String

    val userDisplayName: String?

    val isEmailVerified: Boolean?

    fun resetSignUpState()

    fun resetSignInState()

    fun resetChangeEmailState()

    fun resetChangePasswordState()

    fun resetResetPasswordState()

    fun sendVerificationEmail()

    suspend fun getUserId(): String

    fun isThereCurrentUser(): Boolean

    suspend fun setDisplayName(name: String)

    suspend fun createUser(email: String, password: String)

    suspend fun updateEmail(email: String)

    suspend fun updatePassword(password: String)

    suspend fun signIn(email: String, password: String)

    suspend fun reAuthenticate(password: String)

    suspend fun sendPasswordResetEmail(email: String)

    fun signOut()

}