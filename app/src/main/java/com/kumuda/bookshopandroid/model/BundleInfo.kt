package com.kumuda.bookshopandroid.model

data class BundleInfo (
    val code: String?,
   // val text: String?,
    val rate: String?,
    val details: String?,
    var isAdded: Boolean? = false,
    var notification: String? = null,
    var date: String? = null
)