package com.example.linussudoku

import androidx.lifecycle.MutableLiveData

actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"

    val data = MutableLiveData<Pair<Int, Int>>()
}