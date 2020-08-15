package com.example.breakablecam.screens.takingPicture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.breakablecam.R
import com.example.breakablecam.databinding.TakePictureFragmentBinding


class TakePictureFragment : Fragment() {
    private lateinit var viewModel: TakePictureViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: TakePictureFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.take_picture_fragment, container, false
        )

        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        return binding.root
    }
}
