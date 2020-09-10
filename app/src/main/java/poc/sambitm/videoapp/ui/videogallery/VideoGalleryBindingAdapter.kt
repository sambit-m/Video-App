package poc.sambitm.videoapp.ui.videogallery

import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("bitmap")
fun ImageView.setBitmap(data: String?) {
    val uri = Uri.parse(data)
    val bitmap = ThumbnailUtils.createVideoThumbnail(
        uri.path,
        MediaStore.Images.Thumbnails.MINI_KIND
    )
    this.setImageBitmap(bitmap)
}