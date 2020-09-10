package poc.sambitm.videoapp.ui.videogallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import poc.sambitm.videoapp.data.model.VideoInfo
import poc.sambitm.videoapp.databinding.ItemVideoGalleryBinding

class VideoGalleryAdapter(
    private val onVideoItemClickListener: OnVideoItemClickListener,
    private val screenWidth: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var videoList = ArrayList<VideoInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemVideoGalleryBinding.inflate(inflater)
        return VideoGalleryViewHolder(binding, screenWidth)
    }

    override fun getItemCount(): Int = videoList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VideoGalleryViewHolder).setItem(videoList[position], onVideoItemClickListener)
    }


    fun setData(videoList: ArrayList<VideoInfo>?) {
        if (!videoList.isNullOrEmpty()) {
            this.videoList = videoList
        } else
            this.videoList = ArrayList()
        notifyDataSetChanged()
    }
}