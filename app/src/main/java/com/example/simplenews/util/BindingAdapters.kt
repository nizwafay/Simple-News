package com.example.simplenews.util

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.simplenews.R

@BindingAdapter("showLoading")
fun showLoading(view: View, isLoading: Boolean) {
    view.visibility = if (isLoading) View.VISIBLE else View.GONE
}

/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
                .override(1000, 1000))
            .into(imgView)
    }
}