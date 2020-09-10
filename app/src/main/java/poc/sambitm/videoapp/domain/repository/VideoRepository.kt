package poc.sambitm.videoapp.domain.repository

import androidx.lifecycle.LiveData
import poc.sambitm.videoapp.data.model.VideoInfo

interface VideoRepository {
    suspend fun bookmarkVideo(videoInfo: VideoInfo)
    suspend fun unBookmarkVideo(videoInfo: VideoInfo)
    suspend fun getBookmarkedVideos(): List<VideoInfo>?
    fun isBookmarked(data: String): LiveData<List<VideoInfo>?>
}