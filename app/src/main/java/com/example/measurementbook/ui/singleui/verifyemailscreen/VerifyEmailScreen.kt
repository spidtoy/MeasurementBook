package com.example.measurementbook.ui.singleui.verifyemailscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.measurementbook.R
import com.example.measurementbook.generalutilities.uiutilities.GeneralTopAppBar
import com.example.measurementbook.ui.navigation.NavigationDestination

object VerifyEmailScreenDestination : NavigationDestination {
    override val route = "Verify Email Screen"
    override val titleRes = R.string.verify_email
}

@Composable
fun VerifyEmailScreen(
    navigateToSignIn: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VerifyEmailViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            GeneralTopAppBar(
                title = stringResource(VerifyEmailScreenDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        }
    ) { padding ->
        if(!viewModel.sendEmailVerificationError) {
            VerifyEmailScreenBody(
                onVerifyEmailClicked = { viewModel.onVerifyEmailClicked(navigateToSignIn) },
                userEmail = viewModel.userEmail ?: "example@email.com",
                modifier = modifier.padding(padding)
            )
        } else {
            EmailVerificationError(onTryAgainClick = viewModel::onTryAgainClick)
        }
    }
}

@Composable
fun VerifyEmailScreenBody(
    onVerifyEmailClicked: () -> Unit,
    userEmail: String,
    modifier: Modifier
) {
    Column(modifier = modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = stringResource(R.string.notification_of_sending_email_verification_link_b, userEmail),
            fontSize = 12.sp,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.primary,
            maxLines = 3,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = onVerifyEmailClicked, contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.secondary
            )
        ) {
            Text(
                text = stringResource(R.string.verify_email),
                style = MaterialTheme.typography.body1,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}

@Composable
fun EmailVerificationError(
    onTryAgainClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.error),
            fontSize = 13.sp,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.error,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(text = stringResource(R.string.error),
            fontSize = 14.sp,
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.error,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(10.dp))
        TextButton(onClick = onTryAgainClick) {
            Text(
                text = stringResource(R.string.try_again),
                fontSize = 13.sp,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.primary,
            )
        }
    }
}