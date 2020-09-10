package poc.sambitm.videoapp.di

import org.koin.dsl.module
import poc.sambitm.videoapp.data.repository.VideoRepositoryImpl
import poc.sambitm.videoapp.domain.repository.VideoRepository
import poc.sambitm.videoapp.domain.usecases.BookmarkVideoUseCase
import poc.sambitm.videoapp.domain.usecases.GetBookmarkedVideosUseCase
import poc.sambitm.videoapp.domain.usecases.IsBookmarkedUseCase
import poc.sambitm.videoapp.domain.usecases.UnBookmarkVideoUseCase

val repositoryModule = module {
    single<VideoRepository> { VideoRepositoryImpl(videoDao = get()) }
}

val useCaseModule = module {
    factory { BookmarkVideoUseCase(videoRepository = get()) }
    factory { UnBookmarkVideoUseCase(videoRepository = get()) }
    factory { GetBookmarkedVideosUseCase(videoRepository = get()) }
    factory { IsBookmarkedUseCase(videoRepository = get()) }
}