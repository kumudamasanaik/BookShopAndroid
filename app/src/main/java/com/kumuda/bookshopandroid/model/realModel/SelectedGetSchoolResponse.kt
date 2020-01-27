package com.kumuda.bookshopandroid.model.realModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SelectedGetSchoolResponse(
    val Error: String,
    val result: ArrayList<SelectedSchoolItem>,
    val Status: String
):Parcelable