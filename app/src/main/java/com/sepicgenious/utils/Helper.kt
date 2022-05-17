package com.sepicgenious.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class Helper(val context: Context) {

    @SuppressLint("HardwareIds")
    fun getDeviceId(): String? {
        return Settings.Secure.getString(
            context.getContentResolver(), Settings.Secure.ANDROID_ID
        )
    }

    companion object {

    }
}
