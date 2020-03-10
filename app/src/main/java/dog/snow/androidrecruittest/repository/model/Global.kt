package dog.snow.androidrecruittest.repository.model

import androidx.fragment.app.FragmentManager
import dog.snow.androidrecruittest.ui.ListFragment
import dog.snow.androidrecruittest.ui.model.Detail
import dog.snow.androidrecruittest.ui.model.ListItem
import java.util.ArrayList

class Global {

    //state variables
    var isDataDownloaded = false
    var isDetailShowed = false
    var isError = false

    var manager: FragmentManager? = null
    var fragmentList: ListFragment? = null

    var itemList = ArrayList<ListItem>()
    var detailList = HashMap<Int,Detail>()



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