package com.kumuda.bookshopandroid.model.realModel

data class GetPackageResponse(
    val Error: String,
    val Items: ArrayList<BundlePack>,
    val Status: String
)