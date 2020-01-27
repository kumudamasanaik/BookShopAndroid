package com.kumuda.bookshopandroid.util

import android.widget.EditText
import com.kumuda.bookshopandroid.constants.Constants
import com.kumuda.bookshopandroid.model.realModel.CommonRes
import java.util.regex.Pattern

class Validation {
    companion object {
        private var EMAIL_PATTERN: String? = null

        fun isValidStatus(res: CommonRes?): Boolean {
            if (res!!.Status!!.contentEquals(Constants.SUCCESS)) {
                if (!res.TokenID.isNullOrEmpty())
                    CommonUtils.setAuthorizationkey("bearer"+" "+res.TokenID)
                return true
            }
            return false
        }

        fun isValidObject(`object`: Any?): Boolean {
            return `object` != null
        }

        fun isValidString(string: String?): Boolean {
            return string != null && string.trim().isNotEmpty()
        }


        fun validateValue(value: String?): Boolean {
            return value != null && value.isNotEmpty()
        }

        fun isValidOtp(string: String?): Boolean {
            return string != null && string.trim().length == 4
        }

        fun isValidMobileNumber(string: String?): Boolean {
            return string != null && string.trim().length == 10
        }

        fun isValidpinCode(string: String?): Boolean {
            return string != null && string.trim().length == 6
        }

        fun isValidArrayList(list: ArrayList<*>): Boolean {
            return list != null && list.isNotEmpty()
        }

        fun isValidList(list: List<*>?): Boolean {
            return list != null && list.isNotEmpty()
        }

        fun isValidPassword(string: String?): Boolean {
            return string!!.trim().length > 2 && string.trim().length <= 12
        }

        fun isValidAutoCompleteTextValue(editText: EditText): Boolean {
            return editText.text != null && editText.text.toString().trim { it <= ' ' }.isNotEmpty()
        }

        fun isValidEmail(email: String): Boolean {
            EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            val pattern = Pattern.compile(EMAIL_PATTERN)
            val matcher = pattern.matcher(email)
            return matcher.matches()
        }
    }
}