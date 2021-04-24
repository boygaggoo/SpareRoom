package com.spareroom.android.ui

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.tabs.TabLayout
import com.spareroom.android.R
import com.spareroom.android.ui.adapter.RoomTabAdapter
import com.spareroom.android.ui.fragments.DynamicFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    var adapter: RoomTabAdapter? = null
    var callPermissionCheck = 0
    var REQUEST_PERMISSION = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set layout based on system light or dark theme
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )
            Configuration.UI_MODE_NIGHT_NO -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        setContentView(R.layout.activity_main)

        callPermissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)

        if (callPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CALL_PHONE),
                0
            )
        }

        if (supportActionBar == null) {
            setSupportActionBar(toolbar)
        } else toolbar.visibility = View.GONE
        supportActionBar!!.setTitle(resources.getString(R.string.app_name))
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.elevation = 0f


        viewPager.offscreenPageLimit = 3
        //CreateTabFragment();

        //CreateTabFragment();
        adapter = RoomTabAdapter(supportFragmentManager, this)
        adapter!!.addFragment(DynamicFragment.newInstance(), resources.getString(R.string.tab_upcoming))
        adapter!!.addFragment(DynamicFragment.newInstance(), resources.getString(R.string.tab_archived))
        adapter!!.addFragment(DynamicFragment.newInstance(), resources.getString(R.string.tab_options))

        viewPager.adapter = adapter
        roomtablayout.setupWithViewPager(viewPager)


        highLightCurrentTab(0)
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                highLightCurrentTab(position) // for tab change
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

    }

    private fun highLightCurrentTab(position: Int) {
        for (i in 0 until roomtablayout.getTabCount()) {
            val tab: TabLayout.Tab = roomtablayout.getTabAt(i)!!
            tab.customView = null
            tab.customView = adapter!!.getTabView(i)
        }
        val tab: TabLayout.Tab = roomtablayout.getTabAt(position)!!
        tab.customView = null
        tab.customView = adapter!!.getSelectedTabView(position)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle("Roommating")
                alertDialogBuilder.setMessage("Please enable Telephone permission to make phone call")
                    .setCancelable(false)
                    .setPositiveButton(
                        "OK"
                    ) { dialog, id -> dialog.cancel() }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
        }
    }


}