package com.rizrmdhn.storyapp.ui.screen.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.rizrmdhn.core.domain.model.Story
import com.rizrmdhn.core.ui.theme.StoryAppTheme
import com.rizrmdhn.storyapp.ui.components.ErrorScreen
import com.rizrmdhn.storyapp.ui.components.StoryCard
import com.rizrmdhn.storyapp.ui.components.StoryCardLoader
import com.rizrmdhn.storyapp.ui.components.TopBar
import com.rizrmdhn.storyapp.ui.navigation.Screen
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeScreenViewModel = koinViewModel(),
) {
    val location by viewModel.location.collectAsState()
    val listState = rememberLazyListState()

    viewModel.state.collectAsLazyPagingItems().apply {
        when (loadState.refresh) {
            is LoadState.Loading -> {
                Scaffold(
                    topBar = {
                        TopBar(
                            isLocationOn = location == 1,
                            locationSwitch = {
                                viewModel.locationSwitched()
                            },
                            navigateToAbout = {
                                navController.navigate(Screen.About.route)
                            },
                            navigateToSettings = {
                                navController.navigate(Screen.Settings.route)
                            }
                        )
                    }

                ) { innerPadding ->
                    LazyColumn(
                        modifier = Modifier.padding(innerPadding),
                    ) {
                        items(10) {
                            StoryCardLoader()
                        }
                    }
                }
            }

            is LoadState.Error -> {
                val e = (loadState.refresh as LoadState.Error).error
                ErrorScreen(
                    error = e.localizedMessage ?: "An unexpected error occurred!",
                    navController = navController
                )
            }

            else -> {
                HomeContent(
                    navController = navController,
                    isLocationOn = location == 1,
                    locationSwitch = {
                        viewModel.locationSwitched()
                    },
                    story = this,
                    navigateToAbout = { navController.navigate(Screen.About.route) },
                    navigateToSettings = { navController.navigate(Screen.Settings.route) },
                    navigateToDetail = { id ->
                        navController.navigate(Screen.DetailStory.createRoute(id))
                    },
                    navigateToAdd = {
                        navController.navigate(Screen.AddStory.route)
                    },
                    listState = listState
                )
            }
        }
    }
}

@Composable
fun HomeContent(
    navController: NavHostController,
    isLocationOn: Boolean,
    locationSwitch: () -> Unit,
    story: LazyPagingItems<Story>,
    modifier: Modifier = Modifier,
    navigateToAbout: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToDetail: (String) -> Unit,
    navigateToAdd: () -> Unit,
    listState: LazyListState
) {

    Scaffold(
        topBar = {
            TopBar(
                isLocationOn = isLocationOn,
                locationSwitch = locationSwitch,
                navigateToAbout = navigateToAbout,
                navigateToSettings = navigateToSettings
            )
        },
        floatingActionButton = {
            IconButton(
                onClick = {
                    navigateToAdd()
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.onBackground
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Story",
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }
    ) { innerPadding ->

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            state = listState,
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(
                count = story.itemCount,
                key= story.itemKey { it.id }
            ) {
                val item = story[it] ?: return@items
                StoryCard(
                    name = item.name,
                    description = item.description,
                    photoUrl = item.photoUrl,
                    createdAt = item.createdAt,
                    onGetDetailStory = {
                        navigateToDetail(item.id)
                    }
                )
            }
            when (story.loadState.append) {
                is LoadState.Loading -> {
                    item {
                        StoryCardLoader()
                    }
                }

                is LoadState.Error -> {
                    val e = (story.loadState.append as LoadState.Error).error
                    item {
                        ErrorScreen(
                            error = e.localizedMessage ?: "An unexpected error occurred!",
                            navController =navController
                        )
                    }
                }

                is LoadState.NotLoading -> {

                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    StoryAppTheme {
        HomeScreen(
            navController = NavHostController(
                context = LocalContext.current
            ),
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenDarkPreview() {
    StoryAppTheme {
        HomeScreen(
            navController = NavHostController(
                context = LocalContext.current
            ),
        )
    }
}
