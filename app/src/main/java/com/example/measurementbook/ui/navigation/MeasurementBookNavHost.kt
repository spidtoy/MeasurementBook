package com.example.measurementbook.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.measurementbook.ui.singleui.changeemailscreen.ChangeEmailScreen
import com.example.measurementbook.ui.singleui.changeemailscreen.ChangeEmailScreenDestination
import com.example.measurementbook.ui.singleui.changepasswordscreen.ChangePasswordScreen
import com.example.measurementbook.ui.singleui.changepasswordscreen.ChangePasswordScreenDestination
import com.example.measurementbook.ui.singleui.customerdetailscreen.CustomerDetailScreen
import com.example.measurementbook.ui.singleui.customerdetailscreen.CustomerDetailsDestination
import com.example.measurementbook.ui.singleui.customereditscreen.CustomerEditScreen
import com.example.measurementbook.ui.singleui.customereditscreen.CustomerEditsDestination
import com.example.measurementbook.ui.singleui.customerlistscreen.CustomerListAndEntryDestination
import com.example.measurementbook.ui.singleui.customerlistscreen.CustomerListAndEntryScreen
import com.example.measurementbook.ui.singleui.forgotpassword.ForgotPasswordScreen
import com.example.measurementbook.ui.singleui.forgotpassword.ForgotPasswordScreenDestination
import com.example.measurementbook.ui.singleui.homescreen.HomeScreen
import com.example.measurementbook.ui.singleui.homescreen.HomeScreenDestination
import com.example.measurementbook.ui.singleui.settingsscreen.SettingsScreen
import com.example.measurementbook.ui.singleui.settingsscreen.SettingsScreenDestination
import com.example.measurementbook.ui.singleui.signinscreen.SignInScreen
import com.example.measurementbook.ui.singleui.signinscreen.SignInScreenDestination
import com.example.measurementbook.ui.singleui.signupscreen.SignUpScreen
import com.example.measurementbook.ui.singleui.signupscreen.SignUpScreenDestination
import com.example.measurementbook.ui.singleui.verifyemailscreen.VerifyEmailScreen
import com.example.measurementbook.ui.singleui.verifyemailscreen.VerifyEmailScreenDestination

@Composable
fun MeasurementBookNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = HomeScreenDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeScreenDestination.route) {
            HomeScreen(
                navigateToSignUpScreen = { navController.navigate(SignUpScreenDestination.route) {
                    launchSingleTop = true
                    popUpTo(HomeScreenDestination.route) {
                            inclusive = false
                        }
                    }
                },
                navigateToSignInScreen = { navController.navigate(SignInScreenDestination.route) {
                        launchSingleTop = true
                        popUpTo(HomeScreenDestination.route) {
                            inclusive = false
                        }
                    }
                },
                navigateToCustomerList = { navController.navigate(CustomerListAndEntryDestination.route) {
                        launchSingleTop = true
                        popUpTo(HomeScreenDestination.route) {
                           inclusive = false
                        }
                    }
                },
                navigateToVerifyEmail = {
                    navController.navigate(VerifyEmailScreenDestination.route) {
                        launchSingleTop = true
                        popUpTo(HomeScreenDestination.route) {
                            inclusive = false
                        }
                    }
                }
            )
        }
        composable(route = SignUpScreenDestination.route) {
            SignUpScreen(
                navigateToVerifyEmail = {
                    navController.navigate(VerifyEmailScreenDestination.route) {
                        launchSingleTop = true
                        popUpTo(SignUpScreenDestination.route) {
                            inclusive = true
                        }
                    }
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = VerifyEmailScreenDestination.route) {
            VerifyEmailScreen(
                navigateToSignIn = { navController.navigate(SignInScreenDestination.route) {
                    launchSingleTop = true
                    popUpTo(HomeScreenDestination.route) {
                        inclusive = false
                    }
                }
            },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = SignInScreenDestination.route) {
            SignInScreen(
                navigateCustomerList = { navController.navigate(CustomerListAndEntryDestination.route){
                            launchSingleTop = true
                            popUpTo(SignInScreenDestination.route){
                            inclusive = true
                        }
                    }
                },
                navigateToVerifyEmail = {
                    navController.navigate(VerifyEmailScreenDestination.route) {
                        launchSingleTop = true
                        popUpTo(SignInScreenDestination.route) {
                            inclusive = true
                        }
                    }
                },
                navigateToForgotPassword = {
                    navController.navigate(ForgotPasswordScreenDestination.route) {
                        launchSingleTop = true
                        popUpTo(HomeScreenDestination.route) {
                            inclusive = false
                        }
                    }
                },
                onSignUpClick = { navController.navigate(SignUpScreenDestination.route){
                        launchSingleTop = true
                        popUpTo(SignInScreenDestination.route){
                            inclusive = true
                        }
                    }
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = CustomerListAndEntryDestination.route) {
            CustomerListAndEntryScreen(
                navigateToCustomerDetails = { navController.navigate("${CustomerDetailsDestination.route}/${it}"){
                        launchSingleTop = true
                        popUpTo(CustomerListAndEntryDestination.route){
                            inclusive = false
                        }
                    }
                },
                gotoSettingsScreen = {
                      navController.navigate(SettingsScreenDestination.route){
                          launchSingleTop = true
                          popUpTo(CustomerListAndEntryDestination.route){
                              inclusive = false
                          }
                      }
                },
                gotoHomeScreen = {
                    navController.navigate(route = HomeScreenDestination.route){
                        launchSingleTop = true
                        popUpTo(route = HomeScreenDestination.route){
                            inclusive = true
                        }
                    }
                },
                gotoSignUpScreen = { navController.navigate(SignUpScreenDestination.route){
                        launchSingleTop = true
                        popUpTo(HomeScreenDestination.route){
                        inclusive = false
                        }
                    }
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = CustomerDetailsDestination.routeWithArgs,
                   arguments = listOf(navArgument(CustomerDetailsDestination.customerIdArg) {
                       type = NavType.StringType
                   })
                   ) {
                       CustomerDetailScreen(
                           onEditButtonClick = { navController.navigate("${CustomerEditsDestination.route}/${it}"){
                                   launchSingleTop = true
                                   popUpTo(CustomerDetailsDestination.routeWithArgs){
                                      inclusive = false
                                   }
                               }
                           },
                           gotoList = { navController.navigate(CustomerListAndEntryDestination.route){
                                   launchSingleTop = true
                                   popUpTo(HomeScreenDestination.route) {
                                       inclusive = false
                                   }
                               }
                           },
                           navigateUp = { navController.navigateUp() }
                   )
        }
        composable(route = CustomerEditsDestination.routeWithArgs,
            arguments = listOf(navArgument(CustomerEditsDestination.customerIdArg) {
                type = NavType.StringType
            })
        ) {
            CustomerEditScreen(
                gotoList = { navController.navigate(CustomerListAndEntryDestination.route) {
                        launchSingleTop = true
                        popUpTo(HomeScreenDestination.route) {
                            inclusive = false
                        }
                    }
                },
                onBackPressed = { navController.navigateUp() },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = SettingsScreenDestination.route) {
            SettingsScreen(
                goToChangeEmailScreen = {
                    navController.navigate(ChangeEmailScreenDestination.route) {
                        launchSingleTop = true
                        popUpTo(SettingsScreenDestination.route) {
                            inclusive = false
                        }
                    }
                },
                goToChangePasswordScreen = {
                    navController.navigate(ChangePasswordScreenDestination.route) {
                        launchSingleTop = true
                        popUpTo(SettingsScreenDestination.route) {
                            inclusive = false
                        }
                    }
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = ChangeEmailScreenDestination.route) {
            ChangeEmailScreen(
                navigateUp = { navController.navigateUp() },
                goToSignInScreen = { navController.navigate(SignInScreenDestination.route) {
                    launchSingleTop = true
                    popUpTo(HomeScreenDestination.route) {
                        inclusive = false
                       }
                    }
                }
            )
        }
        composable(route = ChangePasswordScreenDestination.route) {
            ChangePasswordScreen(
                navigateUp = { navController.navigateUp() },
                goToSignInScreen = { navController.navigate(SignInScreenDestination.route) {
                    launchSingleTop = true
                    popUpTo(HomeScreenDestination.route) {
                        inclusive = false
                        }
                    }
                }
            )
        }
        composable(route = ForgotPasswordScreenDestination.route) {
            ForgotPasswordScreen(
                navigateUp = { navController.navigateUp() },
                goToSignInScreen = { navController.navigate(SignInScreenDestination.route) {
                    launchSingleTop = true
                    popUpTo(HomeScreenDestination.route) {
                        inclusive = false
                        }
                    }
                }
            )
        }
    }
}