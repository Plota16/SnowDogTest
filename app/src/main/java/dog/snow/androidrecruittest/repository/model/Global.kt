package dog.snow.androidrecruittest.repository.model

import dog.snow.androidrecruittest.ui.model.Detail
import dog.snow.androidrecruittest.ui.model.ListItem
import java.util.ArrayList

class Global {

    var itemList = ArrayList<ListItem>()
    var detailList = ArrayList<Detail>()


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