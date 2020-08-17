package com.example.breakablecam.screens.takingPicture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.breakablecam.R
import com.example.breakablecam.databinding.TakePictureFragmentBinding

class TakePictureFragment : Fragment() {
    private lateinit var viewModel: TakePictureViewModel
    private lateinit var arFragment: FaceArFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: TakePictureFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.take_picture_fragment, container, false
        )

        (activity as AppCompatActivity).supportActionBar?.hide()

        viewModel = ViewModelProvider(this).get(TakePictureViewModel::class.java)

        binding.apply {
            viewModel.setStickerViewSource(stickerView)
            viewModel.setMakeupViewSource(makeupView)
            viewModel.setTakePhotoViewSource(takePhotoView)
            takePictureViewModel = viewModel
        }

        val fragmentManager = activity?.supportFragmentManager

        if (fragmentManager?.findFragmentById(R.id.face_fragment) != null)
            arFragment = fragmentManager.findFragmentById(R.id.face_fragment) as FaceArFragment

        return binding.root
    }
}
