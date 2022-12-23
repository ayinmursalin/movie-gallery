package com.creativijaya.moviegallery.utils

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun ImageView.loadImageUrl(imageUrl: String) {
    if (imageUrl.isEmpty()) return

    Glide.with(this.context)
        .load(imageUrl)
        .into(this)
}

