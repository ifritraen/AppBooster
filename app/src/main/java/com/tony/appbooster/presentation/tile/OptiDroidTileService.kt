package com.tony.appbooster.presentation.tile

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.tony.appbooster.domain.model.settings.AppOptimizationType
import com.tony.appbooster.presentation.worker.OptimizationWorker

/**
 * Quick Settings Tile service for OptiDroid.
 * Allows users to trigger app optimization directly from the Android Quick Settings panel.
 */
class OptiDroidTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        val tile = qsTile ?: return
        tile.state = Tile.STATE_INACTIVE
        tile.label = "OptiDroid Boost"
        tile.updateTile()
    }

    override fun onClick() {
        super.onClick()
        val tile = qsTile ?: return

        if (tile.state == Tile.STATE_INACTIVE) {
            tile.state = Tile.STATE_ACTIVE
            tile.label = "Boosting..."
            tile.updateTile()

            // Enqueue optimization work
            OptimizationWorker.enqueue(this, AppOptimizationType.SPEED_PROFILE)
        } else {
            tile.state = Tile.STATE_INACTIVE
            tile.label = "OptiDroid Boost"
            tile.updateTile()

            OptimizationWorker.cancel(this)
        }
    }
}
