package dog.snow.androidrecruittest.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.repository.model.Global
import kotlinx.android.synthetic.main.details_fragment.*
import kotlinx.android.synthetic.main.list_fragment.*
class DetailsFragment : Fragment(){

    var detailId = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        manageDarkMode()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden){
            val item = Global.getInstance()!!.detailList[detailId]
            tv_photo_title.text = item!!.photoTitle
            tv_album_title.text = item.albumTitle
            tv_email.text = item.email
            tv_phone.text = item.phone
            tv_username.text= item.username

            loadImage(item.url)


        }
    }

    private fun loadImage(baseUrl : String){
        val url = GlideUrl(
            baseUrl, LazyHeaders.Builder()
                .addHeader("User-Agent", "your-user-agent")
                .build()
        )
        val options: RequestOptions?
        val currentNightMode = requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        options = if(currentNightMode == Configuration.UI_MODE_NIGHT_YES){
            RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.ic_placeholder)
        } else{
            RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.ic_placeholder_dark)
        }
        Glide.with(requireContext()).load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(options)
            .into(iv_photo)
    }

    private fun manageDarkMode(){
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if(currentNightMode == Configuration.UI_MODE_NIGHT_YES){
            val colorValue = ContextCompat.getColor(requireContext(), R.color.dark_background_color)
            detail_id.setBackgroundColor(colorValue)
        }
        if(currentNightMode == Configuration.UI_MODE_NIGHT_NO){
            val colorValue = ContextCompat.getColor(requireContext(), R.color.light_background_color)
            detail_id.setBackgroundColor(colorValue)
        }
    }

}
