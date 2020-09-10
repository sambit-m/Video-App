package poc.sambitm.videoapp.ui.videogallery

import androidx.recyclerview.widget.RecyclerView
import poc.sambitm.videoapp.data.model.VideoInfo
import poc.sambitm.videoapp.databinding.ItemVideoGalleryBinding
import poc.sambitm.videoapp.utils.dpToPx

class VideoGalleryViewHolder(
    private val binding: ItemVideoGalleryBinding,
    private val screenWidth: Int
) : RecyclerView.ViewHolder(binding.root) {
    fun setItem(
        videoInfo: VideoInfo,
        onVideoItemClickListener: OnVideoItemClickListener
    ) {
        binding.item = videoInfo
        itemView.setOnClickListener {
            onVideoItemClickListener.onVideoItemClick(videoInfo)
        }
        val itemWidth = screenWidth / 2
        binding.imageViewThumbnail.layoutParams.width =
            dpToPx(
                itemView.context,
                itemWidth.toFloat()
            )
        binding.executePendingBindings()
    }
}