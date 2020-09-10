package poc.sambitm.videoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_info")
data class VideoInfo(
    val title: String? = null,
    val duration: String,
    @PrimaryKey
    val data: String
) : BaseModel()