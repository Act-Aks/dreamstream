package com.dreamstream.feature.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dreamstream.core.designsystem.component.GlassCard
import com.dreamstream.core.designsystem.component.GlassSurface
import com.dreamstream.core.designsystem.component.GlassTopBar
import com.dreamstream.core.designsystem.component.GradientBackground
import com.dreamstream.core.designsystem.theme.GlassDefaults
import com.dreamstream.core.presentation.resources.action_back
import com.dreamstream.core.presentation.ui.ObserveAsEvents
import com.dreamstream.feature.settings.domain.model.AppLanguage
import com.dreamstream.feature.settings.presentation.resources.Res
import com.dreamstream.feature.settings.presentation.resources.settings_language_section_title
import com.dreamstream.feature.settings.presentation.resources.settings_language_subtitle
import com.dreamstream.feature.settings.presentation.resources.settings_language_system_default
import com.dreamstream.feature.settings.presentation.resources.settings_title
import dev.chrisbanes.haze.HazeState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import com.dreamstream.core.presentation.resources.Res as CoreRes

// ─────────────────────────────────────────────────────────────────────────────
// Root — injects ViewModel, observes events
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun SettingsRoot(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is SettingsEvent.NavigateBack -> onNavigateBack()
        }
    }

    SettingsScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Screen — pure state + callbacks, previewable
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun SettingsScreen(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hazeState = remember { HazeState() }

    Box(modifier = modifier.fillMaxSize()) {
        GradientBackground(hazeState = hazeState)

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                GlassTopBar(
                    title = {
                        Text(
                            text = stringResource(Res.string.settings_title),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                        )
                    },
                    hazeState = hazeState,
                    navigationIcon = {
                        IconButton(onClick = { onAction(SettingsAction.OnBackClick) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(CoreRes.string.action_back),
                                tint = Color.White,
                            )
                        }
                    },
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                LanguageSection(
                    currentLanguage = state.currentLanguage,
                    onLanguageSelected = { onAction(SettingsAction.OnLanguageSelected(it)) },
                    hazeState = hazeState,
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Private composable helpers
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun LanguageSection(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    hazeState: HazeState,
) {
    GlassSurface(
        hazeState = hazeState,
        style = GlassDefaults.thin,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(Res.string.settings_language_section_title),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
            )
            Text(
                text = stringResource(Res.string.settings_language_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.width(8.dp))

            AppLanguage.entries.forEach { language ->
                LanguageOption(
                    language = language,
                    isSelected = language == currentLanguage,
                    onClick = { onLanguageSelected(language) },
                    hazeState = hazeState,
                )
            }
        }
    }
}

@Composable
private fun LanguageOption(
    language: AppLanguage,
    isSelected: Boolean,
    onClick: () -> Unit,
    hazeState: HazeState,
) {
    GlassCard(
        hazeState = hazeState,
        onClick = onClick,
        style = if (isSelected) GlassDefaults.regular else GlassDefaults.thin,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = languageDisplayName(language),
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
                modifier = Modifier.weight(1f),
            )

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun languageDisplayName(language: AppLanguage): String = when (language) {
    AppLanguage.SYSTEM -> stringResource(Res.string.settings_language_system_default)
    else -> language.displayName
}
