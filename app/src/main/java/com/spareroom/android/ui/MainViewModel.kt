package com.spareroom.android.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.spareroom.android.intent.MainStateEvent
import com.spareroom.android.model.SpareRoomModel
import com.spareroom.android.repository.MainRepository
import com.spareroom.android.utils.DataState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.Instant

class MainViewModel
@ViewModelInject
constructor(
    private val mainRepository: MainRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _dataState: MutableLiveData<DataState<List<SpareRoomModel>>> = MutableLiveData()

    val dataState: LiveData<DataState<List<SpareRoomModel>>>
        get() = _dataState

    fun setStateEvent(mainStateEvent: MainStateEvent) {
        viewModelScope.launch {
            when(mainStateEvent) {

                is MainStateEvent.GetSpareRoomEvents -> {
                    mainRepository.getSpareRoomEvents()
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }

                is MainStateEvent.None -> {
                    //When nothing happen.
                }

            }
        }
    }

}
