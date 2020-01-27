package com.kumuda.bookshopandroid.screens.school

import android.app.Activity
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
import com.kumuda.bookshopandroid.model.SchoolRes
import com.kumuda.bookshopandroid.model.realModel.GetSchoolResponse
import com.kumuda.bookshopandroid.model.realModel.SchoolItem
import com.kumuda.bookshopandroid.model.realModel.SelectedGetSchoolResponse
import com.kumuda.bookshopandroid.screens.base.SubBaseActivity
import com.kumuda.bookshopandroid.screens.cart.CartActivity
import com.kumuda.bookshopandroid.screens.checkout.CheckoutActivity
import com.kumuda.bookshopandroid.screens.classlist.ClassListActivity
import com.kumuda.bookshopandroid.screens.home.HomeActivity
import com.kumuda.bookshopandroid.screens.myprofile.ProfileActivity
import com.kumuda.bookshopandroid.util.CommonUtils
import com.kumuda.bookshopandroid.util.Validation
import com.kumuda.bookshopandroid.util.showToastMsg
import kotlinx.android.synthetic.main.activity_school.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*


class SchoolActivity : SubBaseActivity(), IAdapterClickListener, View.OnClickListener,
    SchoolContract.View {

    private var mContext: Context? = null
    private lateinit var snackbbar: View
    private lateinit var mySchoolListAdapter: BaseRecAdapter
    lateinit var presenter: SchoolPresenter
    private lateinit var schoolListRes: GetSchoolResponse
    private lateinit var resitem: ArrayList<SchoolItem>
    private lateinit var list: ArrayList<SchoolItem>
    private var value = ArrayList<SelectedGetSchoolResponse>()
    private lateinit var source: String
    private var scList = arrayListOf<SchoolItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_school)
        layoutInflater.inflate(R.layout.activity_school, fragment_layout)
        mContext = this
        setToolBarTittle(getString(R.string.school))
        init()
    }


    override fun init() {
        intent?.apply {
            intent.extras?.apply {
                source = intent.getStringExtra(Constants.SOURCE)
            }
        }

        setUpSchoolListRecyclerView()
        updateUI()
        presenter = SchoolPresenter(this)
        snackbbar = snack_barview
        empty_button.setOnClickListener { getSchool() }
        error_button.setOnClickListener { getSchool() }

        tv_cancel.setOnClickListener(this)
        tv_ok.setOnClickListener(this)
        getSchool()
    }

    private fun updateUI() {

    }

    private fun setUpSchoolListRecyclerView() {
        val linearLayout = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mySchoolListAdapter = BaseRecAdapter(
            this,
            R.layout.partialy_scool_list_item,
            adapterType = Constants.TYPE_SCHOOL_LIST_ADAPTER,
            adapterClickListener = this
        )
        rv_school.apply {
            layoutManager = linearLayout
            adapter = mySchoolListAdapter
            isNestedScrollingEnabled = false
        }
        // loadBundles()
    }

    override fun getSchool() {
        if (NetworkStatus.isOnline2(this)) {
            showLoader()
            presenter.callGetSchoolApi()
        } else {
            showToastMsg(getString(R.string.error_no_internet))
        }
    }

    override fun setSchoolResp(res: GetSchoolResponse) {
        if (res.Status == Constants.SUCCESS) {
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            schoolListRes = res
            resitem = res.Items
            //CommonUtils.saveList(resitem)
            if (Validation.isValidList(res.Items)) {
                setupData()
            } else
                showViewState(MultiStateView.VIEW_STATE_EMPTY)
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }

    private fun setupData() {
        /*if (source == Constants.BEFORE_SEL) {
            if (Validation.isValidList(loginresitem)) {
                mySchoolListAdapter.addList(resitem)
            }
        } else if (source == Constants.AFTER_SEL) {
            if (Validation.isValidList(CommonUtils.getSavedList())) {
                mySchoolListAdapter.addList(CommonUtils.getSavedList()!!)
            }
        } *//*else {
            if (Validation.isValidList(schoolListRes.Items)) {
                mySchoolListAdapter.addList(schoolListRes.Items!!)
            }
        }*/

        if (Validation.isValidList(schoolListRes.Items)) {
            if (CommonUtils.getSavedList()?.isNotEmpty() != null) {
                scList = CommonUtils.getSavedList()!!
                mySchoolListAdapter.addList(CommonUtils.getSavedList()!!)
            } else {
                scList = schoolListRes.Items
                mySchoolListAdapter.addList(schoolListRes.Items)
            }
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

    override fun onclick(item: Any, pos: Int, type: String?, op: String, view: View) {
        if (Validation.isValidObject(item) && type == Constants.TYPE_SCHOOL_LIST_ADAPTER) {
            if (item is SchoolItem) {
                scList.get(pos).isSelected = item.isSelected
                //TODO here i commented toast message.
                //toast("Hello", Toast.LENGTH_LONG)
            }
        }
    }

    private fun navigateToProfile(list: ArrayList<SchoolItem>) {
        val intent = Intent(context, ProfileActivity::class.java)
        intent.apply {
            putExtra(Constants.SOURCE, Constants.SOURCE_School)
           // putExtra(Constants.ProfileID, CommonUtils.getProfileID())
            putExtra(Constants.SELECTED_CHECK_BOX, list)
        }
        startActivity(intent)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.tv_cancel -> {
                for (sc in scList) {
                        sc.isSelected = false
                }
                CommonUtils.saveList(scList)
                //Toast.makeText(context, "Unselected", Toast.LENGTH_LONG).show()
                navigateToProfileActivity()
            }

            R.id.tv_ok -> {

/*                for (i in 0 until rv_school.childCount) {
                    scList[i].isSelected = rv_school.getChildAt(i).isSelected
                }*/
                var count = 0
                for (sc in scList) {
                    if (sc.isSelected) count++
                }
            //    Toast.makeText(context, "THE COUNT IS" + count, Toast.LENGTH_LONG).show()
                CommonUtils.saveList(scList)
                navigateToProfileActivity()
            }
        }
    }

    private fun navigateToProfileActivity() {
        val intent = Intent(context, ProfileActivity::class.java)
        setResult(Activity.RESULT_OK, intent)
        finish()
        /*intent.apply {
            putExtra(Constants.SOURCE, Constants.SOURCE_School)
            putExtra(Constants.SELECTED_CHECK_BOX, list)

        }*/
    }

    private fun loadBundles() {
        val bundles = ArrayList<SchoolRes>()
        // for (i in 1 until 10) {
        val schoolres1 = SchoolRes(
            "1",
            "Learn by Art school",
            "Marathahalli,Banglore"
        )
        val schoolres2 = SchoolRes(
            "2",
            "Hiriya prethamika school kasaragodu",
            "kasaragodu"
        )
        val schoolres3 = SchoolRes(
            "3",
            "Sainika School ",
            "vijayapura"
        )
        val schoolres4 = SchoolRes(
            "4",
            "GN National Public School",
            "Karwar"
        )
        val schoolres5 = SchoolRes(
            "5",
            "Goverenment school",
            "Manjari"
        )
        val schoolres6 = SchoolRes(
            "6",
            "Green Park central school",
            "Mysore"
        )
        val schoolres7 = SchoolRes(
            "7",
            "R.D.S School",
            "Manjari "
        )

        bundles.add(schoolres1)
        bundles.add(schoolres2)
        bundles.add(schoolres3)
        bundles.add(schoolres4)
        bundles.add(schoolres5)
        bundles.add(schoolres6)
        bundles.add(schoolres7)
        mySchoolListAdapter.addList(bundles)
    }

    private fun navigateToClassListActivity() {
        val intent = Intent(context, ClassListActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToCartActivity() {
        val intent = Intent(context, CartActivity::class.java)
        intent.putExtra(Constants.SOURCE,Constants.SCHOOL)
        startActivity(intent)
    }

    private fun navigateToCheckoutActivity() {
        val intent = Intent(context, CheckoutActivity::class.java)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, HomeActivity::class.java))
                intent.putExtra(Constants.SOURCE, Constants.SCHOOL)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}