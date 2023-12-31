package com.example.measurementbook.ui.singleui.homescreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.measurementbook.R
import com.example.measurementbook.generalutilities.uiutilities.GeneralTopAppBar
import com.example.measurementbook.ui.navigation.NavigationDestination

object HomeScreenDestination : NavigationDestination {
    override val route = "Home Screen"
    override val titleRes = R.string.home_screen
}

@Composable
fun HomeScreen(
    navigateToSignUpScreen: () -> Unit,
    navigateToSignInScreen: () -> Unit,
    navigateToCustomerList: () -> Unit,
    navigateToVerifyEmail: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            GeneralTopAppBar(title = stringResource(HomeScreenDestination.titleRes),
                canNavigateBack = false)
        },

    ) { innerPadding ->
        if(viewModel.thereIsCurrentUser && !(viewModel.signedOut)){
            ThereIsUserScreen(
                userName = viewModel.userDisplayName,
                isEmailVerified = viewModel.isEmailVerified,
                navigateToCustomerList = navigateToCustomerList,
                navigateToVerifyEmail = navigateToVerifyEmail,
                onSignOutClicked = viewModel::signOut
            )
        } else {
            HomeScreenBody(
                onSignUpClick = {
                    navigateToSignUpScreen()
                    viewModel.resetSignedOut()
                },
                onSignInClick = {
                    navigateToSignInScreen()
                    viewModel.resetSignedOut()
                },
                onTryTheAppButtonClick = {
                    navigateToCustomerList()
                    viewModel.resetSignedOut()
                },
                modifier = modifier.padding(innerPadding),
            )
        }

    }
}

@Composable
fun ThereIsUserScreen(
    userName: String,
    isEmailVerified: Boolean,
    navigateToCustomerList: () -> Unit,
    navigateToVerifyEmail: () -> Unit,
    onSignOutClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = stringResource(R.string.user_with_email_signed_in, userName),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            color = colors.primary,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            if(isEmailVerified) {
                UserOptions(
                    text = stringResource(R.string.go_to_customer_list),
                    onTextClicked = navigateToCustomerList
                )
            } else {
                UserOptions(
                    text = stringResource(R.string.verify_email),
                    onTextClicked = navigateToVerifyEmail
                )
            }
            Spacer(Modifier.width(30.dp))
            UserOptions(
                text = stringResource(R.string.sign_out),
                onTextClicked = onSignOutClicked
            )
        }
    }
}

@Composable
fun UserOptions(
    text: String,
    onTextClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(modifier = modifier
        .height(40.dp)
        .width(145.dp),
        onClick = onTextClicked, contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colors.primary,
            contentColor = colors.secondary
        )
    ) {
        Text(text = text,
            style = MaterialTheme.typography.body1,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp))
    }
}

@Composable
fun HomeScreenBody(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
    onTryTheAppButtonClick: () -> Unit,
    modifier:Modifier = Modifier
){
    Box(modifier = modifier
        .fillMaxSize()
        .background(colors.secondary)){
        Column(modifier = modifier
            .fillMaxSize()
            .align(Alignment.TopCenter)
            .padding(top = 70.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TryTheAppButton(onTryTheAppButtonClick, stringResource(R.string.try_app))
            Spacer(modifier = Modifier.height(30.dp))
            SignInButton(onSignInClick, stringResource(R.string.sign_in))
        }
        SignUp(onSignUpClick = onSignUpClick, modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 70.dp))
        Text(text = stringResource(R.string.creator),
            style = TextStyle(fontSize = 10.sp, color = colors.onSurface),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 4.dp))
    }

}

@Composable
fun SignUp(onSignUpClick: () -> Unit, modifier: Modifier = Modifier){
    Column(modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = stringResource(R.string.signup_message),
            style = MaterialTheme.typography.body1,
            fontSize = 14.sp,
            color = colors.primary,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        SignUpButton(onSignUpClick)
    }
}

@Composable
fun SignUpButton(onSignUpClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(modifier = modifier
        .height(45.dp)
        .width(170.dp),
        border = BorderStroke(1.dp, color = colors.primary),
        onClick = onSignUpClick, contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colors.secondary,
            contentColor = colors.primary
        )
    ) {
        Text(text = stringResource(R.string.sign_up),
            style = MaterialTheme.typography.body1,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 12.dp))
    }
}


@Composable
fun SignInButton(onSignInClick: () -> Unit, text: String, modifier: Modifier = Modifier) {
    Button(modifier = modifier
        .height(45.dp)
        .width(170.dp),
        border = BorderStroke(1.dp, color = colors.primary),
        onClick = onSignInClick, contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colors.secondary,
            contentColor = colors.primary
        )
    ) {
        Text(text = text,
            style = MaterialTheme.typography.body1,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 12.dp))
    }
}

@Composable
fun TryTheAppButton(onTryTheAppButtonClick: () -> Unit, text: String, modifier: Modifier = Modifier){
    Button(modifier = modifier
        .height(45.dp)
        .width(170.dp),
        onClick = onTryTheAppButtonClick, contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colors.primary,
            contentColor = colors.secondary
        )
    ) {
        Text(text = text,
            style = MaterialTheme.typography.body1,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp))
    }
}
