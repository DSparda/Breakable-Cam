package com.example.breakablecam.screens.takingPicture

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.view.PixelCopy
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.breakablecam.R
import com.example.breakablecam.databinding.ActivityTakePictureBinding
import com.google.android.material.snackbar.Snackbar
import com.google.ar.core.AugmentedFace
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.*
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.Texture
import com.google.ar.sceneform.ux.AugmentedFaceNode
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TakePictureActivity : AppCompatActivity() {

    private lateinit var viewModel: TakePictureViewModel
    private lateinit var arFragment: FaceArFragment
    private var modelRenderable: ModelRenderable? = null
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
                    makeupView.visibility = GONE
                    takePhotoView.visibility = GONE
                    stickerView.visibility = GONE
                    makeupView1.visibility = VISIBLE
                    makeupView1a.visibility = VISIBLE
                    backArrow.visibility = VISIBLE
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
                    makeupView.visibility = GONE
                    takePhotoView.visibility = GONE
                    stickerView.visibility = GONE
                    stickerView1.visibility = VISIBLE
                    backArrow.visibility = VISIBLE
                    val params =
                        faceFragmentCointanier.layoutParams as ConstraintLayout.LayoutParams
                    params.bottomToTop = R.id.stickerView1
                }
                viewModel.doneTapSticker()
            }
        })
        binding.apply {
            backArrow.setOnClickListener {
                makeupView.visibility = VISIBLE
                takePhotoView.visibility = VISIBLE
                stickerView.visibility = VISIBLE
                makeupView1.visibility = GONE
                makeupView1a.visibility = GONE
                backArrow.visibility = GONE
                stickerView1.visibility = GONE
                backArrow.visibility = GONE
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
                            if (node.anchor != null) {
                                node.anchor?.detach()
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
                            if (node.anchor != null) {
                                node.anchor?.detach()
                            }
                        }
                        if (node !is Camera && node !is Sun) {
                            node.setParent(null)
                        }
                    }
                }
            }
        })
        binding.takePhotoView.setOnClickListener {
            takePhoto()
        }
    }

    private fun loadTexture(tex: Int) {
        Texture.builder()
            .setSource(this, tex)
            .build()
            .thenAccept { texture -> meshTexture = texture }
    }
    private fun loadModel(mod: Int) {
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
    private fun generateFilename(): String? {
        val date =
            SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                .format(Date())
        return Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        ).toString() + File.separator + "Sceneform/" + date + "_screenshot.jpg"
    }

    @Throws(IOException::class)
    private fun saveBitmapToDisk(bitmap: Bitmap, filename: String) {
        val out = File(filename)
        if (!out.parentFile.exists()) {
            out.parentFile.mkdirs()
        }
        try {
            FileOutputStream(filename).use { outputStream ->
                ByteArrayOutputStream().use { outputData ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputData)
                    outputData.writeTo(outputStream)
                    outputStream.flush()
                    outputStream.close()
                }
            }
        } catch (ex: IOException) {
            throw IOException("Failed to save bitmap to disk", ex)
        }
    }

    private fun takePhoto() {
        val filename = generateFilename()
        val view: ArSceneView = arFragment.getArSceneView()

        // Create a bitmap the size of the scene view.
        val bitmap = Bitmap.createBitmap(
            view.width, view.height,
            Bitmap.Config.ARGB_8888
        )

        // Create a handler thread to offload the processing of the image.
        val handlerThread = HandlerThread("PixelCopier")
        handlerThread.start()
        // Make the request to copy.
        PixelCopy.request(view, bitmap, { copyResult ->
            if (copyResult === PixelCopy.SUCCESS) {
                try {
                    saveBitmapToDisk(bitmap, filename!!)
                } catch (e: IOException) {
                    val toast = Toast.makeText(
                        this, e.toString(),
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    return@request
                }
                val snackbar = Snackbar.make(
                    findViewById(android.R.id.content),
                    "Photo saved", Snackbar.LENGTH_LONG
                )
                snackbar.setAction(
                    "Open in Photos"
                ) { v: View? ->
                    val photoFile = File(filename)
                    val photoURI = FileProvider.getUriForFile(
                        this,
                        this.getPackageName()
                            .toString() + ".ar.codelab.name.provider",
                        photoFile
                    )
                    val intent = Intent(Intent.ACTION_VIEW, photoURI)
                    intent.setDataAndType(photoURI, "image/*")
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivity(intent)
                }
                snackbar.show()
            } else {
                val toast = Toast.makeText(
                    this,
                    "Failed to copyPixels: $copyResult", Toast.LENGTH_LONG
                )
                toast.show()
            }
            handlerThread.quitSafely()
        }, Handler(handlerThread.looper))
    }
}

