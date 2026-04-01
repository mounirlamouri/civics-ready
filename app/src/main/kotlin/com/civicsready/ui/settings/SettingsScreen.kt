package com.civicsready.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.civicsready.R
import com.civicsready.domain.model.FederalOfficials
import com.civicsready.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val keyboard = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings), style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back), tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(16.dp))

            // ── Zip Code ─────────────────────────────────────────────────────
            SectionTitle(stringResource(R.string.settings_location_title))
            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value         = state.zipInput,
                    onValueChange = viewModel::onZipChanged,
                    label         = { Text(stringResource(R.string.settings_zip_label)) },
                    placeholder   = { Text(stringResource(R.string.settings_zip_placeholder)) },
                    singleLine    = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction    = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(onSearch = {
                        keyboard?.hide()
                        viewModel.lookupZip()
                    }),
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(12.dp))
                Button(
                    onClick  = {
                        keyboard?.hide()
                        viewModel.lookupZip()
                    },
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    enabled  = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.padding(horizontal = 4.dp), color = White, strokeWidth = 2.dp)
                    } else {
                        Text(stringResource(R.string.settings_lookup))
                    }
                }
            }

            // Error message
            state.lookupError?.let { error ->
                Spacer(Modifier.height(6.dp))
                Text(error, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
            }

            // Partial-match notice (state resolved, district not found)
            state.lookupNotice?.let { notice ->
                Spacer(Modifier.height(6.dp))
                Text(notice, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.tertiaryContainer)
            }

            // Resolved officials
            if (state.officials.isResolved) {
                Spacer(Modifier.height(12.dp))
                Card(
                    modifier  = Modifier.fillMaxWidth(),
                    shape     = RoundedCornerShape(16.dp),
                    colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OfficialRow(stringResource(R.string.settings_official_state),          state.officials.stateName)
                        OfficialRow(stringResource(R.string.settings_official_capital),        state.officials.stateCapital)
                        OfficialRow(stringResource(R.string.settings_official_governor),       state.officials.governor)
                        OfficialRow(stringResource(R.string.settings_official_senator1),      state.officials.senator1)
                        OfficialRow(stringResource(R.string.settings_official_senator2),      state.officials.senator2)
                        OfficialRow(stringResource(R.string.settings_official_representative), state.officials.representative)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))

            // ── Federal officials note ────────────────────────────────────────
            SectionTitle(stringResource(R.string.settings_federal_title, FederalOfficials.LAST_UPDATED))
            Spacer(Modifier.height(8.dp))
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(16.dp),
                colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OfficialRow(stringResource(R.string.settings_federal_president),        FederalOfficials.PRESIDENT)
                    OfficialRow(stringResource(R.string.settings_federal_vp),   FederalOfficials.VICE_PRESIDENT)
                    OfficialRow(stringResource(R.string.settings_federal_speaker),   FederalOfficials.SPEAKER_OF_HOUSE)
                    OfficialRow(stringResource(R.string.settings_federal_chief_justice),    FederalOfficials.CHIEF_JUSTICE)
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(
                stringResource(R.string.settings_uscis_link),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (FederalOfficials.isStale()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    stringResource(R.string.staleness_warning),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiaryContainer
                )
            }

            Spacer(Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))

            // ── 65/20 Toggle ─────────────────────────────────────────────────
            SectionTitle(stringResource(R.string.settings_6520_title))
            Spacer(Modifier.height(4.dp))
            Text(
                stringResource(R.string.settings_6520_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier          = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.settings_6520_toggle), style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                Switch(
                    checked         = state.is6520Mode,
                    onCheckedChange = viewModel::toggle6520Mode,
                    colors          = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.onPrimary, checkedTrackColor = MaterialTheme.colorScheme.primary)
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
}

@Composable
private fun OfficialRow(label: String, value: String) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment     = Alignment.Top
    ) {
        Text(
            text     = "$label:",
            style    = MaterialTheme.typography.bodyMedium,
            color    = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(120.dp)
        )
        Text(value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
    }
}
