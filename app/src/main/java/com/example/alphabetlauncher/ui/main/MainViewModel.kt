package com.example.alphabetlauncher.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alphabetlauncher.data.model.AppInfo
import com.example.alphabetlauncher.data.repository.AppRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel : ViewModel() {

    private var allApps: List<AppInfo> = emptyList()

    private val _uiState = MutableStateFlow(MainUiState())

    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private var appRepository: AppRepository? = null

    init {
        startClock()
    }

    fun loadApps(context: Context) {

        if (appRepository == null) {
            appRepository = AppRepository(context.applicationContext)
        }

        val apps = appRepository!!.getLaunchableApps()

        allApps = apps

        _uiState.value = _uiState.value.copy(
            favoriteApps = apps.take(7)
        )
    }

    fun filterAppsByLetter(letter: Char) {

        val filteredApps = allApps.filter { app ->
            app.appName.firstOrNull()?.uppercaseChar() == letter
        }

        _uiState.value = _uiState.value.copy(
            favoriteApps = filteredApps
        )
    }

    fun searchApps(query: String) {

        val searchText = query.trim()

        if (searchText.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                favoriteApps = emptyList()
            )
            return
        }

        val filteredApps = allApps.filter { app ->
            app.appName.contains(
                searchText,
                ignoreCase = true
            )
        }

        _uiState.value = _uiState.value.copy(
            favoriteApps = filteredApps
        )
    }

    fun showAllApps() {

        _uiState.value = _uiState.value.copy(
            favoriteApps = allApps.take(7)
        )
    }

    private fun startClock() {

        viewModelScope.launch {

            while (true) {

                val currentTime = SimpleDateFormat(
                    "h:mm a",
                    Locale.getDefault()
                ).format(Date())

                val currentDate = SimpleDateFormat(
                    "EEE dd MMM",
                    Locale.getDefault()
                ).format(Date())

                _uiState.value = _uiState.value.copy(
                    currentTime = currentTime,
                    currentDate = currentDate
                )

                delay(1000)
            }
        }
    }
}