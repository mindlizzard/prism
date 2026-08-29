package app.lawnchair.ui.preferences.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.lawnchair.preferences2.asState
import app.lawnchair.preferences2.preferenceManager2
import app.lawnchair.prism.PrismBrand
import app.lawnchair.ui.theme.PrismThemeProfileSwatch
import app.lawnchair.ui.theme.prismThemeProfileLabel
import com.android.launcher3.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PrismDashboardHeader(
    modifier: Modifier = Modifier,
) {
    val profile by preferenceManager2().prismThemeProfile.asState()
    val profileName = prismThemeProfileLabel(profile)
    val colorScheme = MaterialTheme.colorScheme
    val gradient = Brush.linearGradient(
        colors = listOf(
            colorScheme.primaryContainer,
            colorScheme.tertiaryContainer,
            colorScheme.secondaryContainer,
        ),
    )

    Surface(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLargeIncreased,
        color = Color.Transparent,
        shadowElevation = 4.dp,
    ) {
        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraLargeIncreased)
                .background(gradient)
                .padding(20.dp),
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 54.dp, y = (-64).dp)
                    .size(170.dp)
                    .background(
                        brush = Brush.radialGradient(
                            listOf(
                                colorScheme.onPrimaryContainer.copy(alpha = 0.18f),
                                Color.Transparent,
                            ),
                        ),
                        shape = CircleShape,
                    ),
            )

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(
                        modifier = Modifier.size(72.dp),
                        shape = MaterialTheme.shapes.large,
                        color = colorScheme.onPrimaryContainer.copy(alpha = 0.10f),
                        border = BorderStroke(
                            1.dp,
                            colorScheme.onPrimaryContainer.copy(alpha = 0.18f),
                        ),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.prism_launcher_foreground),
                            contentDescription = null,
                            modifier = Modifier.padding(6.dp),
                            tint = Color.Unspecified,
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = PrismBrand.NAME,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onPrimaryContainer,
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = stringResource(id = R.string.prism_tagline),
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorScheme.onPrimaryContainer.copy(alpha = 0.78f),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Surface(
                    shape = CircleShape,
                    color = colorScheme.onPrimaryContainer.copy(alpha = 0.10f),
                    border = BorderStroke(
                        1.dp,
                        colorScheme.onPrimaryContainer.copy(alpha = 0.14f),
                    ),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        PrismThemeProfileSwatch(
                            profile = profile,
                            dotSize = 8.dp,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(
                                id = R.string.prism_theme_engine_active,
                                profileName,
                            ),
                            style = MaterialTheme.typography.labelLarge,
                            color = colorScheme.onPrimaryContainer,
                        )
                    }
                }
            }
        }
    }
}
