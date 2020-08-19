package com.example.breakablecam.screens.takingPicture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.breakablecam.R
import com.example.breakablecam.databinding.ActivityTakePictureBinding
import com.google.ar.core.AugmentedFace
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.*
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.Texture
import com.google.ar.sceneform.ux.AugmentedFaceNode
import kotlinx.android.synthetic.main.activity_take_picture.*
import kotlinx.coroutines.delay
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
                setSticker1ViewSource(stickerView1)
                setBackArrowSource(backArrow)
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
                    backArrow.visibility = View.VISIBLE
                    val params =
                        faceFragmentCointanier.layoutParams as ConstraintLayout.LayoutParams
                    params.bottomToTop = R.id.makeupView1
                }
                viewModel.doneTapMakeup()
            }
        })

        viewModel.stickerTap.observe(this, Observer { check ->
            if (check == 1) {
                binding.apply {
                    makeupView.visibility = View.GONE
                    takePhotoView.visibility = View.GONE
                    stickerView.visibility = View.GONE
                    stickerView1.visibility = View.VISIBLE
                    backArrow.visibility = View.VISIBLE
                    val params =
                        faceFragmentCointanier.layoutParams as ConstraintLayout.LayoutParams
                    params.bottomToTop = R.id.stickerView1
                }
                viewModel.doneTapSticker()
            }
        })
        binding.apply {
            backArrow.setOnClickListener {
                makeupView.visibility = View.VISIBLE
                takePhotoView.visibility = View.VISIBLE
                stickerView.visibility = View.VISIBLE
                makeupView1.visibility = View.GONE
                makeupView1a.visibility = View.GONE
                backArrow.visibility = View.GONE
                stickerView1.visibility = View.GONE
                backArrow.visibility = View.GONE
                val params =
                    faceFragmentCointanier.layoutParams as ConstraintLayout.LayoutParams
                params.bottomToTop = R.id.takePhotoView
            }
        }

        arFragment = supportFragmentManager.findFragmentById(R.id.face_fragment) as FaceArFragment
        val sceneView = arFragment.arSceneView
        sceneView.cameraStreamRenderPriority = Renderable.RENDER_PRIORITY_FIRST
        val scene = sceneView.scene
        loadTexture(R.drawable.makeup1a)
        loadModel(R.raw.fox_face)
        viewModel.makeupTap1a.observe(this, Observer { check ->
            when (check) {
                1 -> {
                    scene.addOnUpdateListener {
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
                    }
                }
                2 -> {
                    val children: List<Node> =
                        ArrayList(arFragment.arSceneView.scene.children)
                    for (node in children) {
                        if (node is AnchorNode) {
                            if ((node as AnchorNode).getAnchor() != null) {
                                (node as AnchorNode).getAnchor()?.detach()
                            }
                        }
                        if (node !is Camera && node !is Sun) {
                            node.setParent(null)
                        }
                    }
                }
            }
        })
        viewModel.sticker1Tap.observe(this, Observer { check ->
            when (check) {
                1 -> {
                    scene.addOnUpdateListener {
                        val collection: Collection<AugmentedFace>? =
                            sceneView.session?.getAllTrackables(AugmentedFace::class.java)
                        collection?.forEach { face ->
                            if (!faceNodeMap.containsKey(face)) {
                                val faceNode = AugmentedFaceNode(face)
                                faceNode.apply {
                                    setParent(scene)
                                    faceRegionsRenderable = modelRenderable
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
                    }
                }
                2 -> {
                    val children: List<Node> =
                        ArrayList(arFragment.arSceneView.scene.children)
                    for (node in children) {
                        if (node is AnchorNode) {
                            if (node.getAnchor() != null) {
                                node.getAnchor()?.detach()
                            }
                        }
                        if (node !is Camera && node !is Sun) {
                            node.setParent(null)
                        }
                    }
                }
            }
        })
    }

    fun loadTexture(tex: Int) {
        Texture.builder()
            .setSource(this, tex)
            .build()
            .thenAccept { texture -> meshTexture = texture }
    }
    fun loadModel(mod: Int) {
        ModelRenderable.builder()
            .setSource(this, mod)
            .build()
            .thenAccept { model ->
                model.apply {
                    isShadowCaster = false // optional
                    isShadowReceiver = false
                }
                modelRenderable = model
            }
    }
}

