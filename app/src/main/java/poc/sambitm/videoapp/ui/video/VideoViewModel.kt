package poc.sambitm.videoapp.ui.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import poc.sambitm.videoapp.data.model.VideoInfo
import poc.sambitm.videoapp.domain.usecases.BookmarkVideoUseCase
import poc.sambitm.videoapp.domain.usecases.UnBookmarkVideoUseCase

class VideoViewModel(
    private val bookmarkVideoUseCase: BookmarkVideoUseCase,
    private val unBookmarkVideoUseCase: UnBookmarkVideoUseCase
) : ViewModel() {

//    private val _isBookmarked = MutableLiveData<Boolean>()
//    val isBookmarked: LiveData<Boolean>
//        get() = _isBookmarked

    fun bookmarkVideo(videoInfo: VideoInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            bookmarkVideoUseCase.invoke(videoInfo)
        }
    }

    fun unBookmarkVideo(videoInfo: VideoInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            unBookmarkVideoUseCase.invoke(videoInfo)
        }
    }
}