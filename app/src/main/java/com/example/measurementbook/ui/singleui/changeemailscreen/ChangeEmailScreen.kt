package com.example.measurementbook.ui.singleui.changeemailscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
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
import com.example.measurementbook.network.services.ChangeEmailState
import com.example.measurementbook.ui.navigation.NavigationDestination
import com.example.measurementbook.ui.singleui.signupscreen.EnterText

object ChangeEmailScreenDestination : NavigationDestination {
    override val route = "Change Email Screen"
    override val titleRes = R.string.change_email
}

@Composable
fun ChangeEmailScreen(
    navigateUp: () -> Unit,
    goToSignInScreen: () -> Unit,
    viewModel: ChangeEmailScreenViewModel = hiltViewModel()
){
    Scaffold(
        topBar = {
            GeneralTopAppBar(
                title = stringResource(ChangeEmailScreenDestination.titleRes),
                canNavigateBack = true,
                navigateUp = {
                    navigateUp()
                    viewModel.onTryAgainClick()
                }
            )
        }
    ) { paddingValues ->
        ChangeEmailScreenBody(
            oldEmail = viewModel.oldEmail,
            oldHeader = stringResource(R.string.enter_old_email),
            newEmail = viewModel.newEmail,
            newHeader = stringResource(R.string.enter_new_email),
            placeholder = stringResource(R.string.email_example),
            oldEmailError = viewModel.oldEmailError,
            newEmailError = viewModel.newEmailError,
            displayNotUserEmail = viewModel.displayNotUserEmail,
            changeEmailState = viewModel.changeEmailState.collectAsState().value,
            onOldEmailChange = viewModel::onOldEmailChange,
            onNewEmailChange = viewModel::onNewEmailChange,
            onChangeEmailButtonClicked = viewModel::onChangeEmailButtonClicked,
            goToSignInScreen = {
                goToSignInScreen()
                viewModel.onTryAgainClick()
            },
            onTryAgainClick = viewModel::onTryAgainClick,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun ChangeEmailScreenBody(
    oldEmail: String,
    oldHeader: String,
    newEmail: String,
    newHeader: String,
    placeholder: String,
    oldEmailError: Boolean,
    newEmailError: Boolean,
    displayNotUserEmail: Boolean,
    changeEmailState: ChangeEmailState,
    onOldEmailChange: (String) -> Unit,
    onNewEmailChange: (String) -> Unit,
    onChangeEmailButtonClicked: () -> Unit,
    goToSignInScreen: () -> Unit,
    onTryAgainClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(modifier = modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        EnterText(
            text = oldEmail,
            header = oldHeader,
            placeholder = placeholder,
            onTextValueChange = onOldEmailChange
        )
        if(oldEmailError){
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = stringResource(R.string.email_error),
                fontSize = 12.sp, style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center, color = colors.error)
        }
        Spacer(Modifier.height(12.dp))
        EnterText(
            text = newEmail,
            header = newHeader,
            placeholder = placeholder,
            onTextValueChange = onNewEmailChange
        )
        if(newEmailError){
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = stringResource(R.string.email_error),
                fontSize = 12.sp, style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center, color = colors.error)
        }
        Spacer(Modifier.height(14.dp))
        when(changeEmailState){
            is ChangeEmailState.NotInitialized -> Column(){
                ChangeButton(
                    onButtonClicked = onChangeEmailButtonClicked,
                    buttonText = stringResource(R.string.change_email)
                )
                if(displayNotUserEmail) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = stringResource(R.string.not_user_email),
                        fontSize = 12.sp, style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center, color = colors.error)
                }
            }
            is ChangeEmailState.ChangeEmailInInProgress -> CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                color = colors.secondaryVariant
            )
            is ChangeEmailState.ChangeEmailSuccess -> ProceedMessage(
                displayText = stringResource(R.string.email_changed_successfully),
                buttonText = stringResource(R.string.sign_in),
                onButtonClicked = goToSignInScreen
            )
            is ChangeEmailState.Error -> ErrorScreen(
                changeEmailState = changeEmailState,
                onTryAgainClick = onTryAgainClick
            )
        }
    }
}

@Composable
fun ChangeButton(
    onButtonClicked: () -> Unit,
    buttonText: String
) {
    Button(
        onClick = onButtonClicked,
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colors.primary,
            contentColor = colors.secondary
        )
    ) {
        Text(
            text = buttonText,
            style = MaterialTheme.typography.body1,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
fun ProceedMessage(
    displayText: String,
    buttonText: String,
    onButtonClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(
            text = displayText,
            style = MaterialTheme.typography.h6,
            fontSize = 14.sp,
            color = colors.primary
        )
        Spacer(Modifier.width(12.dp))
        ChangeButton(
            onButtonClicked = onButtonClicked,
            buttonText = buttonText
        )
    }
}

@Composable
fun ErrorScreen(
    changeEmailState: ChangeEmailState.Error,
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
            color = colors.error,
            style = MaterialTheme.typography.h6
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = changeEmailState.exception?.message ?: stringResource(R.string.unknown_error),
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