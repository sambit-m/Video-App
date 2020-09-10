package poc.sambitm.videoapp.di

import androidx.room.Room
import org.koin.dsl.module
import poc.sambitm.videoapp.data.db.AppDatabase

val dataBaseModule = module {
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "video_app_database").build()
    }
    single { get<AppDatabase>().videoDao() }
}