package com.kumuda.bookshopandroid.util

import android.content.Context
import android.widget.ImageView
import com.kumuda.bookshopandroid.R
import com.squareup.picasso.Picasso

class ImageUtils{
    companion object{
        fun setCircleImageWithPicasso(context: Context, imageView: ImageView, path: String?) {
            if (path != null && path.length > 0) {
                Picasso.get()
                    .load(path)/// add image Base url
                    .error(R.drawable.ic_launcher_background)
                    .placeholder(R.drawable.ic_launcher_background)
                    .noFade()
                    .into(imageView)

            }
    }
    }
}
