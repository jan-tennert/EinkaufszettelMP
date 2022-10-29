package io.github.jan.einkaufszettel.common.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import java.io.File

actual class UpdateInstaller(private val context: Context) {

    @SuppressLint("QueryPermissionsNeeded")
    actual fun install(file: File) {
        context.getExternalFilesDir(null)!!.absolutePath
        val intent = Intent(Intent.ACTION_VIEW)
        val downloadedApk = FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            file
        )
        intent.setDataAndType(downloadedApk, "application/vnd.android.package-archive")
        val resInfoList: List<ResolveInfo> = context.packageManager
            .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            context.grantUriPermission(
                context.applicationContext.packageName.toString() + ".provider",
                downloadedApk,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivity(context, intent, null)
    }

}