package cn.kirilenkoo.www.coolpets.ui.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.base.BaseApplication
import cn.kirilenkoo.www.coolpets.base.BaseFragment
import cn.kirilenkoo.www.coolpets.binding.FragmentDataBindingComponent
import cn.kirilenkoo.www.coolpets.databinding.PostDetailFragmentBinding
import cn.kirilenkoo.www.coolpets.databinding.PostEditFragmentBinding
import cn.kirilenkoo.www.coolpets.di.Injectable
import cn.kirilenkoo.www.coolpets.thirdparty.GlideApp
import cn.kirilenkoo.www.coolpets.ui.view.StateImageView
import cn.kirilenkoo.www.coolpets.util.autoCleared
import cn.kirilenkoo.www.coolpets.util.getPath
import cn.kirilenkoo.www.coolpets.viewmodel.EditPost
import cn.kirilenkoo.www.coolpets.viewmodel.PostDetailViewModel
import cn.kirilenkoo.www.coolpets.viewmodel.PostEditViewModel
import timber.log.Timber
import javax.inject.Inject

class PostEditFragment : BaseFragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: PostEditViewModel

    var binding by autoCleared<PostEditFragmentBinding>()
    var dataBindingComponent = FragmentDataBindingComponent(this)
//    var coverPath:String? = null
//    val contents:ArrayList<String> = ArrayList()

    private val PICK_IMAGE_COVER_REQUEST = 1
    private val PICK_IMAGE_CONTENT_REQUEST = 2
    companion object {
        fun newInstance() = PostEditFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val dataBinding = DataBindingUtil.inflate<PostEditFragmentBinding>(
                inflater,
                R.layout.post_edit_fragment,
                container,
                false,
                dataBindingComponent
        )
        binding = dataBinding
        binding.panelListener = View.OnClickListener {
            when (binding.containerEditPanel.indexOfChild(it)){
                0 -> {
                    Timber.d("0")
                }
                1 -> addImageContent()
                2 -> {
                    var bundle = Bundle()
                    bundle.putParcelable("editPost", viewModel.getTmpPost())
                    findNavController().navigate(R.id.postPreviewFragment,bundle)
                }
                3 -> Timber.d("3")

            }
        }
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PostEditViewModel::class.java)
        return dataBinding.root
    }

    private fun addImageContent() {
        val intent = Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_CONTENT_REQUEST)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("tmpPost", viewModel.getTmpPost())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(savedInstanceState!=null){
            val tmpPost:EditPost = savedInstanceState.getParcelable("tmpPost")
            viewModel.setTmpPost(tmpPost)
            GlideApp.with(this).load(tmpPost.coverPath).centerCrop().into(binding.imgPostCover)
            val imgList:ArrayList<ImageView> = ArrayList()
            for(c in tmpPost.contents){
                val llp:LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                val contentImage = StateImageView(activity as Context)
                binding.containerEditContents.addView(contentImage,llp)
                GlideApp.with(this).load(c.url).fitCenter().into(contentImage)
                imgList.add(contentImage)
            }
            viewModel.rebindImageViews(binding.imgPostCover,imgList,viewLifecycleOwner)
        }
        binding.imgPostCover.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(intent,PICK_IMAGE_COVER_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when (requestCode){
                PICK_IMAGE_COVER_REQUEST -> {
                    val imageUri:Uri = data?.data!!
                    Timber.d(getPath(activity as Context,imageUri))
                    binding.imgPostCover.setImageURI(imageUri)
                    val coverPath = getPath(activity as Context, imageUri)
                    GlideApp.with(this).load(coverPath).centerCrop().into(binding.imgPostCover)
                    viewModel.addPostCover(binding.imgPostCover,coverPath,viewLifecycleOwner)

                }
                PICK_IMAGE_CONTENT_REQUEST -> {
                    val imageUri:Uri = data?.data!!
                    Timber.d(getPath(activity as Context,imageUri))
                    val contentPath = getPath(activity as Context, imageUri)
                    val llp:LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                    val contentImage = StateImageView(activity as Context)
                    binding.containerEditContents.addView(contentImage,llp)
                    GlideApp.with(this).load(contentPath).fitCenter().into(contentImage)
                    viewModel.addImgContent(contentImage,contentPath,viewLifecycleOwner)
                }
            }
        }

    }
}
