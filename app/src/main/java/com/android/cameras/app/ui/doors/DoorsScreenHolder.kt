package com.android.cameras.app.ui.doors

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.window.Dialog
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
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
import com.android.cameras.app.ui.theme.CircleIcon
import com.android.cameras.app.ui.theme.ErrorWindow
import com.android.cameras.app.ui.theme.Preloader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DoorsScreenHolder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.light_grey))
    ) {
        val context = LocalContext.current.applicationContext
        val viewModel = injectedViewModel {
            (context as App).appComponent.doorsComponent().build().doorsViewModelFactory.create()
        }
        val state by viewModel.state.observeAsState()

        val pullRefreshState = rememberPullRefreshState(
            refreshing = state is DoorsState.Refresh,
            onRefresh = viewModel::refreshData
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            when (val currentState = state) {
                is DoorsState.Content -> DoorsList(
                    state = currentState,
                    onChangeFavorite = viewModel::updateFavoriteDoorById,
                    onChangeName = viewModel::updateNameDoorById
                )

                is DoorsState.Error -> ErrorWindow(currentState.error)
                is DoorsState.Loading -> {
                    Preloader(modifier = Modifier.align(Alignment.Center))
                }

                DoorsState.Refresh -> {}

                null -> {}
            }

            PullRefreshIndicator(
                refreshing = state is DoorsState.Refresh,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }
}

@Composable
private fun DoorsList(
    state: DoorsState.Content,
    onChangeFavorite: (id: Long) -> Unit,
    onChangeName: (id: Long, name: String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(items = state.data, key = { it.id }) {
            DoorListItem(
                doorUiModel = it,
                onChangeFavorite = onChangeFavorite,
                onChangeName = onChangeName
            )
        }
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
private fun DoorListItem(
    doorUiModel: DoorUiModel,
    onChangeFavorite: (id: Long) -> Unit,
    onChangeName: (id: Long, name: String) -> Unit,
) {
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val scope = rememberCoroutineScope()

    SwappableItem(
        swipeableState = swipeableState
    ) {
        ButtonIconsRow(
            doorUiModel = doorUiModel,
            onChangeFavorite = onChangeFavorite,
            onChangeName = onChangeName,
            scope = scope,
            swipeableState = swipeableState
        )
        MainItem(doorUiModel = doorUiModel, swipeableState = swipeableState)
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
private fun MainItem(
    doorUiModel: DoorUiModel,
    swipeableState: SwipeableState<Int>
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
            .width(333.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(doorUiModel.borderCornerShapePercent)
            )
    ) {
        Column {
            ImageItem(
                imageUrl = doorUiModel.imageUrl
            )
            TextItem(name = doorUiModel.name)
        }
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
private fun SwappableItem(
    swipeableState: SwipeableState<Int>,
    content: @Composable () -> Unit
) {
    Box(
        contentAlignment = Alignment.CenterEnd,
        modifier = Modifier
            .width(333.dp)
            .swipeable(
                state = swipeableState,
                anchors = mapOf(
                    0f to 0,
                    -dipToPx(context = LocalContext.current, 100f) to 1,
                ),
                thresholds = { _, _ ->
                    FractionalThreshold(0.3f)
                },
                orientation = Orientation.Horizontal
            )
    ) {
        content.invoke()
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun ButtonIconsRow(
    doorUiModel: DoorUiModel,
    onChangeFavorite: (id: Long) -> Unit,
    onChangeName: (id: Long, name: String) -> Unit,
    scope: CoroutineScope,
    swipeableState: SwipeableState<Int>
) {
    val isDialogVisible = remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
    ) {
        CircleIcon(
            icon = painterResource(id = R.drawable.outline_mode_edit_outline_36),
            iconColor = colorResource(id = R.color.light_blue),
            onClickable = {
                scope.launch { swipeableState.animateTo(0, tween(500, 0)) }
                isDialogVisible.value = isDialogVisible.value.not()
            }
        )

        Spacer(modifier = Modifier.width(9.dp))

        CircleIcon(
            icon = painterResource(doorUiModel.favoritesButtonIcon),
            iconColor = Color.Yellow,
            onClickable = {
                scope.launch { swipeableState.animateTo(0, tween(500, 0)) }
                onChangeFavorite.invoke(doorUiModel.id)
            }
        )

        EditDialog(
            id = doorUiModel.id,
            isDialogVisible = isDialogVisible,
            dismissButtonClick = { isDialogVisible.value = false },
            confirmButtonClick = { isDialogVisible.value = false },
            confirmButtonClickChangeName = onChangeName,
            onDismiss = { isDialogVisible.value = false }
        )
    }
}

@Composable
private fun ImageItem(imageUrl: String?) {
    if (imageUrl.isNullOrEmpty().not()) {
        Box {
            SubcomposeAsyncImage(
                model = imageUrl,
                loading = { Preloader() },
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(207.dp)
            )
        }
    }
}

@Composable
private fun TextItem(name: String) {
    Box {
        Text(
            text = name,
            textAlign = TextAlign.Start,
            fontSize = 17.sp,
            modifier = Modifier
                .height(72.dp)
                .fillMaxWidth()
                .background(colorResource(id = R.color.white))
                .wrapContentHeight(align = Alignment.CenterVertically)
                .padding(start = 16.dp)
        )

        Icon(
            painter = painterResource(id = R.drawable.baseline_lock_outline_24),
            contentDescription = null,
            tint = colorResource(id = R.color.light_blue),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 27.dp)
        )
    }
}

@Composable
fun EditDialog(
    id: Long,
    isDialogVisible: State<Boolean>,
    dismissButtonClick: () -> Unit,
    confirmButtonClick: () -> Unit,
    confirmButtonClickChangeName: (id: Long, name: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf("") }

    if (isDialogVisible.value) {
        Dialog(onDismiss) {
            Surface(shape = MaterialTheme.shapes.medium) {
                Column {
                    Column(Modifier.padding(24.dp)) {
                        Text(text = "Enter name")
                        Spacer(Modifier.size(16.dp))
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it })
                    }
                    Spacer(Modifier.size(4.dp))
                    Row(
                        Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        Arrangement.spacedBy(8.dp, Alignment.End),
                    ) {
                        TransparentButton(text = "cancel", onClick = dismissButtonClick)
                        TransparentButton(
                            text = "ok",
                            onClick = {
                                confirmButtonClick.invoke()
                                confirmButtonClickChangeName.invoke(id, name)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TransparentButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick.invoke() },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = Color.Black
        )
    ) {
        Text(
            text = text,
        )
    }
}

@Composable
@Preview
private fun DoorsScreenPreview() {
    val data = listOf(
        DoorUiModel(
            id = 1,
            name = "Test name 1",
            room = "room 1",
            favorites = true,
            imageUrl = "",
            favoritesButtonIcon = R.drawable.baseline_star_border_20,
            borderCornerShapePercent = 20
        ),
        DoorUiModel(
            id = 2,
            name = "Test name 2",
            room = "room 1",
            favorites = true,
            imageUrl = "",
            favoritesButtonIcon = R.drawable.baseline_star_border_20,
            borderCornerShapePercent = 20
        ),
        DoorUiModel(
            id = 3,
            name = "Test name3",
            room = "room 2",
            favorites = true,
            imageUrl = "",
            favoritesButtonIcon = R.drawable.baseline_star_border_20,
            borderCornerShapePercent = 20
        )
    )
    DoorsList(state = DoorsState.Content(data), onChangeFavorite = {}, onChangeName = { _, _ -> })
}