package com.kumuda.bookshopandroid.api

import com.google.gson.JsonObject
import com.kumuda.bookshopandroid.constants.Constants
import com.kumuda.bookshopandroid.util.CommonUtils

object ApiRequestParam {
    var respParamObj = JsonObject()

   /* fun getCommonParameter(jsonObject: JsonObject) {
        jsonObject.addProperty(Constants.CUSTOMER_ID, CommonUtils.getCustomerID())
    }*/

    /*LOGIN*/
    fun login(email: String, password: String,otp:String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            addProperty(Constants.mobileNumber, email)
            addProperty(Constants.password, password)
            addProperty(Constants.OTP_, otp)
        }
        return respParamObj
    }
 fun changePassword(email: String, password: String,otp:String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            addProperty(Constants.mobileNumber, email)
            addProperty(Constants.password, password)
            addProperty(Constants.OTP_, otp)
        }
        return respParamObj
    }

    fun sendOtp(mobileNumber: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            addProperty(Constants.MobileNumber, mobileNumber)
        }
        return respParamObj
    }

    fun getNotificationRequestParameters(): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            addProperty(Constants.ProfileID, "6")//Todo  hard coded profile id in notification request parameter.
            addProperty(Constants.LastNotificationID, "")
        }
        return respParamObj
    }

    fun getClassListRequestParameters(school_id:String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            addProperty(Constants.SchoolID, school_id)
        }
        return respParamObj
    }
    fun getBundleListRequestParameters(class_id:String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            addProperty(Constants.ClassID, class_id)
        }
        return respParamObj
    }

    fun getOrderRequestParameters(): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            addProperty(Constants.ProfileID, CommonUtils.getProfileID())
            addProperty(Constants.LastOrderID, "")
        }
        return respParamObj
    }

    fun getMyOrderDetailsParameters(order_no: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            respParamObj.addProperty(Constants.OrderID, order_no)
        }
        return respParamObj
    }

    fun getProfilrParameters(profile_id:String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            respParamObj.addProperty(Constants.ProfileID, profile_id)
        }
        return respParamObj
    }
    fun getUpdateCartRequestParameters(package_id:String,quantity:String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            addProperty(Constants.ProfileID, CommonUtils.getProfileID())
            addProperty(Constants.PackageID,package_id)
            addProperty(Constants.Quantity,quantity)
        }
        return respParamObj
    }

    fun getCartRequestParameters(): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
        }
        return respParamObj
    }
}