package com.rizrmdhn.storyapp.ui.screen.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.rizrmdhn.core.data.Resource
import com.rizrmdhn.core.domain.model.Story
import com.rizrmdhn.core.ui.theme.StoryAppTheme
import com.rizrmdhn.storyapp.R
import com.rizrmdhn.storyapp.ui.components.ErrorScreen
import com.rizrmdhn.storyapp.ui.components.StoryCard
import com.rizrmdhn.storyapp.ui.components.StoryCardLoader
import com.rizrmdhn.storyapp.ui.components.TopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeScreenViewModel = koinViewModel(),
    locale: String,
    setLocale: (String) -> Unit
) {
    val moreItems by viewModel.loadMore.collectAsState()
    val isFetchingMore by viewModel.isFetchingMore.collectAsState()

    viewModel.state.collectAsState().value.let { state ->
        when (state) {
            is Resource.Loading -> {
                viewModel.getStories()

                Scaffold(
                    topBar = {
                        TopBar(
                            locale = locale,
                            setLocale = setLocale
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

            is Resource.Success -> {
                state.data?.let {
                    HomeContent(
                        story = viewModel.storyList,
                        loadMore = {
                            viewModel.getMoreStories()
                        },
                        moreItems = moreItems,
                        setMoreItem = {
                            viewModel.setLoadMore(it)
                        },
                        fetchingMore = isFetchingMore,
                        locale = locale,
                        setLocale = setLocale
                    )
                }
            }

            is Resource.Error -> {
                state.message?.let {
                    ErrorScreen(
                        error = it,
                        navController = navController
                    )
                }
            }

        }
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    story: List<Story>,
    loadMore: () -> Unit,
    moreItems: Boolean,
    setMoreItem: (Boolean) -> Unit,
    fetchingMore: Boolean,
    locale: String,
    setLocale: (String) -> Unit
) {
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopBar(
                locale = locale,
                setLocale = setLocale
            )
        }

    ) { innerPadding ->
        if (story.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Text(
                    text = "No story available",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                state = listState,
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(story, key = { it.id }) { story ->
                    StoryCard(
                        name = story.name,
                        description = story.description,
                        photoUrl = story.photoUrl,
                        createdAt = story.createdAt,
                    )
                }
                items(story.size) { index ->
                    if (index == story.size - 1) {
                        if (moreItems) {
                            if (fetchingMore) {
                                StoryCardLoader()
                            } else {
                                Button(
                                    onClick = {
                                        loadMore()
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.onSurface,
                                        contentColor = MaterialTheme.colorScheme.background
                                    ),
                                ) {
                                    Text(text = stringResource(R.string.load_more))
                                }
                            }
                        } else {
                            setMoreItem(false)
                            Text(
                                text = stringResource(R.string.no_more_items),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
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
            locale = "en",
            setLocale = {}
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
            locale = "en",
            setLocale = {}
        )
    }
}
