package com.example.breakablecam.screens.takingPicture

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.breakablecam.R


class TakePictureFragment : Fragment() {

    companion object {
        fun newInstance() =
            TakePictureFragment()
    }

    private lateinit var viewModel: TakePictureViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.take_picture_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TakePictureViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
