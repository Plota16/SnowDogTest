package dog.snow.androidrecruittest

import DownloadDataService
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dog.snow.androidrecruittest.repository.model.Global
import kotlinx.android.synthetic.main.splash_activity.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception


class SplashActivity : Activity() {

    private lateinit var logo: ImageView
    private lateinit var text: ImageView
    private var width : Float = 0.0f
    private var height : Float = 0.0f
    private var animationDuration = 700.toLong()
     override fun onCreate(icicle: Bundle?) {
         setContentView(R.layout.splash_activity)
         super.onCreate(icicle)

         initVariables()
         menageDarkMode()
         setIntroAnimation()
         downloadData()

         GlobalScope.launch(context = Main){
             while (!Global.getInstance()!!.isDataDownloaded){
                 delay(20)
             }
             setOutroAnimation()
         }
    }

    private fun initVariables(){
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        logo = findViewById(R.id.iv_logo_sd_symbol)
        text = findViewById(R.id.iv_logo_sd_text)
        width = displayMetrics.widthPixels.toFloat()
        height = displayMetrics.heightPixels.toFloat()
    }

    private fun startMainActivity(){
        val mainIntent = Intent(this, MainActivity::class.java)
        this.startActivity(mainIntent)
        this.finish()
    }

    private fun menageDarkMode(){
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if(currentNightMode == Configuration.UI_MODE_NIGHT_YES){
            logo.setColorFilter(ContextCompat.getColor(applicationContext, R.color.sd_color_white), android.graphics.PorterDuff.Mode.SRC_IN)
            text.setColorFilter(ContextCompat.getColor(applicationContext, R.color.sd_color_white), android.graphics.PorterDuff.Mode.SRC_IN)
            window.statusBarColor = resources.getColor(R.color.sd_color_black_mask);
        }
        if(currentNightMode == Configuration.UI_MODE_NIGHT_NO){
            logo.setColorFilter(ContextCompat.getColor(applicationContext, R.color.sd_color_black), android.graphics.PorterDuff.Mode.SRC_IN)
            text.setColorFilter(ContextCompat.getColor(applicationContext, R.color.sd_color_black), android.graphics.PorterDuff.Mode.SRC_IN)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            window.statusBarColor = resources.getColor(R.color.sd_color_white);
        }
    }

    private fun setIntroAnimation(){
        val objectAnimatorLogo = ObjectAnimator.ofFloat(logo,"translationX",-width,0f)
        val objectAnimatorText = ObjectAnimator.ofFloat(text,"translationX",width,0f)

        objectAnimatorLogo.duration = animationDuration
        objectAnimatorText.duration = animationDuration

        objectAnimatorLogo.start()
        objectAnimatorText.start()

    }

    private fun setOutroAnimation(){
        progres.visibility = View.GONE

        val objectAnimatorLogo = ObjectAnimator.ofFloat(logo,"translationY",0f,-height)
        val objectAnimatorText = ObjectAnimator.ofFloat(text,"translationY",0f,-height)

        objectAnimatorLogo.duration = animationDuration
        objectAnimatorText.duration = animationDuration

        objectAnimatorLogo.start()
        objectAnimatorText.start()

        GlobalScope.launch(Main) {
            delay(animationDuration)
            startMainActivity()
        }
    }

    private fun executeDownloadTask(){
        GlobalScope.launch(Main){
            delay(animationDuration)
            progres.visibility = View.VISIBLE
            delay(150)
            val task = DownloadDataService()
            task.execute()
        }

    }

    private fun showError(errorMessage: String?) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.cant_download_dialog_title)
            .setMessage(getString(R.string.cant_download_dialog_message, errorMessage))
            .setPositiveButton(R.string.cant_download_dialog_btn_positive) { _, _ -> downloadData() }
            .setNegativeButton(R.string.cant_download_dialog_btn_negative) { _, _ -> finish() }
            .create()
            .apply { setCanceledOnTouchOutside(false) }
            .show()
    }

    fun downloadData(){
        if(isNetworkOn(this)){
            executeDownloadTask()
        }
        else{
            showError("No Internet Connection")
        }
    }

    fun isNetworkOn(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}