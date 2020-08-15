package com.example.breakablecam.screens.takingPicture

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import com.example.breakablecam.R

class TakePictureViewModel : ViewModel() {
    fun setStickerViewSource(imageView: ImageView?) {
        imageView?.setImageResource(R.drawable.sticker)
    }
    fun setTakePhotoViewSource(imageView: ImageView?) {
        imageView?.setImageResource(R.drawable.takephoto)
    }
    fun setMakeupViewSource(imageView: ImageView?) {
        imageView?.setImageResource(R.drawable.makeup)
    }
}
