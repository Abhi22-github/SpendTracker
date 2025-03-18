package com.roaa.expensetracker.composable.onBoarding

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.roaa.expensetracker.composable.navigation.Destinations
import com.roaa.expensetracker.composable.navigation.NavigationManager
import com.roaa.expensetracker.composable.navigation.handleBackNavigation
import com.roaa.expensetracker.composable.utils.darken
import com.roaa.expensetracker.hilt.AllViewModel
import com.roaa.expensetracker.model.uiDataModels.OnboardingContent
import com.roaa.expensetracker.utilities.onboardingPageContentList
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OnboardingScreen(
    navController: NavController, navigationManager: NavigationManager, viewModel: AllViewModel
) {
    BackHandler {
        handleBackNavigation(navigationManager)
    }
    val list = onboardingPageContentList
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { list.size })
    val scope = rememberCoroutineScope()

    Scaffold {
        ConstraintLayout {
            val (pager, indicator, button,skipButton) = createRefs()
            HorizontalPager(state = pagerState, modifier = Modifier.constrainAs(pager) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) { page ->
                IntroPages(list.get(page), page, pagerState)
            }
            Row(modifier = Modifier.constrainAs(indicator) {
                bottom.linkTo(button.top, 30.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
                repeat(pagerState.pageCount) {
                    PageIndicator(selected = it == pagerState.currentPage)
                }
            }
            FilledTonalButton(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                    if (pagerState.currentPage == pagerState.pageCount - 1) {
                        viewModel.preferencesViewModel.saveOnboardingState(true)
                        navController.popBackStack()
                        navController.navigate(Destinations.ListScreen)
                    }
                },
                modifier = Modifier
                    .constrainAs(button) {
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom, 64.dp)
                    }
                    .animateContentSize(),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = list[pagerState.currentPage].color.darken(
                        0.8f
                    )
                )) {
                Text(if (pagerState.currentPage == list.size - 1) "Get Started" else "Next", color = Color.White)
            }
//            FilledTonalButton(
//                onClick = {
//                    scope.launch {
//                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
//                    }
//                    if (pagerState.settledPage == pagerState.pageCount - 1) {
//                        viewModel.preferencesViewModel.saveOnboardingState(true)
//                        navController.popBackStack()
//                        navController.navigate(Destinations.ListScreen)
//                    }
//                },
//                modifier = Modifier
//                    .constrainAs(skipButton) {
//                        end.linkTo(parent.end,0.dp)
//                        top.linkTo(parent.top, 64.dp)
//                    }
//                    .animateContentSize(),
//                colors = ButtonDefaults.textButtonColors(
//                    contentColor = list[pagerState.currentPage].color.darken(
//                        0.8f
//                    )
//                )) {
//                Text("Skip")
//            }
        }

    }
}

@Composable
fun IntroPages(pageContent: OnboardingContent, page: Int, pagerState: PagerState) {
    val color by animateColorAsState(pageContent.color)
    Surface {
        Box(
            Modifier
                .fillMaxSize()
                .background(color = color), contentAlignment = Alignment.Center
        ) {
            ConstraintLayout(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                val (illustration, text) = createRefs()
                //illustration
                Column(Modifier.constrainAs(illustration) {
                    bottom.linkTo(text.top, 128.dp)
                }, verticalArrangement = Arrangement.Center) {
                    Image(
                        painter = painterResource(pageContent.icon),
                        contentDescription = null,
                    )
                }
                //title and heading
                Column(
                    Modifier
                        .fillMaxWidth()
                        .constrainAs(text) {

                            bottom.linkTo(parent.bottom, 64.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = pageContent.title,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Black
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = pageContent.description,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Black.copy(alpha = 0.6f)
                    )
                    Spacer(Modifier.height(226.dp))
                }
            }
        }
    }
}

@Composable
fun PageIndicator(modifier: Modifier = Modifier, selected: Boolean) {
    val width by animateDpAsState(if (selected) 16.dp else 8.dp, animationSpec = tween(400))
    Box(
        Modifier
            .padding(horizontal = 2.dp)
            .width(width)
            .height(8.dp)
            .background(
                if (selected) Color.Black else Color.Black.copy(alpha = 0.5f),
                RoundedCornerShape(50)
            )
    ) {}
}

//@Preview
//@Composable
//private fun IntroPagesPreview() {
//    IntroPages(
//        OnboardingContent("TEst", "sfjldkjs", color1, R.drawable.onboarding_1),
//        page,
//        pagerState
//    )
//}