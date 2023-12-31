package com.example.measurementbook.ui.singleui.changepasswordscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.measurementbook.R
import com.example.measurementbook.generalutilities.uiutilities.GeneralTopAppBar
import com.example.measurementbook.network.services.ChangePasswordState
import com.example.measurementbook.ui.navigation.NavigationDestination
import com.example.measurementbook.ui.singleui.changeemailscreen.ChangeButton
import com.example.measurementbook.ui.singleui.changeemailscreen.ProceedMessage
import com.example.measurementbook.ui.singleui.signupscreen.EnterConfirmPassword
import com.example.measurementbook.ui.singleui.signupscreen.EnterPassword
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

object ChangePasswordScreenDestination : NavigationDestination {
    override val route = "Change Password Screen"
    override val titleRes = R.string.change_password
}

@Composable
fun ChangePasswordScreen(
    navigateUp: () -> Unit,
    goToSignInScreen: () -> Unit,
    viewModel: ChangePasswordScreenViewModel = hiltViewModel()
){
    Scaffold(
        topBar = {
            GeneralTopAppBar(
                title = stringResource(ChangePasswordScreenDestination.titleRes),
                canNavigateBack = true,
                navigateUp = {
                    navigateUp()
                    viewModel.onTryAgainClick()
                }
            )
        }
    ){ padding ->
        ChangePasswordScreenBody(
            oldPassword = viewModel.oldPassword,
            newPassword = viewModel.newPassword,
            confirmPassword = viewModel.confirmPassword,
            onOldPasswordChange = viewModel::onOldPasswordChange,
            onNewPasswordChange = viewModel::onNewPasswordChange,
            onConfirmPasswordValueChange = viewModel::onConfirmPasswordValueChange,
            lowOldPasswordStrength = viewModel.lowOldPasswordStrength,
            lowNewPasswordStrength = viewModel.lowNewPasswordStrength,
            togglePassword = viewModel::togglePassword,
            showPassword = viewModel.showPassword,
            passwordNotMatch = viewModel.passwordNotMatch,
            changePasswordState = viewModel.changePasswordState.collectAsState().value,
            onChangePasswordButtonClicked = viewModel::onChangePasswordButtonClicked,
            goToSignInScreen = {
                goToSignInScreen()
                viewModel.onTryAgainClick()
                               },
            onTryAgainClick = viewModel::onTryAgainClick,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun ChangePasswordScreenBody(
    oldPassword: String,
    newPassword: String,
    confirmPassword: String,
    onOldPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordValueChange: (String) -> Unit,
    lowOldPasswordStrength: Boolean,
    lowNewPasswordStrength: Boolean,
    togglePassword: () -> Unit,
    showPassword: Boolean,
    passwordNotMatch: Boolean,
    changePasswordState: ChangePasswordState,
    onChangePasswordButtonClicked: () -> Unit,
    goToSignInScreen: () -> Unit,
    onTryAgainClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EnterPassword(
            password = oldPassword,
            onPasswordValueChange = onOldPasswordChange,
            togglePassword = togglePassword,
            showPassword = showPassword,
            header = stringResource(R.string.enter_old_password)
        )
        if (lowOldPasswordStrength) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(R.string.password_not_long),
                fontSize = 12.sp, style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center, color = MaterialTheme.colors.error
            )
        }
        Spacer(Modifier.height(12.dp))
        EnterPassword(
            password = newPassword,
            onPasswordValueChange = onNewPasswordChange,
            togglePassword = togglePassword,
            showPassword = showPassword,
            header = stringResource(R.string.enter_password)
        )
        if (lowNewPasswordStrength) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(R.string.password_not_long),
                fontSize = 12.sp, style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center, color = MaterialTheme.colors.error
            )
        }
        Spacer(Modifier.height(12.dp))
        EnterConfirmPassword(
            confirmPassword = confirmPassword,
            onConfirmPasswordValueChange = onConfirmPasswordValueChange,
            togglePassword = togglePassword,
            showPassword = showPassword
        )
        if (passwordNotMatch) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(R.string.password_not_match),
                fontSize = 12.sp, style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center, color = MaterialTheme.colors.error
            )
        }
        Spacer(Modifier.height(14.dp))
        when (changePasswordState) {
            is ChangePasswordState.NotInitialized -> Column() {
                ChangeButton(
                    onButtonClicked = onChangePasswordButtonClicked,
                    buttonText = stringResource(R.string.change_password)
                )
            }
            is ChangePasswordState.ChangePasswordInInProgress -> CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                color = MaterialTheme.colors.secondaryVariant
            )
            is ChangePasswordState.ChangePasswordSuccess -> ProceedMessage(
                displayText = stringResource(R.string.password_changed_successfully),
                buttonText = stringResource(R.string.sign_in),
                onButtonClicked = goToSignInScreen
            )
            is ChangePasswordState.Error -> ErrorScreen(
                changePasswordState = changePasswordState,
                onTryAgainClick = onTryAgainClick
            )
        }
    }
}

@Composable
fun ErrorScreen(changePasswordState: ChangePasswordState.Error,
                onTryAgainClick: () -> Unit,
                modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_error_24),
            contentDescription = stringResource(R.string.error),
            alignment = Alignment.Center,
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = stringResource(R.string.operation_failed),
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.h6
        )
        Spacer(modifier = Modifier.height(12.dp))
        if(changePasswordState.exception is FirebaseAuthInvalidCredentialsException){
            Text(text = stringResource(R.string.invalid_old_password),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.body1
            )
        } else {
            Text(text = changePasswordState.exception?.message ?: stringResource(R.string.unknown_error),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.body1
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onTryAgainClick) {
            Text(
                text = stringResource(R.string.try_again),
                fontSize = 13.sp,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
        }
    }
}