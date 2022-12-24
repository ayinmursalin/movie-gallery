package com.creativijaya.moviegallery.utils

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun ImageView.loadImageUrl(
    imageUrl: String,
    @DrawableRes placeHolder: Int = 0
) {
    if (imageUrl.isEmpty()) {
        if (placeHolder > 0) {
            Glide.with(this.context)
                .load(placeHolder)
                .into(this)
        }

        return
    }

    val placeholder = callOrNull {
        ContextCompat.getDrawable(this.context, placeHolder)
    }

    Glide.with(this.context)
        .load(imageUrl)
        .placeholder(placeholder)
        .into(this)
}

