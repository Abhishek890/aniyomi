package eu.kanade.domain.items.episode.interactor

import eu.kanade.domain.entries.anime.interactor.GetAnimeFavorites
import eu.kanade.domain.entries.anime.interactor.SetAnimeEpisodeFlags
import eu.kanade.domain.library.service.LibraryPreferences
import tachiyomi.core.util.lang.withNonCancellableContext
import tachiyomi.domain.entries.anime.model.Anime

class SetAnimeDefaultEpisodeFlags(
    private val libraryPreferences: LibraryPreferences,
    private val setAnimeEpisodeFlags: SetAnimeEpisodeFlags,
    private val getFavorites: GetAnimeFavorites,
) {

    suspend fun await(anime: Anime) {
        withNonCancellableContext {
            with(libraryPreferences) {
                setAnimeEpisodeFlags.awaitSetAllFlags(
                    animeId = anime.id,
                    unseenFilter = filterEpisodeBySeen().get(),
                    downloadedFilter = filterEpisodeByDownloaded().get(),
                    bookmarkedFilter = filterEpisodeByBookmarked().get(),
                    sortingMode = sortEpisodeBySourceOrNumber().get(),
                    sortingDirection = sortEpisodeByAscendingOrDescending().get(),
                    displayMode = displayEpisodeByNameOrNumber().get(),
                )
            }
        }
    }

    suspend fun awaitAll() {
        withNonCancellableContext {
            getFavorites.await().forEach { await(it) }
        }
    }
}
