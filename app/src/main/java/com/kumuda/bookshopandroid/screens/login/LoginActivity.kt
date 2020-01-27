package com.kumuda.bookshopandroid.screens.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kumuda.bookshopandroid.MyApplication
import com.kumuda.bookshopandroid.R
import com.kumuda.bookshopandroid.api.NetworkStatus
import com.kumuda.bookshopandroid.constants.Constants
import com.kumuda.bookshopandroid.model.realModel.CostomerResp
import com.kumuda.bookshopandroid.model.realModel.SendOtpResponse
import com.kumuda.bookshopandroid.screens.changepassword.ChangePasswordActivity
import com.kumuda.bookshopandroid.screens.home.HomeActivity
import com.kumuda.bookshopandroid.screens.myprofile.ProfileActivity
import com.kumuda.bookshopandroid.screens.registration.RegistrationActivity
import com.kumuda.bookshopandroid.util.CommonUtils
import com.kumuda.bookshopandroid.util.Validation
import com.kumuda.bookshopandroid.util.showToastMsg
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(),LoginContract.View, View.OnClickListener {

    private lateinit var context: Context
    private val TAG = "LoginInActivity"
    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        context = this
        init()
    }

    override fun init() {
       /* if (CommonUtils.isUserLogin()) {
            navigateToHomeScreen()
            finish()
        } else {
            presenter = LoginPresenter(this)
            btn_get_otp.setOnClickListener(this)
            btn_continue.setOnClickListener(this)
            tv_forgotPassword.setOnClickListener(this)
            tv_register.setOnClickListener(this)
        }*/
        presenter = LoginPresenter(this)
        btn_get_otp.setOnClickListener(this)
        btn_continue.setOnClickListener(this)
        tv_forgotPassword.setOnClickListener(this)
        tv_register.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_get_otp ->{
                btn_get_otp.setBackgroundDrawable(getDrawable(R.drawable.bg_dark_background))
                sendOtp()
            }

            R.id.btn_continue->{
                btn_continue.setBackgroundDrawable(getDrawable(R.drawable.bg_dark_background))
                // navigateToRegisterscreen()
                doLogin()
            }
            R.id.tv_forgotPassword->{
                navigateToChangePasswordActivity(et_mobile_num.text.toString())
            }
            R.id.tv_register->{
                navigateToRegistrationScreen()
            }
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
               // Toast.makeText(MyApplication.context, " OTP is Successful", Toast.LENGTH_SHORT).show()
                et_otp.setText(res.OTP)
            }
        }
        else {
            Toast.makeText(MyApplication.context, " Something went wrong", Toast.LENGTH_LONG).show()
        }
    }

    override fun doLogin() {
        if (presenter.validateLogin(et_mobile_num.text.toString(), et_password.text.toString())) {
            CommonUtils.setCustomerPassword(et_password.text.toString())
            if (NetworkStatus.isOnline2(this)) {
                showLoader()
                presenter.doLogin(et_mobile_num.text.toString(), et_password.text.toString(),et_otp.text.toString())
            } else {
                showToastMsg(getString(R.string.error_no_internet))
            }
        }
    }

    override fun setLoginResp(res: CostomerResp) {
        if (Validation.isValidStatus(res)) {
            hideLoader()
            CommonUtils.setCustomerData(res)
           // Toast.makeText(context, " Login is Successful", Toast.LENGTH_LONG).show()
            //navigateToProfileScreen()
           // showMsg(res.Message.toString())
            navigateToHomeScreen()
        }
        else{
            showMsg(res.Message.toString())
        }
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(MyApplication.context, HomeActivity::class.java)
        intent.putExtra(Constants.SOURCE, Constants.LOGIN)
        startActivity(intent)
    }
    private fun navigateToRegistrationScreen() {
        val intent = Intent(MyApplication.context, RegistrationActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun navigateToChangePasswordActivity(mobile_num: String) {
        val intent = Intent(context, ChangePasswordActivity::class.java)
        intent.putExtra(Constants.SOURCE, Constants.LOGIN)
        intent.putExtra(Constants.MobileNumber, mobile_num)
        startActivity(intent)
    }

    private fun navigateToProfileScreen() {
        val intent = Intent(MyApplication.context, ProfileActivity::class.java)
        intent.putExtra(Constants.SOURCE, Constants.LOGIN)
        startActivity(intent)
        finish()
    }

    override    fun invalidEmailPhone() {
        et_mobile_num.error = getString(R.string.err_please_enter_valid_mobile)
    }

    override fun invalidPass() {
        et_password.error = getString(R.string.err_please_enter_valid_password)
    }

    override fun showMsg(msg: String?) {
        showToastMsg(msg!!)
    }

    override fun showLoader() {
        CommonUtils.showLoading(this, true)
    }

    override fun hideLoader() {
        CommonUtils.hideLoading()
    }

    override fun showViewState(state: Int) {

    }

    private fun navigateToRegisterscreen() {
        val intent = Intent(context, RegistrationActivity::class.java)
        startActivity(intent)
    }
}