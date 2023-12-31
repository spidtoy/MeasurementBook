package com.example.measurementbook.ui.singleui.forgotpassword

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
import com.example.measurementbook.network.services.ResetPasswordState
import com.example.measurementbook.ui.navigation.NavigationDestination
import com.example.measurementbook.ui.singleui.changeemailscreen.ChangeButton
import com.example.measurementbook.ui.singleui.changeemailscreen.ProceedMessage
import com.example.measurementbook.ui.singleui.signupscreen.EnterText

object ForgotPasswordScreenDestination : NavigationDestination {
    override val route = "Reset Password Screen"
    override val titleRes = R.string.reset_password
}

@Composable
fun ForgotPasswordScreen(
    navigateUp: () -> Unit,
    goToSignInScreen: () -> Unit,
    viewModel: ForgotPasswordViewModel = hiltViewModel()
){
    Scaffold(
        topBar = {
            GeneralTopAppBar(
                title = stringResource(ForgotPasswordScreenDestination.titleRes),
                canNavigateBack = true,
                navigateUp = {
                    navigateUp()
                    viewModel.onTryAgainClick()
                }
            )
        }
    ){ paddingValues ->
        ForgotPasswordScreenBody(
            email = viewModel.email,
            onEmailValueChange = viewModel::onEmailValueChange,
            onButtonClicked = viewModel::onResetPasswordButtonClicked,
            resetPasswordState = viewModel.resetPasswordState.collectAsState().value,
            goToSignInScreen = {
                goToSignInScreen()
                viewModel.onTryAgainClick()
            },
            emailError = viewModel.emailError,
            onTryAgainClick = viewModel::onTryAgainClick,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun ForgotPasswordScreenBody(
    email: String,
    onEmailValueChange: (String) -> Unit,
    onButtonClicked: () -> Unit,
    emailError: Boolean,
    resetPasswordState: ResetPasswordState,
    goToSignInScreen: () -> Unit,
    onTryAgainClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        EnterText(
            text = email,
            header = stringResource(R.string.enter_email),
            placeholder = stringResource(R.string.email_example),
            onTextValueChange = onEmailValueChange
        )
        if(emailError){
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = stringResource(R.string.email_error),
                fontSize = 12.sp, style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center, color = MaterialTheme.colors.error)
        }
        Spacer(Modifier.height(12.dp))
        when(resetPasswordState) {
            is ResetPasswordState.NotInitialized ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.send_reset_password_msg),
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary
                    )
                    Spacer(Modifier.height(16.dp))
                    ChangeButton(
                        onButtonClicked = onButtonClicked,
                        buttonText = stringResource(R.string.reset_password)
                    )
                }
            is ResetPasswordState.InProgress ->
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = MaterialTheme.colors.secondaryVariant
                )
            is ResetPasswordState.Success ->
                ProceedMessage(
                    displayText = stringResource(R.string.password_reset_sent),
                    buttonText = stringResource(R.string.sign_in),
                    onButtonClicked = goToSignInScreen
                )
            is ResetPasswordState.Error -> ErrorScreen(
                resetPasswordState = resetPasswordState,
                onTryAgainClick = onTryAgainClick
            )
        }

    }
}

@Composable
fun ErrorScreen(
    resetPasswordState: ResetPasswordState.Error,
    onTryAgainClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
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
        Text(text = resetPasswordState.exception?.message ?: stringResource(R.string.unknown_error),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.body1
        )
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