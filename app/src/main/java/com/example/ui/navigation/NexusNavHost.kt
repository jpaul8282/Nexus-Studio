package com.example.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.AccountProfileScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PrivacyPolicyScreen
import com.example.ui.screens.ProjectDetailScreen
import com.example.ui.screens.TemplatesScreen
import com.example.viewmodel.NexusStudioViewModel

object NexusRoutes {
    const val HOME = "home"
    const val ACCOUNT_PROFILE = "account_profile"
    const val PRIVACY_POLICY = "privacy_policy"
    const val CHATBOT = "chatbot"
    const val TEMPLATES = "templates"
    const val PROJECT_DETAIL = "project_detail/{projectId}"
    fun projectDetail(id: String) = "project_detail/$id"
}

@Composable
fun NexusNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: NexusStudioViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = NexusRoutes.HOME,
        modifier = modifier
    ) {
        composable(
            route = NexusRoutes.HOME,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToDetail = { projectId ->
                    navController.navigate(NexusRoutes.projectDetail(projectId))
                },
                onNavigateToPrivacyPolicy = {
                    navController.navigate(NexusRoutes.PRIVACY_POLICY)
                },
                onNavigateToAccountProfile = {
                    navController.navigate(NexusRoutes.ACCOUNT_PROFILE)
                },
                onNavigateToChatbot = {
                    navController.navigate(NexusRoutes.CHATBOT)
                },
                onNavigateToTemplates = {
                    navController.navigate(NexusRoutes.TEMPLATES)
                }
            )
        }

        composable(
            route = NexusRoutes.TEMPLATES,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(350)
                ) + fadeIn(animationSpec = tween(350))
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(350)
                ) + fadeOut(animationSpec = tween(350))
            }
        ) {
            TemplatesScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToCopilot = { navController.navigate(NexusRoutes.CHATBOT) },
                onOpenInquiryWithTemplate = {
                    navController.popBackStack(NexusRoutes.HOME, inclusive = false)
                }
            )
        }

        composable(
            route = NexusRoutes.CHATBOT,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(350)
                ) + fadeIn(animationSpec = tween(350))
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(350)
                ) + fadeOut(animationSpec = tween(350))
            }
        ) {
            com.example.ui.screens.ChatbotScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = NexusRoutes.ACCOUNT_PROFILE,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(350)
                ) + fadeIn(animationSpec = tween(350))
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(350)
                ) + fadeOut(animationSpec = tween(350))
            }
        ) {
            AccountProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToPrivacyPolicy = { navController.navigate(NexusRoutes.PRIVACY_POLICY) },
                onNavigateToChatbot = { navController.navigate(NexusRoutes.CHATBOT) },
                onNavigateToTemplates = { navController.navigate(NexusRoutes.TEMPLATES) }
            )
        }

        composable(
            route = NexusRoutes.PRIVACY_POLICY,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(350)
                ) + fadeIn(animationSpec = tween(350))
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(350)
                ) + fadeOut(animationSpec = tween(350))
            }
        ) {
            PrivacyPolicyScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = NexusRoutes.PROJECT_DETAIL,
            arguments = listOf(navArgument("projectId") { type = NavType.StringType }),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(350)
                ) + fadeIn(animationSpec = tween(350))
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(350)
                ) + fadeOut(animationSpec = tween(350))
            }
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
            ProjectDetailScreen(
                projectId = projectId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
