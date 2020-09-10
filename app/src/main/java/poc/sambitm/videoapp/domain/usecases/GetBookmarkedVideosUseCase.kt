package poc.sambitm.videoapp.domain.usecases

import poc.sambitm.videoapp.domain.repository.VideoRepository

class GetBookmarkedVideosUseCase(private val videoRepository: VideoRepository) {
    suspend fun invoke() = videoRepository.getBookmarkedVideos()
}