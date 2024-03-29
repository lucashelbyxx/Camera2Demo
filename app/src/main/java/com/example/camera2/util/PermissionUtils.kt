package com.example.camera2.util

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.camera2.MainActivity
import com.example.camera2.log


object PermissionUtils {
    private val TAG = PermissionUtils::class.java.simpleName
    const val PERMISSION_REQUEST_CODE = 100
    const val PERMISSION_SETTING_CODE = 101

    var count = 0

    private var permissionExplainDialog: AlertDialog? = null
    private var permissionSettingDialog: android.app.AlertDialog? = null

    /**
     * 第一步：检查权限
     */
    fun checkPermission(activity: MainActivity, permissionList: Array<String>, callBack: Runnable) {
        var allGranted = true
        permissionList.forEach {
            var result = ContextCompat.checkSelfPermission(activity, it)
            log(TAG, "检查权限 $it, 结果 $result")

            if (result != PackageManager.PERMISSION_GRANTED) {
                allGranted = false
            }
        }

        if (allGranted) {
            Log.d(TAG+"callBack", Log.getStackTraceString(Throwable()))
            count++
            log(TAG, "callBack, 次数 $count")
            callBack.run()
        } else {
            startRequestPermission(activity, permissionList)
        }
    }

    /**
     * 第二步：如果用户之前拒绝过，显示需要权限的提示框，否则直接请求相关权限
     */
    private fun startRequestPermission(activity: AppCompatActivity, permissionList: Array<String>) {

        permissionList.forEach {
            /**
             * shouldShowRequestPermissionRationale
             * 如果应用之前请求过该权限但用户拒绝了该方法就会返回true
             *
             * 如果用户之前拒绝了权限请求并且勾选了权限请求对话框的”不再询问”，该方法会返回false，
             * 如果设备策略禁止该应用获得该权限也会返回false
             *
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, it)) {

                // 向用户显示一个解释，要以异步非阻塞的方式
                // 该线程将等待用户响应！等用户看完解释后再继续尝试请求权限
                log(TAG, "  showPermissionExplainDialog(TAG, )")
                showPermissionExplainDialog(activity, permissionList)
            } else {
                /**
                 * 当你的应用调用requestPermissions()方法时，系统会向用户展示一个标准对话框，
                 * 你的应用不能修改也不能自定义这个对话框，如果你需要给用户一些额外的信息和解释你就需要在
                 * 调用requestPermissions()之前像上面一样" 解释为什么应用需要这些权限"
                 */
                log(TAG, "requestPermission")
                requestPermission(activity, permissionList)
            }
        }
    }

    /**
     * 第三步：请求权限
     */
    private fun requestPermission(activity: AppCompatActivity, permissionList: Array<String>) {
        ActivityCompat.requestPermissions(activity, permissionList, PERMISSION_REQUEST_CODE)
    }

    /**
     * 当用户之前拒绝过，展示一个对话框，解释为什么需要权限
     */
    private fun showPermissionExplainDialog(activity: AppCompatActivity, permissionList: Array<String>) {
        if (permissionExplainDialog == null) {
            permissionExplainDialog = AlertDialog.Builder(activity).setTitle("权限申请")
                .setMessage(
                    "您刚才拒绝了相关权限，但是现在应用需要这个权限，" +
                            "点击确定申请权限，点击取消将无法使用该功能"
                )
                .setPositiveButton("确定") { dialog, _ ->
                    requestPermission(activity, permissionList)
                    dialog.cancel()
                }
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.cancel()
                }
                .create()
        }

        permissionExplainDialog?.let {
            if (!it.isShowing) {
                it.show()
            }
        }
    }

    /**
     * 最后一步，当用户拒绝并勾选了不再提示，那么只给你引导用户去设置界面打开权限
     */
    fun showPermissionSettingDialog(activity: AppCompatActivity) {
        if (permissionSettingDialog == null) {
            permissionSettingDialog = android.app.AlertDialog.Builder(activity)
                .setTitle("权限设置")
                .setMessage("您刚才拒绝了相关的权限，请到应用设置页面更改应用的权限")
                .setPositiveButton("确定") { dialog, _ ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    val uri = Uri.fromParts("package", activity.packageName, null)
                    intent.data = uri
                    activity.startActivityForResult(intent, PERMISSION_SETTING_CODE)
                    dialog.cancel()
                }
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.cancel()
                }
                .create()

        }

        permissionSettingDialog?.let {
            if (!it.isShowing) {
                it.show()
            }
        }
    }

}