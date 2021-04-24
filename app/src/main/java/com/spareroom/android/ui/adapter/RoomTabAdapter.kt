package com.spareroom.android.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.spareroom.android.R
import java.util.*

class RoomTabAdapter internal constructor(fm: FragmentManager?, private val context: Context) :
    FragmentStatePagerAdapter(fm!!) {
    private val mFragmentList: MutableList<Fragment> = ArrayList()
    private val mFragmentTitleList: MutableList<String> = ArrayList()
    var tabLayout: TabLayout? = null
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }

    fun getTabView(position: Int): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        val tabTextView = view.findViewById<TextView>(R.id.tabTextView)
        tabTextView.text = mFragmentTitleList[position]
        tabTextView.setTextColor(ContextCompat.getColor(context, R.color.translucent_white_48))
        return view
    }

    fun getSelectedTabView(position: Int): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        val tabTextView = view.findViewById<TextView>(R.id.tabTextView)
        tabTextView.text = mFragmentTitleList[position]
        tabTextView.setTextColor(ContextCompat.getColor(context, R.color.white))
        return view
    }
}
