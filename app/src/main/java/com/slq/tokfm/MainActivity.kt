package com.slq.tokfm

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private var camera: Camera? = null
    private var cameraId = 0
    private var photoHandler: PhotoHandler? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // do we have a camera?
        if (!getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
                    .show();
        } else {
            cameraId = findFrontFacingCamera();
            if (cameraId < 0) {
                Toast.makeText(this, "No front facing camera found.",
                        Toast.LENGTH_LONG).show();
            } else {


                if (camera != null) {
                    camera?.release();
                    camera = null;
                }
                camera = Camera.open(cameraId);
            }
        }

        photoHandler = PhotoHandler(getApplicationContext())

    }

    fun listPodcasts(view: View) {
        Log.d("LIST PODCASTS", "listPodcasts called!")
        camera?.stopPreview()
        camera?.startPreview();
        camera?.takePicture(null, null, photoHandler);
    }

    private fun findFrontFacingCamera(): Int {
        var cameraId = -1
        // Search for the front facing camera
        val numberOfCameras = Camera.getNumberOfCameras()
        for (i in 0..numberOfCameras - 1) {
            val info = CameraInfo()
            Camera.getCameraInfo(i, info)
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                Log.d(DEBUG_TAG, "Camera found")
                cameraId = i
                break
            }
        }
        return cameraId
    }

    override fun onPause() {
        if (camera != null) {
            camera?.release();
            camera = null;
        }
        super.onPause();
    }

    companion object {
        val DEBUG_TAG = "MakePhotoActivity"
    }
}
