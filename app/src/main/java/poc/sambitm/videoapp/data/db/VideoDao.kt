package poc.sambitm.videoapp.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import poc.sambitm.videoapp.data.model.VideoInfo

@Dao
interface VideoDao {
    @Insert
    suspend fun bookmarkVideo(videoInfo: VideoInfo)

    @Delete
    suspend fun unBookmarkVideo(videoInfo: VideoInfo)

    @Query("SELECT * FROM video_info WHERE data= :data")
    fun isBookmarked(data: String): Flow<List<VideoInfo>?>

    @Query("SELECT * FROM video_info")
    suspend fun getBookmarkedVideos(): List<VideoInfo>?
}