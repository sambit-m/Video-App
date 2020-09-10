package poc.sambitm.videoapp.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import poc.sambitm.videoapp.ui.video.VideoViewModel
import poc.sambitm.videoapp.ui.videogallery.VideoGalleryViewModel

val viewModelModule = module {
    viewModel {
        VideoGalleryViewModel(getBookmarkedVideosUseCase = get())
    }
    viewModel {
        VideoViewModel(
            bookmarkVideoUseCase = get(),
            unBookmarkVideoUseCase = get()
        )
    }
}