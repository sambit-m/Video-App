package poc.sambitm.videoapp.domain.usecases

import poc.sambitm.videoapp.data.model.VideoInfo
import poc.sambitm.videoapp.domain.repository.VideoRepository

class UnBookmarkVideoUseCase(private val videoRepository: VideoRepository) {
    suspend fun invoke(videoInfo: VideoInfo) = videoRepository.unBookmarkVideo(videoInfo)
}