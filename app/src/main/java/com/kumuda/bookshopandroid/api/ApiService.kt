package com.kumuda.bookshopandroid.api

import com.google.gson.JsonObject
import com.kumuda.bookshopandroid.constants.Constants
import com.kumuda.bookshopandroid.model.inputmodel.DeliveryAddressInput
import com.kumuda.bookshopandroid.model.inputmodel.RegistrationInput
import com.kumuda.bookshopandroid.model.inputmodel.SaveProfileInput
import com.kumuda.bookshopandroid.model.realModel.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST(ApiConstants.REGISTER)
    fun doRegister(@Body registerInput: RegistrationInput): Call<CostomerResp>

    @POST(ApiConstants.LOGIN)
    fun doLogin(@Header(Constants.AUTHORIZATION) header:String, @Body  jsonObject:JsonObject): Call<CostomerResp>

    @POST(ApiConstants.SEND_OTP)
    fun sendOtp(@Body json: JsonObject): Call<SendOtpResponse>

    @POST(ApiConstants.NOTIFICATION)
    fun getNotification(@Header(Constants.AUTHORIZATION) header:String, @Body  jsonObject:JsonObject): Call<NotificationResponse>

    @POST(ApiConstants.SCHOOL)
    fun getSchool(@Header(Constants.AUTHORIZATION) header:String): Call<GetSchoolResponse>

    @POST(ApiConstants.CLASS_LIST)
    fun getClassList(@Header(Constants.AUTHORIZATION) header:String, @Body  jsonObject:JsonObject): Call<GetClassList>

    @POST(ApiConstants.BUNDLE_LIST)
    fun getBundleList(@Header(Constants.AUTHORIZATION) header:String, @Body  jsonObject:JsonObject): Call<GetPackageResponse>

    @POST(ApiConstants.CHANGE_PASSWORD)
    fun doChangePAssword(@Header(Constants.AUTHORIZATION) header:String, @Body  jsonObject:JsonObject): Call<CommonRes>

    @POST(ApiConstants.GET_ORDER)//Todo need to implement
    fun getOrder(@Header(Constants.AUTHORIZATION) header:String, @Body  jsonObject:JsonObject): Call<GetOrderResp>

    @POST(ApiConstants.GET_ORDER_DETAIL)
    fun getOrderDetail(@Header(Constants.AUTHORIZATION) header:String, @Body  jsonObject:JsonObject): Call<OrderDetailResponseModel>

    @POST(ApiConstants.SAVE_PROFILE)
    fun saveProfile(@Header(Constants.AUTHORIZATION) header:String, @Body  saveProfileInput: SaveProfileInput): Call<SaveProfileResp>

    @POST(ApiConstants.GET_PROFILE)
    fun getProfile(@Header(Constants.AUTHORIZATION) header:String, @Body  jsonObject:JsonObject): Call<GetProfileDetailResponse>

    @POST(ApiConstants.GET_CART)
    fun getCartList(@Header(Constants.AUTHORIZATION) header:String, @Body  jsonObject:JsonObject): Call<CommonRes>

    @POST(ApiConstants.UPDATE_CART)
    fun getUpdateCart(@Header(Constants.AUTHORIZATION) header:String, @Body  jsonObject:JsonObject): Call<UpdateCartResp>

    @POST(ApiConstants.SAVE_DELIVERY_ADDRESS)
    fun saveDeliveryAddress(@Header(Constants.AUTHORIZATION) header:String, @Body  deliveryAddressInput: DeliveryAddressInput): Call<CommonRes>

}