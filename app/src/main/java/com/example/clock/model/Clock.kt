package com.example.clock.model

import kotlinx.coroutines.Job

data class Clock(
    var startTime: Long = 0L,
    var timerJob: Job? = null,
)