package dog.snow.androidrecruittest.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.repository.model.Global
import dog.snow.androidrecruittest.ui.adapter.ClickListener
import dog.snow.androidrecruittest.ui.adapter.ListAdapter
import dog.snow.androidrecruittest.ui.adapter.RecyclerTouchListener
import kotlinx.android.synthetic.main.layout_search.*
import kotlinx.android.synthetic.main.list_fragment.*

class ListFragment : Fragment(), SearchView.OnQueryTextListener{

    var adapter : ListAdapter? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menageDarkMode()

        adapter = ListAdapter(requireContext(), Global.getInstance()!!.itemList)
        rv_items!!.adapter = adapter
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
    et_search.setOnQueryTextListener(this)
    }

    private fun menageDarkMode(){
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if(currentNightMode == Configuration.UI_MODE_NIGHT_YES){
            val colorValue = ContextCompat.getColor(requireContext(), R.color.dark_background_color)
            layout.setBackgroundColor(colorValue)
        }
        if(currentNightMode == Configuration.UI_MODE_NIGHT_NO){
            val colorValue = ContextCompat.getColor(requireContext(), R.color.light_background_color)
            layout.setBackgroundColor(colorValue)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter!!.filter(newText!!)
        return false
    }
}