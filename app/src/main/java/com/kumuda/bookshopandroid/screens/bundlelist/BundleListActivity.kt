package com.kumuda.bookshopandroid.screens.bundlelist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumuda.bookshopandroid.R
import com.kumuda.bookshopandroid.api.NetworkStatus
import com.kumuda.bookshopandroid.constants.Constants
import com.kumuda.bookshopandroid.customviews.MultiStateView
import com.kumuda.bookshopandroid.listener.IAdapterClickListener
import com.kumuda.bookshopandroid.model.BundleInfo
import com.kumuda.bookshopandroid.model.realModel.BundlePack
import com.kumuda.bookshopandroid.model.realModel.GetPackageResponse
import com.kumuda.bookshopandroid.model.realModel.UpdateCartResp
import com.kumuda.bookshopandroid.screens.base.SubBaseActivity
import com.kumuda.bookshopandroid.screens.bundlelist.adapter.BundleListAdapter
import com.kumuda.bookshopandroid.screens.cart.CartActivity
import com.kumuda.bookshopandroid.screens.checkout.CheckoutActivity
import com.kumuda.bookshopandroid.util.CommonUtils
import com.kumuda.bookshopandroid.util.Validation
import com.kumuda.bookshopandroid.util.loge
import com.kumuda.bookshopandroid.util.showToastMsg
import kotlinx.android.synthetic.main.activity_bundle_list.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.tool_bar_with_back.*
import java.util.*

class BundleListActivity : SubBaseActivity(), IAdapterClickListener, View.OnClickListener ,BundleListContract.View{

    private var mContext: Context? = null
    private lateinit var myBundleListAdapter: BundleListAdapter
    private lateinit var snackbbar: View
    lateinit var presenter: BundleListPresenter
    private lateinit var schoolListRes: GetPackageResponse
    private  lateinit var  cartResp:UpdateCartResp
    private lateinit var source: String
    private lateinit var class_id: String
    private var cartList = arrayListOf<BundlePack>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_bundle_list)
        layoutInflater.inflate(R.layout.activity_bundle_list, fragment_layout)
        mContext = this
        setToolBarTittle(getString(R.string.BUNDLE))
        init()
    }

    override fun onResume() {
        super.onResume()
        getBundleList()
    }

    override fun init() {
        intent?.apply {
            intent.extras?.apply {
                source = intent.getStringExtra(Constants.SOURCE)
                class_id=intent.getStringExtra(Constants.ClassID)
            }
        }
        presenter= BundleListPresenter(this)
        snackbbar = snack_barview
        empty_button.setOnClickListener { getBundleList() }
        error_button.setOnClickListener { getBundleList() }

        setUpBundleListRecyclerView()
        checkout_btn.setOnClickListener(this)
        total_cart.setOnClickListener(this)
       // getBundleList()
    }

    override fun getBundleList() {
        if (NetworkStatus.isOnline2(this)) {
            showLoader()
            presenter.callBundleListApi(class_id)
        } else {
            showToastMsg(getString(R.string.error_no_internet))
        }
    }

    override fun setBundleListResp(res: GetPackageResponse) {
        if (res.Status == Constants.SUCCESS) {
            hideLoader()
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            schoolListRes = res
            if (Validation.isValidList(res.Items)) {
                setupData()
            } else
                showViewState(MultiStateView.VIEW_STATE_EMPTY)
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }

    private fun setupData() {
        if (Validation.isValidList(schoolListRes.Items))
            myBundleListAdapter.addList(schoolListRes.Items!!)
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

    private fun setUpBundleListRecyclerView() {
        val linearLayout = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        myBundleListAdapter = BundleListAdapter(
            this,
            R.layout.partialy_bundle_list_item,
            adapterType = Constants.TYPE_BUNDLE_LIST_ADAPTER,
            adapterClickListener = this
        )
        rv_bundle_list.apply {
            layoutManager = linearLayout
            adapter = myBundleListAdapter
            isNestedScrollingEnabled = false
        }
        //loadBundles();
    }

    private fun loadBundles() {
        val bundles = ArrayList<BundleInfo>()
        // for (i in 1 until 10) {
        val bundleInfo = BundleInfo(
            "ENGLISH",
            "200.00",
            "These bundle of book contains 10 set of books  - "
        )
        val bundleInfo2 = BundleInfo(
            "KANNADA",
            "175.00",
            "These bundle of book contains 5 set of books "
        )
        val bundleInfo3 = BundleInfo(
            "HINDI",
            "163.00",
            "These bundle of book contains 2 set of books")
        val bundleInfo4 = BundleInfo(
            "MATHEMATICS",
            "200.00",
            "These bundle of book contains 12 set of books")
        bundles.add(bundleInfo)
        bundles.add(bundleInfo2)
        bundles.add(bundleInfo3)
        bundles.add(bundleInfo4)
       // myBundleListAdapter.addList(bundles)
    }

    override fun onclick(item: Any, pos: Int, type: String?, op: String, view: View) {
        when (type) {
            Constants.ADD_TO_CART -> {
                      if(item is BundlePack) {
                    //presenter.callAddToCartApi(item.ID.toString(),item.quantity.toString())
                    item.isAdded=true
                    item.quantity=1
                    this.myBundleListAdapter.notifyDataSetChanged()
                    presenter.callAddToCartApi(item)
                }
            }

            Constants.REMOVE_FROM_CART -> {
                CommonUtils.showConfirmationDialog(this, this, getString(R.string.do_u_want_to_remove), item, pos, view)
            }

            Constants.DELETE_CONFIRMATION -> {
                if(item is BundlePack) {
                    item.isAdded = false
                  item.quantity=-1 //Todo quantity value is changed to 0, means item has to be remove from to cart
                    this.myBundleListAdapter.notifyDataSetChanged()
                   // presenter.callRemoveFromCartApi(item.ID.toString(),item.quantity.toString())
                    presenter.callRemoveFromCartApi(item)
                }
            }
        }
    }

    override fun setAddToCartResp(res: UpdateCartResp,item:BundlePack) {
        if (res.Status == Constants.SUCCESS) {
            cartResp = res
            item.isAdded=true
            cartList.add(item)
            loge("cartItem"+ cartList)
            CommonUtils.saveCartList(cartList)

            CommonUtils.getCartList()

            loge("cartList"+ CommonUtils.getCartList())
            this.myBundleListAdapter.notifyDataSetChanged()
            showToastMsg("successfully added")
        } else {
            showToastMsg(" Status is failed")
        }
    }

    override fun setRemoveFromCartResp(res: UpdateCartResp,item:BundlePack) {
        if (res.Status == Constants.SUCCESS) {
            cartResp = res
            cartList.remove(item)
            CommonUtils.saveCartList(cartList)
            getBundleList()
            this.myBundleListAdapter.notifyDataSetChanged()
            CommonUtils.getCartList()
            showToastMsg("successfully Removed")
        } else {
            showToastMsg(" Status is failed")
        }
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

    private fun navigateToCartActivity() {
        val intent = Intent(context, CartActivity::class.java)
        intent.putExtra(Constants.SOURCE,Constants.BUNDLE)
        startActivity(intent)
    }

    private fun navigateToCheckoutActivity() {
        val intent = Intent(context, CheckoutActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }*/

    fun updateCartbadgeCount() {
        if(!((CommonUtils.getCartList().isNullOrEmpty())||(CommonUtils.getCartList()?.size==0))) {
            val crat_count=CommonUtils.getCartList()?.size
            cart_badge.text=crat_count.toString()
        }
        else {
            cart_badge.visibility=View.GONE
        }
    }

}