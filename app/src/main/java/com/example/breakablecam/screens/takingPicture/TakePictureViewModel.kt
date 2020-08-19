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

    fun setSticker1ViewSource(imageView: ImageView?) {
        imageView?.setImageResource(R.drawable.sticker1)
    }

    fun setBackArrowSource(imageView: ImageView?) {
        imageView?.setImageResource(R.drawable.back_arrow)
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
        if (makeupTap1a.value == 1)
            _makeupTap1a.value = 2
        else _makeupTap1a.value = 1
    }

    private val _stickerTap = MutableLiveData<Int>()
    val stickerTap: LiveData<Int>
        get() = _stickerTap

    fun tapSticker() {
        _stickerTap.value = 1
    }

    fun doneTapSticker() {
        _stickerTap.value = -1
    }

    private val _sticker1Tap = MutableLiveData<Int>()
    val sticker1Tap: LiveData<Int>
        get() = _sticker1Tap

    fun tapSticker1() {
        if (sticker1Tap.value == 1)
            _sticker1Tap.value = 2
        else _sticker1Tap.value = 1
    }
}
