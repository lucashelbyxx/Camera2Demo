package com.example.camera2.camera

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.camera2.databinding.ActivityCamera2Binding

class Camera2Activity : AppCompatActivity() {

    private lateinit var mCamera2Helper: Camera2Helper
    lateinit var binding: ActivityCamera2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCamera2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        mCamera2Helper = Camera2Helper(this, binding.textureView)

        binding.btnTakePic.setOnClickListener { mCamera2Helper.takePic() }
        binding.ivExchange.setOnClickListener { mCamera2Helper.exchangeCamera() }
    }

    override fun onDestroy() {
        super.onDestroy()
        mCamera2Helper.releaseCamera()
        mCamera2Helper.releaseThread()
    }
}

