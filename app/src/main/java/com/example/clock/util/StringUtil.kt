package com.example.clock.util

import android.annotation.SuppressLint

object StringUtil {
    @SuppressLint("DefaultLocale")
     fun formatTime(seconds: Long): String {
        val minutes = (seconds / 60).toInt()
        val remainingSeconds = (seconds % 60).toInt()

        return String.format("%02d:%02d", minutes, remainingSeconds)
    }
}