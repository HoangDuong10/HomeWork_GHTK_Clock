package com.example.clock.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clock.model.Clock
import kotlinx.coroutines.*

class ClockViewModel : ViewModel() {

//    private var _listClock: MutableLiveData<MutableList<Clock>> = MutableLiveData(mutableListOf())
//    val listClock: LiveData<MutableList<Clock>> get() = _listClock
    private var listClock: MutableList<Clock> = mutableListOf()
    private val _position: MutableLiveData<Int> = MutableLiveData()
    val position: LiveData<Int> get() = _position
    private val _areTimersRunning: MutableLiveData<Boolean> = MutableLiveData(false)
    val areTimersRunning: LiveData<Boolean> get() = _areTimersRunning

    fun addItem(clock : Clock) {
        listClock.add(clock)
    }

    fun getListClock(): List<Clock> {
        return listClock
    }

    fun getClockAtPosition(position: Int): Clock {
        return listClock[position]
    }

    fun updateTimer(position: Int) {
        val updateClock = listClock
        if (updateClock[position].timerJob?.isActive == true) {
            updateClock[position].timerJob?.cancel()
        } else {
            val job = viewModelScope.launch {
                while (true) {
                    updateClock[position].startTime++
                    _position.value = position
                    delay(1000)
                }
            }
            updateClock[position].timerJob = job
        }
        _position.postValue(position)
    }

    fun resetTimerAtPosition(position: Int) {
        val updateClock = listClock
        updateClock[position].timerJob?.cancel()
        updateClock[position].startTime = 0
        _position.value = position

    }


    fun resetAllTimers() {
        listClock.forEachIndexed { index, _ ->
            listClock[index].timerJob?.cancel()
            listClock[index].startTime = 0
        }
        _areTimersRunning.value = false
    }


    fun updateAllTimers() {
        if (_areTimersRunning.value==true) {
           listClock.forEach { clock ->
               clock.timerJob?.cancel()
            }
        } else {
         listClock.forEachIndexed { index, clock ->
                clock.timerJob?.cancel()
                val job = viewModelScope.launch {
                    while (true) {
                        clock.startTime += 1
                        _position.value = index
                        delay(1000)
                    }
                }
                clock.timerJob = job
            }
        }
    _areTimersRunning.value = !_areTimersRunning.value!!
    }

}