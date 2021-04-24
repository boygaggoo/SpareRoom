package com.spareroom.android.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.spareroom.android.R
import com.spareroom.android.intent.MainStateEvent
import com.spareroom.android.model.SpareRoomModel
import com.spareroom.android.ui.MainViewModel
import com.spareroom.android.utils.DataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DynamicFragment : Fragment() {
    private val viewModel: MainViewModel by viewModels()
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
        subscribeObservers()
        viewModel.setStateEvent(MainStateEvent.GetSpareRoomEvents)
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState) {
                is DataState.Success<List<SpareRoomModel>> -> {

                }
                is DataState.Error -> {

                }
                is DataState.Loading -> {

                }
            }
        })
    }
    override fun onDestroy() {
        super.onDestroy()
        viewModel.dataState.removeObservers(this)
    }
}