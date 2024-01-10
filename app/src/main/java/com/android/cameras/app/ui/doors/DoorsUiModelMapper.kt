package com.android.cameras.app.ui.doors

import com.android.cameras.app.R
import com.android.cameras.app.domain.DoorModel
import javax.inject.Inject

class DoorsUiModelMapper @Inject constructor() {

    operator fun invoke(model: List<DoorModel>): List<DoorUiModel> =
        model.map {
            DoorUiModel(
                id = it.id,
                room = it.room ?: "",
                name = it.name,
                favorites = it.favorites,
                favoritesButtonIcon = if (it.favorites) R.drawable.baseline_star_full_36 else R.drawable.baseline_star_border_36,
                imageUrl = it.imgUrl,
                borderCornerShapePercent = if (it.imgUrl.isNullOrEmpty()) 20 else 5
            )
        }
}