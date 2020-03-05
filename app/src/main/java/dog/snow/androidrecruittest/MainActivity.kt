package dog.snow.androidrecruittest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dog.snow.androidrecruittest.repository.model.Global
import dog.snow.androidrecruittest.ui.ListFragment

class MainActivity : AppCompatActivity(R.layout.main_activity){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val fragmentList = ListFragment()
        transaction.add(R.id.container, fragmentList)
        transaction.commit()


    }
}