package tv.own.owntv.features.setup

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.tv.material3.Text
import tv.own.owntv.MitsflixBrand
import tv.own.owntv.R
import tv.own.owntv.ui.stage.StageButton
import tv.own.owntv.ui.stage.StagePopup
import tv.own.owntv.ui.theme.StageColors
import tv.own.owntv.ui.theme.mpx
import tv.own.owntv.ui.theme.stageAccent
import tv.own.owntv.ui.theme.stageText

/**
 * Mitsflix: "Get a playlist". Shows mitsflix.com in big type with a QR code to scan on a phone (most
 * TVs have no browser). "Open website" appears only when something on the device can open a link.
 * [onAddNow], when given, adds an "Add playlist" button that goes straight to adding one.
 */
@Composable
fun GetPlaylistDialog(onDismiss: () -> Unit, onAddNow: (() -> Unit)? = null) {
    val context = LocalContext.current
    val siteIntent = remember {
        Intent(Intent.ACTION_VIEW, Uri.parse(MitsflixBrand.SITE_URL)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    val canOpen = remember { runCatching { siteIntent.resolveActivity(context.packageManager) != null }.getOrDefault(false) }
    val first = remember { FocusRequester() }
    LaunchedEffect(Unit) { kotlinx.coroutines.delay(60); runCatching { first.requestFocus() } }
    val accent = stageAccent.accent

    StagePopup(
        onDismiss = onDismiss,
        title = stringResource(R.string.mitsflix_get_playlist_title),
        body = stringResource(R.string.mitsflix_get_playlist_body),
        width = 820.mpx,
        buttons = {
            if (canOpen) {
                StageButton(
                    stringResource(R.string.mitsflix_open_site),
                    onClick = { runCatching { context.startActivity(siteIntent) } },
                    height = 56.mpx, textSize = 19,
                )
            }
            if (onAddNow != null) {
                StageButton(
                    stringResource(R.string.mitsflix_add_playlist_now),
                    onClick = onAddNow,
                    height = 56.mpx, textSize = 19,
                )
            }
            StageButton(
                stringResource(R.string.common_ok),
                onClick = onDismiss,
                height = 56.mpx, textSize = 19, tinted = true,
                modifier = Modifier.focusRequester(first),
            )
        },
    ) {
        Row(
            Modifier.padding(top = 24.mpx),
            horizontalArrangement = Arrangement.spacedBy(28.mpx),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val qr = remember { tv.own.owntv.core.companion.CompanionLink.renderQr(MitsflixBrand.SITE_URL, 512)?.asImageBitmap() }
            if (qr != null) {
                Image(
                    bitmap = qr,
                    contentDescription = stringResource(R.string.mitsflix_qr_description),
                    modifier = Modifier.size(190.mpx).clip(RoundedCornerShape(14.mpx)).background(Color.White),
                )
            }
            Column {
                Text(MitsflixBrand.SITE, style = stageText(44, 800), color = accent)
                Text(
                    stringResource(R.string.mitsflix_scan_qr),
                    style = stageText(18, 500), color = StageColors.Muted,
                    modifier = Modifier.padding(top = 10.mpx),
                )
            }
        }
    }
}
