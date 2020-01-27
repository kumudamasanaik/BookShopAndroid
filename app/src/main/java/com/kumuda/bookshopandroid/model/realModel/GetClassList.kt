package com.kumuda.bookshopandroid.model.realModel

data class GetClassList(
    val Error: String,
    val Items: ArrayList<CalssItem>,
    val Status: String
)