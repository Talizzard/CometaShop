package com.example.cometashop

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.example.cometashop.ui.StoreCategoryScreen
import com.example.cometashop.ui.StoreHomeScreen
import com.example.cometashop.ui.theme.CometaShopTheme
import com.example.cometashop.data.Produto
import com.example.cometashop.viewmodel.CartViewModel
import com.example.cometashop.viewmodel.CategoryViewModel
import com.example.cometashop.viewmodel.CategoryViewModelFactory
import com.example.cometashop.viewmodel.ProductViewModel
import com.example.cometashop.viewmodel.ProductViewModelFactory
import com.example.cometashop.viewmodel.StoreViewModel
import com.example.cometashop.viewmodel.StoreViewModelFactory
import com.example.cometashop.data.AppDatabase
import com.example.cometashop.data.repository.CategoriaRepository
import com.example.cometashop.data.repository.ProdutoRepository
import com.example.cometashop.ui.AboutScreen
import com.example.cometashop.ui.AdminMenuScreen
import com.example.cometashop.ui.theme.CometaBlack

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val banco = AppDatabase.obterInstancia(applicationContext)
        val categoriaRepository = CategoriaRepository(
            banco.categoriaDao(),
            banco.produtoDao(),
            applicationContext
        )
        val produtoRepository = ProdutoRepository(banco.produtoDao(), applicationContext)

        setContent {
            CometaShopTheme {
                val navController = rememberNavController()
                val categoryViewModel: CategoryViewModel = viewModel(
                    factory = CategoryViewModelFactory(categoriaRepository)
                )
                val productViewModel: ProductViewModel = viewModel(
                    factory = ProductViewModelFactory(produtoRepository)
                )
                val storeViewModel: StoreViewModel = viewModel(
                    factory = StoreViewModelFactory(produtoRepository, categoriaRepository)
                )

                val cartViewModel: CartViewModel = viewModel()

                val contexto = LocalContext.current
                val aoAdicionarAoCarrinho: (Produto) -> Unit = { produto ->
                    cartViewModel.adicionar(produto)
                    Toast.makeText(contexto, "Adicionado ao carrinho!", Toast.LENGTH_SHORT).show()
                }

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
                            onNavigateToClient = {
                                navController.navigate("store") {
                                    launchSingleTop = true
                                }
                            },
                            onNavigateToAdmin = {
                                navController.navigate("admin") {
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

                    composable("store") {
                        StoreHomeScreen(
                            viewModel = storeViewModel,
                            cartViewModel = cartViewModel,
                            onCategoriaClick = { categoriaId ->
                                storeViewModel.abrirCategoria(categoriaId)
                                navController.navigate("store_category") {
                                    launchSingleTop = true
                                }
                            },
                            onProdutoClick = { productId ->
                                navController.navigate("product_details/$productId")
                            }
                        )
                    }

                    composable("store_category") {
                        StoreCategoryScreen(
                            viewModel = storeViewModel,
                            cartViewModel = cartViewModel,
                            onProdutoClick = { productId ->
                                navController.navigate("product_details/$productId")
                            }
                        )
                    }

                    composable("admin") {
                        AdminMenuScreen(
                            onNavigateToCategories = {
                                navController.navigate("categories") {
                                    launchSingleTop = true
                                }
                            },
                            onNavigateToProducts = {
                                navController.navigate("products") {
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
                        val categoryState by categoryViewModel.uiState.collectAsState()
                        ProductScreen(
                            viewModel = productViewModel,
                            categories = categoryState.categorias,
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
                            onBackClick = { navController.popBackStack() },
                            onAdicionarAoCarrinho = aoAdicionarAoCarrinho
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
