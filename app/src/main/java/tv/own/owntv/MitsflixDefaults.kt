package tv.own.owntv

import android.content.Context
import kotlinx.coroutines.flow.first
import org.koin.core.context.GlobalContext
import tv.own.owntv.core.database.dao.SourceDao
import tv.own.owntv.core.epg.EpgSourceStore
import tv.own.owntv.core.network.DohPresets
import tv.own.owntv.core.repository.EpgRepository
import tv.own.owntv.core.settings.SettingsRepository
import tv.own.owntv.features.settings.runSemiAutoEpgSync

/**
 * Mitsflix Player's own defaults, each applied once per install (an update applies a new one once too).
 * A user who changes a setting afterwards keeps their choice.
 */
object MitsflixDefaults {

    suspend fun apply(context: Context) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val koin = GlobalContext.get()

        // Cloudflare DNS (DNS-over-HTTPS) on by default, unless the user already set up their own DNS.
        if (!prefs.getBoolean(DNS_CLOUDFLARE, false)) {
            runCatching {
                val settings = koin.get<SettingsRepository>()
                val current = settings.dnsConfig.first()
                if (!current.enabled) settings.saveDns(true, current.host, current.port, DohPresets.CLOUDFLARE)
                prefs.edit().putBoolean(DNS_CLOUDFLARE, true).apply()
            }
        }

        // Playlists added before the guide was automatic: fetch each one's own guide once, if it has one
        // and it isn't on the EPG sources list yet. New playlists get theirs as soon as they're added.
        if (!prefs.getBoolean(EPG_EXISTING, false)) {
            runCatching {
                val store = koin.get<EpgSourceStore>()
                val epg = koin.get<EpgRepository>()
                val known = store.getAll().map { it.url }.toSet()
                for (source in koin.get<SourceDao>().getAllOnce()) {
                    val urls = epg.guideUrls(source)
                    if (urls.isNotEmpty() && urls.none { it in known }) runSemiAutoEpgSync(source, epg, store) { }
                }
                prefs.edit().putBoolean(EPG_EXISTING, true).apply()
            }
        }
    }

    // Preference names live in app/build.gradle.kts (no text literals in Kotlin for the i18n gate).
    private val PREFS get() = BuildConfig.MITSFLIX_DEFAULTS_PREFS
    private val DNS_CLOUDFLARE get() = BuildConfig.MITSFLIX_DEFAULT_DNS_KEY
    private val EPG_EXISTING get() = BuildConfig.MITSFLIX_DEFAULT_EPG_KEY
}
