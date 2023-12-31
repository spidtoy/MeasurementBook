package com.example.measurementbook.ui.singleui.settingsscreen

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
import com.example.measurementbook.R
import com.example.measurementbook.generalutilities.uiutilities.GeneralTopAppBar
import com.example.measurementbook.ui.navigation.NavigationDestination

object SettingsScreenDestination : NavigationDestination {
    override val route = "Settings Screen"
    override val titleRes = R.string.settings
}

@Composable
fun SettingsScreen(
    goToChangeEmailScreen: () -> Unit,
    goToChangePasswordScreen: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
){
    Scaffold(
        topBar = {
            GeneralTopAppBar(
                title = stringResource(SettingsScreenDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        }
    ){ padding ->
        SettingsScreenBody(
            goToChangeEmailScreen = goToChangeEmailScreen,
            goToChangePasswordScreen = goToChangePasswordScreen,
            modifier = modifier.padding(padding)
        )
    }
}

@Composable
fun SettingsScreenBody(
    goToChangeEmailScreen: () -> Unit,
    goToChangePasswordScreen: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(modifier = modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ){
        Spacer(Modifier.height(12.dp))
        Actions(
            message = stringResource(R.string.change_email_address),
            buttonText = stringResource(R.string.change_email),
            onButtonClicked = goToChangeEmailScreen
        )
        Spacer(Modifier.height(16.dp))
        Actions(
            message = stringResource(R.string.change_password_message),
            buttonText = stringResource(R.string.change_password),
            onButtonClicked = goToChangePasswordScreen
        )
    }
}

@Composable
fun Actions(
    message: String,
    buttonText: String,
    onButtonClicked: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ){
        Text(
            text = message,
            style = MaterialTheme.typography.h6,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 12.dp)
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = onButtonClicked, contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.secondary
            ),
            modifier = Modifier.padding(start = 12.dp)
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
}