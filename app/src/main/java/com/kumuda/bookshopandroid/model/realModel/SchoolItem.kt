package com.kumuda.bookshopandroid.model.realModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SchoolItem(
    val Address: String,
    val ID: Int,
    val Name: String,
    var isSelected: Boolean
):Parcelable