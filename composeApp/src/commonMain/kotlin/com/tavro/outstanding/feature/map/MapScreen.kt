package com.tavro.outstanding.feature.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.swmansion.kmpmaps.core.CameraPosition
import com.swmansion.kmpmaps.core.Coordinates
import com.swmansion.kmpmaps.core.Map
import com.swmansion.kmpmaps.core.MapProperties
import com.swmansion.kmpmaps.core.MapTheme
import com.swmansion.kmpmaps.core.MapType
import com.swmansion.kmpmaps.core.MapUISettings
import com.tavro.outstanding.designsystem.theme.JadeTheme

/** Where the camera starts before the user's own location is known. */
private val InitialCameraPosition = CameraPosition(
    coordinates = Coordinates(latitude = 59.3293, longitude = 18.0686),
    zoom = 11f,
)

/**
 * Renders the native map — Google Maps on Android, MapKit on iOS — via kmp-maps.
 *
 * Showing the user's location asks for the location permission the first time this screen is
 * composed; set [MapProperties.isMyLocationEnabled] to `false` to drop that prompt.
 */
@Composable
fun MapScreen(modifier: Modifier) {
    Map(
        modifier = modifier.fillMaxSize(),
        cameraPosition = InitialCameraPosition,
        properties = MapProperties(
            isMyLocationEnabled = true,
            isTrafficEnabled = false,
            mapType = MapType.NORMAL,
            mapTheme = if (JadeTheme.colorScheme.isDark) MapTheme.DARK else MapTheme.LIGHT,
        ),
        uiSettings = MapUISettings(
            compassEnabled = true,
            myLocationButtonEnabled = true,
        ),
    )
}
