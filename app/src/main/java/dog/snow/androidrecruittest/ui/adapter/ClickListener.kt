package dog.snow.androidrecruittest.ui.adapter

import android.view.View

interface ClickListener {
    fun onClick(view: View, position: Int)

    fun onLongClick(view: View?, position: Int)
}