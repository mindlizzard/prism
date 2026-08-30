package app.lawnchair.ui.preferences.destinations

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.lawnchair.preferences.observeAsState
import app.lawnchair.preferences.preferenceManager
import app.lawnchair.preferences2.asState
import app.lawnchair.preferences2.preferenceManager2
import app.lawnchair.prism.PrismBrand
import app.lawnchair.ui.preferences.components.layout.PreferenceGroup
import com.android.launcher3.InvariantDeviceProfile
import com.android.launcher3.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PrismLauncherProfiles(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val prefs = preferenceManager()
    val prefs2 = preferenceManager2()
    val scope = rememberCoroutineScope()

    val currentTheme by prefs2.prismThemeProfile.asState()
    val currentColumns by prefs.workspaceColumns.observeAsState()
    val currentRows by prefs.workspaceRows.observeAsState()
    val currentDockIcons by prefs.hotseatColumns.observeAsState()
    val currentIconSize by prefs2.homeIconSizeFactor.asState()

    val profiles = listOf(
        PrismLauncherProfile(
            label = stringResource(id = R.string.prism_profile_calm),
            theme = PrismBrand.ThemeProfile.PASTEL,
            columns = 4,
            rows = 6,
            dockIcons = 4,
            iconSize = 1.15f,
            horizontalPadding = 0.9f,
            verticalPadding = 0.9f,
        ),
        PrismLauncherProfile(
            label = stringResource(id = R.string.prism_profile_balanced),
            theme = PrismBrand.ThemeProfile.WALLPAPER,
            columns = 5,
            rows = 7,
            dockIcons = 5,
            iconSize = 1f,
            horizontalPadding = 1f,
            verticalPadding = 1f,
        ),
        PrismLauncherProfile(
            label = stringResource(id = R.string.prism_profile_power),
            theme = PrismBrand.ThemeProfile.VIBRANT,
            columns = 6,
            rows = 8,
            dockIcons = 6,
            iconSize = 0.86f,
            horizontalPadding = 0.7f,
            verticalPadding = 0.75f,
        ),
    )

    PreferenceGroup(
        modifier = modifier,
        heading = stringResource(id = R.string.prism_profiles_title),
        description = stringResource(id = R.string.prism_profiles_description),
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            profiles.forEach { profile ->
                val isCurrent = currentTheme == profile.theme &&
                    currentColumns == profile.columns &&
                    currentRows == profile.rows &&
                    currentDockIcons == profile.dockIcons &&
                    currentIconSize == profile.iconSize
                val applyProfile: () -> Unit = {
                    scope.launch {
                        val unfoldedDockIcons = if (
                            InvariantDeviceProfile.deviceType == InvariantDeviceProfile.TYPE_MULTI_DISPLAY
                        ) {
                            profile.dockIcons + 2
                        } else {
                            profile.dockIcons
                        }
                        prefs.batchEdit {
                            prefs.workspaceColumns.set(profile.columns)
                            prefs.workspaceRows.set(profile.rows)
                            prefs.hotseatColumns.set(profile.dockIcons)
                            prefs.hotseatColumnsUnfolded.set(unfoldedDockIcons)
                        }
                        prefs2.homeIconSizeFactor.set(profile.iconSize)
                        prefs2.workspacePaddingHorizontalFactor.set(profile.horizontalPadding)
                        prefs2.workspacePaddingVerticalFactor.set(profile.verticalPadding)
                        InvariantDeviceProfile.INSTANCE.get(context).onPreferencesChanged(context)
                        Toast.makeText(
                            context,
                            context.getString(R.string.prism_profile_applied, profile.label),
                            Toast.LENGTH_SHORT,
                        ).show()
                        prefs2.prismThemeProfile.set(profile.theme)
                    }
                }

                if (isCurrent) {
                    Button(
                        onClick = applyProfile,
                        shapes = ButtonDefaults.shapes(),
                    ) {
                        Text(text = profile.label)
                    }
                } else {
                    OutlinedButton(
                        onClick = applyProfile,
                        shapes = ButtonDefaults.shapes(),
                    ) {
                        Text(text = profile.label)
                    }
                }
            }
        }
    }
}

private data class PrismLauncherProfile(
    val label: String,
    val theme: PrismBrand.ThemeProfile,
    val columns: Int,
    val rows: Int,
    val dockIcons: Int,
    val iconSize: Float,
    val horizontalPadding: Float,
    val verticalPadding: Float,
)
