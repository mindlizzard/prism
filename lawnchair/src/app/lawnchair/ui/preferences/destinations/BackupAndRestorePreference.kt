package app.lawnchair.ui.preferences.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.lawnchair.backup.ui.restoreBackupOpener
import app.lawnchair.backup.ui.restoreNovaBackupOpener
import app.lawnchair.ui.preferences.LocalIsExpandedScreen
import app.lawnchair.ui.preferences.components.NavigationActionPreference
import app.lawnchair.ui.preferences.components.controls.ClickablePreference
import app.lawnchair.ui.preferences.components.layout.PreferenceGroup
import app.lawnchair.ui.preferences.components.layout.PreferenceLayout
import app.lawnchair.ui.preferences.navigation.CreateBackup
import com.android.launcher3.R

@Composable
fun BackupAndRestorePreference(
    modifier: Modifier = Modifier,
) {
    PreferenceLayout(
        label = stringResource(R.string.backup_and_restore_label),
        backArrowVisible = !LocalIsExpandedScreen.current,
        modifier = modifier,
    ) {
        PrismBackupHeader()
        PreferenceGroup(
            heading = stringResource(id = R.string.prism_backup_save_section),
        ) {
            NavigationActionPreference(
                label = stringResource(R.string.create_backup),
                subtitle = stringResource(R.string.create_backup_description),
                destination = CreateBackup,
            )
        }
        PreferenceGroup(
            heading = stringResource(id = R.string.prism_backup_restore_section),
            description = stringResource(id = R.string.prism_backup_restore_warning),
        ) {
            ClickablePreference(
                label = stringResource(R.string.restore_backup),
                subtitle = stringResource(R.string.restore_backup_description),
                onClick = restoreBackupOpener(),
            )
        }
        PreferenceGroup(
            heading = stringResource(id = R.string.prism_backup_import_section),
        ) {
            ClickablePreference(
                label = stringResource(R.string.restore_nova_backup),
                subtitle = stringResource(R.string.restore_nova_backup_description),
                onClick = restoreNovaBackupOpener(),
            )
        }
    }
}

@Composable
private fun PrismBackupHeader(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.10f),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.backup_restore),
                    contentDescription = null,
                    modifier = Modifier.padding(14.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = stringResource(id = R.string.prism_backup_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    text = stringResource(id = R.string.prism_backup_summary),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.78f),
                )
            }
        }
    }
}
