package com.kumuda.bookshopandroid.model.realModel

data class SendOtpResponse(
	val Status: String? = null,
	val Validity: Int? = null,
	val Message: Any? = null,
	val Error: String? = null,
	val OTP: String? = null
)
