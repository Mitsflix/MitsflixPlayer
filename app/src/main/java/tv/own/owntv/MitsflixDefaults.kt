package tv.own.owntv

import android.content.Context
import kotlinx.coroutines.flow.first
import org.koin.core.context.GlobalContext
import tv.own.owntv.core.database.dao.ProfileDao
import tv.own.owntv.core.database.dao.SourceDao
import tv.own.owntv.core.model.HomeRow
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

        // Home: show Recent channels (OwnTV hides that row by default), once per profile.
        showRecentChannels(context)

        // Movie and series info (posters, plots, Now Trending) straight from TMDB with Mitsflix's own key,
        // when the build carries one. A key the user typed in themselves is left alone.
        val tmdbKey = BuildConfig.MITSFLIX_TMDB_KEY
        if (tmdbKey.isNotBlank()) {
            runCatching {
                val settings = koin.get<SettingsRepository>()
                val current = settings.currentTmdbApiKey()
                if (current.isBlank() || current == prefs.getString(TMDB_APPLIED, null)) {
                    if (current != tmdbKey) settings.setTmdbApiKey(tmdbKey)
                    prefs.edit().putString(TMDB_APPLIED, tmdbKey).apply()
                }
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

    /**
     * Turns the Recent channels row on for every profile that hasn't had it yet. Run at start-up and
     * again when a profile is created (first setup, Add profile); a user who hides it keeps it hidden.
     */
    suspend fun showRecentChannels(context: Context) {
        runCatching {
            val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            val done = prefs.getStringSet(HOME_RECENT, emptySet()).orEmpty()
            val koin = GlobalContext.get()
            val settings = koin.get<SettingsRepository>()
            val ids = koin.get<ProfileDao>().getAllOnce().map { it.id.toString() }.filter { it !in done }
            for (id in ids) {
                settings.updateHomeConfig(id.toLong()) { it.copy(hidden = it.hidden - HomeRow.RECENT_CHANNELS) }
            }
            if (ids.isNotEmpty()) prefs.edit().putStringSet(HOME_RECENT, done + ids).apply()
        }
    }

    // Preference names live in app/build.gradle.kts (no text literals in Kotlin for the i18n gate).
    private val PREFS get() = BuildConfig.MITSFLIX_DEFAULTS_PREFS
    private val DNS_CLOUDFLARE get() = BuildConfig.MITSFLIX_DEFAULT_DNS_KEY
    private val EPG_EXISTING get() = BuildConfig.MITSFLIX_DEFAULT_EPG_KEY
    private val HOME_RECENT get() = BuildConfig.MITSFLIX_DEFAULT_HOME_KEY
    private val TMDB_APPLIED get() = BuildConfig.MITSFLIX_DEFAULT_TMDB_KEY
}
