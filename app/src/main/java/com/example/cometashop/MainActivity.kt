package com.example.cometashop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cometashop.ui.CategoryScreen
import com.example.cometashop.ui.HomeScreen
import com.example.cometashop.ui.ProductDetailScreen
import com.example.cometashop.ui.ProductScreen
import com.example.cometashop.ui.theme.CometaShopTheme
import com.example.cometashop.viewmodel.CategoryViewModel
import com.example.cometashop.viewmodel.ProductViewModel
import com.example.cometashop.ui.AboutScreen
import com.example.cometashop.ui.theme.CometaBlack

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CometaShopTheme {
                val navController = rememberNavController()
                val categoryViewModel: CategoryViewModel = viewModel()
                val productViewModel: ProductViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.background(CometaBlack),
                    enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(400)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(400)
                        )
                    },
                    popEnterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(400)
                        )
                    },
                    popExitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(400)
                        )
                    }
                ) {
                    composable("home") {
                        HomeScreen(
                            onNavigateToCategories = {
                                navController.navigate("categories") {
                                    launchSingleTop = true
                                }
                            },
                            onNavigateToProducts = {
                                navController.navigate("products") {
                                    launchSingleTop = true
                                }
                            },
                            onNavigateToAbout = {
                                navController.navigate("about") {
                                    launchSingleTop = true
                                }
                            }
                        )
                    }

                    composable("categories") {
                        CategoryScreen(
                            viewModel = categoryViewModel,
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable("products") {
                        val categories by categoryViewModel.categories.collectAsState()
                        LaunchedEffect(Unit) {
                            categoryViewModel.carregar()
                        }
                        ProductScreen(
                            viewModel = productViewModel,
                            categories = categories,
                            onBackClick = { navController.popBackStack() },
                            onProductClick = { productId ->
                                navController.navigate("product_details/$productId")
                            }
                        )
                    }
                    composable(
                        route = "product_details/{productId}",
                        arguments = listOf(navArgument("productId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val productId = backStackEntry.arguments?.getInt("productId") ?: 0

                        ProductDetailScreen(
                            productId = productId,
                            viewModel = productViewModel,
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable("about") {
                        AboutScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
