package com.kumuda.bookshopandroid.util

import androidx.appcompat.widget.AppCompatImageView
import com.kumuda.bookshopandroid.R
import com.kumuda.bookshopandroid.api.ApiConstants
import com.squareup.picasso.Picasso


object ImageLoader {

    fun setImage(imageView: AppCompatImageView, imageUrl: String) {
        Picasso.get()
                .load(ApiConstants.IMAGE_BASE_URL + imageUrl)
              //  .placeholder(R.drawable.keep_cart_logo)
                .fit()
               // .error(R.drawable.keep_cart_logo)
                .into(imageView)
    }


    fun loadImagesWithoutBaseUrl(imageView: AppCompatImageView, imageUrl: String) {
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.changepassword)
                .fit()
                .error(R.drawable.changepassword)
                .into(imageView)
    }

}