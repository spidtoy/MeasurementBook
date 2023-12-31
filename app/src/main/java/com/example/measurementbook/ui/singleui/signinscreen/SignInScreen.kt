package com.example.measurementbook.ui.singleui.signinscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.measurementbook.network.services.SignInState
import com.example.measurementbook.ui.navigation.NavigationDestination
import com.example.measurementbook.ui.singleui.homescreen.SignUpButton
import com.example.measurementbook.ui.singleui.signupscreen.EnterPassword
import com.example.measurementbook.ui.singleui.signupscreen.EnterText

object SignInScreenDestination : NavigationDestination {
    override val route = "Sign In Screen"
    override val titleRes = R.string.sign_in
}

@Composable
fun SignInScreen(
    navigateCustomerList: () -> Unit,
    navigateToVerifyEmail: () -> Unit,
    navigateToForgotPassword: () -> Unit,
    onSignUpClick: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignInScreenViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            GeneralTopAppBar(
                title = stringResource(SignInScreenDestination.titleRes),
                canNavigateBack = true,
                navigateUp = {
                    navigateUp()
                    viewModel.resetSignInState()
                }
            )
        },
        ) { innerPadding ->
        SignInScreenBody (
            emailAddress = viewModel.emailAddress,
            password = viewModel.password,
            showErrorLoginInputs = viewModel.showErrorLoginInputs,
            onEmailValueChange = viewModel::updateEmailAddress,
            onPasswordValueChange = viewModel::updatePassword,
            togglePassword = viewModel::toggleShowPassword,
            showPassword = viewModel.showPassword,
            signInState = viewModel.signInState.collectAsState().value,
            navigateCustomerList = {
                navigateCustomerList()
                viewModel.resetSignInState()
            },
            navigateToVerifyEmail = {
                navigateToVerifyEmail()
                viewModel.resetSignInState()
            },
            onSignInClick = viewModel::signIn,
            onSignUpClick = onSignUpClick,
            onTryAgainClick = viewModel::resetSignInState,
            onForgotPasswordClick = navigateToForgotPassword,
            modifier = modifier.padding(innerPadding)
        )
    }
}


@Composable
fun SignInScreenBody(
    emailAddress: String,
    password: String,
    showErrorLoginInputs: Boolean,
    onEmailValueChange: (String) -> Unit,
    onPasswordValueChange: (String) -> Unit,
    togglePassword: () -> Unit,
    showPassword: Boolean,
    signInState: SignInState,
    navigateCustomerList: () -> Unit,
    navigateToVerifyEmail: () -> Unit,
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onTryAgainClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Spacer(Modifier.height(16.dp))
        EnterText(
            text = emailAddress,
            header = stringResource(R.string.enter_email),
            placeholder = stringResource(R.string.email_example),
            onTextValueChange = onEmailValueChange
        )
        EnterPassword(
            password = password,
            onPasswordValueChange = onPasswordValueChange,
            togglePassword = togglePassword,
            showPassword = showPassword,
            header = stringResource(R.string.enter_password)
        )
        Spacer(Modifier.height(20.dp))
        when(signInState) {
            is SignInState.NotInitialized ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Button(modifier = modifier
                        .height(40.dp),
                        onClick = onSignInClick,
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = MaterialTheme.colors.secondary
                        )
                    ) {
                        Text(text = stringResource(R.string.sign_in),
                            style = MaterialTheme.typography.body1,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 12.dp))
                    }
                    Spacer(modifier.height(12.dp))
                    TextButton(onClick = onForgotPasswordClick) {
                        Text(
                            text = stringResource(R.string.forgot_password),
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
            is SignInState.SignInInProgress ->
                CircularProgressIndicator(
                    modifier = Modifier.size(30.dp).padding(top = 12.dp),
                    color = MaterialTheme.colors.secondaryVariant
                )
            is SignInState.SignInSuccessfulToList -> LaunchedEffect(Unit) { navigateCustomerList() }
            is SignInState.SignInSuccessfulToVerify -> LaunchedEffect(Unit) { navigateToVerifyEmail() }
            is SignInState.Error -> ErrorScreen(signInState, onTryAgainClick)
        }
        if(showErrorLoginInputs){ ErrorLoginInputs() }
        Spacer(Modifier.height(60.dp))
        Text(text = stringResource(R.string.signup_message),
            style = MaterialTheme.typography.body1,
            fontSize = 14.sp,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        SignUpButton(onSignUpClick)
    }

}

@Composable
    fun ErrorLoginInputs(modifier: Modifier = Modifier) {
        Column {
            Spacer(modifier.height(10.dp))
            Text(
                text = stringResource(R.string.invalid_number_or_password),
                fontSize = 12.sp, style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center, color = MaterialTheme.colors.error
            )
        }
    }

@Composable
fun ErrorScreen(signInState: SignInState.Error,
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
        Text(text = signInState.exception?.message ?: stringResource(R.string.unknown_error),
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