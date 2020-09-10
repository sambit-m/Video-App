package poc.sambitm.videoapp.ui.video

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.android.synthetic.main.fragment_video.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import poc.sambitm.videoapp.R
import poc.sambitm.videoapp.data.model.VideoInfo
import poc.sambitm.videoapp.databinding.FragmentVideoBinding
import poc.sambitm.videoapp.domain.usecases.IsBookmarkedUseCase
import poc.sambitm.videoapp.ui.BaseFragment
import poc.sambitm.videoapp.ui.MainActivity


class VideoFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: VideoViewModel by viewModel()
    private lateinit var binding: FragmentVideoBinding
    private val isBookmarkedVideosUseCase: IsBookmarkedUseCase by inject()
    private lateinit var videoInfo: VideoInfo
    private var isBookmarkButtonClicked = false
    private var lastPosition: Long = 0;

    companion object {
        const val KEY_VIDEO_INFO = "video_info"
        const val BUTTON_BOOKMARK = "bookmark"
        const val BUTTON_UNBOOKMARK = "unbookmark"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video, container, false)
        rootView = binding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fullScreen()
        (arguments?.getSerializable(KEY_VIDEO_INFO) as VideoInfo?)?.let {
            videoInfo = it
            initializePlayer(Uri.parse(it.data))
        }
        imageView_bookmark.setOnClickListener(this)
        isBookmarkedVideosUseCase.invoke(videoInfo.data)
            .observe(viewLifecycleOwner, Observer {
                if (it.isNullOrEmpty()) {
                    binding.imageViewBookmark.apply {
                        tag = BUTTON_BOOKMARK
                        setImageDrawable(
                            ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.ic_bookmark_off,
                                requireContext().theme
                            )
                        )
                    }
                    if (isBookmarkButtonClicked)
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.bookmark_removed),
                            Toast.LENGTH_SHORT
                        ).show()
                } else {
                    binding.imageViewBookmark.apply {
                        tag = BUTTON_UNBOOKMARK
                        setImageDrawable(
                            ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.ic_bookmark_on,
                                requireContext().theme
                            )
                        )
                    }
                    if (isBookmarkButtonClicked)
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.bookmarked_successfully),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                }
                isBookmarkButtonClicked = false
            })
    }

    private fun initializePlayer(videoUrl: Uri, lastPosition: Long = 0L) {
        binding.playerView.player.let {
            if (it == null) {
                val player = ExoPlayerFactory.newSimpleInstance(
                    requireContext(),
                    DefaultRenderersFactory(requireActivity()),
                    DefaultTrackSelector(requireContext()), DefaultLoadControl()
                )
                binding.playerView.player = player
                val mediaSource: MediaSource = buildMediaSource(videoUrl)
                player.prepare(mediaSource, false, false)
            }
        }
        binding.playerView.player?.playWhenReady = true
        binding.playerView.player?.seekTo(lastPosition)
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        return ProgressiveMediaSource.Factory(
            DefaultDataSourceFactory(activity, "Exoplayer-local")
        ).createMediaSource(uri)
    }

    override fun onClick(v: View?) {
        when (v?.tag) {
            BUTTON_BOOKMARK -> {
                if (::videoInfo.isInitialized) {
                    isBookmarkButtonClicked = true
                    viewModel.bookmarkVideo(videoInfo)
                }
            }
            BUTTON_UNBOOKMARK -> {
                if (::videoInfo.isInitialized) {
                    isBookmarkButtonClicked = true
                    viewModel.unBookmarkVideo(videoInfo)
                }
            }
        }
    }

    private fun pausePlayer() {
        binding.playerView.player?.playWhenReady = false
        binding.playerView.player?.currentPosition?.let {
            lastPosition = it
        }
    }

    private fun startPlayer() {
        initializePlayer(Uri.parse(videoInfo.data), lastPosition)
    }

    override fun onResume() {
        super.onResume()
        startPlayer()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onStop() {
        super.onStop()
        pausePlayer()
    }

    override fun onDetach() {
        super.onDetach()
        hideFullScreen()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.playerView.player?.stop()
        binding.playerView.player?.release()
    }

    private fun fullScreen() {
        requireActivity().window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        (requireActivity() as MainActivity).supportActionBar?.hide()
    }

    private fun hideFullScreen() {
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        (requireActivity() as MainActivity).supportActionBar?.show()
    }
}