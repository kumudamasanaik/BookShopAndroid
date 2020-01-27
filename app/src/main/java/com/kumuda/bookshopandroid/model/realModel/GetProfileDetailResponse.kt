package com.kumuda.bookshopandroid.model.realModel

data class GetProfileDetailResponse(
    val Address1: String,
    val Address2: String,
    val City: String,
    val ClassID: Int,
    val EmailAddress: String,
    val Error: String,
    val IsEmailVerified: String,
    val MobileNumber: Long,
    val PostalCode: String,
    val ProfileID: Int,
    val ProfileName: String,
    val SchoolID: Int,
    val State: String,
    val Status: String
)