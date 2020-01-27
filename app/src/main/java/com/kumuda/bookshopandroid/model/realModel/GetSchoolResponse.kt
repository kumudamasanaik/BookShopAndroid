package com.kumuda.bookshopandroid.model.realModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetSchoolResponse(
    val Error: String,
    val Items: ArrayList<SchoolItem>,
    val Status: String
):Parcelable