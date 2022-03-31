package com.leen.fytacodetest

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.leen.fytacodetest.databinding.ActivityMainBinding
import com.leen.fytacodetest.utils.Constants
import com.leen.fytacodetest.utils.FileUtil
import kotlinx.coroutines.*
import okhttp3.RequestBody
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var imageCapture: ImageCapture
    private lateinit var identifyPlantUseCase: IdentifyPlantUseCase
    private lateinit var fileUtil: FileUtil

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        identifyPlantUseCase = (application as MyApplication).appCompositionRoot.identifyPlantUseCase

        // Request camera permissions
        if (permissionsGranted()) {
            openCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        // Set up the listeners for take photo button
        viewBinding.btnCaptureImg.setOnClickListener { takePhoto() }

        viewBinding.btnImportImg.setOnClickListener {
            if (permissionsGranted()) {
                openGallery()
            }
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
        fileUtil = FileUtil()

    }

    private fun identifyPlant(images: RequestBody) {
        coroutineScope.launch {
            try {
                val result = identifyPlantUseCase.identifyPlant(images)
                when (result) {
                    is IdentifyPlantUseCase.Result.Success -> {
                        Log.d(TAG, "identifyPlant: ${result.plant}")
                    }
                    is IdentifyPlantUseCase.Result.Failure -> Log.d(TAG, "identifyPlant: ${result.error}")
                }
            } catch (exception: Exception) {
                throw exception
            }
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri = data!!.data
            val requestBody = identifyPlantUseCase.buildRequestBody(this, uri!!)
            identifyPlant(requestBody)
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = Constants.IMAGE_FILE_TYPE
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }

    private fun takePhoto() {

        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture

        imageCapture.takePicture(cameraExecutor, object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(imageProxy: ImageProxy) {

                val capturedImageBitmap = fileUtil.imageProxyToBitmap(imageProxy)
                val capturedImageUri = fileUtil.getImageUri(this@MainActivity, capturedImageBitmap)
                val requestBody = identifyPlantUseCase.buildRequestBody(this@MainActivity, capturedImageUri!!)

                //make api request
                identifyPlant(requestBody)

                //close image capture a new one after the request
                imageProxy.close()
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
            }
        })
    }


    private fun openCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build()

            // Select back camera as a default view
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try { // Unbind use cases before rebinding
                cameraProvider.unbindAll() // Bind use cases to camera
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun permissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode, permissions, grantResults
        ) //check the correctness of request code
        if (requestCode == REQUEST_CODE_PERMISSIONS) { //start camera if permission is granted and show toast if permission denied
            if (permissionsGranted()) {
                openCamera()
            } else {
                Toast.makeText(this, "Permissions not granted!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        coroutineScope.coroutineContext.cancelChildren()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "FytaTestApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        ).toTypedArray()
    }
}

