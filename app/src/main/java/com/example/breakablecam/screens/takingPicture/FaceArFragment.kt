package com.example.breakablecam.screens.takingPicture

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.ux.ArFragment
import java.util.*

class FaceArFragment : ArFragment() {

    override fun getSessionConfiguration(session: Session?): Config {
        return Config(session).apply { augmentedFaceMode = Config.AugmentedFaceMode.MESH3D }
    }

    override fun getSessionFeatures(): MutableSet<Session.Feature> {
        return EnumSet.of(Session.Feature.FRONT_CAMERA)
    }
    override fun getAdditionalPermissions(): Array<String?>? {
        val additionalPermissions = super.getAdditionalPermissions()
        val permissionLength =
            additionalPermissions?.size ?: 0
        val permissions =
            arrayOfNulls<String>(permissionLength + 1)
        permissions[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE
        if (permissionLength > 0) {
            System.arraycopy(
                additionalPermissions,
                0,
                permissions,
                1,
                additionalPermissions!!.size
            )
        }
        return permissions
    }

    /**
     * Override to turn off planeDiscoveryController. Plane trackables are not supported with the
     * front camera.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val layout = super.onCreateView(inflater, container, savedInstanceState) as FrameLayout
        planeDiscoveryController.apply {
            hide()
            setInstructionView(null)
        }
        return layout
    }
}