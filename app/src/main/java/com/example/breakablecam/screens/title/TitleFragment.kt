package com.example.breakablecam.screens.title

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.breakablecam.R
import com.example.breakablecam.databinding.TitleFragmentBinding

class TitleFragment : Fragment() {
    private lateinit var viewModel: TitleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: TitleFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.title_fragment, container, false
        )
        viewModel = ViewModelProvider(this).get(TitleViewModel::class.java)

        binding.apply {
            viewModel.setChoosingViewSource(choosingView)
            viewModel.setTakingViewSource(takingView)
            viewModel.setWallpaperViewSource(wallpaperView)
            titleViewModel = viewModel
        }

        binding.lifecycleOwner = this

        viewModel.checkNav.observe(viewLifecycleOwner, Observer { check ->
            when(check) {
                1 -> {
                    this.findNavController()
                        .navigate(TitleFragmentDirections.actionTitleFragmentToTakePictureActivity())
                    viewModel.doneNav()
                }
            }
        })
        return binding.root
    }
}
