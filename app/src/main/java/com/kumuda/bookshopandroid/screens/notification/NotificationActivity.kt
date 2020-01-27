package com.kumuda.bookshopandroid.screens.notification

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
import com.kumuda.bookshopandroid.model.NotificationResp
import com.kumuda.bookshopandroid.model.realModel.NotificationResponse
import com.kumuda.bookshopandroid.screens.base.SubBaseActivity
import com.kumuda.bookshopandroid.screens.home.HomeActivity
import com.kumuda.bookshopandroid.util.CommonUtils
import com.kumuda.bookshopandroid.util.Validation
import com.kumuda.bookshopandroid.util.showToastMsg
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import java.util.*

class NotificationActivity : SubBaseActivity(), IAdapterClickListener, NotificationContract.View {

    lateinit var notificationAdapter: BaseRecAdapter
    private lateinit var snackbbar: View
    private val TAG = "NotificationActivity"
    lateinit var presenter: NotificationPresenter
    private var mContext: Context? = null
    private lateinit var notificationListRes: NotificationResponse


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_notification, fragment_layout)
        mContext = this
        setToolBarTittle(getString(R.string.notifications))
        hideCartView()
        init()
    }

    override fun init() {
        presenter = NotificationPresenter(this)
        snackbbar = snack_barview
        empty_button.setOnClickListener { getNotification() }
        error_button.setOnClickListener { getNotification() }
        setupNotificationRecyclerView()
        getNotification()

    }

    private fun setupNotificationRecyclerView() {
        val linearLayout = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        notificationAdapter = BaseRecAdapter(
            this,
            R.layout.partialy_notification_list_items,
            adapterType = Constants.TYPE_NOTIFICATION_ADAPTER,
            adapterClickListener = this
        )
        rv_notification.apply {
            layoutManager = linearLayout
            adapter = notificationAdapter
            isNestedScrollingEnabled = false
        }
        //loadBundles()
    }

    override fun getNotification() {
        if (NetworkStatus.isOnline2(this)) {
            showLoader()
            presenter.callGetNotificationApi()
        } else {
            showToastMsg(getString(R.string.error_no_internet))
        }
    }

    override fun setNotificationResp(res: NotificationResponse) {
        if (res.Status == Constants.SUCCESS) {
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            notificationListRes = res
           // Toast.makeText(MyApplication.context, "Successful", Toast.LENGTH_LONG).show()
            if (Validation.isValidList(res.Items)) {
                setupData()
            } else
                showViewState(MultiStateView.VIEW_STATE_EMPTY)
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }

    private fun setupData() {
        if (Validation.isValidList(notificationListRes.Items))
            notificationAdapter.addList(notificationListRes.Items!!)
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
        val bundles = ArrayList<NotificationResp>()
        // for (i in 1 until 10) {
        val bundleInfo =
            NotificationResp("Your profile is  successfully completed", "Dec 21 2019 23:02:02")
        val bundleInfo2 =
            NotificationResp("Mobile number is verified  successfully", "Dec 12 2019 23:02:02")
        val bundleInfo3 =
            NotificationResp(" Email Id is verified successfully ", "Dec 19 2019 23:02:02")
        val bundleInfo4 =
            NotificationResp("Your profile is  successfully updated", "Dec 20 2019 23:02:02")
        val bundleInfo5 =
            NotificationResp("Order is successfully Delivered", "Dec 2182019 23:02:02")
        bundles.add(bundleInfo)
        bundles.add(bundleInfo2)
        bundles.add(bundleInfo3)
        bundles.add(bundleInfo4)
        bundles.add(bundleInfo5)
        notificationAdapter.addList(bundles)
    }


    override fun onclick(item: Any, pos: Int, type: String?, op: String, view: View) {

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
