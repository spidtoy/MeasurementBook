package com.example.measurementbook.ui.singleui.customerlistscreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.measurementbook.R
import com.example.measurementbook.generalutilities.uiutilities.GeneralTopAppBar
import com.example.measurementbook.generalutilities.uiutilities.Size
import com.example.measurementbook.ui.navigation.NavigationDestination
import com.example.measurementbook.ui.sharedui.*

object CustomerListAndEntryDestination : NavigationDestination {
    override val route = "Customer List And Entry Screen"
    override val titleRes = R.string.customer_list
    const val titleResB = R.string.add_customer
}

@Composable
fun CustomerListAndEntryScreen(
    navigateToCustomerDetails: (String) -> Unit,
    gotoSettingsScreen: () -> Unit,
    gotoHomeScreen: () -> Unit,
    gotoSignUpScreen: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CustomerListScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getUserCustomers()
    }

    Scaffold(
        topBar = {
            GeneralTopAppBar(
                title = if(viewModel.addCustomerOn) { stringResource(CustomerListAndEntryDestination.titleResB) }
            else { stringResource(CustomerListAndEntryDestination.titleRes) },
            canNavigateBack = true,
            navigateUp = if (viewModel.addCustomerOn) {
            {
              viewModel.onBackPressed()
              viewModel.resetShowMustSignUpScreen()
              viewModel.resetNameEmpty()
            }
            }else {navigateUp},
            actions = {
                if(viewModel.isThereCurrentUser) {
                    TopBarMenu(
                        displayDropdownMenu = viewModel.displayDropdownMenu,
                        onDismissRequest = viewModel::onDismissRequest,
                        onMenuIconClicked = viewModel::onMenuIconClicked,
                        gotoSettingsScreen = {
                            gotoSettingsScreen()
                            viewModel.resetDisplayDropdownMenu()
                        },
                        signOutAndGoHomeScreen = {
                            viewModel.signOut()
                            gotoHomeScreen()
                        }
                    )
                }
            }
            )
        },
        floatingActionButton = {
            if(!viewModel.addCustomerOn) {
                FloatingActionButton(
                    onClick = { viewModel.updateAddCustomerOn() },
                    modifier = Modifier.navigationBarsPadding()
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_customer_button),
                        tint = MaterialTheme.colors.primary
                    )
                }
            }
        }

        ) { innerPadding ->
        CustomerEntryBody(name = viewModel.userDisplayName,
            listEmpty = viewModel.customerList.isEmpty(),
            addCustomerOn = viewModel.addCustomerOn,
            customerList = viewModel.customerList,
            navigateToCustomerDetails = navigateToCustomerDetails,
            customerUiState = viewModel.customerUiState,
            updateCustomerUiState = viewModel::updateCustomerUiState,
            addAnotherMeasurement = viewModel::addAnotherMeasurement,
            removeMeasurement = viewModel::removeMeasurement,
            menuItems = viewModel.menuItems,
            onBackPressed = viewModel::onBackPressed,
            onSaveClick = viewModel::onSaveClick,
            mainError = viewModel.mainError,
            resetMainError = viewModel::resetMainError,
            nameEmpty = viewModel.nameEmpty,
            showMustSignUpScreen = viewModel.showMustSignUpScreen,
            gotoSignUp = {
                gotoSignUpScreen()
                viewModel.resetShowMustSignUpScreen()
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun CustomerEntryBody(name: String,
                      listEmpty: Boolean,
                      addCustomerOn: Boolean,
                      customerList: List<Customer>,
                      navigateToCustomerDetails: (String) -> Unit,
                      customerUiState: CustomerUiState,
                      updateCustomerUiState: (CustomerUiState) -> Unit,
                      addAnotherMeasurement: () -> Unit,
                      removeMeasurement: (Measurement) -> Unit,
                      menuItems: List<String>,
                      onBackPressed: () -> Unit,
                      onSaveClick: () -> Unit,
                      mainError: Boolean,
                      resetMainError: () -> Unit,
                      nameEmpty: Boolean,
                      showMustSignUpScreen: Boolean,
                      gotoSignUp: () -> Unit,
                      modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(12.dp))
        Row (modifier = Modifier.align(Alignment.Start), horizontalArrangement = Arrangement.Start) {
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.welcome_message, name),
                fontSize = 18.sp, style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center, color = MaterialTheme.colors.primary,
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        if(mainError){
            TextButton(onClick = resetMainError) {
                Text(
                    text = "Internal error, click here to retry",
                    fontSize = 14.sp, style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center, color = MaterialTheme.colors.error
                )
            }
        }else if(listEmpty) {
            if (addCustomerOn) {
                Column{
                    DataForm(
                        customerUiState = customerUiState,
                        updateCustomerUiState = updateCustomerUiState,
                        menuItems = menuItems,
                        addAnotherMeasurement = addAnotherMeasurement,
                        removeMeasurement = removeMeasurement,
                        onBackPressed = onBackPressed,
                        onSaveClick = onSaveClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if(showMustSignUpScreen){
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ){
                            Spacer(Modifier.height(10.dp))
                            TextButton(onClick = gotoSignUp) {
                                Text(
                                    text = stringResource(R.string.signup_here),
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.body1,
                                    color = MaterialTheme.colors.error
                                )
                            }
                        }
                    }
                    if(nameEmpty){
                        Text(
                            text = stringResource(R.string.name_not_empty),
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.error,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                }
            } else {
                WhenListIsEmpty(modifier = Modifier.align(Alignment.Start))
            }
        } else {
            if (addCustomerOn) {
                Column {
                    DataForm(
                        customerUiState = customerUiState,
                        updateCustomerUiState = updateCustomerUiState,
                        menuItems = menuItems,
                        addAnotherMeasurement = addAnotherMeasurement,
                        removeMeasurement = removeMeasurement,
                        onBackPressed = onBackPressed,
                        onSaveClick = onSaveClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if(nameEmpty){
                        Text(
                            text = stringResource(R.string.name_not_empty),
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 10.dp).align(Alignment.CenterHorizontally)
                        )
                    }
                }
            } else {
                WhenListIsNotEmpty(
                    customerList = customerList,
                    onCustomerClick = { navigateToCustomerDetails(it.id) }
                )
            }
        }
    }
}

@Composable
fun WhenListIsEmpty(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row {
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.no_added_customer),
                fontSize = 14.sp, style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center, color = MaterialTheme.colors.primary
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.add_customer_message_a),
                fontSize = 12.sp, style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.primary
            )
           Image(painter = painterResource(id = R.drawable.baseline_add_24),
                    contentDescription = stringResource(R.string.add_customer_button),
                    alignment = Alignment.Center,
                    modifier = Modifier.size(28.dp)
           )
            Text(
                text = stringResource(R.string.add_customer_message_b),
                fontSize = 12.sp, style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
fun WhenListIsNotEmpty(
    customerList: List<Customer>,
    onCustomerClick: (Customer) -> Unit,
    modifier: Modifier = Modifier
) {
       CustomerList(
            customerList = customerList,
            onCustomerClick = onCustomerClick,
            modifier = modifier
       )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomerList(customerList: List<Customer>,
                 onCustomerClick: (Customer) -> Unit,
                 modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        stickyHeader {
            CustomerListHeader()
        }
        item {
            Spacer(Modifier.height(10.dp))
        }
        items(items = customerList, key = { it.id }) { item ->
            SingleCustomer(customer = item, onItemClick = onCustomerClick)
            Divider()
            Spacer(Modifier.height(10.dp))
        }
    }

}

@Composable
fun CustomerListHeader(modifier: Modifier = Modifier){
    Row(modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = stringResource(R.string.customer_name),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.primary,
            fontSize = 15.sp)
        Spacer(modifier = Modifier.weight(1.0f))
        Text(text = stringResource(R.string.customer_phone_number),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.primary,
            fontSize = 14.sp)
        Spacer(modifier = Modifier.width(12.dp))
    }
}

@Composable
private fun SingleCustomer(
    customer: Customer,
    onItemClick: (Customer) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { onItemClick(customer) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = customer.name,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.primary,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.weight(1.0f))
        Text(
            text = customer.phone,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.primary,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.width(12.dp))
    }
}


@Composable
fun DataForm (
    customerUiState: CustomerUiState,
    updateCustomerUiState: (CustomerUiState) -> Unit,
    menuItems: List<String>,
    addAnotherMeasurement: () -> Unit,
    removeMeasurement: (Measurement) -> Unit,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    onSaveClick: () -> Unit = {},
) {
    BackHandler {
        onBackPressed()
    }
    val size = Size()
    val screenWidth = size.width()
    val focusManager = LocalFocusManager.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        FirstTextField(
            value = customerUiState.name,
            screenWidth = screenWidth,
            onValueChange = { updateCustomerUiState(customerUiState.copy(name = it)) },
            topText = stringResource(R.string.customer_name),
            placeholder = stringResource(R.string.customer_name),
            focusManager = focusManager
        )
        Spacer(Modifier.height(18.dp))
        FirstTextField(
            value = customerUiState.phone,
            screenWidth = screenWidth,
            onValueChange = { updateCustomerUiState(customerUiState.copy(phone = it)) },
            topText = stringResource(R.string.customer_phone_number),
            placeholder = stringResource(R.string.customer_phone_number),
            focusManager = focusManager,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            )
        )
        Spacer(Modifier.height(18.dp))
        customerUiState.measurement.forEachIndexed { index, element ->
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                ) {
                    SecondTextField(
                        value = element.name,
                        topText = stringResource(R.string.measurement_name),
                        placeholder = stringResource(R.string.measurement_name),
                        focusManager = focusManager,
                        onValueChange = {
                            val list = customerUiState.measurement
                            list[index] = element.copy(name = it)
                            updateCustomerUiState(
                                customerUiState.copy(measurement = list)
                            )
                        },
                        modifier = Modifier.weight(3.7f)
                    )
                    Spacer(Modifier.weight(0.1f))
                    SecondTextField(
                        value = element.value,
                        topText = stringResource(R.string.value),
                        placeholder = stringResource(R.string.value),
                        focusManager = focusManager,
                        onValueChange = {
                            val list = customerUiState.measurement
                            list[index] = element.copy(value = it)
                            updateCustomerUiState(
                                customerUiState.copy(measurement = list)
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.weight(2f)
                    )
                    Spacer(Modifier.weight(0.1f))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1.6f)
                    )
                    {
                        Text(
                            text = stringResource(R.string.unit),
                            color = MaterialTheme.colors.primary,
                            style = MaterialTheme.typography.body1,
                            fontSize = 12.sp
                        )
                        Spacer(Modifier.height(3.dp))
                        ComposeMenu(
                            menuItems = menuItems,
                            element = element,
                            onDropdownClick = {
                                val list = customerUiState.measurement
                                list[index] = element.copy(menuExpandedState = true)
                                updateCustomerUiState(
                                    customerUiState.copy(measurement = list)
                                )
                            },
                            onDismissRequest = {
                                val list = customerUiState.measurement
                                list[index] = element.copy(menuExpandedState = false)
                                updateCustomerUiState(
                                    customerUiState.copy(measurement = list)
                                )
                            },
                            onDropdownMenuItemClick = {
                                val list = customerUiState.measurement
                                list[index] = element.copy(unit = it, menuExpandedState = false)
                                updateCustomerUiState(
                                    customerUiState.copy(measurement = list)
                                )
                            }
                        )
                    }
                    Spacer(Modifier.weight(0.1f))
                    IconButton(onClick = {removeMeasurement(element)},
                        modifier = Modifier
                            .weight(0.4f)
                            .align(Alignment.CenterVertically)
                            .padding(top = 16.dp)) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear",
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .height(40.dp)
            .padding(horizontal = 15.dp),
            onClick = addAnotherMeasurement,
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.secondary
            )
        ) {
            Text(text = stringResource(R.string.add_another_measurement),
                style = MaterialTheme.typography.body1,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp))
        }
        Spacer(Modifier.height(12.dp))
        Button(modifier = Modifier
            .height(40.dp)
            .width(90.dp),
            onClick = onSaveClick,
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.secondary
            )
        ) {
            Text(text = stringResource(R.string.save),
                style = MaterialTheme.typography.body1,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}

@Composable
fun FirstTextField(value: String,
                   screenWidth: Int,
                   onValueChange:  (String) -> Unit,
                   topText: String,
                   focusManager: FocusManager,
                   modifier: Modifier = Modifier,
                   placeholder: String = "",
                   enabled: Boolean = true,
                   keyboardOptions: KeyboardOptions = KeyboardOptions(
                       imeAction = ImeAction.Done
                   )
) {
    Column( horizontalAlignment = Alignment.Start, modifier = modifier.width((screenWidth - 30).dp)) {
        Text(
            text = topText,
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.body1,
            fontSize = 12.sp
        )
        Spacer(Modifier.height(3.dp))
        MyTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Placeholder(placeholder = placeholder)},
            textStyle = TextStyle(fontSize = 12.sp, color = MaterialTheme.colors.primary),
            singleLine = true,
            enabled = enabled,
            shape = RoundedCornerShape(20.dp),
            contentPadding = PaddingValues(start = 10.dp),
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }),
            trailingIcon = {
                IconButton(onClick = {
                },
                    modifier = Modifier.size(16.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear",
                    )
                }
            },
            modifier = Modifier
                .height(45.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun SecondTextField(value: String,
                    topText: String,
                    placeholder: String,
                    focusManager: FocusManager,
                    onValueChange:  (String) -> Unit,
                    modifier: Modifier = Modifier,
                    keyboardOptions: KeyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    )
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(
            text = topText,
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.body1,
            fontSize = 12.sp
        )
        Spacer(Modifier.height(3.dp))
        MyTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(fontSize = 12.sp, color = MaterialTheme.colors.primary),
            placeholder = { Placeholder(placeholder = placeholder) },
            singleLine = true,
            contentPadding = PaddingValues(start = 10.dp),
            shape = RoundedCornerShape(20.dp),
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }),
            modifier = Modifier
                .height(45.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun Placeholder(placeholder: String) {
    Row {
        Text(text = placeholder, textAlign =  TextAlign.Center,
            fontSize = 12.sp, color = MaterialTheme.colors.secondaryVariant,
        )
    }
}
