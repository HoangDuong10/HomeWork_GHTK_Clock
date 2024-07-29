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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[ClockViewModel::class.java]
        disPlayListClock()
        onClickAddOrPauseOrPlayOrResetAll()
        onClickPauseOrPlayOrResetAtPosition()
        viewModel.position.observe(this){position->
            val newClock = viewModel.getClockAtPosition(position)
            clockAdapter.updateClockAtPosition(position, newClock)

        }
        viewModel.areTimersRunning.observe(this) {
            val newListClock = viewModel.getListClock() // Phương thức để lấy toàn bộ danh sách Clock
            clockAdapter.updateAllItem(newListClock)

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
            viewModel.addItem(clock)
        }

        binding.btnReset.setOnClickListener{
            viewModel.resetAllTimers()
            binding.btnPlayOrPause.setImageResource(R.drawable.ic_play)
        }

        binding.btnPlayOrPause.setOnClickListener {
            viewModel.updateAllTimers()
            updateButton()
        }
    }

    private fun updateButton() {
        if(viewModel.areTimersRunning.value == true){
            binding.btnPlayOrPause.setImageResource(R.drawable.ic_pause)
        }else{
            binding.btnPlayOrPause.setImageResource(R.drawable.ic_play)
        }
    }

    private fun onClickPauseOrPlayOrResetAtPosition(){
        clockAdapter.onClickPauseOrPlay={ position, _ ->
            viewModel.updateTimer(position)
        }
//
        clockAdapter.onClickReset={ position, _ ->
            viewModel.resetTimerAtPosition(position)
        }
    }
}