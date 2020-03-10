package dog.snow.androidrecruittest.repository.model

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import dog.snow.androidrecruittest.ui.DetailsFragment
import dog.snow.androidrecruittest.ui.ListFragment
import dog.snow.androidrecruittest.ui.model.Detail
import dog.snow.androidrecruittest.ui.model.ListItem
import java.util.ArrayList

class Global {

    //state variables
    var isDataDownloaded = false

    var manager: FragmentManager? = null
    var fragmentList: ListFragment? = null

    var itemList = ArrayList<ListItem>()
    var detailList = HashMap<Int,Detail>()
    var isDetailShowed = false


    companion object {
        private var mInstance: Global? = null

        @Synchronized fun getInstance(): Global? {
            if (null == mInstance) {
                mInstance = Global()
            }
            return mInstance
        }
    }
}