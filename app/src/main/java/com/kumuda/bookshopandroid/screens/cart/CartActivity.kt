package com.kumuda.bookshopandroid.screens.cart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumuda.bookshopandroid.MyApplication
import com.kumuda.bookshopandroid.R
import com.kumuda.bookshopandroid.api.NetworkStatus
import com.kumuda.bookshopandroid.constants.Constants
import com.kumuda.bookshopandroid.customviews.MultiStateView
import com.kumuda.bookshopandroid.listener.IAdapterClickListener
import com.kumuda.bookshopandroid.model.CartModel
import com.kumuda.bookshopandroid.model.realModel.BundlePack
import com.kumuda.bookshopandroid.model.realModel.CommonRes
import com.kumuda.bookshopandroid.model.realModel.UpdateCartResp
import com.kumuda.bookshopandroid.screens.base.SubBaseActivity
import com.kumuda.bookshopandroid.screens.cart.adapter.CartAdapter
import com.kumuda.bookshopandroid.screens.checkout.CheckoutActivity
import com.kumuda.bookshopandroid.screens.home.HomeActivity
import com.kumuda.bookshopandroid.util.CommonUtils
import com.kumuda.bookshopandroid.util.showToastMsg
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.app_bar_home.*
import java.util.*

class CartActivity : SubBaseActivity(), View.OnClickListener, IAdapterClickListener ,CartContractr.View{

    private var mContext: Context? = null
    private lateinit var myCartAdapter: CartAdapter
    private var source: String?=null
    lateinit var presenter: CartPresenter
    private lateinit var snackbbar: View
    private lateinit var list:String
    private var cartList = arrayListOf<BundlePack>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_cart)
        layoutInflater.inflate(R.layout.activity_cart, fragment_layout)
        mContext = this
        setToolBarTittle(getString(R.string.CART))
        hideCartView()
        init()
    }

    override fun onResume() {
        super.onResume()
        //cartList= CommonUtils.getCartList()!!

    }

    override fun init() {
        intent?.apply {
            intent.extras?.apply {
                source = intent.getStringExtra(Constants.SOURCE)
            }
        }
        presenter= CartPresenter(this)
        snackbbar = snack_barview
        /*empty_button.setOnClickListener { getCart() }
        error_button.setOnClickListener { getCart() }
*/
        setUIdata()
        cart_btn.setOnClickListener(this)
    }

    private fun setUIdata() {
        if((CommonUtils.getCartList().isNullOrEmpty())||(CommonUtils.getCartList()?.size==0)){
            linear.visibility=View.GONE
            btn_continue_cart.visibility=View.GONE
            showViewState(MultiStateView.VIEW_STATE_EMPTY)
        }
        else {
            linear.visibility = View.VISIBLE
            btn_continue_cart.visibility = View.VISIBLE
            setUpCartListRecyclerView()
            updateUI(CommonUtils.getCartList())
            val item_total = CommonUtils.getCartList()?.get(0)!!.Rate
            val delivery_charge = 10
            val grand_total = item_total + delivery_charge

            tv_cart_items_total_rate.text = CommonUtils.getRupeesSymbol(this, item_total.toString())
            tv_cart_delivery_item.text = CommonUtils.getRupeesSymbol(this, delivery_charge.toString())
            tv_cart_total.text = CommonUtils.getRupeesSymbol(this, grand_total.toString())
            tv_bottom_cart_total.text = CommonUtils.getRupeesSymbol(this, grand_total.toString())
        }
    }

    private fun updateUI(cartList: ArrayList<BundlePack>?) {
        myCartAdapter.addList(cartList!!)
    }

    private fun setUpCartListRecyclerView() {
        val linearLayout = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        myCartAdapter = CartAdapter(this, R.layout.partialy_cart_list_item, adapterType = Constants.TYPE_CART_LIST_ADAPTER, adapterClickListener = this)
        rv_cart.apply {
            layoutManager = linearLayout
            adapter = myCartAdapter
            isNestedScrollingEnabled = false
        }
       // loadBundles()
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.cart_btn -> {
                navigateToCheckOutActivity()
            }
        }
    }

    override fun getCart() {
        if (NetworkStatus.isOnline2(this)) {
            showLoader()
            presenter.callGetCartApi()
        } else {
            showToastMsg(getString(R.string.error_no_internet))
        }
    }

    override fun setGetCartResp(res: CommonRes) {
        if (res.Status == Constants.SUCCESS) {
            Toast.makeText(MyApplication.context, "Get CartList is Successful", Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(MyApplication.context, "Something went wrong", Toast.LENGTH_LONG).show()
        }
    }

    override fun onclick(item: Any, pos: Int, type: String?, op: String, view: View) {
        when(type){
            Constants.REMOVE_FROM_CART->{
               /* if(item is BundlePack) {
                    item.isAdded = false
                    item.quantity=-1 //Todo quantity value is changed to 0, means item has to be remove from to cart
                    this.myCartAdapter.notifyDataSetChanged()
                    // presenter.callRemoveFromCartApi(item.ID.toString(),item.quantity.toString())
                    presenter.callRemoveFromCartApi(item)
                }*/
                CommonUtils.showConfirmationDialog(this, this, getString(R.string.do_u_want_to_remove), item, pos, view)
            }

            Constants.DELETE_CONFIRMATION -> {
                if(item is BundlePack) {
                    item.isAdded = false
                    item.quantity = -1 //Todo quantity value is changed to 0, means item has to be remove from to cart
                  /*  cartList.remove(item)
                    CommonUtils.saveCartList(cartList)
                    this.myCartAdapter.notifyDataSetChanged()
*/
                    this.myCartAdapter.notifyDataSetChanged()
                    presenter.callRemoveFromCartApi(item)
                }
            }
        }
    }

    override fun setRemoveFromCartResp(res: UpdateCartResp, item: BundlePack) {
        if (res.Status == Constants.SUCCESS) {
            item.isAdded=false
            cartList.remove(item)
            CommonUtils.saveCartList(cartList)
            this.myCartAdapter.notifyDataSetChanged()

            showToastMsg("successfully Removed")
            setUIdata()
        } else {
            showToastMsg(" Status is failed")
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
        if (base_multistateview != null)
            base_multistateview.viewState = state
    }

    private fun loadBundles() {
        val bundles = ArrayList<CartModel>()
        // for (i in 1 until 10) {
        val bundleInfo = CartModel(
            "ENGLISH",
            "200.00",
            "538.00",delivery_charge = "10.00",total = "548.00")
        val bundleInfo2 = CartModel(
            "KANNADA",
            "175.00",
            "538.00",delivery_charge = "10.00",total = "548.00")
        val bundleInfo3 = CartModel(
            "HINDI",
            "163.00",
            "538.00",delivery_charge = "10.00",total = "548.00")
        tv_cart_total.text = CommonUtils.getRupeesSymbol(this.mContext!!, bundleInfo.total!!)
        tv_cart_items_total_rate.text = CommonUtils.getRupeesSymbol(this.mContext!!, bundleInfo.item_total!!)
        tv_cart_delivery_item.text = CommonUtils.getRupeesSymbol(this.mContext!!, bundleInfo.delivery_charge!!)
        tv_cart_total.text = CommonUtils.getRupeesSymbol(this.mContext!!, bundleInfo.total!!)
        tv_bottom_cart_total.text = CommonUtils.getRupeesSymbol(this.mContext!!, bundleInfo.total!!)

        bundles.add(bundleInfo)
        bundles.add(bundleInfo2)
        bundles.add(bundleInfo3)
       // myCartAdapter.addList(bundles)
    }

    private fun navigateToCheckOutActivity() {
        val intent = Intent(context, CheckoutActivity::class.java)
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