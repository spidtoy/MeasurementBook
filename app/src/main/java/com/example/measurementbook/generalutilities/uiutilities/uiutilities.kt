package com.example.measurementbook.generalutilities.uiutilities

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.measurementbook.R

@Composable
fun GeneralTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    actions: @Composable (RowScope.() -> Unit) = {},
    navigateUp: () -> Unit = {}
) = if (canNavigateBack) {
    TopAppBar(title = {TitleText(title)},
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.primary,
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    tint = MaterialTheme.colors.secondary,
                    contentDescription = stringResource(R.string.back_button)
                )
            }
        },
        actions = actions
    )
} else {
    TopAppBar(title = { TitleText(title) }, modifier = modifier)
}

@Composable
fun TitleText(text: String, modifier: Modifier = Modifier){
    Text(text = text,
        style = MaterialTheme.typography.body1,
        fontSize = 16.sp,
        color = MaterialTheme.colors.secondary,
        modifier = modifier
    )
}

class Size {
    @Composable
    fun height(): Int {
        val configuration = LocalConfiguration.current
        return configuration.screenHeightDp
    }
    @Composable
    fun width(): Int {
        val configuration = LocalConfiguration.current
        return configuration.screenWidthDp
    }
}

fun String.isEmailValid() = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
