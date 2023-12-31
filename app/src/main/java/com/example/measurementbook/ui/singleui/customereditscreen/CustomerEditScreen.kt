package com.example.measurementbook.ui.singleui.customereditscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import com.example.measurementbook.ui.singleui.customerlistscreen.DataForm

object CustomerEditsDestination : NavigationDestination {
    override val route = "Customer Edit"
    override val titleRes = R.string.edit_customer
    const val customerIdArg = "customerId"
    val routeWithArgs = "$route/{$customerIdArg}"
}

@Composable
fun CustomerEditScreen (
    gotoList: () -> Unit,
    onBackPressed: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CustomerEditViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            GeneralTopAppBar(
                title = stringResource(CustomerEditsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = {
                    navigateUp()
                    viewModel.resetNameEmpty()
                }
            )
        }
    ) { innerPadding ->
        val nameEmpty = viewModel.nameEmpty
        Column {
            Spacer(Modifier.height(20.dp))
            DataForm(
                customerUiState = viewModel.customerUiState,
                updateCustomerUiState = viewModel::updateCustomerUiState,
                menuItems = viewModel.menuItems,
                addAnotherMeasurement = viewModel::addAnotherMeasurement,
                removeMeasurement = viewModel::removeMeasurement,
                onSaveClick = {
                    viewModel.onSaveClick(gotoList)
                },
                onBackPressed = {
                    onBackPressed()
                    viewModel.resetNameEmpty()
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            )
            Spacer(Modifier.height(10.dp))
            if(nameEmpty){
                Text(
                    text = stringResource(R.string.name_not_empty),
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.padding(top = 10.dp).align(Alignment.CenterHorizontally)
                )
            }
        }
    }

}