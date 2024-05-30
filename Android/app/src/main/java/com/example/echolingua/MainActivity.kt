package com.example.echolingua

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.view.Surface
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.UseCaseGroup
import androidx.camera.core.ViewPort
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.echolingua.ui.navigator.MyNavigator
import com.example.echolingua.ui.page.MainTranslatePage
import com.example.echolingua.ui.theme.EchoLinguaTheme
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID
import java.util.concurrent.TimeUnit

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {

    private var imageCapture: ImageCapture? = null

    private var cameraProvider: ProcessCameraProvider? = null

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EchoLinguaTheme {
                MyNavigator()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        pickMedia.unregister()
    }

    private fun <I, O> ComponentActivity.registerActivityResultLauncher(
        contract: ActivityResultContract<I, O>, callback: ActivityResultCallback<O>
    ): ActivityResultLauncher<I> {
        val key = UUID.randomUUID().toString()
        return activityResultRegistry.register(key, contract, callback)
    }

    fun pickMedia(
        onSuccess: (Uri) -> Unit = {},
        onFailure: () -> Unit = {},
        visualMediaType: ActivityResultContracts.PickVisualMedia.VisualMediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
    ) {
        pickMedia =
            registerActivityResultLauncher(contract = ActivityResultContracts.PickVisualMedia(),
                callback = { result ->
                    result?.let {
                        onSuccess(it)
                    } ?: onFailure()
                })
        pickMedia.launch(
            PickVisualMediaRequest(
                visualMediaType
            )
        )
    }

    fun takePhoto(
        onImageSavedCallback: (File) -> Unit = {}
    ) {
        val name = SimpleDateFormat(
            "yyyy-MM-dd-HH-mm-ss-SSS", Locale.US
        ).format(System.currentTimeMillis())
        val imageFile = File.createTempFile(name, ".jpeg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(imageFile).build()

        imageCapture?.takePicture(outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Log.d(TAG, msg)
                    onImageSavedCallback(imageFile)
                }
            })
    }

    fun startCamera(width: Int, height: Int) {
        val previewView = findViewById<PreviewView>(R.id.camera_view)
        previewView.visibility = View.VISIBLE

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(
            {
                val viewPort = ViewPort.Builder(Rational(width, height), Surface.ROTATION_0).build()
                cameraProvider = cameraProviderFuture.get()
                imageCapture = ImageCapture.Builder().build()
                imageCapture?.let {
                    bindPreview(cameraProvider, it, previewView, viewPort)
                }
            }, ContextCompat.getMainExecutor(this)
        )

    }

    private fun bindPreview(
        cameraProvider: ProcessCameraProvider?,
        imageCapture: ImageCapture,
        previewView: PreviewView,
        viewPort: ViewPort
    ) {
        val preview = androidx.camera.core.Preview.Builder().build()

        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

        preview.setSurfaceProvider(previewView.getSurfaceProvider())

        val useCaseGroup = UseCaseGroup.Builder().addUseCase(preview).addUseCase(imageCapture)
            .setViewPort(viewPort).build()

        val camera = cameraProvider?.bindToLifecycle(
            this as LifecycleOwner, cameraSelector, useCaseGroup
        )

        val cameraControl = camera?.cameraControl

        cameraControl?.let { initCameraListeners(it) }
    }

    private fun initCameraListeners(cameraControl: CameraControl) {
        val previewView = findViewById<PreviewView>(R.id.camera_view)

        previewView.setOnTouchListener { view, motionEvent ->
            view.performClick()

            val meteringPoint =
                previewView.meteringPointFactory.createPoint(motionEvent.x, motionEvent.y)

            val action = FocusMeteringAction.Builder(
                meteringPoint, FocusMeteringAction.FLAG_AF
            ).apply {
                setAutoCancelDuration(2, TimeUnit.SECONDS)
            }.build()

            val result = cameraControl.startFocusAndMetering(action)

            return@setOnTouchListener result.isDone
        }
    }

    fun stopCamera() {
        cameraProvider?.unbindAll()
        val previewView = findViewById<PreviewView>(R.id.camera_view)
        previewView.visibility = View.GONE
    }

}