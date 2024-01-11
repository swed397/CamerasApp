package com.android.cameras.app.ui.cameras

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.SwipeableState
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import coil.compose.SubcomposeAsyncImage
import com.android.cameras.app.App
import com.android.cameras.app.R
import com.android.cameras.app.di.injectedViewModel
import com.android.cameras.app.dipToPx
import com.android.cameras.app.ui.doors.DoorsState
import com.android.cameras.app.ui.theme.CircleIcon
import com.android.cameras.app.ui.theme.ErrorWindow
import com.android.cameras.app.ui.theme.Preloader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CamerasScreenHolder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.light_grey))
    ) {
        val context = LocalContext.current.applicationContext
        val viewModel = injectedViewModel {
            (context as App).appComponent.camerasViewModelFactory.create()
        }
        val state by viewModel.state.observeAsState()

        val pullRefreshState = rememberPullRefreshState(
            refreshing = state is CamerasState.Refresh,
            onRefresh = viewModel::refreshData
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)

        ) {
            when (val currentState = state) {
                is CamerasState.Content -> CamerasList(
                    state = currentState,
                    onChangeFavorite = viewModel::updateFavoriteCameraById
                )

                is CamerasState.Loading -> Preloader(
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )

                is CamerasState.Error -> ErrorWindow(error = currentState.error)
                is CamerasState.Refresh -> {}

                else -> {}
            }

            PullRefreshIndicator(
                refreshing = state is CamerasState.Refresh,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CamerasList(
    state: CamerasState.Content,
    onChangeFavorite: (id: Long, key: String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        state.data.forEach { (title, data) ->
            stickyHeader {
                Text(
                    text = title,
                    textAlign = TextAlign.Start,
                    fontSize = 21.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(id = R.color.light_grey))
                        .padding(top = 10.dp, start = 21.dp),
                )
            }

            items(data, key = { it.id }) {
                CamerasListItem(cameraModel = it, onChangeFavorite = onChangeFavorite)
            }
        }
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
private fun CamerasListItem(
    cameraModel: CameraUiModel,
    onChangeFavorite: (id: Long, key: String) -> Unit,
) {
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val scope = rememberCoroutineScope()

    SwappableItem(
        swipeableState = swipeableState
    ) {
        FavoriteButtonIcon(
            cameraModel = cameraModel,
            onChangeFavorite = onChangeFavorite,
            scope = scope,
            swipeableState = swipeableState
        )
        MainItem(cameraModel = cameraModel, swipeableState = swipeableState)
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
private fun MainItem(
    cameraModel: CameraUiModel,
    swipeableState: SwipeableState<Int>
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
            .width(333.dp)
            .height(279.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(5)
            )
    ) {
        Column {
            ImageItem(
                cameraModel = cameraModel
            )
            TextItem(text = cameraModel.name)
        }
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun BoxScope.FavoriteButtonIcon(
    cameraModel: CameraUiModel,
    onChangeFavorite: (id: Long, key: String) -> Unit,
    scope: CoroutineScope,
    swipeableState: SwipeableState<Int>
) {
    CircleIcon(
        icon = painterResource(cameraModel.favoritesButtonIcon),
        iconColor = cameraModel.favoritesIconColor,
        onClickable = {
            scope.launch { swipeableState.animateTo(0, tween(500, 0)) }
            onChangeFavorite.invoke(cameraModel.id, cameraModel.category)
        },
        modifier = Modifier
            .align(Alignment.CenterEnd)
    )
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
private fun SwappableItem(
    swipeableState: SwipeableState<Int>,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .width(333.dp)
            .height(279.dp)
            .swipeable(
                state = swipeableState,
                anchors = mapOf(
                    0f to 0,
                    -dipToPx(context = LocalContext.current, 50f) to 1,
                ),
                thresholds = { _, _ ->
                    FractionalThreshold(0.3f)
                },
                orientation = Orientation.Horizontal
            ),
        content = content
    )
}

@Composable
private fun ImageItem(cameraModel: CameraUiModel) {
    Box {
        SubcomposeAsyncImage(
            model = cameraModel.imageUrl,
            loading = { Preloader() },
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(207.dp)
        )

        if (cameraModel.rec) {
            Icon(
                painter = painterResource(id = cameraModel.recIcon),
                contentDescription = null,
                tint = cameraModel.recIconColor,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            )
        }
        if (cameraModel.favorites) {
            Icon(
                painter = painterResource(cameraModel.favoritesIcon),
                contentDescription = null,
                tint = cameraModel.favoritesIconColor,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 3.dp, start = 306.dp)
            )
        }
    }
}

@Composable
private fun TextItem(text: String) {
    Text(
        text = text,
        textAlign = TextAlign.Start,
        fontSize = 17.sp,
        modifier = Modifier
            .height(72.dp)
            .fillMaxWidth()
            .background(colorResource(id = R.color.white))
            .wrapContentHeight(align = Alignment.CenterVertically)
            .padding(start = 16.dp)
    )
}

@Composable
@Preview
fun CamerasScreenPreview() {
    val items = hashMapOf(
        "room" to listOf(
            CameraUiModel(
                id = 1L,
                category = "Other",
                name = "Test name",
                favorites = true,
                favoritesIcon = R.drawable.baseline_star_full_24,
                favoritesIconColor = Color.Yellow,
                rec = true,
                recIcon = R.drawable.rec_img,
                recIconColor = Color.Red,
                favoritesButtonIcon = R.drawable.baseline_star_full_20,
                favoritesButtonColor = Color.Yellow,
                imageUrl = ""
            )
        ),
        "room" to listOf(
            CameraUiModel(
                id = 1L,
                category = "Other",
                name = "Test name",
                favorites = true,
                favoritesIcon = R.drawable.baseline_star_full_24,
                favoritesIconColor = Color.Yellow,
                rec = true,
                recIcon = R.drawable.rec_img,
                recIconColor = Color.Red,
                favoritesButtonIcon = R.drawable.baseline_star_full_20,
                favoritesButtonColor = Color.Yellow,
                imageUrl = ""
            )
        ),
        "room" to listOf(
            CameraUiModel(
                id = 1L,
                category = "Other",
                name = "Test name",
                favorites = true,
                favoritesIcon = R.drawable.baseline_star_full_24,
                favoritesIconColor = Color.Yellow,
                rec = true,
                recIcon = R.drawable.rec_img,
                recIconColor = Color.Red,
                favoritesButtonIcon = R.drawable.baseline_star_full_20,
                favoritesButtonColor = Color.Yellow,
                imageUrl = ""
            )
        )
    )
    CamerasList(state = CamerasState.Content(items), onChangeFavorite = { _, _ -> })
}