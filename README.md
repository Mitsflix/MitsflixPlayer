# Mitsflix Player

The Mitsflix IPTV player for Android TV, Fire TV and Android boxes.

**Download (always the latest):**
https://github.com/Mitsflix/MitsflixPlayer/releases/latest/download/MitsflixPlayer.apk

Need a playlist? Visit **https://mitsflix.com**.

## How updates work
- **OwnTV updates (automatic):** every morning the *Follow OwnTV releases* workflow checks OwnTV.
  When OwnTV publishes a new version (say v5.3.0), it is merged in, the Mitsflix wording is
  re-applied, and Mitsflix Player v5.3.0 is built, signed and released. Installed copies offer the
  update, with OwnTV's "What's new" notes (renamed to Mitsflix Player).
- **If a merge needs a hand:** when OwnTV changes the same lines Mitsflix changed, nothing is
  published and an issue is opened on this repository saying which files. Ask Claude to fix it.
- **Run it now:** Actions → Follow OwnTV releases → Run workflow.

## Licence and credit
Mitsflix Player is a modified version of **OwnTV** by ahXN00
(https://github.com/ahXN00/OwnTV), used under the GNU General Public License v3 (see `LICENSE`).
Changes from OwnTV: Mitsflix branding (name, icons, banner, splash, logo), a "Get a playlist"
option linking to mitsflix.com, the package ID `com.mitsflix.player`, and updates from this repository.
The original OwnTV README is kept in `README-OwnTV.md`.

Mitsflix Player is a player only: it ships no channels or streams.
