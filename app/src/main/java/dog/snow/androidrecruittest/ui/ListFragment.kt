package dog.snow.androidrecruittest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.repository.model.Global
import dog.snow.androidrecruittest.ui.adapter.ClickListener
import dog.snow.androidrecruittest.ui.adapter.ListAdapter
import dog.snow.androidrecruittest.ui.adapter.RecyclerTouchListener
import kotlinx.android.synthetic.main.list_fragment.*

class ListFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = Global.getInstance()!!.itemList

        rv_items!!.adapter = ListAdapter(requireContext(), list)
        rv_items!!.layoutManager = LinearLayoutManager(requireContext())
        rv_items!!.addOnItemTouchListener(
            RecyclerTouchListener(
                requireContext(),
                rv_items!!,
                object : ClickListener{
                    override fun onClick(view: View, position: Int) {
                        Toast.makeText(requireContext(), position.toString(), Toast.LENGTH_SHORT).show()
                    }

                    override fun onLongClick(view: View?, position: Int) {

                    }
                }

            )
        )
    }
}