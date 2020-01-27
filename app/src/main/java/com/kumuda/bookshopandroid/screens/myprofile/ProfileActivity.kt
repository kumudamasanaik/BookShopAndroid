package com.kumuda.bookshopandroid.screens.myprofile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumuda.bookshopandroid.R
import com.kumuda.bookshopandroid.api.NetworkStatus
import com.kumuda.bookshopandroid.commonadapter.BaseRecAdapter
import com.kumuda.bookshopandroid.constants.Constants
import com.kumuda.bookshopandroid.customviews.MultiStateView
import com.kumuda.bookshopandroid.listener.IAdapterClickListener
import com.kumuda.bookshopandroid.model.SchoolRes
import com.kumuda.bookshopandroid.model.inputmodel.SaveProfileInput
import com.kumuda.bookshopandroid.model.realModel.GetProfileDetailResponse
import com.kumuda.bookshopandroid.model.realModel.SaveProfileResp
import com.kumuda.bookshopandroid.model.realModel.SchoolItem
import com.kumuda.bookshopandroid.model.realModel.SelectedGetSchoolResponse
import com.kumuda.bookshopandroid.screens.base.SubBaseActivity
import com.kumuda.bookshopandroid.screens.classlist.ClassListActivity
import com.kumuda.bookshopandroid.screens.home.HomeActivity
import com.kumuda.bookshopandroid.screens.school.SchoolActivity
import com.kumuda.bookshopandroid.util.CommonUtils
import com.kumuda.bookshopandroid.util.Validation
import com.kumuda.bookshopandroid.util.showToastMsg
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.et_mobile_num
import kotlinx.android.synthetic.main.activity_profile.et_name
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.add_school_layoyt.*
import kotlinx.android.synthetic.main.app_bar_home.*

class ProfileActivity : SubBaseActivity(), View.OnClickListener,IAdapterClickListener,ProfileContract.View {
    private lateinit var school_name: String
    private lateinit var class_name: String
    private var mContext: Context? = null
    private lateinit var mySelectedSchoolListAdapter: BaseRecAdapter
    var schoolRes: SelectedGetSchoolResponse? = null
    private var source: String? = ""
    private lateinit var profile_id: String
    private var list: ArrayList<String>? = null
    private var value = ArrayList<SelectedGetSchoolResponse>()
    private lateinit var profileInput: SaveProfileInput
    lateinit var presenter: ProfilePresenter
    private lateinit var getProfileResp: GetProfileDetailResponse

    //Todo i need to set
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_profile)
        layoutInflater.inflate(R.layout.activity_profile, fragment_layout)
        mContext = this
        setToolBarTittle(getString(R.string.PROFILE))


        init()
    }

    override fun init() {
        presenter= ProfilePresenter(this)

        intent?.apply {
            intent.extras?.apply {
                source = intent.getStringExtra(Constants.SOURCE)
                profile_id = intent.getStringExtra(Constants.ProfileID)
            }
        }

        setUpSelectedSchoolListRecyclerView()
        btn_continue_profile.setOnClickListener(this)
        add_school_hyper_link_layout.setOnClickListener(this)
        add_school_image.setOnClickListener(this)

        if (source == Constants.REGISTRATION_SOURCE) {
            if (Validation.isValidList(CommonUtils.getSavedList())) {
                updateUI()
                add_school_hyper_link_layout.visibility =View.GONE
                list_layout.visibility = View.VISIBLE
            }
            else {
                add_school_hyper_link_layout.visibility =View.VISIBLE
                list_layout.visibility = View.GONE
            }
        }

        else if (source == Constants.HOME) {
            if (Validation.isValidList(CommonUtils.getSavedList())) {
                val listOfSchool = CommonUtils.getSavedList()
                var count: Int = 0
                var scList = arrayListOf<SchoolItem>()
                if (listOfSchool?.isNotEmpty()!!) {
                    for (list in listOfSchool) {
                        if (list.isSelected) {
                            count++
                            scList.add(list)
                        }
                    }
                }
                if (count > 0) {
                    list_layout.visibility = View.VISIBLE
                    add_school_hyper_link_layout.visibility = View.GONE
                    mySelectedSchoolListAdapter.addList(scList)
                } else {
                    list_layout.visibility = View.GONE
                }
            }
            else{
                add_school_hyper_link_layout.visibility = View.VISIBLE
            }
        }


        /* if (source==Constants.SOURCE_School){
             intent?.apply {
                 intent.extras?.apply {
                     source = intent.getStringExtra(Constants.SOURCE)
                     list=intent.getStringArrayListExtra(Constants.SELECTED_CHECK_BOX)
                 }
             }

             if (list.isNullOrEmpty()){
                 add_school_hyper_link_layout.visibility=View.VISIBLE
                 list_layout.visibility=View.GONE
             }
             else{
                 list_layout.visibility=View.VISIBLE
                 add_school_hyper_link_layout.visibility=View.GONE
             }
             setUpSelectedSchoolListRecyclerView()
             updateUI()
             btn_continue_profile.setOnClickListener(this)
             add_school_hyper_link_layout.setOnClickListener(this)
             add_school_image.setOnClickListener(this)
         }
        else{
             setUpSelectedSchoolListRecyclerView()
             updateUI()
             btn_continue_profile.setOnClickListener(this)
             add_school_hyper_link_layout.setOnClickListener(this)
             add_school_image.setOnClickListener(this)
         }
  */
        updateUI()
        getProfile()

    }

    private fun setUpSelectedSchoolListRecyclerView() {
        val linearLayout = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mySelectedSchoolListAdapter = BaseRecAdapter(
            this,
            R.layout.partialy_selected_scool_list_item,
            adapterType = Constants.TYPE_SCHOOL_LIST_ADAPTER,
            adapterClickListener = this
        )
        rv_school_list.apply {
            layoutManager = linearLayout
            adapter = mySelectedSchoolListAdapter
            isNestedScrollingEnabled = false
        }
    }

    override fun getProfile() {
        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            presenter.callGetProfileApi(profile_id)
        } else {
            showToastMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setGetProfileResp(res: GetProfileDetailResponse) {
        if (res.Status== Constants.SUCCESS){
            hideLoader()
            getProfileResp=res
            CommonUtils.setProfileDetail(getProfileResp)
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
          //  Toast.makeText(MyApplication.context, " GEt Profile is Successful", Toast.LENGTH_SHORT).show()
            setdata()
        }
        else
            showViewState(MultiStateView.VIEW_STATE_EMPTY)
    }

    private fun setdata() {
        et_mobile_num.setText(CommonUtils.getMobileNumber().toString())
        et_mobile_num.isEnabled = false

        et_email_id.setText(CommonUtils.getEmailAddress())
        et_email_id.isEnabled = false

        et_name.setText(CommonUtils.getProfilename())
        et_name.isEnabled = true

        et_address1.setText(CommonUtils.getAddress1())
        et_address1.isEnabled = true

        et_address2.setText(CommonUtils.getAddress2())
        et_city.setText(CommonUtils.getCity())
        et_state.setText(CommonUtils.getState())
        et_postal_code.setText(CommonUtils.getPostalCode())
    }

    override fun saveProfile() {
        profileInput = SaveProfileInput(
            ProfileID = CommonUtils.getProfileID(),
            ProfileName =et_name.text.toString(),
            EmailAddress = CommonUtils.getEmailAddress(),
            SchoolID = "4",
            ClassID = "5",
            Address1 = et_address1.text.toString(),
            Address2 = et_address2.text.toString(),
            City =et_city.text.toString() ,
            State =et_state.text.toString() ,
            PostalCode =et_postal_code.text.toString()
        )
        if (presenter.validate(profileInput)) {
            if (NetworkStatus.isOnline2(this)) {
                showLoader()
                presenter.callSaveProfileApi(profileInput)
            }
            else {
                Toast.makeText(this, getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun setSaveProfileResp(res: SaveProfileResp) {
        if (res.Status == Constants.SUCCESS) {
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
           // Toast.makeText(MyApplication.context, "Profile is Successfully Saved", Toast.LENGTH_SHORT).show()
            navigateToHomeScreen()
        } else{
            showViewState(MultiStateView.VIEW_STATE_EMPTY)
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }

    override fun showSignupValidateErrorMsg(msg: String) {
        when (msg) {
            "1" -> et_name.error = getString(R.string.err_please_enter_name)
            "2" -> et_mobile_num.error = getString(R.string.err_please_enter_valid_mobile)
            "3" -> et_otp.error = getString(R.string.err_please_enter_valid_otp)
            "4" -> et_address1.error = getString(R.string.err_please_enter_address)
            "5" -> et_address2.error = getString(R.string.err_please_enter_address)
            "6" -> et_city.error = getString(R.string.err_please_enter_city_name)
            "7" -> et_state.error = getString(R.string.err_please_enter_state)
            "8" -> et_postal_code.error = getString(R.string.err_please_enter_pin_code)
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

    private fun updateUI() {
        if (Validation.isValidList(CommonUtils.getSavedList())) {
            var fullList = CommonUtils.getSavedList()
            var selList = arrayListOf<SchoolItem>()
            for (list in fullList!!) {
                if (list.isSelected)
                    selList.add(list)
            }
            mySelectedSchoolListAdapter.addList(selList)
        }
    }

    private fun loadData() {
        if (list == null || list.toString().isEmpty()) {
            add_school_hyper_link_layout.visibility = View.VISIBLE
            list_layout.visibility = View.GONE
        } else {
            list_layout.visibility = View.VISIBLE
            add_school_hyper_link_layout.visibility = View.GONE
            val bundles = ArrayList<SchoolRes>()
            val schoolres1 = SchoolRes(
                "1",
                "Learn by Art school",
                "Marathahalli,Banglore", isSelected = true
            )
            val schoolres2 = SchoolRes(
                "1",
                "Hiriya prethamika school kasaragodu",
                "kasaragodu", isSelected = true
            )
            val schoolres3 = SchoolRes(
                "1",
                "Sainika School ",
                "vijayapura", isSelected = true
            )
            bundles.add(schoolres1)
            bundles.add(schoolres2)
            bundles.add(schoolres3)
            mySelectedSchoolListAdapter.addList(bundles)
        }
    }

    override fun onclick(item: Any, pos: Int, type: String?, op: String, view: View) {
        navigateToSchoolScreen()
    }

    private fun navigateToSchoollist() {
        val intent = Intent(context, SchoolActivity::class.java)
        intent.apply {
            putExtra(Constants.SOURCE, Constants.MY_PROFILE)
            putExtra(Constants.SELECTED_CHECK_BOX, list)
        }
        startActivity(intent)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_continue_profile -> {
                saveProfile()
                // navigateToHomeScreen()
            }

            R.id.add_school_hyper_link_layout -> {
                navigateToSchoolScreen()
            }

            R.id.add_school_image -> {
                navigateToSchoolScreen()
            }
        }
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(context, HomeActivity::class.java)
        intent.putExtra(Constants.SOURCE, Constants.MY_PROFILE)
        startActivity(intent)
        finish()
    }

    private fun navigateToSchoolScreen() {
        val intent = Intent(context, SchoolActivity::class.java)
        intent.apply {
            putExtra(Constants.SOURCE, Constants.BEFORE_SEL)
        }
        startActivityForResult(intent, Constants.ADD_SCHOOL)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.ADD_SCHOOL && resultCode == Activity.RESULT_OK) {
            val listOfSchool = CommonUtils.getSavedList()
            var count : Int = 0
            var scList = arrayListOf<SchoolItem>()
            if (listOfSchool?.isNotEmpty()!!) {
                for (list in listOfSchool) {
                    if (list.isSelected) {
                        count++
                        scList.add(list)
                    }
                }
            }

            if (count > 0) {
                list_layout.visibility = View.VISIBLE
                add_school_hyper_link_layout.visibility = View.GONE
                mySelectedSchoolListAdapter.addList(scList)
            } else {
                list_layout.visibility = View.GONE
                add_school_hyper_link_layout.visibility = View.VISIBLE
            }
        }
    }

    private fun navigateToSchoolScreen(type: String) {
        val intent = Intent(context, SchoolActivity::class.java)
        intent.apply {
            if (type.equals(Constants.BEFORE_SEL))
                putExtra(Constants.SOURCE, Constants.BEFORE_SEL)
            else putExtra(Constants.SOURCE, Constants.AFTER_SEL)

        }
        startActivity(intent)
    }

    private fun navigateToClassListScreen() {
        val intent = Intent(context, ClassListActivity::class.java)
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