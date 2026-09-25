package com.example.alphabetlauncher.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.alphabetlauncher.R
import com.example.alphabetlauncher.databinding.ActivitySearchBinding
import com.example.alphabetlauncher.ui.main.AppAdapter
import com.example.alphabetlauncher.ui.main.MainViewModel
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val viewModel: MainViewModel by viewModels()

    private lateinit var appAdapter: AppAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        viewModel.loadApps(this)

        observeUiState()

        binding.etSearch.requestFocus()

        window.setSoftInputMode(
            android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        )

        binding.etSearch.addTextChangedListener(
            object : android.text.TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    viewModel.searchApps(s.toString())
                }

                override fun afterTextChanged(
                    s: android.text.Editable?
                ) {
                }
            }
        )

        overridePendingTransition(
            R.anim.slide_in_bottom,
            R.anim.fade_out
        )
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

        binding.rvSearchApps.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this)

        binding.rvSearchApps.adapter = appAdapter
    }

    private fun observeUiState() {

        lifecycleScope.launch {

            viewModel.uiState.collect { state ->

                appAdapter.submitList(state.favoriteApps)
            }
        }
    }

    override fun finish() {
        super.finish()

        overridePendingTransition(
            R.anim.fade_in,
            R.anim.slide_out_bottom
        )
    }
}