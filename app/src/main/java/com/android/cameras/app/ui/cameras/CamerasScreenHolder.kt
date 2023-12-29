package com.android.cameras.app.ui.cameras

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.android.cameras.app.App
import com.android.cameras.app.R
import com.android.cameras.app.di.injectedViewModel
import com.android.cameras.app.domain.CameraModel
import com.android.cameras.app.ui.theme.Preloader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun CamerasScreenHolder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow)
    ) {
        val context = LocalContext.current.applicationContext
        val viewModel = injectedViewModel {
            (context as App).appComponent.camerasViewModelFactory.create()
        }
        val state by viewModel.state.observeAsState()

        Box(modifier = Modifier.fillMaxSize()) {
            when (val currentState = state) {
                is CamerasState.Content -> CamerasList(state = currentState)
                is CamerasState.Loading -> Preloader(modifier = Modifier.align(Alignment.Center))
                else -> {}
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CamerasList(state: CamerasState.Content) {
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
                        .background(Color.White)
                        .padding(top = 10.dp, start = 21.dp),
                )
            }

            items(data, key = { it.id }) {
                CamerasListItem(cameraModel = it)
            }
        }
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
private fun CamerasListItem(cameraModel: CameraModel) {
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val scope = rememberCoroutineScope()

    SwappableItem(
        swipeableState = swipeableState
    ) {
        FavoriteButtonIcon(scope = scope, swipeableState = swipeableState)
        MainItem(cameraModel = cameraModel, swipeableState = swipeableState)
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
private fun BoxScope.MainItem(
    cameraModel: CameraModel,
    swipeableState: SwipeableState<Int>
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .width(333.dp)
                .height(279.dp)
                .clip(shape = RoundedCornerShape(5))
        ) {
            ImageItem(
                imgUrl = cameraModel.imageUrl,
                showRecIcon = cameraModel.rec,
                isFavorite = cameraModel.favorites
            )
            TextItem(text = cameraModel.name)
        }
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun BoxScope.FavoriteButtonIcon(scope: CoroutineScope, swipeableState: SwipeableState<Int>) {
    Icon(
        painter = painterResource(id = R.drawable.baseline_star_full_24),
        contentDescription = null,
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .padding(end = 30.dp)
            .border(
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(50)
            )
            .clickable {
                scope.launch { swipeableState.animateTo(0, tween(500, 0)) }
            }
    )
}

@OptIn(ExperimentalWearMaterialApi::class, ExperimentalMaterialApi::class)
@Composable
private fun SwappableItem(
    swipeableState: SwipeableState<Int>,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(207.dp)
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
private fun ImageItem(imgUrl: String, showRecIcon: Boolean, isFavorite: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SubcomposeAsyncImage(
            model = imgUrl,
            loading = { Preloader() },
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
                .width(279.dp)
                .height(207.dp)
        )

        if (showRecIcon) {
            Icon(
                painter = painterResource(id = R.drawable.rec_img),
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            )
        }
        if (isFavorite) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_star_full_24),
                contentDescription = null,
                tint = Color.Yellow,
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
            .background(Color.White)
            .wrapContentHeight(align = Alignment.CenterVertically)
            .padding(start = 16.dp)
    )
}

@Composable
@Preview
fun CamerasScreenPreview() {
    val items = hashMapOf(
        "room" to listOf(
            CameraModel(
                id = 1L,
                category = "Room",
                name = "Name 1",
                favorites = true,
                rec = true,
                imageUrl = ""
            )
        ),
        "room" to listOf(
            CameraModel(
                id = 2L,
                category = "Room",
                name = "Name 2",
                favorites = false,
                rec = false,
                imageUrl = ""
            )
        ),
        "room" to listOf(
            CameraModel(
                id = 3L,
                category = "Room",
                name = "Name 3",
                favorites = true,
                rec = true,
                imageUrl = ""
            )
        )
    )
    CamerasList(state = CamerasState.Content(items))
}

private fun dipToPx(context: Context, dpValue: Float) =
    dpValue * context.resources.displayMetrics.density