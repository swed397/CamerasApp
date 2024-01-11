package com.android.cameras.app.ui.cameras

import androidx.compose.ui.graphics.Color
import com.android.cameras.app.R
import com.android.cameras.app.domain.CameraModel
import javax.inject.Inject

class CamerasUiModelMapper @Inject constructor() {

    operator fun invoke(model: List<CameraModel>): Map<String, List<CameraUiModel>> =
        model.map {
            CameraUiModel(
                id = it.id,
                category = it.category ?: "Other",
                name = it.name,
                favorites = it.favorites,
                favoritesIcon = R.drawable.baseline_star_full_24,
                favoritesIconColor = Color.Yellow,
                rec = it.rec,
                recIcon = R.drawable.rec_img,
                recIconColor = Color.Red,
                favoritesButtonIcon = if (it.favorites) R.drawable.baseline_star_full_20 else R.drawable.baseline_star_border_20,
                favoritesButtonColor = Color.Yellow,
                imageUrl = it.imageUrl
            )
        }.groupBy { it.category }
}