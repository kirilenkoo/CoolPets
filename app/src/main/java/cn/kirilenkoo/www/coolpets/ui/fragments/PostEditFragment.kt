package cn.kirilenkoo.www.coolpets.ui.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.api.submitPost
import cn.kirilenkoo.www.coolpets.base.BaseApplication
import cn.kirilenkoo.www.coolpets.base.BaseFragment
import cn.kirilenkoo.www.coolpets.binding.FragmentDataBindingComponent
import cn.kirilenkoo.www.coolpets.databinding.PostDetailFragmentBinding
import cn.kirilenkoo.www.coolpets.databinding.PostEditFragmentBinding
import cn.kirilenkoo.www.coolpets.di.Injectable
import cn.kirilenkoo.www.coolpets.thirdparty.GlideApp
import cn.kirilenkoo.www.coolpets.ui.view.StateImageView
import cn.kirilenkoo.www.coolpets.util.FindMedian
import cn.kirilenkoo.www.coolpets.util.autoCleared
import cn.kirilenkoo.www.coolpets.util.getPath
import cn.kirilenkoo.www.coolpets.viewmodel.EditPost
import cn.kirilenkoo.www.coolpets.viewmodel.PostDetailViewModel
import cn.kirilenkoo.www.coolpets.viewmodel.PostEditViewModel
import timber.log.Timber
import java.io.File
import java.util.*
import java.util.concurrent.ThreadLocalRandom
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
                0 -> addTextContent()
                1 -> addImageContent()
                2 -> {
                    var bundle = Bundle()
                    bundle.putParcelable("editPost", viewModel.getTmpPost())
                    findNavController().navigate(R.id.action_postEdeitFragment_to_postPreviewFragment,bundle)
                }
                3 -> {
                    viewModel.getTmpPost().postTitle = binding.editPostTitle.text.toString()
                    viewModel.submitPost()
                }

            }
        }
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PostEditViewModel::class.java)
        viewModel.postUpdateData.observe(viewLifecycleOwner, Observer {
            Timber.d("submit:${it.status}->${it.data}")
        })
        return dataBinding.root
    }

    private fun addTextContent() {
        val edit = EditText(activity as Context)
        edit.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                val s = p0.toString()
                viewModel.textChanged(binding.containerEditContents.indexOfChild(edit),s)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        val llp:LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        binding.containerEditContents.addView(edit,llp)
        viewModel.addTextContent(null)
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
        var tmpPost:EditPost
        if(savedInstanceState!=null){
            tmpPost = savedInstanceState.getParcelable("tmpPost")
            viewModel.setTmpPost(tmpPost)

        }else{
            //back from preview
            tmpPost = viewModel.getTmpPost()
        }
        GlideApp.with(this).load(tmpPost.coverPath).centerCrop().into(binding.imgPostCover)
        val viewList:ArrayList<View> = ArrayList()
        for(c in tmpPost.contents){
            val llp:LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            if(c.url?.isEmpty() != true){
                val contentImage = StateImageView(activity as Context)
                binding.containerEditContents.addView(contentImage,llp)
                GlideApp.with(this).load(c.url).fitCenter().into(contentImage)
                viewList.add(contentImage)
            }else{
                val contentText = EditText(activity as Context)
                binding.containerEditContents.addView(contentText,llp)
                contentText.setText(c.text)
                viewList.add(contentText)
            }
        }
        viewModel.rebindImageViews(binding.imgPostCover,viewList,viewLifecycleOwner)
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
