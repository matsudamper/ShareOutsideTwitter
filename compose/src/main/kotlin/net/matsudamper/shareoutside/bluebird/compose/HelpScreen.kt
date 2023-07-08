package net.matsudamper.shareoutside.bluebird.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import coil.compose.rememberAsyncImagePainter
import net.matsudamper.shareoutside.bluebird.compose.lib.Indicator
import net.matsudamper.shareoutside.bluebird.compose.lib.TabLayout
import net.matsudamper.shareoutside.bluebird.compose.lib.Visibility
import net.matsudamper.shareoutside.bluebird.compose.lib.rememberTabLayoutState
import net.matsudamper.shareoutside.bluebird.compose.theme.ShareOutsideTwitterTheme
import net.matsudamper.shareoutside.bluebird.resources.R

public data class HelpScreenUiState(
    val event: Event,
) {
    public interface Event {
        public fun onClickBack()
    }
}

private enum class Page {
    Twitter,
    ThisApp,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun HelpScreen(
    modifier: Modifier = Modifier,
    uiState: HelpScreenUiState,
) {
    val tabLayoutState = rememberTabLayoutState()
    val coroutineScope = rememberCoroutineScope()
    var page by remember {
        mutableStateOf(Page.Twitter)
    }
    ShareOutsideTwitterTheme {
        Scaffold(
            modifier = modifier,
            topBar = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    TopAppBar(
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    uiState.event.onClickBack()
                                },
                            ) {
                                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                            }
                        },
                        title = {
                            Text(text = "設定方法")
                        },
                    )
                    TabLayout(
                        modifier = Modifier
                            .fillMaxWidth(),
                        size = 2,
                        state = tabLayoutState,
                    ) {
                        when (it) {
                            0 -> {
                                Box(
                                    modifier = Modifier
                                        .clickable {
                                            coroutineScope.launch {
                                                page = Page.Twitter
                                                tabLayoutState.animateTo(0)
                                            }
                                        }
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(text = "Twitterアプリの設定")
                                }
                            }

                            1 -> {
                                Box(
                                    modifier = Modifier
                                        .clickable {
                                            coroutineScope.launch {
                                                page = Page.ThisApp
                                                tabLayoutState.animateTo(1)
                                            }
                                        }
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(text = "このアプリの設定")
                                }
                            }
                        }
                    }
                }
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(vertical = 12.dp),
            ) {
                val savedStateHolder = rememberSaveableStateHolder()

                when (page) {
                    Page.Twitter -> {
                        savedStateHolder.SaveableStateProvider(Page.Twitter) {
                            TwitterHelp(
                                modifier = Modifier.fillMaxSize(),
                                requestNextPage = {
                                    coroutineScope.launch {
                                        tabLayoutState.animateTo(1)
                                        page = Page.ThisApp
                                    }
                                },
                            )
                        }
                    }

                    Page.ThisApp -> {
                        savedStateHolder.SaveableStateProvider(Page.ThisApp) {
                            ThisAppHelp(modifier = Modifier.fillMaxSize())
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TwitterHelp(
    modifier: Modifier = Modifier,
    requestNextPage: () -> Unit,
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    HelpScaffold(
        modifier = modifier,
        pageCount = 3,
        pagerState = pagerState,
        image = { index ->
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(maxHeight = 500.dp),
                painter = when (index) {
                    0 -> rememberAsyncImagePainter(model = R.drawable.setting_shared_1)
                    1 -> rememberAsyncImagePainter(model = R.drawable.setting_twitter_1)
                    2 -> rememberAsyncImagePainter(model = R.drawable.setting_twitter_2)
                    else -> throw IllegalStateException()
                },
                contentDescription = null,
            )
        },
        text = { index ->
            val text = when (index) {
                0 -> "設定から「デフォルトで開く」を開きます。"
                1 -> "「対応リンクを開く」を押して無効化します。"
                2 -> "オフにできたら次のページでこのアプリでリンクを開く設定を行います。"
                else -> throw IllegalStateException()
            }
            Text(
                modifier = Modifier,
                text = text,
            )
        },
        nextButton = {
            OutlinedButton(
                onClick = {
                    if (it == 2) {
                        requestNextPage()
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
            ) {
                Text(text = "次へ")
            }
        },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ThisAppHelp(modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    HelpScaffold(
        modifier = modifier,
        pageCount = 3,
        pagerState = pagerState,
        image = { index ->
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(maxHeight = 500.dp),
                painter = when (index) {
                    0 -> rememberAsyncImagePainter(model = R.drawable.setting_shared_1)
                    1 -> rememberAsyncImagePainter(model = R.drawable.setting_this_app_1)
                    2 -> rememberAsyncImagePainter(model = R.drawable.setting_this_app_2)
                    else -> throw IllegalStateException()
                },
                contentDescription = null,
            )
        },
        text = { index ->
            val text = when (index) {
                0 -> "設定から「デフォルトで開く」を開きます。"
                1 -> "「リンクを追加」を押します。"
                2 -> "サポートされているリンクから「twitter.com」を選びます。"
                else -> throw IllegalStateException()
            }
            Text(
                modifier = Modifier,
                text = text,
            )
        },
        nextButton = { index ->
            Visibility(index != 2) {
                OutlinedButton(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    },
                ) {
                    Text(text = "次へ")
                }
            }
        },
    )
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
private fun HelpScaffold(
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(),
    image: @Composable (index: Int) -> Unit,
    text: @Composable (index: Int) -> Unit,
    nextButton: @Composable (index: Int) -> Unit,
    @Suppress("SameParameterValue") pageCount: Int,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = pagerState,
            pageCount = pageCount,
        ) { index ->
            FlowColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    image(index)
                }

                Box(
                    modifier = Modifier
                        .sizeIn(minHeight = 100.dp)
                        .padding(12.dp),
                ) {
                    text(index)
                }
            }
        }
        Indicator(
            count = pageCount,
            currentIndex = { pagerState.currentPage + pagerState.currentPageOffsetFraction },
        )
        Spacer(modifier = Modifier.height(12.dp))
        nextButton(pagerState.currentPage)
    }
}

@Preview
@Preview(widthDp = 500, heightDp = 200)
@Composable
private fun Preview() {
    HelpScreen(
        uiState = HelpScreenUiState(
            event = object : HelpScreenUiState.Event {
                override fun onClickBack() {
                }
            },
        ),
    )
}
