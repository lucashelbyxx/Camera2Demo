package com.example.camera2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.camera2.databinding.ActivityMainBinding
import com.example.camera2.util.PermissionUtils
import com.example.camera2.util.PermissionUtils.PERMISSION_REQUEST_CODE
import com.example.camera2.util.PermissionUtils.PERMISSION_SETTING_CODE
import com.example.camera2.camera.Camera2Activity
import com.example.camera2.camera.Camera2ActivityFace

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    lateinit var binding: ActivityMainBinding

    private val permissionsList = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //todo Camera2 录像实现

        binding.btCapture.setOnClickListener {
            PermissionUtils.checkPermission(this, permissionsList, Runnable {
                startActivity(Intent(this, CaptureActivity::class.java))
            })
        }

        binding.btCamera2.setOnClickListener {
            PermissionUtils.checkPermission(this, permissionsList, Runnable {
                val intent = Intent(this, Camera2Activity::class.java)
                startActivity(intent)
            })
        }

        binding.btCamera2Face.setOnClickListener {
            PermissionUtils.checkPermission(this, permissionsList, Runnable {
                startActivity(Intent(this, Camera2ActivityFace::class.java))
            })
        }

        PermissionUtils.checkPermission(this, permissionsList, Runnable {
        })

    }

    /**
     * 第四步，请求权限的结果回调
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        log(TAG, "onRequestPermissionsResult ")

        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                var allGranted = true

                grantResults.forEach {
                    if (it != PackageManager.PERMISSION_GRANTED) {
                        allGranted = false
                    }
                }

                if (allGranted) {  //已获得全部权限
                    log(TAG, "onRequestPermissionsResult 已获得全部权限")
                } else {
                    log(TAG, "权限请求被拒绝了,不能继续依赖该权限的相关操作了，展示setting ")

                    // 权限请求被拒绝了,不能继续依赖该权限的相关操作了
                    PermissionUtils.showPermissionSettingDialog(this)
                }
            }
        }
    }

    /**
     * 当从设置权限页面返回后，重新请求权限
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PERMISSION_SETTING_CODE -> { //第五步，当从设置权限页面返回后，重新请求权限
                log(TAG, "从设置权限页面返回后，重新请求权限")
                PermissionUtils.checkPermission(this, permissionsList, Runnable {
                    val intent = Intent(this, Camera2Activity::class.java)
                    startActivity(intent)
                })
            }
        }
    }
}
