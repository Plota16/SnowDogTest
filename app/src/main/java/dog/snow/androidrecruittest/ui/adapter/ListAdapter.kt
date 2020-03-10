package dog.snow.androidrecruittest.ui.adapter


import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.repository.model.Global
import dog.snow.androidrecruittest.ui.model.ListItem
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class ListAdapter(val ctx: Context, val activity : Activity, private val list : ArrayList<ListItem>) :
    RecyclerView.Adapter<MyViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(ctx)
    private val arrayList: ArrayList<ListItem> = ArrayList<ListItem>()

    init {

        this.arrayList.addAll(Global.getInstance()!!.itemList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = inflater.inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvTitle.text = list[position].title
        holder.tvAlbumTitle.text = list[position].albumTitle

        val url = GlideUrl(
            list[position].thumbnailUrl, LazyHeaders.Builder()
                .addHeader("User-Agent", "your-user-agent")
                .build()
        )

        val options: RequestOptions?
        val currentNightMode = ctx.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        options = if(currentNightMode == Configuration.UI_MODE_NIGHT_YES){
            RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.ic_placeholder)
        } else{
            RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.ic_placeholder_dark)
        }
        Glide.with(ctx).load(url)
            .transition(withCrossFade())
            .apply(options)
            .into(holder.ivThumb)


    }

    override fun getItemCount(): Int {
        return list.size
    }


    fun filter(text: String) {
        var charText = text
        charText = charText.toLowerCase(Locale.getDefault())
        Global.getInstance()!!.itemList.clear()
        if (charText.isEmpty()) {
            Global.getInstance()!!.itemList.addAll(arrayList)
        } else {
            for (wp in arrayList) {
                if (wp.title.toLowerCase(Locale.getDefault()).contains(charText) or wp.albumTitle.toLowerCase(Locale.getDefault()).contains(charText)) {
                    Global.getInstance()!!.itemList.add(wp)
                }
            }
        }
        val empty = activity.findViewById<TextView>(R.id.tv_empty)
        GlobalScope.launch(Main){
            if(Global.getInstance()!!.itemList.size == 0){
                empty.visibility = View.VISIBLE
            }
            else{
                empty.visibility = View.GONE
            }
        }
        notifyDataSetChanged()
    }

}