package com.tony.appbooster.presentation.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.tony.appbooster.R
import com.tony.appbooster.domain.model.settings.AppOptimizationType
import com.tony.appbooster.presentation.activity.MainActivity
import com.tony.appbooster.presentation.worker.AnalysisWorker
import com.tony.appbooster.presentation.worker.OptimizationWorker

/**
 * AppWidgetProvider for OptiDroid home screen widget.
 * Provides interactive Scan and Optimize controls directly from the home screen.
 */
class OptiDroidWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            ACTION_SCAN -> {
                AnalysisWorker.enqueue(context, AppOptimizationType.SPEED_PROFILE)
                updateAllWidgets(context)
            }
            ACTION_OPTIMIZE -> {
                OptimizationWorker.enqueue(context, AppOptimizationType.SPEED_PROFILE)
                updateAllWidgets(context)
            }
            ACTION_STOP -> {
                OptimizationWorker.cancel(context)
                AnalysisWorker.cancel(context)
                updateAllWidgets(context)
            }
        }
    }

    companion object {
        const val ACTION_SCAN = "com.tony.appbooster.widget.ACTION_SCAN"
        const val ACTION_OPTIMIZE = "com.tony.appbooster.widget.ACTION_OPTIMIZE"
        const val ACTION_STOP = "com.tony.appbooster.widget.ACTION_STOP"

        fun updateAllWidgets(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, OptiDroidWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
            for (appWidgetId in appWidgetIds) {
                updateWidget(context, appWidgetManager, appWidgetId)
            }
        }

        private fun updateWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.optidroid_widget_layout)

            // Open MainActivity on title click
            val mainIntent = Intent(context, MainActivity::class.java)
            val mainPendingIntent = PendingIntent.getActivity(
                context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_title, mainPendingIntent)

            // Scan action pending intent
            val scanIntent = Intent(context, OptiDroidWidgetProvider::class.java).apply {
                action = ACTION_SCAN
            }
            val scanPendingIntent = PendingIntent.getBroadcast(
                context, 101, scanIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_btn_scan, scanPendingIntent)

            // Optimize action pending intent
            val optimizeIntent = Intent(context, OptiDroidWidgetProvider::class.java).apply {
                action = ACTION_OPTIMIZE
            }
            val optimizePendingIntent = PendingIntent.getBroadcast(
                context, 102, optimizeIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_btn_optimize, optimizePendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
