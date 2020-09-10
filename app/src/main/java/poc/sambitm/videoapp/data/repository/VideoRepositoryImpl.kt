package poc.sambitm.videoapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import poc.sambitm.videoapp.data.model.VideoInfo
import poc.sambitm.videoapp.data.db.VideoDao
import poc.sambitm.videoapp.domain.repository.VideoRepository

class VideoRepositoryImpl(
    private val videoDao: VideoDao
) : VideoRepository {
    override suspend fun bookmarkVideo(videoInfo: VideoInfo) {
        videoDao.bookmarkVideo(videoInfo)
    }

    override suspend fun unBookmarkVideo(videoInfo: VideoInfo) {
        videoDao.unBookmarkVideo(videoInfo)
    }

    override suspend fun getBookmarkedVideos(): List<VideoInfo>? {
        return videoDao.getBookmarkedVideos()
    }

    override fun isBookmarked(data: String): LiveData<List<VideoInfo>?> {
        return videoDao.isBookmarked(data).asLiveData()
    }
}