package com.raen.optidroid.data.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.raen.optidroid.domain.model.common.Resource
import com.raen.optidroid.domain.model.common.ResourceError
import com.raen.optidroid.domain.model.settings.AppOptimizationType
import com.raen.optidroid.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val SETTINGS_DATA_STORE_NAME = "app_settings"

private val Context.settingsDataStore by preferencesDataStore(
    name = SETTINGS_DATA_STORE_NAME
)

@Singleton
class SettingsDataStoreRepository @Inject constructor(
    @param:ApplicationContext private val applicationContext: Context
) : SettingsRepository {

    private object Keys {
        val APP_OPTIMIZATION_TYPE: Preferences.Key<String> =
            stringPreferencesKey("app_optimization_type")
        val AUTO_OPTIMIZATION_ENABLED: Preferences.Key<Boolean> =
            booleanPreferencesKey("auto_optimization_enabled")
        val UNLOCK_DELAY_MINUTES: Preferences.Key<Int> =
            intPreferencesKey("unlock_delay_minutes")
        val PERIODIC_SCHEDULE_HOURS: Preferences.Key<Int> =
            intPreferencesKey("periodic_schedule_hours")
        val MIN_UNLOCK_INTERVAL_HOURS: Preferences.Key<Int> =
            intPreferencesKey("min_unlock_interval_hours")
        val LAST_UNLOCK_TIMESTAMP: Preferences.Key<Long> =
            androidx.datastore.preferences.core.longPreferencesKey("last_unlock_timestamp")
        val OPTIMIZE_PRIVATE_SPACE: Preferences.Key<Boolean> =
            booleanPreferencesKey("optimize_private_space")
    }

    override fun observeAppOptimizationType(): Flow<Resource<AppOptimizationType>> {
        return applicationContext.settingsDataStore
            .data
            .map { preferences ->
                val rawValue = preferences[Keys.APP_OPTIMIZATION_TYPE]
                val type = rawValue
                    ?.let { stored ->
                        runCatching { AppOptimizationType.valueOf(stored) }
                            .getOrDefault(AppOptimizationType.SPEED_PROFILE)
                    }
                    ?: AppOptimizationType.SPEED_PROFILE

                Resource.Success(type) as Resource<AppOptimizationType>
            }
            .catch { throwable ->
                emit(
                    Resource.Error(
                        ResourceError.DatabaseError(
                            message = throwable.message ?: "Unable to read optimization type"
                        )
                    )
                )
            }
    }

    override suspend fun setAppOptimizationType(
        type: AppOptimizationType
    ): Resource<Unit> {
        return try {
            applicationContext.settingsDataStore.edit { preferences ->
                preferences[Keys.APP_OPTIMIZATION_TYPE] = type.name
            }
            Resource.Success(Unit)
        } catch (throwable: Throwable) {
            Resource.Error(
                ResourceError.DatabaseError(
                    message = throwable.message ?: "Unable to persist optimization type"
                )
            )
        }
    }

    override fun observeAutoOptimizationEnabled(): Flow<Resource<Boolean>> {
        return applicationContext.settingsDataStore.data
            .map { preferences ->
                Resource.Success(preferences[Keys.AUTO_OPTIMIZATION_ENABLED] ?: false) as Resource<Boolean>
            }
            .catch { emit(Resource.Error(ResourceError.DatabaseError(it.message ?: "Error"))) }
    }

    override suspend fun setAutoOptimizationEnabled(enabled: Boolean): Resource<Unit> {
        return try {
            applicationContext.settingsDataStore.edit { preferences ->
                preferences[Keys.AUTO_OPTIMIZATION_ENABLED] = enabled
            }
            Resource.Success(Unit)
        } catch (t: Throwable) {
            Resource.Error(ResourceError.DatabaseError(t.message ?: "Error"))
        }
    }

    override fun observeUnlockDelayMinutes(): Flow<Resource<Int>> {
        return applicationContext.settingsDataStore.data
            .map { preferences ->
                Resource.Success(preferences[Keys.UNLOCK_DELAY_MINUTES] ?: 0) as Resource<Int>
            }
            .catch { emit(Resource.Error(ResourceError.DatabaseError(it.message ?: "Error"))) }
    }

    override suspend fun setUnlockDelayMinutes(minutes: Int): Resource<Unit> {
        return try {
            applicationContext.settingsDataStore.edit { preferences ->
                preferences[Keys.UNLOCK_DELAY_MINUTES] = minutes
            }
            Resource.Success(Unit)
        } catch (t: Throwable) {
            Resource.Error(ResourceError.DatabaseError(t.message ?: "Error"))
        }
    }

    override fun observePeriodicScheduleHours(): Flow<Resource<Int>> {
        return applicationContext.settingsDataStore.data
            .map { preferences ->
                Resource.Success(preferences[Keys.PERIODIC_SCHEDULE_HOURS] ?: 1) as Resource<Int>
            }
            .catch { emit(Resource.Error(ResourceError.DatabaseError(it.message ?: "Error"))) }
    }

    override suspend fun setPeriodicScheduleHours(hours: Int): Resource<Unit> {
        return try {
            applicationContext.settingsDataStore.edit { preferences ->
                preferences[Keys.PERIODIC_SCHEDULE_HOURS] = hours
            }
            Resource.Success(Unit)
        } catch (t: Throwable) {
            Resource.Error(ResourceError.DatabaseError(t.message ?: "Error"))
        }
    }

    override fun observeMinUnlockIntervalHours(): Flow<Resource<Int>> {
        return applicationContext.settingsDataStore.data
            .map { preferences ->
                Resource.Success(preferences[Keys.MIN_UNLOCK_INTERVAL_HOURS] ?: 0) as Resource<Int>
            }
            .catch { emit(Resource.Error(ResourceError.DatabaseError(it.message ?: "Error"))) }
    }

    override suspend fun setMinUnlockIntervalHours(hours: Int): Resource<Unit> {
        return try {
            applicationContext.settingsDataStore.edit { preferences ->
                preferences[Keys.MIN_UNLOCK_INTERVAL_HOURS] = hours
            }
            Resource.Success(Unit)
        } catch (t: Throwable) {
            Resource.Error(ResourceError.DatabaseError(t.message ?: "Error"))
        }
    }

    override fun observeLastUnlockTimestamp(): Flow<Resource<Long>> {
        return applicationContext.settingsDataStore.data
            .map { preferences ->
                Resource.Success(preferences[Keys.LAST_UNLOCK_TIMESTAMP] ?: 0L) as Resource<Long>
            }
            .catch { emit(Resource.Error(ResourceError.DatabaseError(it.message ?: "Error"))) }
    }

    override suspend fun setLastUnlockTimestamp(timestamp: Long): Resource<Unit> {
        return try {
            applicationContext.settingsDataStore.edit { preferences ->
                preferences[Keys.LAST_UNLOCK_TIMESTAMP] = timestamp
            }
            Resource.Success(Unit)
        } catch (t: Throwable) {
            Resource.Error(ResourceError.DatabaseError(t.message ?: "Error"))
        }
    }

    override fun observeOptimizePrivateSpace(): Flow<Resource<Boolean>> {
        return applicationContext.settingsDataStore.data
            .map { preferences ->
                Resource.Success(preferences[Keys.OPTIMIZE_PRIVATE_SPACE] ?: true) as Resource<Boolean>
            }
            .catch { emit(Resource.Error(ResourceError.DatabaseError(it.message ?: "Error"))) }
    }

    override suspend fun setOptimizePrivateSpace(enabled: Boolean): Resource<Unit> {
        return try {
            applicationContext.settingsDataStore.edit { preferences ->
                preferences[Keys.OPTIMIZE_PRIVATE_SPACE] = enabled
            }
            Resource.Success(Unit)
        } catch (t: Throwable) {
            Resource.Error(ResourceError.DatabaseError(t.message ?: "Error"))
        }
    }
}
