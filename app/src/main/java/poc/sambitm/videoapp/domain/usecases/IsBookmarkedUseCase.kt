package poc.sambitm.videoapp.domain.usecases

import poc.sambitm.videoapp.domain.repository.VideoRepository

class IsBookmarkedUseCase(private val videoRepository: VideoRepository) {
    fun invoke(data: String) = videoRepository.isBookmarked(data)
}