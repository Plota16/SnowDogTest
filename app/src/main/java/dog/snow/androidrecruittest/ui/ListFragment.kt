package dog.snow.androidrecruittest.ui

import android.content.res.Configuration
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.repository.model.Global
import dog.snow.androidrecruittest.ui.adapter.IClickListener
import dog.snow.androidrecruittest.ui.adapter.ListAdapter
import dog.snow.androidrecruittest.ui.adapter.RecyclerTouchListener
import kotlinx.android.synthetic.main.layout_search.*
import kotlinx.android.synthetic.main.list_fragment.*


class ListFragment : Fragment(), SearchView.OnQueryTextListener{

    private var adapter : ListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        manageDarkMode()
        initList()

    }

    private fun initList(){
        adapter = ListAdapter(requireContext(), requireActivity(),Global.getInstance()!!.itemList)
        et_search.setOnCloseListener { 
         return@setOnCloseListener true
        }
        rv_items!!.adapter = adapter
        rv_items!!.layoutManager = LinearLayoutManager(requireContext())
        rv_items!!.addOnItemTouchListener(
            RecyclerTouchListener(
                requireContext(),
                rv_items!!,
                object : IClickListener{
                    override fun onClick(view: View, position: Int) {
                        onRecyclerClick(position)
                    }

                    override fun onLongClick(view: View?, position: Int) {

                    }
                }

            )
        )
        et_search.setOnQueryTextListener(this)
    }

    private fun onRecyclerClick(position: Int){
        val detailsFragment =
            DetailsFragment(
                Global.getInstance()!!.itemList[position].id
            )
        detailsFragment.sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        val image = requireActivity().findViewById<ImageView>(R.id.iv_thumb)
        image.transitionName = requireActivity().resources.getString(R.string.simple_fragment_transition)

        val transaction: FragmentTransaction = Global.getInstance()!!.manager!!.beginTransaction()
        transaction.addSharedElement(image,requireActivity().resources.getString(R.string.simple_fragment_transition))
            .setReorderingAllowed(true)
            .replace(R.id.container,detailsFragment)
            .commit()

        Global.getInstance()!!.isDetailShowed = true
    }

    private fun manageDarkMode(){
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if(currentNightMode == Configuration.UI_MODE_NIGHT_YES){
            val colorValue = ContextCompat.getColor(requireContext(), R.color.dark_background_color)
            val colorSearch = ContextCompat.getColor(requireContext(), R.color.sd_color_black_mask)
            layout.setBackgroundColor(colorValue)
            et_search.setBackgroundColor(colorSearch)
        }
        if(currentNightMode == Configuration.UI_MODE_NIGHT_NO){
            val colorValue = ContextCompat.getColor(requireContext(), R.color.light_background_color)
            val colorSearch = ContextCompat.getColor(requireContext(), R.color.sd_color_white_mask)
            layout.setBackgroundColor(colorValue)
            et_search.setBackgroundColor(colorSearch)
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