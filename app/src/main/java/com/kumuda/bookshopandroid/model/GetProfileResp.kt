package com.kumuda.bookshopandroid.model

data class GetProfileResp(
    val Address1: String,
    val Address2: String,
    val City: String,
    val ClassID: Any,
    val EmailAddress: String,
    val Error: String,
    val IsEmailVerified: String,
    val MobileNumber: Int,
    val PostalCode: String,
    val ProfileID: Int,
    val ProfileName: String,
    val SchoolID: Any,
    val State: String,
    val Status: String
)