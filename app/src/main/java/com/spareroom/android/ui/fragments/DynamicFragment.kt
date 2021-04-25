package com.spareroom.android.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spareroom.android.R
import com.spareroom.android.intent.MainStateEvent
import com.spareroom.android.model.DateList
import com.spareroom.android.model.DetailList
import com.spareroom.android.model.SpareRoomModel
import com.spareroom.android.model.UpcomingList
import com.spareroom.android.ui.MainViewModel
import com.spareroom.android.ui.adapter.UpcomingEventListAdapter
import com.spareroom.android.utils.DataState
import com.spareroom.android.utils.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dynamic.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class DynamicFragment : Fragment() {

    // view model object to get data
    private val viewModel: MainViewModel by viewModels()

    // UI element building blocks
    var upcomingEventList: RecyclerView? = null
    var loadingLayout: LinearLayout? = null
    var emptyLayout: LinearLayout? = null
    var errorLayout: LinearLayout? = null
    var noConnectionLayout: LinearLayout? = null
    var offlineRetryBtn: Button? = null
    var errorRetryBtn: Button? = null
    var upcomingConsolidatedList: ArrayList<UpcomingList> = ArrayList<UpcomingList>()
    private val upcomingEventListAdapter: UpcomingEventListAdapter = UpcomingEventListAdapter(ArrayList<UpcomingList>())

    companion object {
        fun newInstance(): DynamicFragment {
            return DynamicFragment()
        }
    }
    //3
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dynamic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingLayout = view.findViewById(R.id.loading_layout)
        emptyLayout = view.findViewById(R.id.empty_layout)
        errorLayout = view.findViewById(R.id.error_layout)
        errorRetryBtn = view.findViewById<Button>(R.id.error_retry_btn)
        noConnectionLayout = view.findViewById(R.id.no_connection_layout)
        offlineRetryBtn = view.findViewById<Button>(R.id.offline_retry_btn)
        upcomingEventList = view.findViewById(R.id.upcomingEventList)
        progress_bar.visibility =  View.VISIBLE
        loadData()

        errorRetryBtn!!.setOnClickListener(View.OnClickListener { loadData() })

        offlineRetryBtn!!.setOnClickListener(View.OnClickListener { loadData() })
        upcomingEventList!!.setOnClickListener(View.OnClickListener {
            val phone = "07466887291"
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phone")
            requireContext().startActivity(intent)
        })

    }
    private fun loadData() {
        if (Util.isConnected(requireContext())) {
            upcomingEventList!!.layoutManager = LinearLayoutManager(context)
            upcomingEventList!!.adapter = upcomingEventListAdapter
            subscribeObservers()
            viewModel.setStateEvent(MainStateEvent.GetSpareRoomEvents)
        } else {
            errorLayout!!.visibility = View.GONE
            emptyLayout!!.visibility = View.GONE
            upcomingEventList!!.visibility = View.GONE
            noConnectionLayout!!.visibility = View.VISIBLE
        }
    }
    @SuppressLint("NewApi")
    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState) {
                is DataState.Success<List<SpareRoomModel>> -> {
                    if (dataState.data != null) {
                        progress_bar.visibility =  View.GONE
                        loadingLayout!!.visibility =  View.GONE
                        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
                        val iter: MutableIterator<SpareRoomModel> = dataState.data.iterator() as MutableIterator<SpareRoomModel>
                        while (iter.hasNext()) {
                            val upcomingModel: SpareRoomModel = iter.next()
                            try {
                                val date = sdf.parse(upcomingModel.start_time)
                                if (System.currentTimeMillis() > date.time) {
                                    iter.remove()
                                }
                            } catch (e: ParseException) {
                                e.printStackTrace()
                            }
                        }

                        val groupedTreeMap: TreeMap<String, MutableList<SpareRoomModel>> = groupDataIntoHashMap(dataState.data)
                        for (date in groupedTreeMap.keys) {
                            val dateItem = DateList()
                            dateItem.date=date
                            upcomingConsolidatedList.add(dateItem)
                            for (upcomingModel in groupedTreeMap[date]!!) {
                                val detailListItem = DetailList()
                                detailListItem.upcomingModel=upcomingModel
                                upcomingConsolidatedList.add(detailListItem)
                            }
                        }
                        upcomingEventList!!.visibility = View.VISIBLE
                        upcomingEventListAdapter.updateUpcomingEvents(upcomingConsolidatedList)
                    } else {
                        emptyLayout!!.visibility = View.VISIBLE
                        loadingLayout!!.visibility = View.VISIBLE
                        upcomingEventList!!.visibility = View.GONE
                    }
                }
                is DataState.Error -> {
                    progress_bar.visibility =  View.GONE
                    errorLayout!!.visibility = View.VISIBLE
                    emptyLayout!!.visibility = View.GONE
                    loadingLayout!!.visibility = View.VISIBLE
                }
                is DataState.Loading -> {
                    emptyLayout!!.visibility = View.GONE
                    upcomingEventList!!.visibility = View.GONE
                    errorLayout!!.visibility = View.GONE
                    noConnectionLayout!!.visibility = View.GONE
                }
            }
        })
    }
    override fun onDestroy() {
        super.onDestroy()
        viewModel.dataState.removeObservers(this)
    }

    private fun groupDataIntoHashMap(listOfUpcomingModel: List<SpareRoomModel>): TreeMap<String, MutableList<SpareRoomModel>> {
        val groupedTreeMap: TreeMap<String, MutableList<SpareRoomModel>> =
            TreeMap<String, MutableList<SpareRoomModel>>()
        for (upcomingModel in listOfUpcomingModel) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            var dateFormatter = SimpleDateFormat("MMMM")
            var startDate: Date? = null
            try {
                startDate = dateFormat.parse(upcomingModel.start_time)
                val date = dateFormatter.format(startDate)
                dateFormatter =
                    if (date.endsWith("01") && !date.endsWith("11")) SimpleDateFormat("MMMM") else if (date.endsWith(
                            "02"
                        ) && !date.endsWith("12")
                    ) SimpleDateFormat("MMMM") else if (date.endsWith("03") && !date.endsWith("13")) SimpleDateFormat(
                        "MMMM"
                    ) else SimpleDateFormat("MMMM")
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val hashMapKey = dateFormatter.format(startDate)
            if (groupedTreeMap.containsKey(hashMapKey)) {
                groupedTreeMap[hashMapKey]!!.add(upcomingModel)
            } else {
                val list: MutableList<SpareRoomModel> = ArrayList<SpareRoomModel>()
                list.add(upcomingModel)
                groupedTreeMap[hashMapKey] = list
            }
        }
        return groupedTreeMap
    }
}