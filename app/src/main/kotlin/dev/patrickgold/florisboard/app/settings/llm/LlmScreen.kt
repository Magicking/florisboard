/*
 * Copyright (C) 2025 The FlorisBoard Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.patrickgold.florisboard.app.settings.llm

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.patrickgold.florisboard.R
import dev.patrickgold.florisboard.app.FlorisPreferenceStore
import dev.patrickgold.florisboard.app.enumDisplayEntriesOf
import dev.patrickgold.florisboard.ime.llm.LlmProvider
import dev.patrickgold.florisboard.lib.compose.FlorisScreen
import dev.patrickgold.jetpref.datastore.model.observeAsState
import dev.patrickgold.jetpref.datastore.ui.ListPreference
import dev.patrickgold.jetpref.datastore.ui.PreferenceGroup
import dev.patrickgold.jetpref.datastore.ui.SwitchPreference
import dev.patrickgold.jetpref.datastore.ui.TextFieldPreference
import org.florisboard.lib.compose.FlorisInfoCard
import org.florisboard.lib.compose.stringRes

@Composable
fun LlmScreen() = FlorisScreen {
    title = stringRes(R.string.settings__llm__title)
    previewFieldVisible = true
    iconSpaceReserved = false

    val prefs by FlorisPreferenceStore

    content {
        FlorisInfoCard(
            modifier = Modifier.padding(8.dp),
            text = """
                AI Input allows you to use Large Language Models (LLMs) to help generate and improve text. 
                Configure your preferred AI provider and API key to enable this feature.
            """.trimIndent().replace('\n', ' '),
        )

        PreferenceGroup(title = stringRes(R.string.pref__llm__title)) {
            SwitchPreference(
                prefs.llm.enabled,
                title = stringRes(R.string.pref__llm__enabled__label),
                summary = stringRes(R.string.pref__llm__enabled__summary),
            )
            val provider by prefs.llm.provider.observeAsState()
            ListPreference(
                prefs.llm.provider,
                title = stringRes(R.string.pref__llm__provider__label),
                entries = enumDisplayEntriesOf(LlmProvider::class),
                enabledIf = { prefs.llm.enabled isEqualTo true },
            )
            TextFieldPreference(
                pref = prefs.llm.apiKey,
                title = stringRes(R.string.pref__llm__api_key__label),
                summary = stringRes(R.string.pref__llm__api_key__summary),
                hint = stringRes(R.string.pref__llm__api_key__hint),
                enabledIf = { prefs.llm.enabled isEqualTo true && prefs.llm.provider isNotEqualTo LlmProvider.NONE },
            )
            TextFieldPreference(
                pref = prefs.llm.model,
                title = stringRes(R.string.pref__llm__model__label),
                summary = stringRes(R.string.pref__llm__model__summary),
                enabledIf = { prefs.llm.enabled isEqualTo true && prefs.llm.provider isNotEqualTo LlmProvider.NONE },
            )
            TextFieldPreference(
                pref = prefs.llm.baseUrl,
                title = stringRes(R.string.pref__llm__base_url__label),
                summary = stringRes(R.string.pref__llm__base_url__summary),
                hint = stringRes(R.string.pref__llm__base_url__hint),
                visibleIf = { provider == LlmProvider.CUSTOM },
                enabledIf = { prefs.llm.enabled isEqualTo true },
            )
        }
    }
}
