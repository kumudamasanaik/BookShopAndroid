package com.kumuda.bookshopandroid.screens.order

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.kumuda.bookshopandroid.model.OrderRes
import com.kumuda.bookshopandroid.model.realModel.GetOrderResp
import com.kumuda.bookshopandroid.model.realModel.MyOrderItem
import com.kumuda.bookshopandroid.screens.base.SubBaseActivity
import com.kumuda.bookshopandroid.screens.home.HomeActivity
import com.kumuda.bookshopandroid.screens.orderdetail.OrderDetailActivity
import com.kumuda.bookshopandroid.util.CommonUtils
import com.kumuda.bookshopandroid.util.Validation
import com.kumuda.bookshopandroid.util.showToastMsg
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import java.util.*

class OrderActivity : SubBaseActivity(), View.OnClickListener, IAdapterClickListener,OrderContract.View {

    private var mContext: Context? = null
    private lateinit var myOrderListAdapter: BaseRecAdapter
    lateinit var presenter:OrderPresenter
    private lateinit var snackbbar: View
    private lateinit var getOrderListRes: GetOrderResp
    private lateinit var myorderItem: ArrayList<MyOrderItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_order)
        layoutInflater.inflate(R.layout.activity_order, fragment_layout)
        mContext = this
        setToolBarTittle(getString(R.string.order))
        init()
    }

    override fun init() {
        snackbbar = snack_barview
        empty_button.setOnClickListener { getOrder() }
        error_button.setOnClickListener { getOrder() }
        presenter= OrderPresenter(this)
        setUpOrderListRecyclerView()
        getOrder()
    }

    private fun setUpOrderListRecyclerView() {
        val linearLayout = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        myOrderListAdapter = BaseRecAdapter(
            this,
            R.layout.partial_order_list_item,
            adapterType = Constants.TYPE_ORDER_LIST_ADAPTER,
            adapterClickListener = this
        )
        rv_order_list.apply {
            layoutManager = linearLayout
            adapter = myOrderListAdapter
            isNestedScrollingEnabled = false
        }
        //loadBundles()
    }

    override fun getOrder() {
        if (NetworkStatus.isOnline2(this)) {
            showLoader()
            presenter.getOrderApi()
        } else {
            showToastMsg(getString(R.string.error_no_internet))
        }
    }

    override fun setGetOrderResp(res: GetOrderResp) {
        if (res.Status== Constants.SUCCESS){
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
           // Toast.makeText(MyApplication.context, " GEt Order is Successful", Toast.LENGTH_LONG).show()
            getOrderListRes = res
            myorderItem=res.Items
            if (Validation.isValidList(res.Items)) {
                setData()
            }
        }
        else {
            showViewState(MultiStateView.VIEW_STATE_EMPTY)
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }

    private fun setData() {
        if (Validation.isValidList(myorderItem))
            myOrderListAdapter.addList(myorderItem)
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
            base_multistateview.viewState = state
    }

    private fun loadBundles() {
        val bundles = ArrayList<OrderRes>()
        // for (i in 1 until 10) {
        val bundleInfo = OrderRes("#547345", "200.00", "Dec 1 2019 23:12:010 ", "acknowledged")
        val bundleInfo2 = OrderRes("#3123", "200.00", "Dec 10 2019 23:12:010 ", "acknowledged")
        val bundleInfo3 = OrderRes("#5222247345", "200.00", "Dec 15 2019 23:12:010 ", "acknowledged")
        val bundleInfo4 = OrderRes("#445547345", "200.00", "Dec 11 2019 23:12:010 ", "acknowledged")
        bundles.add(bundleInfo)
        bundles.add(bundleInfo2)
        bundles.add(bundleInfo3)
        bundles.add(bundleInfo4)
        myOrderListAdapter.addList(bundles)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {

        }
    }

    override fun onclick(item: Any, pos: Int, type: String?, op: String, view: View) {
        if (item is MyOrderItem) {
            navigateToOrderDetailActivity( item.ID)
        }
    }

    private fun navigateToOrderDetailActivity(id: Int?) {
        val intent = Intent(context, OrderDetailActivity::class.java)
        intent.apply {
            putExtra(Constants.SOURCE, Constants.ORDER)
            putExtra(Constants.OrderID, id)
        }
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