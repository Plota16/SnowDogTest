package dog.snow.androidrecruittest.repository.service

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.widget.TextView
import android.widget.Toast
import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.repository.model.Global
import kotlinx.android.synthetic.main.layout_banner.view.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

class ConnectionObserver(val context: Context, val activity: Activity) : Runnable {

    private var networkEnabled = true
    private var isAlive = true

    override fun run() {
        while (isAlive){

            networkEnabled = isNetworkOn(context)
                if(!networkEnabled ){
                    GlobalScope.launch(Main){
                        val view: TextView = activity.findViewById(R.id.banner)
                        view.visibility = View.VISIBLE

                    }
                }
            else{
                    GlobalScope.launch(Main) {
                        val view: TextView = activity.findViewById(R.id.banner)
                        view.visibility = View.GONE }
                }
            sleep(2000)
            }


    }

    private fun isNetworkOn(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}

