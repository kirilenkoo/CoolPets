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
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.base.BaseApplication
import cn.kirilenkoo.www.coolpets.base.BaseFragment
import cn.kirilenkoo.www.coolpets.binding.FragmentDataBindingComponent
import cn.kirilenkoo.www.coolpets.databinding.PostDetailFragmentBinding
import cn.kirilenkoo.www.coolpets.databinding.PostEditFragmentBinding
import cn.kirilenkoo.www.coolpets.di.Injectable
import cn.kirilenkoo.www.coolpets.util.autoCleared
import cn.kirilenkoo.www.coolpets.util.getPath
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
    var coverPath:String? = null

    val PICK_IMAGE_REQUEST = 1
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
                1 -> Timber.d("1")
                2 -> Timber.d("2")
                3 -> Timber.d("3")

            }
        }
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PostEditViewModel::class.java)
        viewModel.tmpPost.observe(viewLifecycleOwner, Observer {
            //if has cover url, set cover url
            //if has savedinstance, set imageview state:
             viewModel.bindImageView(binding.imgPostCover,savedInstanceState?.getString("coverPath"),viewLifecycleOwner)
//            binding.editPostTitle.setText(it.post.title)
            //parse contents to container
        })
        binding.imgPostCover.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//            intent.type = "image/*"
//            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent,PICK_IMAGE_REQUEST)

        }
        return dataBinding.root
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("coverPath", coverPath)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when (requestCode){
                PICK_IMAGE_REQUEST -> {
                    val imageUri:Uri = data?.data!!
                    Timber.d(getPath(activity as Context,imageUri))
                    binding.imgPostCover.setImageURI(imageUri)
                    coverPath = getPath(activity as Context, imageUri)
                    viewModel.bindImageView(binding.imgPostCover,coverPath,viewLifecycleOwner)
                    viewModel.uploadImg(coverPath)

                }
            }
        }

    }
}
