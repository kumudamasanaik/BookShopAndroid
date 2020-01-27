package com.kumuda.bookshopandroid.screens.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kumuda.bookshopandroid.MyApplication.Companion.context
import com.kumuda.bookshopandroid.R
import com.kumuda.bookshopandroid.api.NetworkStatus
import com.kumuda.bookshopandroid.constants.Constants
import com.kumuda.bookshopandroid.model.realModel.CostomerResp
import com.kumuda.bookshopandroid.model.inputmodel.RegistrationInput
import com.kumuda.bookshopandroid.model.realModel.SendOtpResponse
import com.kumuda.bookshopandroid.screens.login.LoginActivity
import com.kumuda.bookshopandroid.screens.myprofile.ProfileActivity
import com.kumuda.bookshopandroid.util.CommonUtils
import com.kumuda.bookshopandroid.util.Validation
import com.kumuda.bookshopandroid.util.showToastMsg
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity(), View.OnClickListener,RegistrationContract.View {
    private var mContext: Context? = null
    private lateinit var registerInput: RegistrationInput
    lateinit var presenter: RegistrationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        // layoutInflater.inflate(R.layout.activity_registration, fragment_layout)
        mContext=this
        init()
    }

    override fun init() {
      /*  if (CommonUtils.isUserLogin()) {
            val intent = Intent(MyApplication.context, HomeActivity::class.java)
            intent.putExtra(Constants.SOURCE, Constants.REGISTRATION_SOURCE)
            startActivity(intent)
        } else {
            presenter = RegistrationPresenter(this)
            btn_get_otp_1.setOnClickListener(this)
            btn__continue_1.setOnClickListener(this)
            tv_login.setOnClickListener(this)
        }*/

        presenter = RegistrationPresenter(this)
        btn_get_otp_1.setOnClickListener(this)
        btn__continue_1.setOnClickListener(this)
        tv_login.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_get_otp_1 -> {
                btn_get_otp_1.setBackgroundDrawable(getDrawable(R.drawable.bg_dark_background))
                sendOtp()
            }
            R.id.btn__continue_1 -> {
                btn__continue_1.setBackgroundDrawable(getDrawable(R.drawable.bg_dark_background))
                doRegister()
                //navigateToChangePasswordScreen()
            }
            R.id.tv_login -> {
                navigateToLogin()
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
        if (res.Status==Constants.SUCCESS){
            if (!(res.OTP!!.isNullOrEmpty())) {
                Toast.makeText(context, " OTP is Successful", Toast.LENGTH_LONG).show()
                et_otp.setText(res.OTP)
            }
        }
        else {
            Toast.makeText(context, " Something went wrong", Toast.LENGTH_LONG).show()
        }
    }

    override fun doRegister() {
        registerInput = RegistrationInput(
            ProfileName = et_name.text.toString(),
            MobileNumber = et_mobile_num.text.toString(),
            OTP = et_otp.text.toString(),
            EmailAddress = et_email.text.toString(),
            Password =et_password.text.toString())
        CommonUtils.saveEmail(et_email.text.toString())
        CommonUtils.saveRegMobileNum(et_mobile_num.text.toString())
        CommonUtils.saveName(et_name.text.toString())
        CommonUtils.saveRegPassword(et_password.text.toString())

        if (presenter.validate(registerInput)) {
            if (NetworkStatus.isOnline2(this)) {
                showLoader()
                presenter.doRegister(registerInput)
            }
            else {
                Toast.makeText(this, getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun setRegistrationResp(res: CostomerResp) {
        if (Validation.isValidStatus(res)) {
            hideLoader()
            CommonUtils.setCustomerData(res)
            Toast.makeText(context, " Registration is Successful", Toast.LENGTH_LONG).show()
            navigateToProfileScreen(res.ProfileID)
            //navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)

    }

    override fun showSignupValidateErrorMsg(msg: String) {
        when (msg) {
            "1" -> et_name.error = getString(R.string.err_please_enter_name)
            "2" -> et_mobile_num.error = getString(R.string.err_please_enter_valid_mobile)
            "3" -> et_otp.error = getString(R.string.err_please_enter_valid_otp)
            "4" -> et_email.error = getString(R.string.err_please_enter_valid_email)
            "5" -> et_password.error = getString(R.string.err_please_enter_valid_password)
        }
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

    private fun navigateToProfileScreen(profileID: String?) {
        val intent = Intent(context, ProfileActivity::class.java)
        intent.putExtra(Constants.SOURCE, Constants.REGISTRATION_SOURCE)
        intent.putExtra(Constants.ProfileID, profileID)
        startActivity(intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}