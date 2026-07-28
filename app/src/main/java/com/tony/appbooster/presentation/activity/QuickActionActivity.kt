package com.tony.appbooster.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.tony.appbooster.domain.model.settings.AppOptimizationType
import com.tony.appbooster.presentation.worker.AnalysisWorker
import com.tony.appbooster.presentation.worker.OptimizationWorker
import dagger.hilt.android.AndroidEntryPoint

/**
 * Lightweight translucent activity handling quick actions from shortcuts, tiles, or widgets.
 * Triggers background work and finishes immediately without taking over the screen.
 */
@AndroidEntryPoint
class QuickActionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (intent?.action) {
            ACTION_SCAN -> {
                AnalysisWorker.enqueue(applicationContext, AppOptimizationType.SPEED_PROFILE)
                Toast.makeText(this, "OptiDroid: Scanning apps...", Toast.LENGTH_SHORT).show()
            }
            ACTION_OPTIMIZE -> {
                OptimizationWorker.enqueue(applicationContext, AppOptimizationType.SPEED_PROFILE)
                Toast.makeText(this, "OptiDroid: Optimizing apps...", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val mainIntent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                startActivity(mainIntent)
            }
        }

        finish()
    }

    companion object {
        const val ACTION_SCAN = "com.tony.appbooster.ACTION_SCAN"
        const val ACTION_OPTIMIZE = "com.tony.appbooster.ACTION_OPTIMIZE"
    }
}
