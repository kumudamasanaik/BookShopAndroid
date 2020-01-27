package com.kumuda.bookshopandroid.screens.orderdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumuda.bookshopandroid.R
import com.kumuda.bookshopandroid.api.NetworkStatus
import com.kumuda.bookshopandroid.commonadapter.BaseRecAdapter
import com.kumuda.bookshopandroid.constants.Constants
import com.kumuda.bookshopandroid.customviews.MultiStateView
import com.kumuda.bookshopandroid.listener.IAdapterClickListener
import com.kumuda.bookshopandroid.listener.ReasonDialogueClickListener
import com.kumuda.bookshopandroid.model.OrdeDetailRes
import com.kumuda.bookshopandroid.model.realModel.OrderDetailResponseModel
import com.kumuda.bookshopandroid.screens.base.SubBaseActivity
import com.kumuda.bookshopandroid.screens.home.HomeActivity
import com.kumuda.bookshopandroid.util.CommonUtils
import com.kumuda.bookshopandroid.util.Validation
import com.kumuda.bookshopandroid.util.showToastMsg
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.partial_common_item.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailActivity : SubBaseActivity(), IAdapterClickListener, View.OnClickListener,OrderDetailsContract.View ,ReasonDialogueClickListener{

    private var mContext: Context? = null
    private lateinit var myorderDetailListAdapter: BaseRecAdapter
    private lateinit var source: String
    lateinit var presenter: OrderDetailPresenter
    private  var order_id: Int = 0
    private lateinit var snackbbar: View
    private lateinit var getOrderDetailRes: OrderDetailResponseModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_order_detail)
        layoutInflater.inflate(R.layout.activity_order_detail, fragment_layout)
        mContext = this
        intent.extras?.apply {
        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)
            order_id = intent.getIntExtra(Constants.OrderID,0)
            }
        }
        setToolBarTittle(getString(R.string.ORDER_DETAIL))
        init()
    }

    override fun init() {
        snackbbar = snack_barview
        empty_button.setOnClickListener { callOrderDetails() }
        error_button.setOnClickListener { callOrderDetails() }

        presenter= OrderDetailPresenter(this)
       /* if (source.contentEquals(Constants.DIALOGUE_BOX)){
            cancelOrderApiCall()
            setUpOrderDetailListRecyclerView()
        }
        else {
            cancel_order_btn.visibility = View.VISIBLE
            setUpOrderDetailListRecyclerView()
            cancel_order_btn.setOnClickListener(this)
            //callOrderDetails()
        }*/
        cancel_order_btn.visibility = View.VISIBLE
        setUpOrderDetailListRecyclerView()
        cancel_order_btn.setOnClickListener(this)
        callOrderDetails()
    }

    private fun setUpOrderDetailListRecyclerView() {
        val linearLayout = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        myorderDetailListAdapter = BaseRecAdapter(this, R.layout.partialy_order_detail_list_item, adapterType = Constants.TYPE_ORDER_DETAIL_LIST_ADAPTER, adapterClickListener = this)
        rv_order_detail.apply {
            layoutManager = linearLayout
            adapter = myorderDetailListAdapter
            isNestedScrollingEnabled = false
        }
        //loadBundles()
    }


    override fun callOrderDetails() {
        if (NetworkStatus.isOnline2(this)) {
            showLoader()
            presenter.callMyOrderDetailsApi(order_id.toString())
        } else {
            showToastMsg(getString(R.string.error_no_internet))
        }
    }

    override fun setOrderDetailsResp(res: OrderDetailResponseModel) {
        if (res.Status== Constants.SUCCESS){
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            getOrderDetailRes=res
           // Toast.makeText(MyApplication.context, " GEt Order is Successful", Toast.LENGTH_LONG).show()
            setData(res)
            setRecyclerViewData()
        }
        else {
            showViewState(MultiStateView.VIEW_STATE_EMPTY)
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }

    private fun setData(res: OrderDetailResponseModel) {
        //Todo total , rate, delivery cahrge, grand total =null so check it and tell them to change.
        tv_order_id.text=res.ID.toString()

        val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val output = SimpleDateFormat("MMM dd yyyy  h:mm a")
        var d: Date? = null
        try {
            d = input.parse(res.Date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val formatted = output.format(d)
        Log.i("DATE", "" + formatted)
        tv_date.text = formatted

        status.text=res.DeliveryMode
        address.text=res.Address
        payment_mode.text=res.DeliveryMode

        val  delivery_charge=res.CourierCharge
        val  item_total=res.Total
        //val total=item_total+delivery_charge

       /* if(res.Total.toString().isNullOrEmpty()){
            order_rate.visibility=View.GONE
        }
        else{
            order_rate.text=res.GrandTotal.toString()
        }*/
        //order_rate.text=res.GrandTotal.toString()
        // tv_item_total.text=res.GrandTotal.toString()
        //   tv_delivery_charge.text=res.CourierCharge.toString()
        //tv_total.text=total.toString()
    }

    private fun setRecyclerViewData() {
        if (Validation.isValidList(getOrderDetailRes.Items))
            myorderDetailListAdapter.addList(getOrderDetailRes.Items)
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
        if (base_multistateview != null)
            base_multistateview.viewState = state    }

    private fun loadBundles() {
        val bundles = ArrayList<OrdeDetailRes>()
        val bundleInfo = OrdeDetailRes(order_id = "#547345", rate = "200.00", date = "Dec 1 2019 23:12:010 ", status = "Acknoledged",total = "210.00",
            item_total = "200.00",delivery_charge = "10.00",delivery_address = "1st Floor, Workshaala Spaces, No 57, 12th Main Rd, Sector 6, HSR Layout, Bengaluru, Karnataka 560102",
            name = "Second Class",payment_mode = "ACKNOWLEDGED")
        bundles.add(bundleInfo)
        myorderDetailListAdapter.addList(bundles)
    }

    private fun cancelOrderApiCall() {
        showToastMsg(getString(R.string.successfully_cancelled))
    }


    override fun onclick(item: Any, pos: Int, type: String?, op: String, view: View) {

    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.cancel_order_btn->{
               // CommonUtils.showConfirmationDialogPopup(this, getString(R.string.successfully_cancelled))
                CommonUtils.customisedReasonDialogue(this,listener = this,body =  getString(R.string.successfully_cancelled))
            }
        }
    }

    override fun onClick(type: String?, reason: String) {
        when(type){
            Constants.ORDER_CANCELLED_CONFIRMATION->{
                showToastMsg("Order Cancel Api has to be call")
                cancel_order_btn.visibility = View.GONE
                cancelOrderApiCall()

            }
        }

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