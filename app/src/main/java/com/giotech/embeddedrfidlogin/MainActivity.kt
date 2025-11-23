package com.giotech.embeddedrfidlogin

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.giotech.embeddedrfidlogin.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val adapter = RfidAdapter(
        onClick = { entry ->
            Snackbar.make(binding.root, entry.name, Snackbar.LENGTH_SHORT).show()
        },
        onNewItem = {
            binding.recycler.post {
                binding.recycler.smoothScrollToPosition(0)
            }
        }
    )
    private val entries = mutableListOf<RfidEntry>()

    private val sheetRepository = SheetRepository()
    private var refreshJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setContentView(binding.root)

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter
        binding.recycler.itemAnimator?.apply {
            addDuration = 300
            removeDuration = 300
            moveDuration = 300
            changeDuration = 300
        }
    }

    private fun startAutoRefresh() {
        // Cancel any existing job
        refreshJob?.cancel()

        refreshJob = lifecycleScope.launch {
            while (isActive == true) {
                try {
                    if (!binding.progressIndicator.isVisible) {
                        binding.progressIndicator.visibility = View.VISIBLE

                        val response = sheetRepository.getEntries()
                        Log.d("AutoRefresh", "startAutoRefresh: ${response.size}")
                        entries.clear()
                        entries.addAll(response)
                        adapter.submitList(entries.toList()) // make a copy for ListAdapter

                        binding.progressIndicator.visibility = View.GONE
                    }
                } catch (e: Exception) {
                    Log.e("AutoRefresh", "Failed to refresh", e)
                    binding.progressIndicator.visibility = View.GONE
                }

                delay(500L) // .5-second interval
            }
        }
    }

    private fun stopAutoRefresh() {
        refreshJob?.cancel()
        refreshJob = null
    }

    override fun onResume() {
        super.onResume()
        startAutoRefresh() // start the periodic refresh
    }

    override fun onPause() {
        super.onPause()
        stopAutoRefresh() // stop when Activity is paused
    }

}
