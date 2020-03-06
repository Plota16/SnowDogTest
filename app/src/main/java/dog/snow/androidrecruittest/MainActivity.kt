package dog.snow.androidrecruittest

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PowerManager
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import dog.snow.androidrecruittest.repository.model.Global
import dog.snow.androidrecruittest.ui.ListFragment
import kotlinx.android.synthetic.main.layout_toolbar.*

class MainActivity : AppCompatActivity(R.layout.main_activity){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))

        manageDarkMode()

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val fragmentList = ListFragment()
        transaction.add(R.id.container, fragmentList)
        transaction.commit()


    }

    private fun manageDarkMode(){
        var actionBarColor : ColorDrawable? = null
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(currentNightMode == Configuration.UI_MODE_NIGHT_YES){
            actionBarColor = ColorDrawable(resources.getColor(R.color.sd_color_black))
            window.statusBarColor = resources.getColor(R.color.sd_color_black)
            val logo = toolbar!!.logo
            logo.setTint(resources.getColor(R.color.sd_color_white))
            toolbar!!.logo = logo
        }
        else if(currentNightMode == Configuration.UI_MODE_NIGHT_NO){
            actionBarColor = ColorDrawable(resources.getColor(R.color.sd_color_white))
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = resources.getColor(R.color.sd_color_white)

        }
        supportActionBar!!.setBackgroundDrawable(actionBarColor)



    }
}