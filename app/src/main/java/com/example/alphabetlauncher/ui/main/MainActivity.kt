package com.example.alphabetlauncher.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.alphabetlauncher.databinding.ActivityMainBinding
import com.example.alphabetlauncher.ui.SearchActivity
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.LinearLayoutManager


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    private lateinit var appAdapter: AppAdapter

    private var swipeStartY = 0f
    private var swipeEndY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupAlphabetBar()

        viewModel.loadApps(this)

        observeUiState()
    }

    private fun setupRecyclerView() {
        appAdapter = AppAdapter { appInfo ->

            val launchIntent =
                packageManager.getLaunchIntentForPackage(
                    appInfo.packageName
                )

            if (launchIntent != null) {
                startActivity(launchIntent)
            }
        }

        binding.rvApps.layoutManager = LinearLayoutManager(this)
        binding.rvApps.adapter = appAdapter
    }

    private fun setupAlphabetBar() {
        binding.alphabetBar.onLetterSelected = { letter ->
            viewModel.filterAppsByLetter(letter)
        }

        binding.alphabetBar.setOnTouchListener { _, event ->

            if (event.action == MotionEvent.ACTION_UP) {
                viewModel.showAllApps()
            }

            false
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                swipeStartY = event.y
            }

            MotionEvent.ACTION_UP -> {
                swipeEndY = event.y

                val swipeDistance = swipeStartY - swipeEndY

                val screenWidth = resources.displayMetrics.widthPixels

                val alphabetBarArea =
                    screenWidth * 0.80f

                val startedFromAlphabetBar =
                    event.x > alphabetBarArea

                if (swipeDistance > 150 && !startedFromAlphabetBar) {

                    val intent = Intent(
                        this,
                        SearchActivity::class.java
                    )

                    startActivity(intent)
                }
            }
        }

        return super.dispatchTouchEvent(event)
    }
    private fun observeUiState() {

        lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiState.collect { state ->

                    binding.tvTime.text = state.currentTime
                    binding.tvDate.text = state.currentDate
                    appAdapter.submitList(state.favoriteApps)
                }
            }
        }
    }
}