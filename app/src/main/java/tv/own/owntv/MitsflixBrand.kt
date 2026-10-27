package tv.own.owntv

/**
 * Everything that makes this build "Mitsflix Player" instead of OwnTV, in one place. The values live
 * in app/build.gradle.kts (buildConfigField) so Kotlin carries no literals for the i18n gate.
 *
 * Mitsflix Player is a modified version of OwnTV (https://github.com/ahXN00/OwnTV), used under the
 * GNU GPL v3. The modified source is published at [SOURCE_REPO].
 */
object MitsflixBrand {
    /** Shown on the "Get a playlist" panel and the About page. */
    val SITE: String get() = BuildConfig.MITSFLIX_SITE
    val SITE_URL: String get() = BuildConfig.MITSFLIX_SITE_URL

    /** GitHub "owner/repo" whose releases the in-app updater checks. */
    val RELEASE_REPO: String get() = BuildConfig.MITSFLIX_RELEASE_REPO

    /** Where the modified source lives (GPLv3 requires it to stay available). */
    val SOURCE_REPO: String get() = BuildConfig.MITSFLIX_SOURCE_REPO

    /** OwnTV's icon-colour and accent-logo settings: hidden, every colour is the same Mitsflix icon. */
    const val SHOW_OWNTV_ICON_SETTINGS = false
}
