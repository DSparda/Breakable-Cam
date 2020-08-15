package com.example.breakablecam.screens.takingPicture

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.breakablecam.R
import com.example.breakablecam.databinding.TakePictureFragmentBinding
import com.google.ar.core.ArCoreApk


class TakePictureFragment : Fragment() {
    companion object {
        const val MIN_OPENGL_VERSION = 3.0
    }

    private lateinit var viewModel: TakePictureViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: TakePictureFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.take_picture_fragment, container, false
        )

        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        fun getOpenGLVersion(activity: Activity): Double {
            val config = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            return config.deviceConfigurationInfo.glEsVersion.toDouble()
        }

        fun checkIsSupportedDeviceOrFinish(activity: Activity): Boolean {
            if (ArCoreApk.getInstance()
                    .checkAvailability(activity) == ArCoreApk.Availability.UNSUPPORTED_DEVICE_NOT_CAPABLE
            ) {
                Toast.makeText(activity, "Augmented Faces requires ArCore", Toast.LENGTH_LONG).show()
                activity.finish()
                return false
            }

            if (getOpenGLVersion(activity) < MIN_OPENGL_VERSION) {
                Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show()
                activity.finish()
                return false
            }

            return true
        }

        return binding.root
    }
}
