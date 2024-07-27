package com.example.clock.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clock.model.Clock
import kotlinx.coroutines.*

class ClockViewModel : ViewModel() {
//    private val _data = MutableLiveData<MutableList<Clock>>()
//    val data: LiveData<MutableList<Clock>> get() = _data

    private val _clockUpdates: MutableLiveData<Map<Int, Clock>> = MutableLiveData(emptyMap())
    val clockUpdates: LiveData<Map<Int, Clock>> get() = _clockUpdates
    var areTimersRunning = false

    fun updateTimer(map: Map<Int, Clock>, position: Int) {
        if (map[position]?.timerJob?.isActive == true) {
            map[position]?.timerJob?.cancel()
        } else {
            val job = viewModelScope.launch {
                while (true) {
                    map[position]?.let {
                        it.startTime++
                    }
                    _clockUpdates.value = map
                    delay(1000)
                }
            }
            map[position]?.timerJob = job
        }
        _clockUpdates.postValue(map)
    }
    fun resetTimerAtPosition(position: Int) {
        val currentMap = _clockUpdates.value ?:return
        val item = currentMap[position]

        item?.timerJob?.cancel()
        item?.startTime = System.currentTimeMillis()
        item?.startTime = 0
        _clockUpdates.postValue(currentMap)
    }

    fun resetAllTimers(mutableMap: MutableMap<Int, Clock>) {
        mutableMap.keys.toList().forEach { index ->
            mutableMap[index]?.timerJob?.cancel()
            mutableMap[index]?.startTime = 0
        }
        areTimersRunning = false
        _clockUpdates.postValue(mutableMap)
    }

    fun toggleAllTimers(map: MutableMap<Int, Clock>) {
        if (areTimersRunning) {
            map.keys.toList().forEach { index ->
                map[index]?.timerJob?.cancel()
                map[index]?.startTime = 0
            }
        } else {
            map.values.forEach{  clock ->
                if (clock.timerJob?.isActive != true) {
                    clock.timerJob?.cancel()
                    val job = viewModelScope.launch {
                        while (true) {
                            clock.startTime += 1
                            delay(1000)
                            _clockUpdates.value = map
                        }
                    }
                    clock.timerJob = job
                }
            }
        }
        areTimersRunning = !areTimersRunning
        _clockUpdates.value = map
    }


}