package com.kumuda.bookshopandroid.screens.changepassword

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.kumuda.bookshopandroid.MyApplication
import com.kumuda.bookshopandroid.R
import com.kumuda.bookshopandroid.api.NetworkStatus
import com.kumuda.bookshopandroid.constants.Constants
import com.kumuda.bookshopandroid.model.realModel.CommonRes
import com.kumuda.bookshopandroid.model.realModel.SendOtpResponse
import com.kumuda.bookshopandroid.screens.base.SubBaseActivity
import com.kumuda.bookshopandroid.screens.home.HomeActivity
import com.kumuda.bookshopandroid.screens.login.LoginActivity
import com.kumuda.bookshopandroid.util.CommonUtils
import com.kumuda.bookshopandroid.util.Validation
import com.kumuda.bookshopandroid.util.showToastMsg
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.app_bar_home.*

class ChangePasswordActivity : SubBaseActivity(), View.OnClickListener,ChangePasswordContract.View {
    private var mContext: Context? = null
    lateinit var presenter: ChangePasswordPresenter
    private var source: String? = ""
    private lateinit var mobile_num: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_change_password)
        layoutInflater.inflate(R.layout.activity_change_password, fragment_layout)
        mContext=this
        setToolBarTittle(getString(R.string.CHANGE_PASSWORD))
        init()
    }

    override fun init() {
        intent?.apply {
            intent.extras?.apply {
                source = intent.getStringExtra(Constants.SOURCE)
                mobile_num = intent.getStringExtra(Constants.MobileNumber)
            }
        }
        et_mobile_num.setText(mobile_num)

        //Todo visibility of back to login is seted
        if(source==Constants.LOGIN)
        {
            tv_back_to_login.visibility=View.VISIBLE
        }
        else{
            tv_back_to_login.visibility=View.GONE
        }

        presenter= ChangePasswordPresenter(this)
        image_show.setOnClickListener(this)
        image_hide.setOnClickListener(this)
        btn_get_otp2.setOnClickListener(this)
        btn_continue_2.setOnClickListener(this)
        tv_back_to_login.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.image_show -> {
                if (image_show.visibility == View.VISIBLE) {
                    image_show.visibility = View.GONE
                }
                image_hide.visibility = View.VISIBLE
                et_new_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
            R.id.image_hide -> {
                if (image_hide.visibility==View.VISIBLE){
                    image_hide.visibility=View.GONE
                }
                image_show.visibility=View.VISIBLE
                et_new_password.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            R.id.btn_get_otp2 -> {
                btn_get_otp2.setBackgroundDrawable(getDrawable(R.drawable.bg_dark_background))
                sendOtp()

            }
            R.id.btn_continue_2 -> {
                btn_continue_2.setBackgroundDrawable(getDrawable(R.drawable.bg_dark_background))
                //navigateToHomeScreen()
                doChangePassword()//Todo need to handle when it comes from Login screen and Home screen(bcz it needs Authorisation id)
            }
            R.id.tv_back_to_login -> {
                navigateToLogin()
            }
        }
    }

    override fun doChangePassword() {
        if (presenter.validateLogin(et_mobile_num.text.toString(), et_new_password.text.toString())) {
            CommonUtils.setCustomerPassword(et_new_password.text.toString())
            if (NetworkStatus.isOnline2(this)) {
                showLoader()
                presenter.doChangePassword(et_mobile_num.text.toString(), et_new_password.text.toString(),et_otp.text.toString())
            } else {
                showToastMsg(getString(R.string.error_no_internet))
            }
        }
    }

    override fun setChangePasswordResp(res: CommonRes) {
        if (Validation.isValidStatus(res)) {
            hideLoader()
            Toast.makeText(context, " Password is Changed Successfully", Toast.LENGTH_LONG).show()
            navigateToHomeScreen()
        }
    }

    override fun sendOtp() {
        if (NetworkStatus.isOnline2(this)) {
            showLoader()
            presenter.doSendOtp(et_mobile_num.text.toString())
        } else {
            showToastMsg(getString(R.string.error_no_internet))
        }
    }

    override fun setOTPResp(res: SendOtpResponse) {
        if (res.Status== Constants.SUCCESS){
            if (!(res.OTP!!.isNullOrEmpty())) {
                Toast.makeText(MyApplication.context, " OTP is Successful", Toast.LENGTH_LONG).show()
                et_otp.setText(res.OTP)
            }
        }
        else {
            Toast.makeText(MyApplication.context, " Something went wrong", Toast.LENGTH_LONG).show()
        }
    }

    override fun invalidEmailPhone() {
        et_mobile_num.error = getString(R.string.err_please_enter_valid_mobile)
    }

    override fun invalidPass() {
        et_new_password.error = getString(R.string.err_please_enter_valid_password)
    }

    override fun showMsg(msg: String?) {

    }

    override fun showLoader() {
        CommonUtils.showLoading(this, true)
    }

    override fun hideLoader() {
        CommonUtils.hideLoading()
    }

    override fun showViewState(state: Int) {

    }

    private fun navigateToHomeScreen() {
        val intent = Intent(context, HomeActivity::class.java)
        intent.putExtra(Constants.SOURCE, Constants.Change_password)

        startActivity(intent)
    }

    private fun navigateToLogin() {
        val intent = Intent(MyApplication.context, LoginActivity::class.java)
        startActivity(intent)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, HomeActivity::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}