package com.example.breakablecam.screens.title

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.breakablecam.R

class TitleViewModel : ViewModel() {
    fun setChoosingViewSource(imageView: ImageView) {
        imageView.setImageResource(R.drawable.gallery)
    }
    fun setTakingViewSource(imageView: ImageView) {
        imageView.setImageResource(R.drawable.camera)
    }
    fun setWallpaperViewSource(imageView: ImageView) {
        imageView.setImageResource(R.drawable.camera_girl)
    }

    private val _checkNav = MutableLiveData<Int>()
    val checkNav: LiveData<Int>
        get() = _checkNav

    fun takingChecked() {
        _checkNav.value = 1
    }

    fun doneNav() {
        _checkNav.value = -1
    }
}
