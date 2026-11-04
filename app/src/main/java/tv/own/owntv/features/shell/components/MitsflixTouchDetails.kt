package tv.own.owntv.features.shell.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import tv.own.owntv.ui.components.OwnTVIcon
import tv.own.owntv.ui.components.OwnTVPopup
import tv.own.owntv.ui.stage.StageButton
import tv.own.owntv.ui.theme.StageColors
import tv.own.owntv.ui.theme.mpx

/** One button in [MitsflixTouchDetails]; [primary] is drawn in the accent (Play). */
internal class MitsflixTouchButton(
    val label: String,
    val icon: OwnTVIcon,
    val primary: Boolean = false,
    val onClick: () -> Unit,
)

/**
 * Mitsflix: what tapping a title does on a phone or tablet. On a TV the remote's focus fills the
 * details card as you move; a tap moves no focus and (for a film) used to start playback at once, so
 * instead the title's details open in a panel on the right, with Play, Favourite, Trailer, Download
 * and the full options under them. Tapping outside the panel closes it.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MitsflixTouchDetails(
    info: VodTitleInfo?,
    series: Boolean,
    buttons: List<MitsflixTouchButton>,
    onDismiss: () -> Unit,
) {
    OwnTVPopup(onDismissRequest = onDismiss, stageLayout = true) {
        Box(
            Modifier
                .fillMaxSize()
                .background(StageColors.Scrim)
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onDismiss),
        ) {
            Column(
                Modifier
                    .align(Alignment.CenterEnd)
                    .padding(top = 28.mpx, bottom = 28.mpx, end = 28.mpx)
                    .width(720.mpx)
                    .fillMaxHeight()
                    // Taps inside the panel stay in the panel.
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {},
                verticalArrangement = Arrangement.spacedBy(16.mpx),
            ) {
                VodDetailsCard(info, hints = emptyList(), series = series, modifier = Modifier.weight(1f).fillMaxWidth())
                FlowRow(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.mpx),
                    verticalArrangement = Arrangement.spacedBy(12.mpx),
                ) {
                    buttons.forEach { b ->
                        StageButton(b.label, onClick = b.onClick, icon = b.icon, tinted = b.primary, height = 60.mpx, textSize = 20)
                    }
                }
            }
        }
    }
}
