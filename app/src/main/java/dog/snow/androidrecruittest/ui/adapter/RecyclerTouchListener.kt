package dog.snow.androidrecruittest.ui.adapter

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class RecyclerTouchListener(
    context: Context,
    recyclerView: RecyclerView,
    private val IClickListener: IClickListener?
) : RecyclerView.OnItemTouchListener {

    private val gestureDetector: GestureDetector

    init {
        gestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val child = recyclerView.findChildViewUnder(e.x, e.y)
                    if (child != null && IClickListener != null) {
                        IClickListener.onLongClick(child, recyclerView.getChildPosition(child))
                    }
                }
            })
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

        val child = rv.findChildViewUnder(e.x, e.y)
        if (child != null && IClickListener != null && gestureDetector.onTouchEvent(e)) {
            IClickListener.onClick(child, rv.getChildPosition(child))
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }
}