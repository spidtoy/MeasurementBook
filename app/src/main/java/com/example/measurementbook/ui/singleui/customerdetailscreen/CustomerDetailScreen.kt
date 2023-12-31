package com.example.measurementbook.ui.singleui.customerdetailscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.measurementbook.R
import com.example.measurementbook.generalutilities.uiutilities.GeneralTopAppBar
import com.example.measurementbook.generalutilities.uiutilities.Size
import com.example.measurementbook.ui.navigation.NavigationDestination
import com.example.measurementbook.ui.sharedui.CustomerUiState
import com.example.measurementbook.ui.sharedui.MyTextField

object CustomerDetailsDestination : NavigationDestination {
    override val route = "Customer Details"
    override val titleRes = R.string.customer_details
    const val customerIdArg = "customerId"
    val routeWithArgs = "$route/{$customerIdArg}"
}

@Composable
fun CustomerDetailScreen(
   onEditButtonClick: (String) -> Unit,
   gotoList: () -> Unit,
   navigateUp: () -> Unit,
   modifier: Modifier = Modifier,
   viewModel: CustomerDetailScreenViewModel = hiltViewModel()
){
    val size = Size()
    val screenWidth = size.width()
    Scaffold(
        topBar = {
            GeneralTopAppBar(
                title = stringResource(CustomerDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        }
    ) { innerPadding ->
        CustomerDetailBody (
            onEditButtonClick = onEditButtonClick,
            onDeleteButtonClick = viewModel::onDeleteButtonClicked,
            customerUiState = viewModel.customerUiState,
            screenWidth = screenWidth,
            showDialog = viewModel.showDialog,
            onConfirmation = {
                viewModel.onConfirmDelete()
                gotoList()
                viewModel.resetShowDialog()
            },
            onCancel = viewModel::resetShowDialog,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun CustomerDetailBody(
    customerUiState: CustomerUiState,
    screenWidth: Int,
    onEditButtonClick: (String) -> Unit,
    onDeleteButtonClick: () -> Unit,
    onConfirmation: () -> Unit,
    onCancel: () -> Unit,
    showDialog: Boolean,
    modifier: Modifier = Modifier
) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
               Spacer(Modifier.height(25.dp))
               Text(
                   text = customerUiState.name,
                   modifier = Modifier.width((screenWidth - 30).dp),
                   textAlign = TextAlign.Start,
                   style = MaterialTheme.typography.h5,
                   color = MaterialTheme.colors.primary,
                   fontSize = 18.sp
               )
               Spacer(Modifier.height(16.dp))
               Text(
                   text = customerUiState.phone,
                   modifier = Modifier.width((screenWidth - 30).dp),
                   textAlign = TextAlign.Start,
                   style = MaterialTheme.typography.h6,
                   color = MaterialTheme.colors.primary,
                   fontSize = 16.sp
               )
               Spacer(Modifier.height(16.dp))
               customerUiState.measurement.forEach { measurement ->
                   Column {
                       Row(
                           modifier = Modifier
                               .fillMaxWidth()
                               .padding(horizontal = 15.dp)
                       )
                       {
                           MyTextField(
                               value = measurement.name,
                               enabled = false,
                               textStyle = TextStyle(
                                   fontSize = 14.sp,
                                   color = MaterialTheme.colors.primary
                               ),
                               singleLine = true,
                               onValueChange = {},
                               contentPadding = PaddingValues(start = 10.dp),
                               modifier = Modifier.weight(2.5f).height(40.dp)
                           )
                           Spacer(Modifier.weight(0.1f))
                           MyTextField(
                               value = "${measurement.value} ${measurement.unit}",
                               enabled = false,
                               textStyle = TextStyle(
                                   fontSize = 14.sp,
                                   color = MaterialTheme.colors.primary
                               ),
                               singleLine = true,
                               onValueChange = {},
                               contentPadding = PaddingValues(start = 10.dp),
                               modifier = Modifier.weight(1f).height(40.dp)
                           )
                           Spacer(Modifier.weight(0.4f))
                       }
                       Spacer(Modifier.height(12.dp))
                   }
               }
               Spacer(Modifier.height(14.dp))
               Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center)
               {
                    Button(modifier = modifier
                        .height(40.dp),
                        onClick = { onEditButtonClick(customerUiState.id) },
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = MaterialTheme.colors.secondary
                        )
                    ) {
                        Text(text = stringResource(R.string.edit),
                            style = MaterialTheme.typography.body1,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 12.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Button(modifier = modifier
                        .height(40.dp),
                        onClick = onDeleteButtonClick,
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = MaterialTheme.colors.secondary
                        )
                    ) {
                        Text(text = stringResource(R.string.delete),
                            style = MaterialTheme.typography.body1,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                    }
               }
               Spacer(Modifier.height(12.dp))
               if(showDialog){
                   DeleteDialog(
                       title =  { DialogTitle() },
                       text = { Text(
                           text = stringResource(R.string.delete_customer),
                           textAlign = TextAlign.Center
                       )},
                       onConfirmation = onConfirmation,
                       onCancel = onCancel,
                       cancelButtonText = "No",
                       confirmButtonText = "Yes",
                       backgroundColor = MaterialTheme.colors.secondary,
                       contentColor = MaterialTheme.colors.onPrimary,
                       shape = RoundedCornerShape(20.dp),
                       modifier = Modifier.align(Alignment.CenterHorizontally)
                   )
               }
            }
}

@Composable
fun DeleteDialog(
    title: @Composable() (() -> Unit)?,
    text: @Composable() (() -> Unit)?,
    onConfirmation: () -> Unit,
    onCancel: () -> Unit,
    cancelButtonText: String,
    confirmButtonText: String,
    backgroundColor: Color,
    contentColor: Color,
    shape: Shape,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
   ){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        AlertDialog(
            onDismissRequest = onDismissRequest,
            buttons = {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                ){
                    Button(
                        onClick = onCancel,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.secondary,
                            contentColor = MaterialTheme.colors.onPrimary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ){
                        Text(text = cancelButtonText)
                    }
                    Spacer(Modifier.width(70.dp))
                    Button(
                        onClick = onConfirmation,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.secondary,
                            contentColor = MaterialTheme.colors.onPrimary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ){
                        Text(text = confirmButtonText)
                    }
                }
            },
            title = title,
            text = text,
            shape = shape,
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            modifier = modifier
        )
    }
}
@Composable
fun DialogTitle(){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 15.dp).fillMaxWidth()
    ){
        Image(
            painter = painterResource(id = R.drawable.baseline_delete_forever_24),
            contentDescription = stringResource(R.string.delete),
            alignment = Alignment.Center,
            modifier = Modifier.size(40.dp).padding(top = 12.dp)
        )
        Text(
            text = stringResource(R.string.attention),
            fontSize = 16.sp,
            letterSpacing = 0.8.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h1,
            fontWeight = FontWeight.Bold
        )
    }
}
