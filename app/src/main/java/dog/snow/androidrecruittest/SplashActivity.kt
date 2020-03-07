package dog.snow.androidrecruittest

import DownloadDataService
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
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


class SplashActivity : Activity() {

    private lateinit var logo: ImageView
    private lateinit var text: ImageView

     override fun onCreate(icicle: Bundle?) {
         setContentView(R.layout.splash_activity)
         logo = findViewById(R.id.iv_logo_sd_symbol)
         text = findViewById(R.id.iv_logo_sd_text)
         menageDarkMode()
         super.onCreate(icicle)



         setIntroAnimation()
         executeDownloadTask()

         GlobalScope.launch(context = Main){
             while (!Global.getInstance()!!.isDataDownloaded){
                 delay(20)
             }
             setOutroAnimation()
         }
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
        val valueAnimator = ValueAnimator.ofFloat(-900f,0f)
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            logo.translationX = value
            text.translationX = -value
        }
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration =  500
        valueAnimator.start()
    }

    private fun setOutroAnimation(){
        val valueAnimator = ValueAnimator.ofFloat(0f, -1200f)
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            logo.translationY = value
            text.translationY = value
        }
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration =  500
        valueAnimator.start()
        progres.visibility = View.GONE

        Handler().postDelayed({
            startMainActivity()
        }, 500)

    }



    private fun executeDownloadTask(){
        Handler().postDelayed({
            progres.visibility = View.VISIBLE
        }, 500)

        Handler().postDelayed({
            val task = DownloadDataService(this)
            task.execute().get()
        }, 600)
    }

    private fun showError(errorMessage: String?) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.cant_download_dialog_title)
            .setMessage(getString(R.string.cant_download_dialog_message, errorMessage))
            .setPositiveButton(R.string.cant_download_dialog_btn_positive) { _, _ -> /*tryAgain()*/ }
            .setNegativeButton(R.string.cant_download_dialog_btn_negative) { _, _ -> finish() }
            .create()
            .apply { setCanceledOnTouchOutside(false) }
            .show()
    }
}