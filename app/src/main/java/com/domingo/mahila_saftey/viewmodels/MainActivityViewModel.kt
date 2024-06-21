package com.domingo.mahila_saftey.viewmodels

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.domingo.mahila_saftey.Repository

class MainActivityViewModel : ViewModel() {
    fun getCurrentTime(): String {
        return Repository().getCurrentTime()
    }
}