package app.lawnchair.ui.preferences.destinations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.lawnchair.gestures.config.GestureHandlerConfig
import app.lawnchair.gestures.handlers.SleepMode
import app.lawnchair.preferences.getAdapter
import app.lawnchair.preferences2.preferenceManager2
import app.lawnchair.ui.preferences.LocalIsExpandedScreen
import app.lawnchair.ui.preferences.components.GestureHandlerPreference
import app.lawnchair.ui.preferences.components.controls.ListPreference
import app.lawnchair.ui.preferences.components.layout.PreferenceGroup
import app.lawnchair.ui.preferences.components.layout.PreferenceLayout
import com.android.launcher3.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GesturePreferences(
    modifier: Modifier = Modifier,
) {
    val prefs = preferenceManager2()
    val scope = rememberCoroutineScope()
    val profiles = listOf(
        PrismGestureProfile(
            label = stringResource(id = R.string.prism_gesture_everyday),
            doubleTap = GestureHandlerConfig.Sleep,
            swipeUp = GestureHandlerConfig.OpenAppDrawer,
            swipeDown = GestureHandlerConfig.OpenNotifications,
            twoFingerSwipeUp = GestureHandlerConfig.Recents,
            twoFingerSwipeDown = GestureHandlerConfig.OpenQuickSettings,
            homePress = GestureHandlerConfig.NoOp,
            backPress = GestureHandlerConfig.NoOp,
        ),
        PrismGestureProfile(
            label = stringResource(id = R.string.prism_gesture_power),
            doubleTap = GestureHandlerConfig.Sleep,
            swipeUp = GestureHandlerConfig.OpenAppSearch,
            swipeDown = GestureHandlerConfig.OpenQuickSettings,
            twoFingerSwipeUp = GestureHandlerConfig.Recents,
            twoFingerSwipeDown = GestureHandlerConfig.OpenNotifications,
            homePress = GestureHandlerConfig.OpenSearch,
            backPress = GestureHandlerConfig.NoOp,
        ),
        PrismGestureProfile(
            label = stringResource(id = R.string.prism_gesture_minimal),
            doubleTap = GestureHandlerConfig.NoOp,
            swipeUp = GestureHandlerConfig.OpenAppDrawer,
            swipeDown = GestureHandlerConfig.OpenNotifications,
            twoFingerSwipeUp = GestureHandlerConfig.NoOp,
            twoFingerSwipeDown = GestureHandlerConfig.NoOp,
            homePress = GestureHandlerConfig.NoOp,
            backPress = GestureHandlerConfig.NoOp,
        ),
    )

    PreferenceLayout(
        label = stringResource(id = R.string.gestures_label),
        backArrowVisible = !LocalIsExpandedScreen.current,
        modifier = modifier,
    ) {
        PreferenceGroup(heading = stringResource(id = R.string.prism_gesture_profiles)) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                profiles.forEach { profile ->
                    Button(
                        onClick = {
                            scope.launch {
                                prefs.doubleTapGestureHandler.set(profile.doubleTap)
                                prefs.swipeUpGestureHandler.set(profile.swipeUp)
                                prefs.swipeDownGestureHandler.set(profile.swipeDown)
                                prefs.twoFingerSwipeUpGestureHandler.set(profile.twoFingerSwipeUp)
                                prefs.twoFingerSwipeDownGestureHandler.set(profile.twoFingerSwipeDown)
                                prefs.homePressGestureHandler.set(profile.homePress)
                                prefs.backPressGestureHandler.set(profile.backPress)
                            }
                        },
                        shapes = ButtonDefaults.shapes(),
                    ) {
                        Text(text = profile.label)
                    }
                }
            }
            Text(
                text = stringResource(id = R.string.prism_gesture_icon_hint),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
        PreferenceGroup {
            GestureHandlerPreference(
                adapter = prefs.doubleTapGestureHandler.getAdapter(),
                label = stringResource(id = R.string.gesture_double_tap),
            )
            GestureHandlerPreference(
                adapter = prefs.swipeUpGestureHandler.getAdapter(),
                label = stringResource(id = R.string.gesture_swipe_up),
            )
            GestureHandlerPreference(
                adapter = prefs.swipeDownGestureHandler.getAdapter(),
                label = stringResource(id = R.string.gesture_swipe_down),
            )
            GestureHandlerPreference(
                adapter = prefs.twoFingerSwipeUpGestureHandler.getAdapter(),
                label = stringResource(id = R.string.gesture_two_finger_swipe_up),
            )
            GestureHandlerPreference(
                adapter = prefs.twoFingerSwipeDownGestureHandler.getAdapter(),
                label = stringResource(id = R.string.gesture_two_finger_swipe_down),
            )
            GestureHandlerPreference(
                adapter = prefs.homePressGestureHandler.getAdapter(),
                label = stringResource(id = R.string.gesture_home_tap),
            )
            GestureHandlerPreference(
                adapter = prefs.backPressGestureHandler.getAdapter(),
                label = stringResource(id = R.string.gesture_back_tap),
            )
        }
        PreferenceGroup(heading = stringResource(id = R.string.sleep_mode_label)) {
            ListPreference(
                adapter = prefs.sleepMode.getAdapter(),
                entries = SleepMode.entries(),
                label = stringResource(id = R.string.sleep_mode_label),
            )
        }
    }
}

private data class PrismGestureProfile(
    val label: String,
    val doubleTap: GestureHandlerConfig,
    val swipeUp: GestureHandlerConfig,
    val swipeDown: GestureHandlerConfig,
    val twoFingerSwipeUp: GestureHandlerConfig,
    val twoFingerSwipeDown: GestureHandlerConfig,
    val homePress: GestureHandlerConfig,
    val backPress: GestureHandlerConfig,
)
