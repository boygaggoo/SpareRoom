package com.spareroom.android.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
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
    var upcomingConsolidatedList: ArrayList<UpcomingList> = ArrayList<UpcomingList>()
    private val upcomingEventListAdapter: UpcomingEventListAdapter =
        UpcomingEventListAdapter(ArrayList<UpcomingList>())

    companion object {
        fun newInstance(): DynamicFragment {
            return DynamicFragment()
        }
    }

    //3
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dynamic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        loadData()

        error_retry_btn!!.setOnClickListener { alertUser() }

        offline_retry_btn!!.setOnClickListener { alertUser() }

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(OnRefreshListener { // Your code to refresh the list here.
            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            loadDataLive()
        })
    }

    private fun loadData() {
        progress_bar.visibility = View.VISIBLE
        if (Util.isConnected(requireContext())) {
            loadDataLive()
        } else {
            // if there is no data in local db as well, show no internet
            loadDataLocal()
        }
    }

    private fun loadDataLive() {
        upcomingEventList!!.layoutManager = LinearLayoutManager(context)
        upcomingEventList!!.adapter = upcomingEventListAdapter
        subscribeObservers()
        viewModel.setStateEvent(MainStateEvent.GetSpareRoomEvents)
    }

    private fun loadDataLocal() {
        upcomingEventList!!.layoutManager = LinearLayoutManager(context)
        upcomingEventList!!.adapter = upcomingEventListAdapter
        subscribeObservers()
        viewModel.setStateEvent(MainStateEvent.GetSpareRoomLocalDbEvents)
    }

    @SuppressLint("NewApi")
    private fun subscribeObservers() {
        viewModel.liveDataState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<List<SpareRoomModel>> -> {
                    if (dataState.data != null) {
                        swipeContainer.setRefreshing(false);
                        progress_bar.visibility = View.GONE
                        loading_layout!!.visibility = View.GONE
                        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
                        val iter: MutableIterator<SpareRoomModel> =
                            dataState.data.iterator() as MutableIterator<SpareRoomModel>
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

                        val groupedTreeMap: TreeMap<String, MutableList<SpareRoomModel>> =
                            groupDataIntoHashMap(dataState.data)
                        for (date in groupedTreeMap.keys) {
                            val dateItem = DateList()
                            dateItem.date = date
                            upcomingConsolidatedList.add(dateItem)
                            for (upcomingModel in groupedTreeMap[date]!!) {
                                val detailListItem = DetailList()
                                detailListItem.upcomingModel = upcomingModel
                                upcomingConsolidatedList.add(detailListItem)
                            }
                        }
                        upcomingEventList!!.visibility = View.VISIBLE
                        upcomingEventListAdapter.updateUpcomingEvents(upcomingConsolidatedList)
                    } else {
                        empty_layout!!.visibility = View.VISIBLE
                        loading_layout!!.visibility = View.VISIBLE
                        upcomingEventList!!.visibility = View.GONE
                    }
                }
                is DataState.Error -> {
                    swipeContainer.setRefreshing(false);
                    progress_bar.visibility = View.GONE
                    error_layout!!.visibility = View.VISIBLE
                    empty_layout!!.visibility = View.GONE
                    loading_layout!!.visibility = View.VISIBLE
                }
                is DataState.Loading -> {
                    empty_layout!!.visibility = View.GONE
                    upcomingEventList!!.visibility = View.GONE
                    error_layout!!.visibility = View.GONE
                    no_connection_layout!!.visibility = View.GONE
                }
            }
        })

        viewModel.localDbDataState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<List<SpareRoomModel>> -> {
                    if (dataState.data.size > 0) {
                        progress_bar.visibility = View.GONE
                        loading_layout!!.visibility = View.GONE
                        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
                        val iter: MutableIterator<SpareRoomModel> =
                            dataState.data.iterator() as MutableIterator<SpareRoomModel>
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

                        val groupedTreeMap: TreeMap<String, MutableList<SpareRoomModel>> =
                            groupDataIntoHashMap(dataState.data)
                        for (date in groupedTreeMap.keys) {
                            val dateItem = DateList()
                            dateItem.date = date
                            upcomingConsolidatedList.add(dateItem)
                            for (upcomingModel in groupedTreeMap[date]!!) {
                                val detailListItem = DetailList()
                                detailListItem.upcomingModel = upcomingModel
                                upcomingConsolidatedList.add(detailListItem)
                            }
                        }
                        upcomingEventList!!.visibility = View.VISIBLE
                        upcomingEventListAdapter.updateUpcomingEvents(upcomingConsolidatedList)
                    } else {
                        error_layout!!.visibility = View.GONE
                        empty_layout!!.visibility = View.GONE
                        upcomingEventList!!.visibility = View.GONE
                        no_connection_layout!!.visibility = View.VISIBLE
                        progress_bar.visibility = View.GONE
                    }
                }
                is DataState.Error -> {
                    progress_bar.visibility = View.GONE
                    error_layout!!.visibility = View.VISIBLE
                    empty_layout!!.visibility = View.GONE
                    loading_layout!!.visibility = View.VISIBLE
                }
                is DataState.Loading -> {
                    empty_layout!!.visibility = View.GONE
                    upcomingEventList!!.visibility = View.GONE
                    error_layout!!.visibility = View.GONE
                    no_connection_layout!!.visibility = View.GONE
                }
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.liveDataState.removeObservers(this)
        viewModel.localDbDataState.removeObservers(this)
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

    fun alertUser() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Androidly Alert")
        builder.setMessage("We have a message")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton("Local Data") { dialog, which ->
            loadDataLocal()
        }

        builder.setNegativeButton("Live Data") { dialog, which ->
            loadDataLive()
        }

        builder.setNeutralButton("Close") { dialog, which ->

        }
        builder.show()
    }
}