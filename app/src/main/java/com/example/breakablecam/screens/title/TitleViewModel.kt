package com.example.breakablecam.screens.title

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import com.example.breakablecam.R

class TitleViewModel : ViewModel() {
    fun setChoosingViewSource(imageView: ImageView) {
        imageView.setImageResource(R.drawable.gallery)
    }
    fun setTakingViewSource(imageView: ImageView) {
        imageView.setImageResource(R.drawable.camera)
    }
}
