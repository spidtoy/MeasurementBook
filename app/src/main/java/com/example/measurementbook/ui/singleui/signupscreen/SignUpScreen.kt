package com.example.measurementbook.ui.singleui.signupscreen


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
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
import com.example.measurementbook.generalutilities.uiutilities.Size
import com.example.measurementbook.network.services.SignUpState
import com.example.measurementbook.ui.navigation.NavigationDestination
import com.example.measurementbook.ui.sharedui.OrdinaryTextField
import com.example.measurementbook.ui.sharedui.PasswordTextField

object SignUpScreenDestination : NavigationDestination {
    override val route = "Sign Up Screen"
    override val titleRes = R.string.sign_up
}


@Composable
fun SignUpScreen(
    navigateToVerifyEmail: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpScreenViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            GeneralTopAppBar(
                title = stringResource(SignUpScreenDestination.titleRes),
                canNavigateBack = true,
                navigateUp = {
                    navigateUp()
                    viewModel.resetSignUpState()
                }
            )
        }
        ) { innerPadding ->
            SignUpScreenBody(
                name = viewModel.name,
                emailAddress = viewModel.emailAddress,
                password = viewModel.password,
                onNameValueChange = viewModel::onNameValueChange,
                onEmailValueChange = viewModel::onEmailValueChange,
                onPasswordValueChange = viewModel::updatePassword,
                togglePassword = viewModel::toggleShowPassword,
                showPassword = viewModel.showPassword,
                confirmPassword = viewModel.confirmPassword,
                onConfirmPasswordValueChange = viewModel::updateConfirmPassword,
                navigateToVerifyEmail = {
                    navigateToVerifyEmail()
                    viewModel.resetSignUpState()
                },
                onContinueButtonClicked = viewModel::onContinueButtonClicked,
                passwordNotMatch = viewModel.passwordNotMatch,
                nameError = viewModel.nameError,
                emailError = viewModel.emailError,
                lowPasswordStrength = viewModel.lowPasswordStrength,
                signUpState = viewModel.signUpState.collectAsState().value,
                onTryAgainClick = viewModel::resetSignUpState,
                modifier = modifier.padding(innerPadding)
            )
    }
}

@Composable
fun SignUpScreenBody(
    name: String,
    emailAddress: String,
    onNameValueChange: (String) -> Unit,
    onEmailValueChange: (String) -> Unit,
    password: String,
    onPasswordValueChange: (String) -> Unit,
    togglePassword: () -> Unit,
    showPassword: Boolean,
    confirmPassword: String,
    onConfirmPasswordValueChange: (String) -> Unit,
    onContinueButtonClicked: () -> Unit,
    navigateToVerifyEmail: () -> Unit,
    passwordNotMatch: Boolean,
    nameError: Boolean,
    emailError: Boolean,
    lowPasswordStrength: Boolean,
    signUpState: SignUpState,
    onTryAgainClick: () -> Unit,
    modifier: Modifier = Modifier
    ) {
        Column(modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            EnterText(
                text = name,
                header = stringResource(R.string.user_name),
                placeholder = stringResource(R.string.enter_user_name),
                onTextValueChange = onNameValueChange
            )
            if(nameError) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = stringResource(R.string.name_error),
                    fontSize = 12.sp, style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center, color = colors.error)
            }
            Spacer(modifier = Modifier.height(30.dp))
            EnterText(
                text = emailAddress,
                header = stringResource(R.string.enter_email),
                placeholder = stringResource(R.string.email_example),
                onTextValueChange = onEmailValueChange
            )
            if(emailError) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = stringResource(R.string.email_error),
                    fontSize = 12.sp, style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center, color = colors.error)
            }

            EnterPassword(
                password = password,
                onPasswordValueChange = onPasswordValueChange,
                togglePassword = togglePassword,
                showPassword = showPassword,
                header = stringResource(R.string.enter_password)
            )
            EnterConfirmPassword(
                confirmPassword = confirmPassword,
                onConfirmPasswordValueChange = onConfirmPasswordValueChange,
                togglePassword = togglePassword,
                showPassword = showPassword
            )
            if(passwordNotMatch) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = stringResource(R.string.password_not_match),
                    fontSize = 12.sp, style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center, color = colors.error)
            }
            if(lowPasswordStrength) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = stringResource(R.string.password_not_long),
                    fontSize = 12.sp, style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center, color = colors.error)
            }
            when (signUpState) {
                is SignUpState.NotInitialized -> ContinueButton(onContinueButtonClicked = onContinueButtonClicked)
                is SignUpState.SignUpInProgress -> CircularProgressIndicator(
                    modifier = Modifier.size(40.dp).padding(top = 10.dp),
                    color = colors.secondaryVariant
                )
                is SignUpState.SignUpSuccessful -> LaunchedEffect(Unit) { navigateToVerifyEmail() }
                is SignUpState.Error -> ErrorScreen(signUpState, onTryAgainClick)
            }
        }
}

@Composable
    fun ErrorScreen(signUpState: SignUpState.Error,
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
                color = colors.error,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = signUpState.exception?.message ?: stringResource(R.string.unknown_error),
                textAlign = TextAlign.Center,
                color = colors.error,
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = onTryAgainClick) {
                Text(
                    text = stringResource(R.string.try_again),
                    fontSize = 13.sp,
                    style = MaterialTheme.typography.body1,
                    color = colors.primary,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )
            }
        }
    }


@Composable
fun EnterText(
    text: String,
    header: String,
    placeholder: String,
    onTextValueChange: (String) -> Unit,
    modifier: Modifier = Modifier) {
    Column(modifier = modifier
        .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {
        val size = Size()
        val screenWidth = size.width()
        Text(text = header,
            style = MaterialTheme.typography.body1,
            color = colors.primary,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OrdinaryTextField(text = text,
            onValueChange = onTextValueChange,
            placeholder = placeholder,
            modifier = Modifier
                .height(45.dp)
                .width((screenWidth - 30).dp)
        )
    }
}

@Composable
fun EnterPassword(
        password: String,
        onPasswordValueChange: (String) -> Unit,
        header: String,
        togglePassword: () -> Unit,
        showPassword: Boolean,
        modifier: Modifier = Modifier) {
            Column(modifier = modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = header,
                style = MaterialTheme.typography.body1,
                color = colors.primary,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
                )
                PasswordTextField(
                    password = password,
                    onValueChange = onPasswordValueChange,
                    togglePassword = togglePassword,
                    showPassword = showPassword,
                    placeholder = stringResource(R.string.enter_password)
                )
            }
}

@Composable
fun EnterConfirmPassword(
    confirmPassword: String,
    onConfirmPasswordValueChange: (String) -> Unit,
    togglePassword: () -> Unit,
    showPassword: Boolean,
    modifier: Modifier = Modifier) {
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(top = 30.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = stringResource(R.string.confirm_password),
            style = MaterialTheme.typography.body1,
            color = colors.primary,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        PasswordTextField(
            password = confirmPassword,
            onValueChange = onConfirmPasswordValueChange,
            togglePassword = togglePassword,
            showPassword = showPassword,
            placeholder = stringResource(R.string.confirm_password)
        )
    }
}

@Composable
fun ContinueButton(onContinueButtonClicked: () -> Unit, modifier: Modifier = Modifier) {
    Column {
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = stringResource(R.string.notification_of_sending_email_verification_link),
            fontSize = 12.sp,
            style = MaterialTheme.typography.body1,
            color = colors.primary,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Button(
            modifier = modifier
                .height(40.dp)
                .width(100.dp),
            onClick = onContinueButtonClicked, contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colors.primary,
                contentColor = colors.secondary
            )
        ) {
            Text(
                text = stringResource(R.string.continue_to_verify_email),
                style = MaterialTheme.typography.body1,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}


