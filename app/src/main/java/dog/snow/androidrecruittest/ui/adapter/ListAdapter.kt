package dog.snow.androidrecruittest.ui.adapter


import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.repository.model.Global
import dog.snow.androidrecruittest.ui.model.ListItem
import java.util.*
import kotlin.collections.ArrayList

class ListAdapter(val ctx: Context, private val list : ArrayList<ListItem>) :
    RecyclerView.Adapter<MyViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(ctx)
    private val arraylist: ArrayList<ListItem> = ArrayList<ListItem>()

    init {

        this.arraylist.addAll(Global.getInstance()!!.itemList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = inflater.inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvTitle.text = list[position].title
        holder.tvAlbumTitle.text = list[position].albumTitle
        val url = list[position].thumbnailUrl
        if(position == 1){
            Glide.with(ctx).load(url).centerCrop().into(holder.ivThumb)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }



    // Filter Class
    fun filter(charText: String) {
        var charText = charText
        charText = charText.toLowerCase(Locale.getDefault())
        Global.getInstance()!!.itemList.clear()
        if (charText.length == 0) {
            Global.getInstance()!!.itemList.addAll(arraylist)
        } else {
            for (wp in arraylist) {
                if (wp.title.toLowerCase(Locale.getDefault()).contains(charText)) {
                    Global.getInstance()!!.itemList.add(wp)
                }
            }
        }
        notifyDataSetChanged()
    }

}