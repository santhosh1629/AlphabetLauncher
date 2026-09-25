package com.example.alphabetlauncher.ui.main

import com.example.alphabetlauncher.data.model.AppInfo

data class MainUiState(
    val currentTime: String = "",
    val currentDate: String = "",
    val favoriteApps: List<AppInfo> = emptyList()
)