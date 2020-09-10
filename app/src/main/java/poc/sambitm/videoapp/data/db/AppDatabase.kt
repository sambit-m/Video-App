package poc.sambitm.videoapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import poc.sambitm.videoapp.data.model.VideoInfo

@Database(entities = [VideoInfo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao
}
