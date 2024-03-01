package com.example.camera2.camera

import android.graphics.RectF
import android.hardware.camera2.params.Face
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.camera2.databinding.ActivityCamera2FaceBinding

class Camera2ActivityFace : AppCompatActivity(), Camera2HelperFace.FaceDetectListener {
    private lateinit var camera2HelperFace: Camera2HelperFace
    lateinit var binding: ActivityCamera2FaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCamera2FaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        camera2HelperFace = Camera2HelperFace(this, binding.textureView)
        camera2HelperFace.setFaceDetectListener(this)

        binding.btnTakePic.setOnClickListener { camera2HelperFace.takePic() }
        binding.ivExchange.setOnClickListener { camera2HelperFace.exchangeCamera() }
    }

    override fun onFaceDetect(faces: Array<Face>, facesRect: ArrayList<RectF>) {
        binding.faceView.setFaces(facesRect)
    }

    override fun onDestroy() {
        super.onDestroy()
        camera2HelperFace.releaseCamera()
        camera2HelperFace.releaseThread()
    }

}

