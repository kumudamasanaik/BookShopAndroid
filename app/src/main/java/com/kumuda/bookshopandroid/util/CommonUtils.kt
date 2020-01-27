package com.kumuda.bookshopandroid.util

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kumuda.bookshopandroid.MyApplication.Companion.context
import com.kumuda.bookshopandroid.R
import com.kumuda.bookshopandroid.constants.Constants
import com.kumuda.bookshopandroid.listener.IAdapterClickListener
import com.kumuda.bookshopandroid.listener.ISelectedDateListener
import com.kumuda.bookshopandroid.listener.ReasonDialogueClickListener
import com.kumuda.bookshopandroid.listener.SelectedListClickListener
import com.kumuda.bookshopandroid.model.SchoolRes
import com.kumuda.bookshopandroid.model.inputmodel.HomeResp
import com.kumuda.bookshopandroid.model.realModel.*
import com.kumuda.bookshopandroid.screens.login.LoginActivity
import com.kumuda.bookshopandroid.screens.orderdetail.OrderDetailActivity
import com.kumuda.bookshopandroid.screens.registration.RegistrationActivity
import com.kumuda.bookshopandroid.util.SharedPreferenceManager.Companion.getPrefVal
import com.kumuda.bookshopandroid.util.SharedPreferenceManager.Companion.setPrefVal
import kotlinx.android.synthetic.main.custome_toast.view.*
import kotlinx.android.synthetic.main.partail_popup_dialog.view.*
import kotlinx.android.synthetic.main.partail_return_popup_dialog.view.*
import kotlinx.android.synthetic.main.partial_selected_scool_list.view.*
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class CommonUtils {
    companion object {
        private var dateListener: ISelectedDateListener? = null
        private var TAG: String = "COMMON UTILS"
        private lateinit var reason: String
        private var myProgressDialog: ProgressDialog? = null

        fun showLoading(mContext: Context, cancelable: Boolean = false) {
            try {
                hideLoading()
                myProgressDialog = ProgressDialog(mContext, R.style.AppTheme_Loading_Dialog)
                myProgressDialog?.apply {
                    setMessage(mContext.getString(R.string.please_wait))
                    setCancelable(true)
                    setOnCancelListener {
                        dismiss()
                    }
                    show()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        fun hideLoading() {
            myProgressDialog?.apply {
                if (isShowing) {
                    dismiss()
                    myProgressDialog = null
                }
            }
        }

        fun startActivity(mContext: Context, activity: Class<*>, bundle: Bundle, actfinish: Boolean) {
            val move = Intent(mContext, activity)
            move.putExtras(bundle)
            mContext.startActivity(move)
            if (mContext is Activity) {
                if (actfinish)
                    mContext.finish()
            }
        }

        fun saveJWTToken(deleveryMethod: String) {
            setPrefVal(Constants.JWT_TOKEN, deleveryMethod, SharedPreferenceManager.VALUE_TYPE.STRING)
        }

        fun getJWTToken(): String {
            return getPrefVal(Constants.JWT_TOKEN, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }

        fun setUserLogin(isLogin: Boolean) {
            setPrefVal(Constants.IS_USER_LOGIN, isLogin, SharedPreferenceManager.VALUE_TYPE.BOOLEAN)
        }

        fun isUserLogin(): Boolean {
            return getPrefVal(Constants.IS_USER_LOGIN, false, SharedPreferenceManager.VALUE_TYPE.BOOLEAN) as Boolean
        }

        fun setCustomerData(customerData: CostomerResp) {
            try {
                setUserLogin(true)
                setCurrentUser(customerData)

                SharedPreferenceManager.apply {
                    setPrefVal(Constants.PROFILE_ID, customerData.ProfileID ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    setPrefVal(Constants.PROFILE_NAME, customerData.ProfileName ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    setPrefVal(Constants.TOKEN_ID, customerData.TokenID ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                }
            }
            catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        fun getProfileID(): String {
            return getPrefVal(Constants.PROFILE_ID, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }
        fun getProfileName(): String {
            return getPrefVal(Constants.PROFILE_NAME, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }
        fun getEmailId(): String {
            return getPrefVal(Constants.EMAIL, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }
        fun getOTP(): String {
            return getPrefVal(Constants.OTP, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }
        fun getCustomerFirstName(): String {
            return getPrefVal(Constants.FIRST_NAME, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }
        fun getCustomerMobileNumber(): String {
            return getPrefVal(Constants.MOBILE, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }

        // saving profile details
        fun setProfileDetail(profileResp: GetProfileDetailResponse) {
            try {
                SharedPreferenceManager.apply {
                    setPrefVal(Constants.ProfileID, profileResp.ProfileID ?: 0, SharedPreferenceManager.VALUE_TYPE.INTEGER)
                    setPrefVal(Constants.ProfileName, profileResp.ProfileName ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    setPrefVal(Constants.MobileNumber, profileResp.MobileNumber ?: 0L, SharedPreferenceManager.VALUE_TYPE.LONG)
                    setPrefVal(Constants.Address1, profileResp.Address1 ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    setPrefVal(Constants.Address2, profileResp.Address2 ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    setPrefVal(Constants.City, profileResp.City ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    setPrefVal(Constants.ClassID, profileResp.ClassID ?: 0, SharedPreferenceManager.VALUE_TYPE.INTEGER)
                    setPrefVal(Constants.EmailAddress, profileResp.EmailAddress ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    setPrefVal(Constants.IsEmailVerified, profileResp.IsEmailVerified ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    setPrefVal(Constants.PostalCode, profileResp.PostalCode ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    setPrefVal(Constants.SchoolID, profileResp.SchoolID ?: 0, SharedPreferenceManager.VALUE_TYPE.INTEGER)
                    setPrefVal(Constants.State, profileResp.State ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                }
            }
            catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        fun getProfileId(): Int {
            return getPrefVal(Constants.ProfileID, 0, SharedPreferenceManager.VALUE_TYPE.INTEGER) as Int
        }
        fun getProfilename(): String {
            return getPrefVal(Constants.ProfileName, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }
        fun getMobileNumber(): Long {
            return getPrefVal(Constants.MobileNumber, 0L, SharedPreferenceManager.VALUE_TYPE.LONG) as Long
        }
        fun getAddress1(): String {
            return getPrefVal(Constants.Address1, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }
        fun getAddress2(): String {
            return getPrefVal(Constants.Address2, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }
        fun getCity(): String {
            return getPrefVal(Constants.City, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }
        fun getEmailAddress(): String {
            return getPrefVal(Constants.EmailAddress, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }
        fun getPostalCode(): String {
            return getPrefVal(Constants.PostalCode, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }
        fun getState(): String {
            return getPrefVal(Constants.State, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }





        fun saveMyAddress(address: Address?) {
            setPrefVal(SharedPreferenceManager.MY_ADDRESS, Gson().toJson(address), SharedPreferenceManager.VALUE_TYPE.STRING)
        }

        fun getMyAddress(): Address? {
            val json = getPrefVal(SharedPreferenceManager.MY_ADDRESS, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
            return try {
                Gson().fromJson<Address>(json, Address::class.java) as Address
            } catch (exp: java.lang.Exception) {
                null
            }
        }

        fun saveDeliveryAddress(address: String?) {
            setPrefVal(Constants.DELIVERY_ADDRESS, address ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
        }

        fun saveEmail(email: String?) {
            setPrefVal(Constants.EMAIL, email ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
        }

        fun getEmail(): String {
            return getPrefVal(Constants.EMAIL, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }

        fun saveRegMobileNum(mobile_num: String?) {
            setPrefVal(Constants.MobileNumber, mobile_num ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
        }

        fun getRegMobileNum(): String {
            return getPrefVal(Constants.MobileNumber, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }

        fun saveRegPassword(password: String) {
            setPrefVal(Constants.password, password ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
        }

        fun getRegPassword(): String {
            return getPrefVal(Constants.password, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }
        fun saveName(name: String) {
            setPrefVal(Constants.PROFILE_NAME, name ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
        }

        fun getName(): String {
            return getPrefVal(Constants.PROFILE_NAME, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }

        fun getDeliveryAddress(): String {
            return getPrefVal(Constants.DELIVERY_ADDRESS, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }

        fun shareApplication(context: Context, share_message: String) {
            try {
                val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "FreshBasket")
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, share_message)
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        fun setAuthorizationkey(key: String?) {
            MyLogUtils.d(TAG, "Authorization key : $key")
            setPrefVal(Constants.TokenID, key!!, SharedPreferenceManager.VALUE_TYPE.STRING)
        }

        fun getAutharizationKey(): String {
            return getPrefVal(Constants.TokenID, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }

        fun getSession(): String {
            val session = getPrefVal(Constants._SESION, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
            if (session.isEmpty()) {
                return generateSession()
            }
            return session
        }

        private fun generateSession(): String {
            return try {
                val chars = "abcdefghijklmnopqrstuvwxyz".toCharArray()
                val sb = StringBuilder()
                val random = Random()
                for (i in 0..15) {
                    val c = chars[random.nextInt(chars.size)]
                    sb.append(c)
                }
                val randomString = sb.toString() + "_" + SimpleDateFormat("ddMMyyyyhhmmssSSS").format(java.util.Date())
                setPrefVal(Constants._SESION, randomString, SharedPreferenceManager.VALUE_TYPE.STRING)
                randomString
            } catch (ex: Exception) {
                ex.printStackTrace()
                return ""
            }
        }

        fun showCustomToast(context: Context, msg: String, length: Int) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout = inflater.inflate(R.layout.custome_toast, null) as View
            val toast = Toast(context)
            toast.setGravity(Gravity.BOTTOM, 0, 0)
            toast.duration = length
            toast.view = layout
            toast.show()
            if (msg != null)
                layout.tv_toast_msg.text = msg
            else
                layout.tv_toast_msg.text = context.getString(R.string.Please_login_to_the_application)
        }

        fun showDialog(context: Context, body: String) {
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialogview = inflater.inflate(R.layout.partail_popup_dialog, null)

            dialogview.dialog_body.text = body
            builder.setView(dialogview)
            val dialog = builder.create()
            dialogview.btn_ok.setOnClickListener {
                dialog.dismiss()
                SharedPreferenceManager.clearPreferences()
                val bundle = Bundle()
                startActivity(context, LoginActivity::class.java, bundle, true)
            }
            dialogview.btn_cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        fun showConfirmationDialog(context: Context, listener: IAdapterClickListener?, body: String, item: Any?, pos: Int,view: View) {
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialogview = inflater.inflate(R.layout.partail_popup_dialog, null)
            val btn:Boolean=false

            dialogview.dialog_body.text = body
            builder.setView(dialogview)

            val dialog = builder.create()
            dialogview.btn_ok.setOnClickListener {
                dialog.dismiss()
                listener?.onclick(item = item!!, pos = pos, type = Constants.DELETE_CONFIRMATION,view = view)
            }
            dialogview.btn_cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        fun showListDialogue(context: Context, listener: SelectedListClickListener, data: ArrayList<SchoolRes>, item: ArrayAdapter<SchoolRes>) {
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialogview = inflater.inflate(R.layout.partial_list, null)
            val dynamicListLayout = dialogview.findViewById(R.id.dialogue_body) as LinearLayout
            builder.setView(dialogview)
            val dialog = builder.create()

            dynamicListLayout.setOnClickListener {
                dialog.dismiss()
                listener.onclick(item = item!!, op = Constants.SELECTED_CHECK_BOX)
            }
            dialog.show()

        }

        /*Reason Dialogue for order Detail*/
        fun showConfirmationDialogPopup(context: Context, body: String) {
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialogview = inflater.inflate(R.layout.partail_return_popup_dialog, null)

            dialogview.return_dialog_body.text = body
            builder.setView(dialogview)

            val dialog = builder.create()
            dialogview.return_btn_ok.setOnClickListener {
                dialog.dismiss()
                val text_reason = dialogview.findViewById<EditText>(R.id.ed_return_reason)
                if (text_reason.text.isNullOrEmpty()) {
                    Toast.makeText(context, " Reason must not be null or empty", Toast.LENGTH_LONG).show()
                } else {
                    val intent = Intent(context, OrderDetailActivity::class.java)
                    intent.putExtra(Constants.REASON, text_reason.toString())
                    intent.putExtra(Constants.SOURCE, Constants.DIALOGUE_BOX)
                    context.startActivity(intent)
                }
            }
            dialogview.return_btn_cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        /*Testing Dialogue*/
        fun customisedReasonDialogue(mcontext:Context, listener: ReasonDialogueClickListener?, body:String){
            val builder = AlertDialog.Builder(mcontext)
            val inflater = LayoutInflater.from(mcontext)
            val dialogview = inflater.inflate(R.layout.partail_return_popup_dialog, null)

            dialogview.return_dialog_body.text = body
            builder.setView(dialogview)

            val dialog = builder.create()
            dialogview.return_btn_ok.setOnClickListener {
                dialog.dismiss()
                val text_reason = dialogview.findViewById<EditText>(R.id.ed_return_reason)
                if (text_reason.text.isNullOrEmpty()) {
                    Toast.makeText(mcontext, " Reason must not be null or empty", Toast.LENGTH_LONG).show()
                } else {
                    var text_reason=text_reason.toString()
                    listener?.onClick(type = Constants.ORDER_CANCELLED_CONFIRMATION, reason = text_reason)
                }
            }

            dialogview.return_btn_cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()

        }

        fun showSelectedSchoolListPopup(context: Context, res: HomeResp) {
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialogview = inflater.inflate(R.layout.partial_selected_scool_list, null)
            builder.setView(dialogview)

            val genderList = dialogview.resources.getStringArray(R.array.class_array)
            builder.setItems(genderList){
                    dialogview, which ->
                when (which) {
                    0->{genderList[0]}
                    1->{genderList[1]}
                    2->{genderList[0]}
                }
            }
            dialogview.dialogue_body.setOnClickListener {
                dialogview.performClick()
            }
            val dialog = builder.create()
            dialog.show()

        }

        fun showSelectedListDialog(context: Context, data: ArrayList<SchoolRes>) {
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dailogview = inflater.inflate(R.layout.partial_selected_scool_list, null)

            val school_name = dailogview.findViewById(R.id.school_name) as TextView
            builder.setView(dailogview)
            if (!data[0].name!!.isEmpty())
                school_name.text = data[0].name!!

            val dialog = builder.create()

            dailogview.setOnClickListener {
                dialog.cancel()
            }
            dialog.show()
        }

        fun convertToBase64(bitmap: Bitmap): String {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
            val byteArrayImage = baos.toByteArray()
            return Base64.encodeToString(byteArrayImage, Base64.DEFAULT)
        }

        fun getRupeesSymbol(context: Context, price: String): String {
            return context.getString(R.string.rs) +" "+price
        }

        fun setSelectedTimeslot(deleveryMethod: String) {
            setPrefVal(Constants.DELIVERY_SELCTED_TIME_SLOT, deleveryMethod, SharedPreferenceManager.VALUE_TYPE.STRING)
        }
        fun setCustomerPassword(password: String) {
            setPrefVal(Constants.PASSWORD, password, SharedPreferenceManager.VALUE_TYPE.STRING)
        }

        fun setDeliverySelectionMethod(deleveryMethod: Boolean) {
            setPrefVal(Constants.DELIVERY_TYPE_SELECTION, deleveryMethod, SharedPreferenceManager.VALUE_TYPE.BOOLEAN)
        }

        //AFTER CHECK OUT CLEAR THE SHARED PREFERENCE DATA
        fun clearSharedPrefernceData() {
            setSelectedTimeslot("")
            setDeliverySelectionMethod(false)
        }

        fun paymentConfirmationDialog(context: Context, body: String) {
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialogview = inflater.inflate(R.layout.partail_popup_dialog, null)
            builder.setCancelable(true)
            dialogview.dialog_body.text = body
            builder.setView(dialogview)
            dialogview.btn_cancel.visibility = View.GONE

            val dialog = builder.create()
            dialogview.btn_ok.setOnClickListener {
                dialog.dismiss()
                navigateHomeScreen(context)
            }

            dialog.show()
        }

        private fun navigateHomeScreen(context: Context) {
            val intent = Intent(context, RegistrationActivity::class.java)
            intent.apply {
                putExtra(Constants.SOURCE, Constants.LANDING_SCREEN)
            }
            context.startActivity(intent)
            if (context is Activity) {
                context.finish()
            }
        }

        fun saveSchool(selectedSchool: SchoolRes) = setPrefVal(Constants.SELECTED_CHECK_BOX, selectedSchool, SharedPreferenceManager.VALUE_TYPE.STRING)

        fun getSchool(): String {
            return getPrefVal(Constants.SELECTED_CHECK_BOX, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }

        fun saveSelectedSchoolItems(selectedSchool:SchoolRes?) = setPrefVal(Constants.SELECTED_CHECK_BOX, Gson().toJson(selectedSchool), SharedPreferenceManager.VALUE_TYPE.STRING)

        fun getSelectedSchoolList(): SchoolRes? {
            val json = getPrefVal(Constants.SELECTED_CHECK_BOX, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
            if (Validation.isValidString(json)) {
                val type = object : TypeToken<List<SchoolRes>>() {}.type
                return Gson().fromJson<SchoolRes>(json, type)
            }
            return null
        }

        fun getDeviceID(): String {
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)
        }

        fun getBitMapURL(imageURL: String): Bitmap? {
            try {
                val url = URL(imageURL)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream

                return BitmapFactory.decodeStream(input)

            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

        /*   fun saveList(list: String) {
               setPrefVal(Constants.SELECTED_CHECK_BOX, list, SharedPreferenceManager.VALUE_TYPE.STRING)
           }

           fun getList():String{
               return getPrefVal(Constants.SELECTED_CHECK_BOX,"",SharedPreferenceManager.VALUE_TYPE.STRING)as String
           }*/

        fun saveList(list:ArrayList<SchoolItem>?) {
            val gson = Gson()
            val json = gson.toJson(list).toString()
            setPrefVal(Constants.SELECTED_CHECK_BOX, json, SharedPreferenceManager.VALUE_TYPE.STRING)
        }


        fun getSavedList():ArrayList<SchoolItem>? {
            val gson = Gson()
            val json = getPrefVal(Constants.SELECTED_CHECK_BOX, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
            if (Validation.isValidString(json)) {
                val type = object : TypeToken<List<SchoolItem>>() {}.type
                return gson.fromJson<ArrayList<SchoolItem>>(json, type)
            }
            return null

        }

        /* Saving Ordered List */
        fun saveMyOrderedList(list:ArrayList<MyOrderItem>?) {
            val gson = Gson()
            val json = gson.toJson(list).toString()
            setPrefVal(Constants.MY_ORDERD_LIST, json, SharedPreferenceManager.VALUE_TYPE.STRING)
        }

        fun getMyOrderedList():ArrayList<MyOrderItem>? {
            val gson = Gson()
            val json = getPrefVal(Constants.MY_ORDERD_LIST, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
            if (Validation.isValidString(json)) {
                val type = object : TypeToken<List<MyOrderItem>>() {}.type
                return gson.fromJson<ArrayList<MyOrderItem>>(json, type)
            }
            return null
        }
        fun setCurrentUser(customer: CostomerResp?) {
            setPrefVal(Constants.CUR_CUSTOMER, Gson().toJson(customer), SharedPreferenceManager.VALUE_TYPE.STRING)
        }

        fun getCurrentUser(): CostomerResp? {
            val json = getPrefVal(Constants.CUR_CUSTOMER, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
            return try {
                Gson().fromJson<CostomerResp>(json, CostomerResp::class.java) as CostomerResp
            } catch (exp: Exception) {
                null
            }
        }

        fun saveCartList(cartList: ArrayList<BundlePack>) {
            val gson = Gson()
            val json = gson.toJson(cartList).toString()
            setPrefVal(Constants.CART_LIST, json, SharedPreferenceManager.VALUE_TYPE.STRING)
        }

        fun getCartList():ArrayList<BundlePack>? {
            val gson = Gson()
            val json = getPrefVal(Constants.CART_LIST, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
            if (Validation.isValidString(json)) {
                val type = object : TypeToken<List<BundlePack>>() {}.type
                return gson.fromJson<ArrayList<BundlePack>>(json, type)
            }
            return null
        }


    }
}