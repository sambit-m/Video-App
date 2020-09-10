package poc.sambitm.videoapp.ui.videogallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import poc.sambitm.videoapp.data.model.VideoInfo
import poc.sambitm.videoapp.domain.usecases.GetBookmarkedVideosUseCase

class VideoGalleryViewModel(
    private val getBookmarkedVideosUseCase: GetBookmarkedVideosUseCase
) : ViewModel() {
    private val _allVideos = MutableLiveData<ArrayList<VideoInfo>>()
    val allVideos: LiveData<ArrayList<VideoInfo>>
        get() = _allVideos

    private val _bookmarkedVideos = MutableLiveData<List<VideoInfo>?>()
    val bookmarkedVideos: LiveData<List<VideoInfo>?>
        get() = _bookmarkedVideos

    fun setAllVideos(allVideos: ArrayList<VideoInfo>) {
        _allVideos.value = allVideos
    }

    fun fetchBookmarkedVideos() {
        viewModelScope.launch(Dispatchers.IO) {
            _bookmarkedVideos.postValue(getBookmarkedVideosUseCase.invoke())
        }
    }
}