package tv.own.owntv.ui.theme

import androidx.compose.ui.graphics.Color
import tv.own.owntv.core.theme.AccentColor

/**
 * Mitsflix Player's look: black and gold, after the Mitsflix logo.
 *
 * Kept in this one file so OwnTV's own theme code stays as it is (smaller merges on every OwnTV
 * update). The hooks are one call each in AccentColor.kt, Theme.kt and StageDraw.kt.
 *
 * - The default accent (OwnTV's "Teal", shown as "Mitsflix Gold") is the logo's gold. The other accent
 *   choices are untouched, so a user can still pick one.
 * - Dark theme surfaces, text and the page behind every screen are warm black instead of OwnTV's
 *   green-grey. The light theme keeps OwnTV's neutrals with the gold accent.
 */
object MitsflixTheme {

    /** The accent preset that is Mitsflix gold: the one every new install starts with. */
    val GoldPreset = AccentColor.TEAL

    data class Roles(val primary: Color, val onPrimary: Color, val primaryContainer: Color, val onPrimaryContainer: Color)

    private val GoldOnDark = Roles(
        primary = Color(0xFFF2B53E),
        onPrimary = Color(0xFF231900),
        primaryContainer = Color(0xFF5B4200),
        onPrimaryContainer = Color(0xFFFFE0A3),
    )
    private val GoldOnLight = Roles(
        primary = Color(0xFF8A6300),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFFFDFA0),
        onPrimaryContainer = Color(0xFF2B1F00),
    )

    /** Mitsflix's colours for [accent], or null to use OwnTV's. */
    fun preset(accent: AccentColor, isDark: Boolean): Roles? =
        if (accent == GoldPreset) (if (isDark) GoldOnDark else GoldOnLight) else null

    /** Warm-black surfaces for the dark theme (same contrast steps as OwnTV's). */
    fun colors(c: OwnTVColors): OwnTVColors = if (!c.isDark) c else c.copy(
        background = Color(0xFF070604),
        surface = Color(0xFF110F0B),
        surfaceContainerLowest = Color(0xFF0B0A07),
        surfaceContainerLow = Color(0xFF1A1712),
        surfaceContainer = Color(0xFF1F1C16),
        surfaceContainerHigh = Color(0xFF29261F),
        surfaceContainerHighest = Color(0xFF34302A),
        onSurface = Color(0xFFEDE6DA),
        onSurfaceVariant = Color(0xFFCFC6B6),
        outline = Color(0xFF9A907F),
        outlineVariant = Color(0xFF4A4337),
        secondary = Color(0xFFD9C6A0),
        onSecondary = Color(0xFF3A2F17),
        secondaryContainer = Color(0xFF52462C),
        onSecondaryContainer = Color(0xFFF6E2BB),
    )

    /** The Stage palette (most screens) with warm neutrals in the dark theme. */
    fun stage(p: StagePalette): StagePalette = if (p.isLight) p else p.copy(
        text = Color(0xFFF7F2E9),
        muted = Color(0xFFB8AE9C),
        dim = Color(0xFF7F7666),
        itemText = Color(0xFFE4DDCF),
        menuItemText = Color(0xFFE8E1D4),
        tagText = Color(0xFFD6CEBE),
        page = Color(0xFF070604),
        glassTint = Color(0xFF1B1710),
        glassOff = Color(0xFF19150F),
        body = Color(0xFFD5CDBF),
        navText = Color(0xFFD1C9BA),
        fxLine = Color(0xFFEAE1CF),
        scrim = Color(5, 4, 2).copy(alpha = 0.55f),
        wash = Color(7, 6, 4),
        knobOff = Color(0xFFDAD2C3),
    )

    // The page behind dark Stage screens: a warm black wash with a gold glow at the top and an
    // amber glow at the bottom (in place of OwnTV's coral and blue).
    val PlainDark = Color(0xFF080705)
    val WashTop = Color(0xFF110D07)
    val WashMid = Color(0xFF080705)
    val WashBottom = Color(0xFF0A0805)
    val GlowTop = Color(255, 196, 92)
    val GlowBottom = Color(214, 140, 40)
}
