package poc.sambitm.videoapp.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import poc.sambitm.videoapp.ui.BaseFragment

class PermissionUtils(private val context: Context) {

    companion object {
        const val PERMISSION_READ_EXTERNAL_STORAGE = 10001
        const val REQUEST_SETTINGS = 1002
    }

    fun isReadExternalStoragePermissionAvailable(): Boolean {
        val readExternalStoragePermission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        return (readExternalStoragePermission == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestReadExternalStoragePermission(
        fragment: BaseFragment
    ) {
        fragment.requestPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            PERMISSION_READ_EXTERNAL_STORAGE
        )
    }

    fun requestReadExternalStoragePermissionDialog(baseFragment: BaseFragment) {
        AlertDialog.Builder(baseFragment.requireContext())
            .setTitle("External storage read permission required")
            .setMessage("Please give permission to access your video files")
            .setPositiveButton("Allow") { dialog, which ->
                this.requestReadExternalStoragePermission(baseFragment)
                dialog.cancel()
            }
            .setCancelable(false)
            .show()
    }
}