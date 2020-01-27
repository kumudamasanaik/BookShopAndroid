package com.kumuda.bookshopandroid.screens.base

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.AppBarLayout
import com.kumuda.bookshopandroid.MyApplication
import com.kumuda.bookshopandroid.R
import com.kumuda.bookshopandroid.constants.Constants
import com.kumuda.bookshopandroid.screens.cart.CartActivity
import com.kumuda.bookshopandroid.screens.home.HomeActivity
import com.kumuda.bookshopandroid.util.CommonUtils
import com.kumuda.bookshopandroid.util.SharedPreferenceManager
import kotlinx.android.synthetic.main.activity_sub_base.*
import kotlinx.android.synthetic.main.tool_bar_with_back.*

abstract class SubBaseActivity : AppCompatActivity() {
    lateinit var context: Context
    private var sharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_base)
        initScreen()
    }

    private fun initScreen() {
        context = this
        drawer_layout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        //showBack()
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setToolbarScrollFlags()
        setSupportActionBar(my_tool_bar)
        setToolBarTittle(getString(R.string.app_name))
        hideSearchView()
        updateCartCount()

        ic_cart.setOnClickListener {
            // startActivity(Intent(this, CartActivity::class.java))
            val intent = Intent(context, CartActivity::class.java)
            intent.putExtra(Constants.SOURCE,Constants.SubBaseActivity)
            startActivity(intent)
            //finish()
        }

        /* search_view.setOnClickListener {
             startActivity(Intent(this, SearchActivity::class.java))
         }*/

        my_tool_bar_image.setOnClickListener {
            //startActivity(Intent(this, HomeActivity::class.java))
            val intent = Intent(MyApplication.context, HomeActivity::class.java)
            intent.putExtra(Constants.SOURCE, Constants.SubBaseActivity)
            startActivity(intent)
            finish()
        }
    }

    fun showMenu() {
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_three_line)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
    }

    private fun hideSearchView() {
        //search_view!!.visibility = View.GONE
    }
    fun hideToolBarImage() {
        my_tool_bar_image!!.visibility = View.GONE
    }

    fun showSearchView() {
        //search_view!!.visibility = View.VISIBLE
    }

    private fun setToolbarScrollFlags(setFlags: Boolean = false) {
        val params: AppBarLayout.LayoutParams = my_tool_bar.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = if (setFlags) AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS else 0
    }

    fun setToolBarTittle(title: String?) {
        tv_screen_title.text = title
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawer_layout!!.openDrawer(GravityCompat.START)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun hideBackbtn() {
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_navigation)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    /*  fun showBack() {
          supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_navigation)
          supportActionBar!!.setDisplayHomeAsUpEnabled(true)
          supportActionBar!!.setDisplayShowTitleEnabled(true)
      }
  */

    fun showCartView() {
        ic_cart!!.visibility = View.VISIBLE
    }

    fun hideCartView() {
        cart_layout!!.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        registerSharedPreferenceChangeListener()
        updateCartCount()
    }

    private fun registerSharedPreferenceChangeListener() {
        sharedPreferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { preferences, key ->
            key?.let {
                if (it.contentEquals(SharedPreferenceManager.PREF_CART_DATA)) {
                    updateCartCount()
                }
            }
        }
        SharedPreferenceManager.getPrefs().registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
    }

   fun updateCartCount() {
       CommonUtils.getCartList()?.apply {
           val cart_count=CommonUtils.getCartList()?.size

           if (cart_count!=null && cart_count.toInt() > 0) {
               cart_badge.text = cart_count.toString()
               cart_badge.visibility = View.VISIBLE
               // cart_badge.BlinkAnimation()
           } else
               cart_badge.visibility = View.GONE
       }
   }

    override fun onPause() {
        super.onPause()
        unregisterSharedPreferenceChangeListener()
    }

    private fun unregisterSharedPreferenceChangeListener() {
        SharedPreferenceManager.getPrefs().unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
    }
}