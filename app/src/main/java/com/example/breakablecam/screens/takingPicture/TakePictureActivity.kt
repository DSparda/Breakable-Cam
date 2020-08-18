package com.example.breakablecam.screens.takingPicture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.breakablecam.R
import com.example.breakablecam.databinding.ActivityTakePictureBinding
import com.google.ar.core.AugmentedFace
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.Texture
import com.google.ar.sceneform.ux.AugmentedFaceNode
import java.util.HashMap

class TakePictureActivity : AppCompatActivity() {

    private lateinit var viewModel: TakePictureViewModel
    private lateinit var arFragment: FaceArFragment
    var modelRenderable: ModelRenderable? = null
    private var meshTexture: Texture? = null
    private val faceNodeMap = HashMap<AugmentedFace, AugmentedFaceNode?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityTakePictureBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_take_picture)

        viewModel = ViewModelProvider(this).get(TakePictureViewModel::class.java)


        binding.apply {
            viewModel.apply {
                setStickerViewSource(stickerView)
                setMakeupViewSource(makeupView)
                setTakePhotoViewSource(takePhotoView)
                setMakeup1ViewSource(makeupView1)
                setMakeup1aViewSource(makeupView1a)
            }
        }

        binding.takePictureViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.makeupTap.observe(this, Observer { check ->
            if (check == 1) {
                binding.apply {
                    makeupView.visibility = View.GONE
                    takePhotoView.visibility = View.GONE
                    stickerView.visibility = View.GONE
                    makeupView1.visibility = View.VISIBLE
                    makeupView1a.visibility = View.VISIBLE
                    val params =
                        binding.faceFragmentCointanier.layoutParams as ConstraintLayout.LayoutParams
                    params.bottomToTop = R.id.makeupView1
                    viewModel.doneTapMakeup()
                }
            }
        })

        arFragment = supportFragmentManager.findFragmentById(R.id.face_fragment) as FaceArFragment
        val sceneView = arFragment.arSceneView
        sceneView.cameraStreamRenderPriority = Renderable.RENDER_PRIORITY_FIRST

        viewModel.makeupTap1a.observe(this, Observer { check ->
            when (check) {
                1 -> {
                    Texture.builder()
                        .setSource(this, R.drawable.makeup1a)
                        .build()
                        .thenAccept { texture -> meshTexture = texture }
                    val scene = sceneView.scene
                    val collection: Collection<AugmentedFace>? =
                        sceneView.session?.getAllTrackables(AugmentedFace::class.java)
                    collection?.forEach { face ->
                        if (!faceNodeMap.containsKey(face)) {
                            val faceNode = AugmentedFaceNode(face)
                            faceNode.apply {
                                setParent(scene)
                                faceMeshTexture = meshTexture
                            }
                            faceNodeMap[face] = faceNode
                        }
                    }

                    val iterator = faceNodeMap.entries.iterator()
                    while (iterator.hasNext()) {
                        val entry = iterator.next()
                        val face = entry.key
                        if (face.trackingState == TrackingState.STOPPED) {
                            val faceNode = entry.value
                            faceNode!!.setParent(null)
                            iterator.remove()
                        }
                    }
                    viewModel.doneTapMakeup1aView()
                }
            }
        })
    }
}

