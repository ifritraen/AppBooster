package com.raen.optidroid.domain.repository

import com.raen.optidroid.domain.model.common.AppInfo
import com.raen.optidroid.domain.model.common.Resource

/**
 * Repository abstraction providing app metadata required by the domain
 * and presentation layers without exposing Android framework dependencies.
 *
 * @return [Resource]\<[AppInfo]\> wrapping version information or a typed error.
 */
interface AppInfoRepository {

    /**
     * Loads metadata for the currently installed application build, such as
     * version name and optional release channel, for use in the UI layer.
     *
     * @return [Resource.Success] when app info is available, or [Resource.Error]
     * representing the underlying failure.
     */
    suspend fun getAppInfo(): Resource<AppInfo>
}
