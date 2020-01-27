package com.kumuda.bookshopandroid.screens.classlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumuda.bookshopandroid.R
import com.kumuda.bookshopandroid.api.NetworkStatus
import com.kumuda.bookshopandroid.commonadapter.BaseRecAdapter
import com.kumuda.bookshopandroid.constants.Constants
import com.kumuda.bookshopandroid.customviews.MultiStateView
import com.kumuda.bookshopandroid.listener.IAdapterClickListener
import com.kumuda.bookshopandroid.model.ClassRes
import com.kumuda.bookshopandroid.model.realModel.CalssItem
import com.kumuda.bookshopandroid.model.realModel.GetClassList
import com.kumuda.bookshopandroid.screens.base.SubBaseActivity
import com.kumuda.bookshopandroid.screens.bundlelist.BundleListActivity
import com.kumuda.bookshopandroid.screens.cart.CartActivity
import com.kumuda.bookshopandroid.screens.checkout.CheckoutActivity
import com.kumuda.bookshopandroid.util.CommonUtils
import com.kumuda.bookshopandroid.util.Validation
import com.kumuda.bookshopandroid.util.showToastMsg
import kotlinx.android.synthetic.main.activity_class_list.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*

class ClassListActivity : SubBaseActivity(), View.OnClickListener, IAdapterClickListener ,ClassListContract.View{

    private var mContext: Context? = null
    private lateinit var myClassListAdapter: BaseRecAdapter
    private lateinit var snackbbar: View
    lateinit var presenter: ClassListPresenter
    private lateinit var classListRes: GetClassList
    private lateinit var classItem: ArrayList<CalssItem>
    private lateinit var source: String
    private lateinit var school_id:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_class_list)
        layoutInflater.inflate(R.layout.activity_class_list, fragment_layout)
        mContext = this
        setToolBarTittle(getString(R.string.CLASS_LIST))
        init()
    }

     override fun init() {
         intent?.apply {
             intent.extras?.apply {
                 source = intent.getStringExtra(Constants.SOURCE)
                 school_id=intent.getStringExtra(Constants.SCHOOL_ID)
             }
         }
        presenter= ClassListPresenter(this)
        snackbbar = snack_barview
         empty_button.setOnClickListener { getClassList() }
         error_button.setOnClickListener { getClassList() }

         setUpClassListRecyclerView()
        checkout_btn.setOnClickListener(this)
        total_cart.setOnClickListener(this)
         getClassList()
    }

    private fun setUpClassListRecyclerView() {
        val linearLayout = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        myClassListAdapter = BaseRecAdapter(this, R.layout.partialy_class_list_item, adapterType = Constants.TYPE_CLASS_LIST_ADAPTER, adapterClickListener = this)
        rv_class.apply {
            layoutManager = linearLayout
            adapter = myClassListAdapter
            isNestedScrollingEnabled = false
        }
        //loadBundles()
    }

    override fun getClassList() {
        if (NetworkStatus.isOnline2(this)) {
            showLoader()
            presenter.callGetClassListApi(school_id)
        } else {
            showToastMsg(getString(R.string.error_no_internet))
        }
    }

    override fun setClassListResp(res: GetClassList) {
        if (res.Status == Constants.SUCCESS) {
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            classListRes = res
            classItem=res.Items
            if (Validation.isValidList(res.Items)) {
                setupData()
            } else
                showViewState(MultiStateView.VIEW_STATE_EMPTY)

        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }

    private fun setupData() {
        if (Validation.isValidList(classListRes.Items))
            myClassListAdapter.addList(classListRes.Items!!)
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

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.checkout_btn -> {
                navigateToCheckoutActivity()
            }
            R.id.total_cart -> {
                navigateToCartActivity()
            }
        }
    }

    private fun loadBundles() {
        val bundles = ArrayList<ClassRes>()
        // for (i in 1 until 10) {
        val classres1 = ClassRes(
            "1",
            "First Standard")
        val classres2 = ClassRes(
            "2",
            "Second Standard")
        val classres3 = ClassRes(
            "3",
            "Third Standard")
        val classres4 = ClassRes(
            "4",
            "Fourth Standard")
        val classres5 = ClassRes(
            "5",
            "Fifth Standard")
        bundles.add(classres1)
        bundles.add(classres2)
        bundles.add(classres3)
        bundles.add(classres4)
        bundles.add(classres5)
        myClassListAdapter.addList(bundles)
    }

    private fun navigateToCartActivity() {
        val intent = Intent(context, CartActivity::class.java)
        intent.putExtra(Constants.SOURCE,Constants.CLASS_LIST)
        startActivity(intent)
    }

    private fun navigateToCheckoutActivity() {
        val intent = Intent(context, CheckoutActivity::class.java)
        startActivity(intent)
    }

    override fun onclick(item: Any, pos: Int, type: String?, op: String, view: View) {
        when(type){
            Constants.CLASS_LIST-> {
                if (item is CalssItem) {
                    navigateToBundleListActivity(item.ID.toString())
                }
            }
        }
    }

    private fun navigateToBundleListActivity(id: String) {
        val intent = Intent(context, BundleListActivity::class.java)
        intent.putExtra(Constants.SOURCE,Constants.CLASS_LIST)
        intent.putExtra(Constants.ClassID,id)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}