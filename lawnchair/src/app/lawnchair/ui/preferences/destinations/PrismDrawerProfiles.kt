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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.lawnchair.preferences.getAdapter
import app.lawnchair.preferences.observeAsState
import app.lawnchair.preferences.preferenceManager
import app.lawnchair.preferences2.preferenceManager2
import app.lawnchair.ui.preferences.components.layout.PreferenceGroup
import com.android.launcher3.InvariantDeviceProfile
import com.android.launcher3.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PrismDrawerProfiles(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val prefs = preferenceManager()
    val prefs2 = preferenceManager2()

    val currentOpacity by prefs.drawerOpacity.observeAsState()
    val searchBackgroundAdapter = prefs2.appDrawerSearchBarBackground.getAdapter()
    val drawerColumnsAdapter = prefs2.drawerColumns.getAdapter()
    val drawerColumnsUnfoldedAdapter = prefs2.drawerColumnsUnfolded.getAdapter()
    val iconSizeAdapter = prefs2.drawerIconSizeFactor.getAdapter()
    val cellHeightAdapter = prefs2.drawerCellHeightFactor.getAdapter()
    val marginAdapter = prefs2.drawerLeftRightMarginFactor.getAdapter()
    val topPaddingAdapter = prefs2.drawerPaddingTopFactor.getAdapter()
    val showLabelsAdapter = prefs2.showIconLabelsInDrawer.getAdapter()

    val profiles = listOf(
        PrismDrawerProfile(
            label = stringResource(id = R.string.prism_drawer_profile_glass),
            opacity = 0.62f,
            searchBackground = true,
            columns = 4,
            iconSize = 1.10f,
            cellHeight = 1.15f,
            margin = 0.80f,
            topPadding = 1.05f,
            showLabels = true,
        ),
        PrismDrawerProfile(
            label = stringResource(id = R.string.prism_drawer_profile_clean),
            opacity = 0.92f,
            searchBackground = true,
            columns = 5,
            iconSize = 1f,
            cellHeight = 1f,
            margin = 1f,
            topPadding = 1.15f,
            showLabels = true,
        ),
        PrismDrawerProfile(
            label = stringResource(id = R.string.prism_drawer_profile_compact),
            opacity = 0.88f,
            searchBackground = false,
            columns = 6,
            iconSize = 0.84f,
            cellHeight = 0.78f,
            margin = 0.50f,
            topPadding = 1f,
            showLabels = false,
        ),
    )

    PreferenceGroup(
        modifier = modifier,
        heading = stringResource(id = R.string.prism_drawer_profiles_title),
        description = stringResource(id = R.string.prism_drawer_profiles_description),
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            profiles.forEach { profile ->
                val isCurrent = currentOpacity == profile.opacity &&
                    searchBackgroundAdapter.state.value == profile.searchBackground &&
                    drawerColumnsAdapter.state.value == profile.columns &&
                    iconSizeAdapter.state.value == profile.iconSize &&
                    cellHeightAdapter.state.value == profile.cellHeight &&
                    marginAdapter.state.value == profile.margin &&
                    topPaddingAdapter.state.value == profile.topPadding &&
                    showLabelsAdapter.state.value == profile.showLabels
                val applyProfile: () -> Unit = {
                    searchBackgroundAdapter.onChange(profile.searchBackground)
                    drawerColumnsAdapter.onChange(profile.columns)
                    if (InvariantDeviceProfile.deviceType == InvariantDeviceProfile.TYPE_MULTI_DISPLAY) {
                        drawerColumnsUnfoldedAdapter.onChange(profile.columns + 2)
                    }
                    iconSizeAdapter.onChange(profile.iconSize)
                    cellHeightAdapter.onChange(profile.cellHeight)
                    marginAdapter.onChange(profile.margin)
                    topPaddingAdapter.onChange(profile.topPadding)
                    showLabelsAdapter.onChange(profile.showLabels)
                    prefs.drawerOpacity.set(profile.opacity)
                    Toast.makeText(
                        context,
                        context.getString(R.string.prism_drawer_profile_applied, profile.label),
                        Toast.LENGTH_SHORT,
                    ).show()
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

private data class PrismDrawerProfile(
    val label: String,
    val opacity: Float,
    val searchBackground: Boolean,
    val columns: Int,
    val iconSize: Float,
    val cellHeight: Float,
    val margin: Float,
    val topPadding: Float,
    val showLabels: Boolean,
)
