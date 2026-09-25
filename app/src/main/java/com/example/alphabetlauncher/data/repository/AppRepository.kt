package com.example.alphabetlauncher.data.repository

import android.content.Context
import android.content.Intent
import com.example.alphabetlauncher.data.model.AppInfo

class AppRepository(
    private val context: Context
) {

    fun getLaunchableApps(): List<AppInfo> {

        val packageManager = context.packageManager

        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val launchableApps = packageManager.queryIntentActivities(
            intent,
            0
        )

        return launchableApps
            .map { resolveInfo ->

                val appInfo = resolveInfo.activityInfo.applicationInfo

                AppInfo(
                    appName = packageManager
                        .getApplicationLabel(appInfo)
                        .toString(),

                    packageName = appInfo.packageName,

                    icon = packageManager
                        .getApplicationIcon(appInfo)
                )
            }
            .distinctBy { it.packageName }
            .sortedBy { it.appName.lowercase() }
    }
}