package com.example.breakablecam.screens.takingPicture

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    fun setMakeup1ViewSource(imageView: ImageView?) {
        imageView?.setImageResource(R.drawable.makeup1)
    }

    fun setMakeup1aViewSource(imageView: ImageView?) {
        imageView?.setImageResource(R.drawable.makeup1a)
    }

    private val _makeupTap = MutableLiveData<Int>()
    val makeupTap: LiveData<Int>
        get() = _makeupTap

    fun tapMakeupView() {
        _makeupTap.value = 1
    }

    fun doneTapMakeup() {
        _makeupTap.value = -1
    }

    private val _makeupTap1a = MutableLiveData<Int>()
    val makeupTap1a: LiveData<Int>
        get() = _makeupTap1a

    fun tapMakeup1aView() {
        _makeupTap1a.value = 1
    }

    fun doneTapMakeup1aView() {
        _makeupTap1a.value = -1
    }
}
