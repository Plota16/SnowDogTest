package dog.snow.androidrecruittest


import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.transition.Fade
import android.transition.TransitionInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar
import dog.snow.androidrecruittest.repository.model.Global
import dog.snow.androidrecruittest.repository.service.ConnectionObserver
import dog.snow.androidrecruittest.ui.ListFragment
import kotlinx.android.synthetic.main.layout_toolbar.*


class MainActivity : AppCompatActivity(R.layout.main_activity){

    override fun onDestroy() {

        Global.getInstance()!!.isDataDownloaded = false
        Global.getInstance()!!.isDetailShowed = false
        Global.getInstance()!!.isError = false
        super.onDestroy()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(findViewById(R.id.toolbar))
        setupOnLogoClick()
        Thread(ConnectionObserver(this,this)).start()
        manageDarkMode()
        initTransaction()
    }

    override fun onBackPressed() {
        if(Global.getInstance()!!.isDetailShowed){
            val transaction = Global.getInstance()!!.manager!!.beginTransaction()
            transaction.replace(R.id.container,Global.getInstance()!!.fragmentList!!)
                .commit()
            Global.getInstance()!!.isDetailShowed = false
        }
        else{
            super.onBackPressed()
        }
    }

    private fun initTransaction(){
        Global.getInstance()!!.manager = supportFragmentManager
        val transaction = Global.getInstance()!!.manager!!.beginTransaction()
        Global.getInstance()!!.fragmentList = ListFragment()
        transaction.replace(R.id.container, Global.getInstance()!!.fragmentList!!)
            .commit()
    }


    private fun manageDarkMode(){
        var actionBarColor : ColorDrawable? = null
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(currentNightMode == Configuration.UI_MODE_NIGHT_YES){
            actionBarColor = ColorDrawable(ContextCompat.getColor(applicationContext,R.color.sd_color_black))
            window.statusBarColor = ContextCompat.getColor(applicationContext,R.color.sd_color_black)
            toolbar!!.logo = ContextCompat.getDrawable(applicationContext,R.drawable.ic_logo_sd_symbol_dark)
            toolbar_title.text = toolbar.title
            supportActionBar!!.setDisplayShowTitleEnabled(false)

        }
        else if(currentNightMode == Configuration.UI_MODE_NIGHT_NO){
            actionBarColor = ColorDrawable(ContextCompat.getColor(applicationContext,R.color.sd_color_white))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            window.statusBarColor = ContextCompat.getColor(applicationContext,R.color.sd_color_white)
            toolbar_title.text = toolbar.title
            supportActionBar!!.setDisplayShowTitleEnabled(false)

        }
        supportActionBar!!.setBackgroundDrawable(actionBarColor)
    }

    private fun setupOnLogoClick(){
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val logoView: View? = getToolbarLogoIcon(toolbar)
        logoView!!.setOnClickListener {
            if(Global.getInstance()!!.isDetailShowed){
                val transaction = Global.getInstance()!!.manager!!.beginTransaction()
                transaction.replace(R.id.container,Global.getInstance()!!.fragmentList!!)
                transaction.commit()
                Global.getInstance()!!.isDetailShowed = false
            }
        }
    }

    private fun getToolbarLogoIcon(toolbar: MaterialToolbar): View? {
        val hadContentDescription =
            TextUtils.isEmpty(toolbar.logoDescription)
        val contentDescription =
            if (!hadContentDescription) toolbar.logoDescription else "logoContentDescription"
        toolbar.logoDescription = contentDescription
        val potentialViews = ArrayList<View>()
        toolbar.findViewsWithText(
            potentialViews,
            contentDescription,
            View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION
        )
        var logoIcon: View? = null
        if (potentialViews.size > 0) {
            logoIcon = potentialViews[0]
        }
        if (hadContentDescription) toolbar.logoDescription = null
        return logoIcon
    }

}