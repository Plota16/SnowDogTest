package dog.snow.androidrecruittest

import DownloadDataService
import android.animation.ValueAnimator
import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.engine.Resource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.layout_progressbar.*
import kotlinx.android.synthetic.main.splash_activity.*

class SplashActivity : Activity() {

    private lateinit var logo: ImageView
    private lateinit var text: ImageView

     override fun onCreate(icicle: Bundle?) {
         super.onCreate(icicle)
         setContentView(R.layout.splash_activity)

         logo = findViewById(R.id.iv_logo_sd_symbol)
         text = findViewById(R.id.iv_logo_sd_text)

         menageDarkMode()
         setIntroAnimation()
         executeDownloadTask()

    }

    private fun menageDarkMode(){
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if(currentNightMode == Configuration.UI_MODE_NIGHT_YES){
            logo.setColorFilter(ContextCompat.getColor(applicationContext, R.color.sd_color_white), android.graphics.PorterDuff.Mode.SRC_IN)
            text.setColorFilter(ContextCompat.getColor(applicationContext, R.color.sd_color_white), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        if(currentNightMode == Configuration.UI_MODE_NIGHT_NO){
            logo.setColorFilter(ContextCompat.getColor(applicationContext, R.color.sd_color_black), android.graphics.PorterDuff.Mode.SRC_IN)
            text.setColorFilter(ContextCompat.getColor(applicationContext, R.color.sd_color_black), android.graphics.PorterDuff.Mode.SRC_IN);
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
        valueAnimator.duration =  1000
        valueAnimator.start()
    }

    private fun executeDownloadTask(){
        Handler().postDelayed({
            progres.visibility = View.VISIBLE
            val task = DownloadDataService(this)
            task.execute().get()
        }, 1200)
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