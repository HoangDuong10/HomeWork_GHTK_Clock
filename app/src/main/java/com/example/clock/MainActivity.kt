package com.example.clock

import com.example.clock.viewmodel.ClockViewModel
import com.example.clock.model.Clock
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.clock.adapter.ClockAdapter
import com.example.clock.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel: ClockViewModel
    val mutableMap: MutableMap<Int, Clock> = mutableMapOf()
    var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[ClockViewModel::class.java]
        val linearLayoutManager = LinearLayoutManager(this)
        binding.rcvClickClock.layoutManager = linearLayoutManager
        val dividerItemDecoration = DividerItemDecoration(this,DividerItemDecoration.VERTICAL)
        binding.rcvClickClock.addItemDecoration(dividerItemDecoration)
        val clockAdapter = ClockAdapter()
        (binding.rcvClickClock.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.rcvClickClock.adapter = clockAdapter
        binding.btnAdd.setOnClickListener{
            val clock = Clock()
            clockAdapter.addItem(clock)
            mutableMap.put(position,clock)
            position++
        }
        clockAdapter.onClickBtn={position,clock ->
            mutableMap[position]?.let { viewModel.toggleTimer(mutableMap,position) }
           // Log.d("tag111",""+viewModel.time.value)
        }

        viewModel.clockUpdates.observe(this, Observer { map ->
            map.forEach { (position, clock) ->
                clockAdapter.updateClockAtPosition(position, clock)
            }
        })
        clockAdapter.onClickReset={position ->
            viewModel.resetTimerAtPosition(position)
        }

        binding.btnReset.setOnClickListener{
            viewModel.resetAllTimers(mutableMap)
            binding.btnPlayOrPause.setImageResource(R.drawable.ic_play)
        }
        binding.btnPlayOrPause.setOnClickListener {
            viewModel.toggleAllTimers(mutableMap)
            updateStartStopButtonText()
        }

    }

    private fun updateStartStopButtonText() {
       if(viewModel.areTimersRunning){
           binding.btnPlayOrPause.setImageResource(R.drawable.ic_pause)
       }else{
           binding.btnPlayOrPause.setImageResource(R.drawable.ic_play)
       }
    }
}