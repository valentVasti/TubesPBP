package com.example.tubespbp

import android.annotation.SuppressLint
import android.hardware.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import java.lang.Exception

class CameraActivity : AppCompatActivity() {

    private var mCamera: Camera? = null
    private var mCameraView: CameraView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        try {
            mCamera = Camera.open()
        } catch (e: Exception) {
            Log.d("Error", "Failed to get camera" + e.message)
        }
        if (mCamera != null) {
            mCameraView = CameraView(this, mCamera!!)
            val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
            camera_view.addView(mCameraView)
        }

        @SuppressLint("MissingInflatedId", "LocalSuppress")
        val imageClose = findViewById<View>(R.id.imgClose) as ImageButton
        imageClose.setOnClickListener { view: View? -> System.exit(0) }

    }
}