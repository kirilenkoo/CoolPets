package cn.kirilenkoo.www.coolpets.binding

import android.content.Context
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import cn.kirilenkoo.www.coolpets.model.PostContent
import com.bumptech.glide.Glide
import javax.inject.Inject
import androidx.databinding.ViewDataBinding
import androidx.databinding.DataBindingUtil
import android.view.ViewGroup
import cn.kirilenkoo.www.coolpets.BR
import cn.kirilenkoo.www.coolpets.thirdparty.GlideApp


class FragmentBindingAdapters @Inject constructor(val fragment: Fragment) {
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, url: String?) {
        GlideApp.with(fragment).load(url).centerInside().into(imageView)
    }
    @BindingAdapter(value = ["contents", "layout"], requireAll = false)
    fun fullContents(container: LinearLayout, contents: List<PostContent>, layout: Int ){
        container.removeAllViews()
        val inflater: LayoutInflater = container.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        for (content in contents){
            val binding = bindLayout(inflater, container,
                    layout, content)
            container.addView(binding.getRoot())
        }
    }

    private fun bindLayout(inflater: LayoutInflater,
                           parent: ViewGroup, layoutId: Int, entry: Any): ViewDataBinding {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater,
                layoutId, parent, false, FragmentDataBindingComponent(fragment))
        if (!binding.setVariable(BR.content, entry)) {
        }
        return binding
    }
}