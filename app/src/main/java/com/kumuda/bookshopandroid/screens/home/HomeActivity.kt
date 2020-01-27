package com.kumuda.bookshopandroid.screens.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumuda.bookshopandroid.MyApplication
import com.kumuda.bookshopandroid.R
import com.kumuda.bookshopandroid.api.NetworkStatus
import com.kumuda.bookshopandroid.commonadapter.BaseRecAdapter
import com.kumuda.bookshopandroid.constants.Constants
import com.kumuda.bookshopandroid.customviews.MultiStateView
import com.kumuda.bookshopandroid.listener.IAdapterClickListener
import com.kumuda.bookshopandroid.listener.SelectedListClickListener
import com.kumuda.bookshopandroid.model.inputmodel.HomeResp
import com.kumuda.bookshopandroid.model.realModel.GetOrderResp
import com.kumuda.bookshopandroid.model.realModel.GetProfileDetailResponse
import com.kumuda.bookshopandroid.model.realModel.MyOrderItem
import com.kumuda.bookshopandroid.model.realModel.SchoolItem
import com.kumuda.bookshopandroid.screens.base.SubBaseActivity
import com.kumuda.bookshopandroid.screens.changepassword.ChangePasswordActivity
import com.kumuda.bookshopandroid.screens.classlist.ClassListActivity
import com.kumuda.bookshopandroid.screens.myprofile.ProfileActivity
import com.kumuda.bookshopandroid.screens.notification.NotificationActivity
import com.kumuda.bookshopandroid.screens.order.OrderActivity
import com.kumuda.bookshopandroid.util.CommonUtils
import com.kumuda.bookshopandroid.util.Validation
import com.kumuda.bookshopandroid.util.showToastMsg
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.partial_profile_detail.*

class HomeActivity : SubBaseActivity(), View.OnClickListener, SelectedListClickListener, IAdapterClickListener,HomeContract.View {

    private val TAG = "HomeActivity"
    private var mContext: Context? = null

    private lateinit var source: String
    private lateinit var snackbbar: View
    private var homeres: HomeResp? = null
    private  lateinit var dialog: AlertDialog
    lateinit var presenter: HomePresenter
    private lateinit var getProfileResp: GetProfileDetailResponse
    private lateinit var orderList:ArrayList<MyOrderItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_home)
         layoutInflater.inflate(R.layout.activity_home, fragment_layout)
        mContext = this@HomeActivity
        /* setToolBarTittle("")
         hideToolBarImage()
         hideCartView()
         showMenu()*/

        toolbar_layout_id.visibility=View.GONE
        presenter= HomePresenter(this)
        init()
    }

    override fun onResume() {
        super.onResume()
       //getOrder()
        getProfile()
    }

    override fun init() {
        intent?.apply {
            intent.extras?.apply {
                source = intent.getStringExtra(Constants.SOURCE)
            }
        }



        make_order_layout.setOnClickListener(this)
        order_layout.setOnClickListener(this)
        notification_layout.setOnClickListener(this)
        change_password_layout.setOnClickListener(this)
        my_profile_layout.setOnClickListener(this)
        sign_out_layout.setOnClickListener(this)
       // UpdateUI()
        //getProfile()
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
            orderList=res.Items
           CommonUtils.saveMyOrderedList(orderList)
           // Toast.makeText(MyApplication.context, " GET Order is Successful", Toast.LENGTH_LONG).show()
        }
        else {
            showViewState(MultiStateView.VIEW_STATE_EMPTY)
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }

    override fun getProfile() {
        if (NetworkStatus.isOnline2(this)) {
            showLoader()
            presenter.callGetProfileApi(CommonUtils.getProfileID())
        } else {
            showToastMsg(getString(R.string.error_no_internet))
        }
    }

    override fun setGetProfileResp(res: GetProfileDetailResponse) {
        if (res.Status== Constants.SUCCESS){
            hideLoader()
            getProfileResp=res
            CommonUtils.setProfileDetail(getProfileResp)
            //Toast.makeText(MyApplication.context, " GEt Profile is Successful", Toast.LENGTH_LONG).show()
           // UpdateUI()
            tv_profile_name.text = getProfileResp.ProfileName
            user_address.text=getProfileResp.Address1
            tv_email.text=getProfileResp.EmailAddress
            user_mob_num.text=getProfileResp.MobileNumber.toString()
        }
        else
            Toast.makeText(MyApplication.context, " Something went wrong", Toast.LENGTH_LONG).show()
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

    private fun UpdateUI() {
        tv_profile_name.text = CommonUtils.getProfilename()
        user_address.text=CommonUtils.getAddress1()
        tv_email.text=CommonUtils.getEmailAddress()
        user_mob_num.text=CommonUtils.getMobileNumber().toString()
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.make_order_layout -> {
                if (Validation.isValidList(CommonUtils.getSavedList())) {
                    val listOfSchool = CommonUtils.getSavedList()
                    var count: Int = 0
                    var scList = arrayListOf<SchoolItem>()
                    if (listOfSchool?.isNotEmpty()!!) {
                        for (list in listOfSchool) {
                            if (list.isSelected) {
                                count++
                            }
                        }
                    }
                    if (count > 0) {
                        showDialog(this)
                    }
                    else{
                       // CommonUtils.showCustomToast(context!!, getString(R.string.Please_add_school), 1)
                        Toast.makeText(MyApplication.context, getString(R.string.Please_add_school), Toast.LENGTH_LONG).show()
                    }
                }
                else
                Toast.makeText(MyApplication.context, getString(R.string.Please_add_school), Toast.LENGTH_LONG).show()
            }

            R.id.order_layout -> {
                navigateToOrderActivity()
            }

            R.id.notification_layout -> {
                navigateToNotificationActivity()
            }

            R.id.change_password_layout -> {
                navigateToChangePasswordActivity(CommonUtils.getMobileNumber().toString())
            }

            R.id.my_profile_layout -> {
                navigateToMyProfileActivity()
            }

            R.id.sign_out_layout -> {
                if (CommonUtils.getProfileID().isNotEmpty()) {
                    CommonUtils.showDialog(this.context!!, getString(R.string.do_u_want_to_logout))
                } else {
                 //   CommonUtils.showCustomToast(context!!, getString(R.string.Please_login_to_the_application), 1)
                    Toast.makeText(MyApplication.context, "Please Login to the application", Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    override fun onclick(item: Any, type: String, op: String) {
        when (op) {
            Constants.SELECTED_CHECK_BOX -> {
                // navigateToClassActivity()
            }
        }
    }

    private fun navigateToOrderActivity() {
        val intent = Intent(context, OrderActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToNotificationActivity() {
        val intent = Intent(context, NotificationActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToChangePasswordActivity(mobileNumber: String) {
        val intent = Intent(context, ChangePasswordActivity::class.java)
        intent.putExtra(Constants.SOURCE,Constants.HOME)
        intent.putExtra(Constants.MobileNumber,mobileNumber)
        startActivity(intent)
    }

    private fun navigateToClassActivity() {
        val intent = Intent(context, ClassListActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToMyProfileActivity() {
        val intent = Intent(context, ProfileActivity::class.java)
        intent.putExtra(Constants.SOURCE,Constants.HOME)
        intent.putExtra(Constants.ProfileID,CommonUtils.getProfileID())
        startActivity(intent)
    }

  /*  override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }*/

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    fun showDialog(context: Context) {
//        val schoolListres = ArrayList<SchoolRes>()
//        schoolListres.add(SchoolRes("1", "Learn by Art school", "Marathahalli,Banglore"))
//        schoolListres.add(SchoolRes("1", "Hiriya prethamika school kasaragodu", "1st Floor, Workshaala Spaces, No 57, 12th Main Rd, Sector 6, HSR Layout, Bengaluru, Karnataka 560102,"))
//        schoolListres.add(SchoolRes("1", "Sainika School ", "vijayapura"))

        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dailogview = inflater.inflate(R.layout.partial_list, null)

        val schoolName = dailogview.findViewById<TextView>(R.id.tv_school_name)
        val location = dailogview.findViewById<TextView>(R.id.tv_school_location)
        val recyclerView = dailogview.findViewById<RecyclerView>(R.id.school_rec_view)

        val adapter: BaseRecAdapter = BaseRecAdapter(context, R.layout.item_school_list, Constants.TYPE_SCHOOL_LIST_ADAPTER, this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        //Todo adding selected items into dialogue
        val selList = arrayListOf<SchoolItem>()
        if (Validation.isValidList(CommonUtils.getSavedList())) {
            val fullList = CommonUtils.getSavedList()
            for (list in fullList!!) {
                if (list.isSelected)
                    selList.add(list)
            }
        }
        adapter.addList(selList)

        dialog = builder.create()
        dialog.setView(dailogview)
        dialog.show()
    }

    override fun onclick(item: Any, pos: Int, type: String?, op: String, view: View) {
        dialog.dismiss()
        if (item is SchoolItem){
            navigatingClassActivity(item.ID.toString())
        }
    }

    private fun navigatingClassActivity(id: String) {
        val intent = Intent(context, ClassListActivity::class.java)
        intent.putExtra(Constants.SOURCE,Constants.HOME)
        intent.putExtra(Constants.SCHOOL_ID,id)
        startActivity(intent)
    }

}