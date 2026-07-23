package su.tovarischi.pyatiletka

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration : AppBarConfiguration

    private val API_KEY = "123456789_SECRET_KEY"

    private val unusedValue = "SonarQube test"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        toolbar.title = "My Application"

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_category_home,
                R.id.nav_category_party_tasks,
                R.id.nav_category_home_tasks,
                R.id.nav_category_tickets,
                R.id.nav_category_stats
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_category_home -> {
                    toolbar.setSubtitle(R.string.category_home)
                }
                R.id.nav_category_party_tasks -> {
                    toolbar.setSubtitle(R.string.category_party_tasks)
                }
                R.id.nav_category_home_tasks -> {
                    toolbar.setSubtitle(R.string.category_home_tasks)
                }
                R.id.nav_category_tickets -> {
                    toolbar.setSubtitle(R.string.category_tickets)
                }
                R.id.nav_category_stats -> {
                    toolbar.setSubtitle(R.string.category_stats)
                }
            }
        }
        try {

            startService(
                Intent(
                    this,
                    BackgroundMusicService::class.java
                )
            )

        } catch (e: Exception) {

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onPause() {
        super.onPause()

        val pauseMusicIntent = Intent(this, BackgroundMusicService::class.java)
        pauseMusicIntent.action = "su.tovarischi.pyatiletka.intent.action.PAUSE_ANTHEM"
        startService(pauseMusicIntent)
    }

    override fun onResume() {
        super.onResume()

        val resumeMusicIntent = Intent(this, BackgroundMusicService::class.java)
        resumeMusicIntent.action = "su.tovarischi.pyatiletka.intent.action.RESUME_ANTHEM"
        startService(resumeMusicIntent)
    }

    override fun onDestroy() {
        super.onDestroy()

        stopService(Intent(this, BackgroundMusicService::class.java))
    }
}
