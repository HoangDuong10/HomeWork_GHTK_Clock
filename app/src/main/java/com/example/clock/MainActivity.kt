package com.example.clock

import com.example.clock.viewmodel.ClockViewModel
import com.example.clock.model.Clock
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.clock.adapter.ClockAdapter
import com.example.clock.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel: ClockViewModel
    private lateinit var clockAdapter : ClockAdapter
    private val clockMap: MutableMap<Int, Clock> = mutableMapOf()
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[ClockViewModel::class.java]
        disPlayListClock()
        onClickAddOrPauseOrPlayOrResetAll()
        onClickPauseOrPlayOrResetAtPosition()
        viewModel.clockUpdates.observe(this) { map ->
            map.forEach { (position, clock) ->
                clockAdapter.updateClockAtPosition(position, clock)
            }
        }

    }

    private fun updateButton() {
       if(viewModel.areTimersRunning){
           binding.btnPlayOrPause.setImageResource(R.drawable.ic_pause)
       }else{
           binding.btnPlayOrPause.setImageResource(R.drawable.ic_play)
       }
    }

    private fun disPlayListClock(){
        val linearLayoutManager = LinearLayoutManager(this)
        binding.rcvClickClock.layoutManager = linearLayoutManager
        val dividerItemDecoration = DividerItemDecoration(this,DividerItemDecoration.VERTICAL)
        binding.rcvClickClock.addItemDecoration(dividerItemDecoration)
        clockAdapter = ClockAdapter()
        (binding.rcvClickClock.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.rcvClickClock.adapter = clockAdapter
    }

    private fun onClickAddOrPauseOrPlayOrResetAll(){
        binding.btnAdd.setOnClickListener{
            val clock = Clock()
            clockAdapter.addItem(clock)
            clockMap[position] = clock
            position++
        }

        binding.btnReset.setOnClickListener{
            viewModel.resetAllTimers(clockMap)
            binding.btnPlayOrPause.setImageResource(R.drawable.ic_play)
        }

        binding.btnPlayOrPause.setOnClickListener {
            viewModel.updateAllTimers(clockMap)
            updateButton()
        }
    }

    private fun onClickPauseOrPlayOrResetAtPosition(){
        clockAdapter.onClickPauseOrPlay={ position, _ ->
            clockMap[position]?.let { viewModel.updateTimer(clockMap,position) }
        }


        clockAdapter.onClickReset={ position, _ ->
            viewModel.resetTimerAtPosition(position)
        }
    }
}