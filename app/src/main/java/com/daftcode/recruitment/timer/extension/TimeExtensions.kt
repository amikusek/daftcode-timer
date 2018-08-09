package com.daftcode.recruitment.timer.extension

import java.util.*
import java.util.concurrent.TimeUnit

fun Long.getFormattedTimeFromMillis() = "${TimeUnit.MILLISECONDS.toSeconds(this).getNumberWithLeadingZeros()}:${(this - TimeUnit.MILLISECONDS.toSeconds(this) * TimeUnit.SECONDS.toMillis(1)).getNumberWithLeadingZeros()}"

fun Long.getNumberWithLeadingZeros() = String.format(Locale.US, "%02d", this).substring(0, 2)
