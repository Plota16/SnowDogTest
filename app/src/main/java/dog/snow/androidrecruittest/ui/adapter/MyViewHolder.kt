package dog.snow.androidrecruittest.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dog.snow.androidrecruittest.R

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val ivThumb: ImageView
    val tvTitle: TextView
    val tvAlbumTitle: TextView

    init {
        ivThumb = itemView.findViewById(R.id.iv_thumb) as ImageView
        tvTitle= itemView.findViewById(R.id.tv_photo_title) as TextView
        tvAlbumTitle = itemView.findViewById(R.id.tv_album_title) as TextView
    }

}