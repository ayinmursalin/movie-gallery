package com.creativijaya.moviegallery.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtil {

    const val API_DATE_FORMAT = "yyyy-MM-dd"
    const val DISPLAY_SIMPLE_DATE_FORMAT = "dd MMM yyyy"

    private val locale by lazy {
        Locale("us", "US")
    }

    fun convertTimeStr(
        time: String,
        fromFormat: String = API_DATE_FORMAT,
        expectedFormat: String = DISPLAY_SIMPLE_DATE_FORMAT
    ): String? = callOrNull {
        translateTime(getTime(time, fromFormat), expectedFormat)
    }

    private fun translateTime(
        time: Long?,
        expectedFormat: String = DISPLAY_SIMPLE_DATE_FORMAT
    ): String? = callOrNull {
        SimpleDateFormat(expectedFormat, locale).format(Date(time.orZero()))
    }

    private fun getTime(
        dateTime: String,
        fromFormat: String = API_DATE_FORMAT
    ): Long? = callOrNull {
        SimpleDateFormat(fromFormat, locale).parse(dateTime)?.time
    }

}
