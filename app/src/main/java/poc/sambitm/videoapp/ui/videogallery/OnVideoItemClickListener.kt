package poc.sambitm.videoapp.ui.videogallery

import poc.sambitm.videoapp.data.model.VideoInfo

interface OnVideoItemClickListener {
    fun onVideoItemClick(viewInfo: VideoInfo)
}