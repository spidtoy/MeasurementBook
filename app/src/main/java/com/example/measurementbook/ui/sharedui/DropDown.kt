package com.example.measurementbook.ui.sharedui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.measurementbook.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ComposeMenu(
    menuItems: List<String>,
    onDropdownClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onDropdownMenuItemClick: (String) -> Unit,
    element: Measurement,
    modifier: Modifier = Modifier
) {
    Box(contentAlignment = Alignment.Center,
        modifier = modifier
            .height(45.dp)
            .fillMaxWidth()
            .border(2.dp, MaterialTheme.colors.primary, shape = RoundedCornerShape(20.dp))
            .clickable(
                onClick = onDropdownClick,
            ),
        ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = element.unit,
                modifier = Modifier.weight(1f),
                textAlign =  TextAlign.Center,
                fontSize = 12.sp,
                color = MaterialTheme.colors.primary
            )
            Icon(
                painter = painterResource(id = R.drawable.outline_keyboard_arrow_down_24),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp, 16.dp).weight(1f),
                tint = MaterialTheme.colors.primary
            )
            DropdownMenu(
                expanded = element.menuExpandedState,
                onDismissRequest = onDismissRequest,
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
            ) {
                menuItems.forEachIndexed { index, title ->
                    DropdownMenuItem(onClick = { onDropdownMenuItemClick(menuItems[index]) }) {
                        Text(text = title,
                            fontSize = 12.sp,
                            color = MaterialTheme.colors.primary)
                    }
                }
            }
        }
    }
}

@Composable
fun TopBarMenu(
    displayDropdownMenu: Boolean,
    onDismissRequest: () -> Unit,
    onMenuIconClicked: () -> Unit,
    gotoSettingsScreen: () -> Unit,
    signOutAndGoHomeScreen: () -> Unit,
){
        IconButton( onClick = onMenuIconClicked) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = MaterialTheme.colors.secondary
            )
        }
        DropdownMenu(
            expanded = displayDropdownMenu,
            onDismissRequest = onDismissRequest
        ){
            DropdownMenuItem(onClick = gotoSettingsScreen) {
                Text(text = stringResource(R.string.settings))
            }
            DropdownMenuItem(onClick = signOutAndGoHomeScreen) {
                Text(text = stringResource(R.string.sign_out))
            }
        }
}

