package com.example.breakablecam

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.ar.core.ArCoreApk

class MainActivity : AppCompatActivity() {
    companion object {
        const val MIN_OPENGL_VERSION = 3.0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkIsSupportedDeviceOrFinish(this)) {
            return
        }
        setContentView(R.layout.activity_main)

        val navController = this.findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    }


    private fun getOpenGLVersion(activity: Activity): Double {
        val config = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return config.deviceConfigurationInfo.glEsVersion.toDouble()
    }

    private fun checkIsSupportedDeviceOrFinish(activity: Activity): Boolean {
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
}
