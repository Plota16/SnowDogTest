package dog.snow.androidrecruittest.ui

import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.repository.model.Global
import kotlinx.android.synthetic.main.details_fragment.*


class DetailsFragment(private var detailId : Int) : Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFragment()
        manageDarkMode()
    }

    private fun initFragment() {

        val toolBar = requireActivity().findViewById<MaterialToolbar>(R.id.toolbar)
        val textToolbar = requireActivity().findViewById<TextView>(R.id.toolbar_title)
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val item = Global.getInstance()!!.detailList[detailId]

        val toolbarParams = toolBar.layoutParams as AppBarLayout.LayoutParams
        toolbarParams.scrollFlags = 0

        tv_photo_title.text = item!!.photoTitle
        tv_album_title.text = item.albumTitle
        tv_email.text = item.email
        tv_phone.text = item.phone
        tv_username.text= item.username
        loadImage(item.url)

        val titlePlaceholder = "  " + item.photoTitle
        textToolbar.text = titlePlaceholder

        if(currentNightMode == Configuration.UI_MODE_NIGHT_YES){
            toolBar.logo = ContextCompat.getDrawable(requireContext(),R.drawable.ic_arrow_back_white_24px)
        }
        else{
            toolBar.logo = ContextCompat.getDrawable(requireContext(),R.drawable.ic_arrow_back_24px)
        }


    }

    override fun onStop() {
        super.onStop()

        val toolBar = requireActivity().findViewById<MaterialToolbar>(R.id.toolbar)
        val textToolbar = requireActivity().findViewById<TextView>(R.id.toolbar_title)
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        textToolbar.text = resources.getString(R.string.app_name)
        if(currentNightMode == Configuration.UI_MODE_NIGHT_YES){
            toolBar.logo = ContextCompat.getDrawable(requireContext(),R.drawable.ic_logo_sd_symbol_dark)
        }
        else{
            toolBar.logo = ContextCompat.getDrawable(requireContext(),R.drawable.ic_logo_sd_symbol)
        }
        val toolbarParams = toolBar.layoutParams as AppBarLayout.LayoutParams
        toolbarParams.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
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
            RequestOptions()
                .placeholder(R.drawable.ic_placeholder)
        } else{
            RequestOptions()
                .placeholder(R.drawable.ic_placeholder_dark)
        }
        Glide.with(requireContext()).load(url)
            .apply(options)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

            })
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
