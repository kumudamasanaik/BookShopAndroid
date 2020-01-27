package com.kumuda.bookshopandroid.screens.checkout

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.kumuda.bookshopandroid.MyApplication
import com.kumuda.bookshopandroid.R
import com.kumuda.bookshopandroid.api.NetworkStatus
import com.kumuda.bookshopandroid.constants.Constants
import com.kumuda.bookshopandroid.model.inputmodel.DeliveryAddressInput
import com.kumuda.bookshopandroid.model.realModel.GetClassList
import com.kumuda.bookshopandroid.screens.base.SubBaseActivity
import com.kumuda.bookshopandroid.screens.home.HomeActivity
import com.kumuda.bookshopandroid.util.CommonUtils
import kotlinx.android.synthetic.main.activity_check_out.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.new_addreess_layout.*
import kotlinx.android.synthetic.main.pick_up.*
import kotlinx.android.synthetic.main.radio_button_layout.*

class CheckoutActivity : SubBaseActivity(), View.OnClickListener,CheckoutContract.View {

    private var mContext: Context? = null
    lateinit var presenter: CheckOutPresenter
    private lateinit var deliveryAddressInput: DeliveryAddressInput

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_check_out)
        layoutInflater.inflate(R.layout.activity_check_out, fragment_layout)
        mContext = this
        setToolBarTittle(getString(R.string.CHECKOUT))
        init()
    }

    override fun init() {
        et_mobile_num.setText(CommonUtils.getMobileNumber().toString())
        et_email_id.setText(CommonUtils.getEmailAddress())
        presenter= CheckOutPresenter(this)
        more_less_btn_layout.setOnClickListener(this)
        home_delivery.setOnClickListener(this)
        pick_up_layout.setOnClickListener(this)
        call_us.setOnClickListener(this)
        btn_payment.setOnClickListener(this)
        updateUI()
    }

    private fun updateUI() {
        et_address1.setText(CommonUtils.getAddress1())
        et_address1.isEnabled=true
        et_address2.setText(CommonUtils.getAddress2())
        et_address2.isEnabled=true
        et_city.setText(CommonUtils.getCity())
        et_city.isEnabled=true
        et_state.setText(CommonUtils.getState())
        et_state.isEnabled=true
        et_postal_code.setText(CommonUtils.getPostalCode())
        et_postal_code.isEnabled=true
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.more_less_btn_layout -> {
                if (more_button_layout.visibility == View.VISIBLE && more_expanded_layout.visibility == View.GONE) {
                    less_button_layout.visibility = View.VISIBLE
                    more_button_layout.visibility = View.GONE
                    more_expanded_layout.visibility = View.VISIBLE
                }
                else{
                    less_button_layout.visibility = View.GONE
                    more_button_layout.visibility = View.VISIBLE
                    more_expanded_layout.visibility = View.GONE
                }
            }
             R.id.call_us -> {
                 val intent = Intent(Intent.ACTION_DIAL)
                 intent.setData(Uri.parse("tel:1234567899"));
                 startActivity(intent)
             }
            R.id.home_delivery -> {
                address_layout.visibility = View.VISIBLE
                expanded_pick_up_layout.visibility = View.GONE
                img_home_delivery.setImageResource(R.drawable.ic_radio_button_checked_black_24dp)
                img_pick_up.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp)
            }
            R.id.pick_up_layout -> {
                address_layout.visibility = View.GONE
                expanded_pick_up_layout.visibility = View.VISIBLE
                img_home_delivery.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp)
                img_pick_up.setImageResource(R.drawable.ic_radio_button_checked_black_24dp)
            }
            R.id.btn_payment -> {
                if (address_layout.visibility == View.VISIBLE) {
                    doSaveDeliveryAddress()
                }
                else{

                }
            }
        }
    }

    override fun doSaveDeliveryAddress() {
        deliveryAddressInput = DeliveryAddressInput(
            DeliveryMode = "DL",
            Address1 = et_address1.text.toString(),
            Address2 = et_address2.text.toString(),
            City =et_city.text.toString(),
            State = et_state.text.toString(),
            PostalCode =et_postal_code.text.toString(),
            MobileNumber = et_mobile_num.text.toString(),
            EmailAddress = et_email_id.text.toString())

        if (presenter.validate(deliveryAddressInput)) {
            if (NetworkStatus.isOnline2(this)) {
                showLoader()
                presenter.callSaveDeliveryAddressApi(deliveryAddressInput)
            }
            else {
                Toast.makeText(this, getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun setSaveDeliveryAddressResp(res: GetClassList) {
        if (res.Status== Constants.SUCCESS){
            Toast.makeText(MyApplication.context, " Delivery Address is saved Successfully", Toast.LENGTH_LONG).show()
        }
        else {
            Toast.makeText(MyApplication.context, " Something went wrong", Toast.LENGTH_LONG).show()
        }
    }

    override fun showValidateErrorMsg(msg: String) {
        when (msg) {
            "1" -> et_address1.error = getString(R.string.err_please_enter_address)
            "2" -> et_address2.error = getString(R.string.err_please_enter_address)
            "3" -> et_city.error = getString(R.string.err_please_enter_city_name)
            "4" -> et_state.error = getString(R.string.err_please_enter_state)
            "5" -> et_postal_code.error = getString(R.string.err_please_enter_pin_code)
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