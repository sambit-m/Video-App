package poc.sambitm.videoapp.ui.videogallery

import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import poc.sambitm.videoapp.R
import poc.sambitm.videoapp.data.model.VideoInfo
import poc.sambitm.videoapp.databinding.FragmentVideoGalleryBinding
import poc.sambitm.videoapp.ui.BaseFragment
import poc.sambitm.videoapp.ui.video.VideoFragment
import poc.sambitm.videoapp.utils.PermissionUtils
import poc.sambitm.videoapp.utils.pxToDp


class VideoGalleryFragment : BaseFragment(),
    OnVideoItemClickListener, View.OnClickListener {

    private val viewModel: VideoGalleryViewModel by viewModel()
    private val permissionUtils: PermissionUtils by inject()
    private lateinit var binding: FragmentVideoGalleryBinding
    private var isShowAllVideosSelected = true
    private lateinit var videoGalleryAdapter: VideoGalleryAdapter

    companion object {
        const val BUTTON_REQUEST_PERMISSION = "request_permission"
        const val BUTTON_PERMISSION_SETTING = "permission_setting"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_video_gallery, container, false)
        rootView = binding.root
        return rootView
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchBookmarkedVideos()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        videoGalleryAdapter = VideoGalleryAdapter(
            this,
            getScreenWidth()
        )
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.recyclerViewVideoGallery)
        binding.recyclerViewVideoGallery.apply {
            setHasFixedSize(true)
            adapter = videoGalleryAdapter
            layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        }

        if (permissionUtils.isReadExternalStoragePermissionAvailable())
            fetchAllVideos()
        else {
            permissionUtils.requestReadExternalStoragePermissionDialog(this)
        }

        viewModel.allVideos.observe(viewLifecycleOwner, Observer {
            if (isShowAllVideosSelected)
                videoGalleryAdapter.setData(it)
        })

        viewModel.bookmarkedVideos.observe(viewLifecycleOwner, Observer {
            if (!isShowAllVideosSelected) {
                videoGalleryAdapter.setData(it as ArrayList<VideoInfo>?)
                if (it.isNullOrEmpty())
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.no_bookmarked_videos_found),
                        Toast.LENGTH_SHORT
                    ).show()
            }
        })

        binding.buttonRetry.setOnClickListener(this)
    }

    private fun fetchAllVideos() {
        binding.buttonRetry.visibility = View.GONE
        binding.recyclerViewVideoGallery.visibility = View.VISIBLE
        val allVideos = ArrayList<VideoInfo>()
        val contentResolver: ContentResolver = requireActivity().contentResolver
        val uri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val title: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                val duration: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))
                val data: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                allVideos.add(
                    VideoInfo(
                        title = title,
                        duration = duration,
                        data = data
                    )
                )
            } while (cursor.moveToNext())
        }
        viewModel.setAllVideos(allVideos)
    }

    override fun onVideoItemClick(viewInfo: VideoInfo) {
        findNavController().navigate(
            R.id.action_videoGalleryFragment_to_videoFragment, bundleOf(
                VideoFragment.KEY_VIDEO_INFO to viewInfo
            )
        )
    }

    private fun getScreenWidth(): Int {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        return pxToDp(
            requireContext(),
            displayMetrics.widthPixels
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_video_gallery, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.show_all_videos).isVisible = !isShowAllVideosSelected
        menu.findItem(R.id.show_bookmarked_videos).isVisible = isShowAllVideosSelected
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_all_videos -> {
                isShowAllVideosSelected = true
                fetchAllVideos()
            }
            R.id.show_bookmarked_videos -> {
                isShowAllVideosSelected = false
                viewModel.fetchBookmarkedVideos()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermissionUtils.PERMISSION_READ_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    binding.buttonRetry.tag = BUTTON_REQUEST_PERMISSION
                    fetchAllVideos()
                } else {
                    binding.buttonRetry.tag = BUTTON_REQUEST_PERMISSION
                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
                    val showRationale = shouldShowRequestPermissionRationale(permissions[0])
                    if (!showRationale) {
                        binding.buttonRetry.tag = BUTTON_PERMISSION_SETTING
                        permissionSettingDialog()
                    }
                    binding.buttonRetry.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun permissionSettingDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("External storage read permission required")
            .setMessage(
                "Please allow to use this feature. Go to settings->Applications(Apps)->${getString(
                    R.string.app_name
                )}->Permissions->Allow storage permission."
            )
            .setPositiveButton("Ok") { dialog, which ->
                goToAppSetting()
                dialog.cancel()
            }
            .setCancelable(false)
            .show()
    }

    private fun goToAppSetting() {
        val myAppSettings = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:" + requireContext().packageName)
        )
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT)
        myAppSettings.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivityForResult(
            myAppSettings,
            PermissionUtils.REQUEST_SETTINGS
        )
    }

    override fun onClick(v: View?) {
        when (v?.tag) {
            BUTTON_REQUEST_PERMISSION -> {
                permissionUtils.requestReadExternalStoragePermissionDialog(this)
            }
            BUTTON_PERMISSION_SETTING -> {
                permissionSettingDialog()
            }
        }
    }
}