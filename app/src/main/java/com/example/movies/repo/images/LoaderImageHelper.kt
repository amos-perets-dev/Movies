package com.example.movies.repo.images

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import io.reactivex.functions.Consumer

class LoaderImageHelper {

    fun preloadImage(
        context: Context?,
        url: String,
        consumer: Consumer<Bitmap>
    ) {

        Glide.with(context!!)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    consumer.accept(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    }

}